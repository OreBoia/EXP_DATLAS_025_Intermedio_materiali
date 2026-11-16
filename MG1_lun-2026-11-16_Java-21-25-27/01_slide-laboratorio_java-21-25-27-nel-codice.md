# Java 21 → 25 → 27 nel codice di tutti i giorni

## Spiegazione teorica

**Quale Java usare**
- Java 21 e Java 25 sono LTS; Java 26 (marzo 2026) e Java 27 (settembre 2026) no: ricevono aggiornamenti per sei mesi
- In produzione si resta su **Java 25**; Java 27 serve per provare le funzionalità in preview

**Modellare il dominio con tipi chiusi (Java 21)**
- **Record**: classe per dati immutabili; il compilatore genera costruttore, accessor, `equals`, `hashCode`, `toString`
- **Costruttore compatto**: valida gli argomenti prima di assegnare i campi
- **Sealed**: `sealed ... permits` dichiara l'elenco chiuso dei sottotipi ammessi
- **Pattern matching per `switch`**: sceglie il ramo in base al tipo; se la gerarchia è sealed il compilatore verifica che tutti i casi siano coperti, quindi niente `default`
- **Record pattern**: estrae i componenti di un record direttamente nel `case`, anche in modo annidato
- **Guardia `when`**: condizione aggiuntiva su un `case`

**Regole di `sealed` e quando preferirlo a `enum`**
- `permits` è facoltativo se i sottotipi stanno nello stesso file (come i record annidati); altrimenti va elencato
- Ogni sottotipo deve essere `final`, `sealed` o `non-sealed` (i record sono già `final`)
- `enum`: ogni valore ha gli stessi dati (un giorno della settimana, un colore)
- `sealed` con record: ogni variante ha dati diversi (`Pagato` ha l'id del pagamento, `Annullato` il motivo)

**Meno rumore nel codice (Java 22)**
- `_` al posto di una variabile o di un componente che non serve: `case Spedito(String corriere, _)`

**Altre novità da conoscere**
- **Sequenced collections (21)**: `getFirst()`, `getLast()`, `reversed()` uniformi su `List`, `LinkedHashSet`, `LinkedHashMap`
- **Stream Gatherers (24)**: operazioni intermedie personalizzate negli stream (finestre, deduplica, scan) con `.gather(...)`
- **Flexible constructor bodies (25)**: istruzioni prima di `super(...)` o `this(...)`, per validare gli argomenti subito
- **Virtual thread (cenno)**: da Java 24 non c'è più *pinning* su `synchronized`; in Spring Boot si attivano con `spring.threads.virtual.enabled=true`

**Cosa portano 26 e 27**
- Java 26: avvisi quando la reflection modifica campi `final` (JEP 500)
- Java 27: G1 è il garbage collector predefinito ovunque e i compact object headers sono attivi di default
- Java 27, in preview: Structured Concurrency, Lazy Constants, tipi primitivi nei pattern

## Esempi teorici di utilizzo

- **Value object di dominio**: importi, indirizzi, codici articolo. Si usa un record quando due oggetti con gli stessi valori devono essere considerati uguali e non devono cambiare dopo la creazione
- **Stati di un processo**: un ordine passa da creato a pagato, spedito o annullato, e ogni stato porta dati diversi. Una gerarchia sealed al posto di un'enum con campi opzionali evita i campi "forse null" e sostituisce State e Visitor
- **Aggiungere uno stato nuovo senza dimenticare nulla**: con uno `switch` esaustivo il compilatore segnala tutti i punti del codice da aggiornare, mentre con una catena di `if/instanceof` l'errore emerge solo a runtime
- **Errori di dominio attesi**: "stock insufficiente" o "cliente bloccato" non sono eccezioni, ma esiti previsti. Un tipo `Result` li rende visibili nella firma del metodo e obbliga il chiamante a gestirli
- **Dati di un elenco in lotti**: elaborare gli ordini a gruppi di N (invio a un'API con limite di batch) con un Gatherer, senza cicli con contatori
- **Prima e ultima voce di una raccolta**: leggere il primo e l'ultimo ordine di una lista senza `list.get(list.size() - 1)`

## Esempi di codice

### Base
Cosa mostra: un value object `Denaro` come record, con validazione nel costruttore compatto.

```java
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

// Record: campi privati e final, accessor, equals, hashCode e toString sono generati
public record Denaro(BigDecimal importo, Currency valuta) {

    // Costruttore compatto: gira prima dell'assegnazione dei campi
    public Denaro {
        if (importo == null || valuta == null)
            throw new IllegalArgumentException("importo e valuta sono obbligatori");
        if (importo.signum() < 0)                       // invariante: mai negativo
            throw new IllegalArgumentException("importo negativo");
        // BigDecimal.equals confronta anche la scala: 10.0 e 10.00 sarebbero diversi.
        // Fisso la scala a 2 decimali (riassegno il parametro prima dei campi)
        importo = importo.setScale(2, RoundingMode.HALF_EVEN);
    }

    // L'oggetto è immutabile: la somma restituisce un NUOVO Denaro
    public Denaro somma(Denaro altro) {
        if (!valuta.equals(altro.valuta()))
            throw new IllegalArgumentException("valute diverse");
        return new Denaro(importo.add(altro.importo()), valuta);
    }
}

// Uso
var eur = Currency.getInstance("EUR");
var a = new Denaro(new BigDecimal("10.00"), eur);
var b = new Denaro(new BigDecimal("5.50"), eur);
System.out.println(a.somma(b));                         // Denaro[importo=15.50, valuta=EUR]
System.out.println(a.equals(new Denaro(new BigDecimal("10.00"), eur))); // true
System.out.println(new Denaro(new BigDecimal("10.0"), eur).equals(a)); // true: stessa scala
```
- Il record elimina il codice ripetitivo di getter, `equals` e `hashCode`
- Il costruttore compatto garantisce che un `Denaro` non valido non esista mai
- Nessun setter: per "modificare" si crea un nuovo oggetto
- `equals` confronta i valori, non l'identità dell'oggetto
- La scala di `BigDecimal` fa parte di `equals`: va normalizzata nel costruttore, altrimenti `10.0` e `10.00` sono due valori diversi (`setScale` arrotonda: per gli euro va bene)

### Intermedio
Cosa mostra: gli stati di un ordine come gerarchia sealed e uno `switch` esaustivo con record pattern e `_`.

```java
// Insieme CHIUSO di stati: nessun altro tipo può implementare StatoOrdine
public sealed interface StatoOrdine {
    record Creato() implements StatoOrdine {}
    record Pagato(String idPagamento) implements StatoOrdine {}
    record Spedito(String corriere, String tracking) implements StatoOrdine {}
    record Annullato(String motivo) implements StatoOrdine {}
}

static String descrizione(StatoOrdine stato) {
    // Nessun default: se aggiungo uno stato, questo switch NON compila più
    return switch (stato) {
        case StatoOrdine.Creato _                       // _ : non mi serve la variabile
            -> "In attesa di pagamento";
        case StatoOrdine.Pagato(String idPagamento)     // record pattern: estrae il componente
            -> "Pagato, rif. " + idPagamento;
        case StatoOrdine.Spedito(String corriere, _)    // _ : ignoro il tracking
            -> "Spedito con " + corriere;
        case StatoOrdine.Annullato(var motivo)          // var: tipo dedotto
            -> "Annullato: " + motivo;
    };
}
```
- `sealed` dice al compilatore quali sono tutti i possibili stati
- Lo `switch` come espressione restituisce un valore e deve coprire ogni caso
- Il record pattern legge i dati dello stato senza chiamare gli accessor
- `_` toglie il rumore dalle variabili inutilizzate

### Avanzato
Cosa mostra: un tipo `Result` per gli errori di dominio, con record pattern annidati e guardia `when`.

```java
// Esito di un'operazione: o un valore, o un errore PREVISTO (non un'eccezione)
public sealed interface Result<T, E> {
    record Ok<T, E>(T valore) implements Result<T, E> {}
    record Errore<T, E>(E errore) implements Result<T, E> {}
}

// Errori di dominio, anch'essi un insieme chiuso
public sealed interface ErroreOrdine {
    record StockInsufficiente(String sku, int richiesti, int disponibili) implements ErroreOrdine {}
    record ClienteBloccato(String idCliente) implements ErroreOrdine {}
}

// La firma dichiara che l'operazione può fallire, e come
Result<Ordine, ErroreOrdine> creaOrdine(RichiestaOrdine richiesta) { /* ... */ }

String messaggio(Result<Ordine, ErroreOrdine> esito) {
    return switch (esito) {
        case Result.Ok(var ordine)                      // pattern annidato su Ok
            -> "Creato ordine " + ordine.id();
        case Result.Errore(ErroreOrdine.StockInsufficiente(var sku, _, var disp))
                when disp == 0                          // guardia: caso particolare
            -> "Articolo esaurito: " + sku;
        case Result.Errore(ErroreOrdine.StockInsufficiente(var sku, var richiesti, var disp))
            -> "Di " + sku + " restano " + disp + " pezzi su " + richiesti;
        case Result.Errore(ErroreOrdine.ClienteBloccato(var id))
            -> "Cliente " + id + " bloccato";
    };                                                  // esaustivo: nessun default
}
```
- `Result` rende l'errore atteso parte del tipo restituito, invece di un'eccezione nascosta
- I record pattern si annidano: un solo `case` apre `Errore` e poi `StockInsufficiente`
- Un `case` con `when` non conta per l'esaustività, quindi serve anche il caso generale
- Aggiungere un nuovo errore a `ErroreOrdine` fa fallire la compilazione degli `switch` che non lo gestiscono

**Result o eccezione?** `Result` per esiti di dominio attesi (stock insufficiente); eccezione per guasti tecnici e bug (database irraggiungibile).

**Attenzione con `@Transactional`:** un metodo che *ritorna* un errore non lancia eccezioni, quindi la transazione va in commit.
```java
@Transactional
public Result<Ordine, ErroreOrdine> creaOrdine(RichiestaOrdine richiesta) {
    var ordine = ordini.save(new Ordine(richiesta));     // scrittura sul database
    var riserva = magazzino.riserva(richiesta);          // Result<Riserva, ErroreOrdine>
    if (riserva instanceof Result.Errore(var errore)) {
        // Nessuna eccezione = COMMIT: senza questa riga l'ordine resterebbe salvato
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return new Result.Errore<>(errore);
    }
    return new Result.Ok<>(ordine);
}
```
- Ritornare `Errore` non annulla la transazione: serve `setRollbackOnly()` oppure lanciare un'eccezione
- Il tema (propagation, regole di rollback) si approfondisce in MG5

### Novità in breve
Cosa mostra: sequenced collections, Stream Gatherers e flexible constructor bodies in tre frammenti.

```java
// Sequenced collections (Java 21): primo e ultimo elemento senza get(size() - 1)
var ultimo = ordini.getLast();      // su lista vuota lancia NoSuchElementException
var inverso = ordini.reversed();    // è una VISTA sulla stessa lista, non una copia

// Stream Gatherers (Java 24): elaborare gli ordini a lotti da 50 per un'API con limite di batch
List<List<Ordine>> lotti = ordini.stream()
        .gather(Gatherers.windowFixed(50))   // operazione intermedia: raggruppa a finestre fisse
        .toList();

// Flexible constructor bodies (Java 25): validare PRIMA di chiamare super(...)
class Sconto extends Promozione {
    Sconto(String codice, int percentuale) {
        if (percentuale < 0 || percentuale > 100)        // prologo del costruttore
            throw new IllegalArgumentException("percentuale non valida");
        super(codice, percentuale);                      // nel prologo non si può usare "this"
    }
}
```
- `getFirst()`, `getLast()` e `reversed()` funzionano allo stesso modo su `List`, `LinkedHashSet` e `LinkedHashMap`
- `reversed()` restituisce una vista: modificare l'originale cambia anche il risultato
- `gather(...)` si inserisce in una pipeline come `map` o `filter`; `Gatherers` offre finestre, scan e altro
- Prima di Java 25 per validare un argomento prima di `super(...)` serviva un metodo statico di appoggio

## Esercizi

### Facile
Obiettivo: creare un record `Temperatura` con un valore in gradi Celsius, che rifiuti nel costruttore compatto valori sotto lo zero assoluto (−273,15), e aggiungere un metodo `inFahrenheit()` che restituisca il valore convertito. Verificare con due istanze che `equals` confronti i valori.

### Medio
Obiettivo: dato un metodo che calcola l'area di una figura geometrica con una catena di `if/instanceof` su classi `Cerchio`, `Rettangolo` e `Triangolo`, riscriverlo con una gerarchia sealed di record e uno `switch` esaustivo senza `default`. Aggiungere poi una quarta figura e verificare che il compilatore segnali lo `switch` da aggiornare.

### Difficile
Obiettivo: realizzare la prenotazione di una sala riunioni che restituisca un `Result<Prenotazione, ErrorePrenotazione>` senza lanciare eccezioni. Gli errori (`SalaOccupata` con orario di inizio e fine, `OrarioNonValido`, `CapienzaSuperata` con persone richieste e massimo) sono una gerarchia sealed. Scrivere uno `switch` con record pattern annidati che produca un messaggio per l'utente, usando una guardia `when` per distinguere una sala occupata per meno di 30 minuti da una occupata più a lungo.
