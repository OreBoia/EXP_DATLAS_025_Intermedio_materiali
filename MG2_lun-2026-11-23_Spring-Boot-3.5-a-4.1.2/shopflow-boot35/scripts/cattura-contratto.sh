#!/usr/bin/env bash
# Fotografa il contratto HTTP di ShopFlow (stati e corpi JSON) con i valori variabili mascherati.
# Si lancia con l'applicazione avviata:  scripts/cattura-contratto.sh <file-di-output>
# Esempio: scripts/cattura-contratto.sh baseline-3.5.txt   ... dopo la migrazione ... scripts/cattura-contratto.sh dopo-4.1.2.txt
set -u
BASE="${BASE:-http://localhost:8080}"
OUT="${1:-/dev/stdout}"

# UUID e date cambiano a ogni esecuzione: si sostituiscono con segnaposto, così il diff mostra solo le differenze vere
maschera() {
  sed -E 's/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}/<UUID>/g; s/[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9:.]+(Z|[+-][0-9]{2}:[0-9]{2})/<DATA>/g'
}
chiama() {   # chiama <titolo> <argomenti di curl...>
  local titolo="$1"; shift
  printf '### %s\n' "$titolo"
  curl -s -w '\nHTTP %{http_code}\n' "$@" | maschera
  printf '\n'
}
ordine() {   # ordine <sku> <quantita> <importo>
  printf '{"idCliente":"C-1","righe":[{"sku":"%s","quantita":%s,"prezzo":{"importo":"%s","valuta":"EUR"}}]}' "$1" "$2" "$3"
}
JSON=(-H 'Content-Type: application/json')

{
  # Crea un ordine e ne legge l'id (serve per le letture successive)
  CREAZIONE=$(curl -s -w '\nHTTP %{http_code}' -X POST "$BASE/ordini" "${JSON[@]}" -d "$(ordine ABC-1 2 10.00)")
  ID=$(printf '%s' "$CREAZIONE" | sed -E 's/.*"valore":"([^"]+)".*/\1/' | head -1)
  printf '### POST /ordini (valido)\n%s\n\n' "$(printf '%s' "$CREAZIONE" | maschera)"

  chiama "GET /ordini/{id}  (nessun header: v1)"            "$BASE/ordini/$ID"
  chiama "GET /ordini/{id}  (X-API-Version: 1.0)"           -H 'X-API-Version: 1.0' "$BASE/ordini/$ID"
  chiama "GET /ordini/{id}  (X-API-Version: 2.0)"           -H 'X-API-Version: 2.0' "$BASE/ordini/$ID"
  chiama "GET /ordini/{id}  (X-API-Version: 9.9)"           -H 'X-API-Version: 9.9' "$BASE/ordini/$ID"
  chiama "GET /ordini/{id}  (ordine inesistente)"           "$BASE/ordini/00000000-0000-0000-0000-000000000000"
  chiama "POST /ordini  (ABC-3 esaurito)"                   -X POST "$BASE/ordini" "${JSON[@]}" -d "$(ordine ABC-3 1 10.00)"
  chiama "POST /ordini  (totale oltre soglia)"              -X POST "$BASE/ordini" "${JSON[@]}" -d "$(ordine ABC-2 1 2000.00)"
  chiama "POST /ordini  (nessuna riga)"                     -X POST "$BASE/ordini" "${JSON[@]}" -d '{"idCliente":"C-1","righe":[]}'
  chiama "POST /ordini  (campo sconosciuto)"                -X POST "$BASE/ordini" "${JSON[@]}" -d '{"idCliente":"C-1","scontoSegreto":99,"righe":[{"sku":"ABC-1","quantita":1,"prezzo":{"importo":"1.00","valuta":"EUR"}}]}'
} > "$OUT"
