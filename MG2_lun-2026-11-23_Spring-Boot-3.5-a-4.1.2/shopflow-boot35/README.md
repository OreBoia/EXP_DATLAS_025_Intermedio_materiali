# ShopFlow su Spring Boot 3.5 (punto di partenza dell'esercizio di migrazione)

Questo progetto è **ShopFlow alla fine di MG5** (stesso codice di `../shopflow`), ma costruito come lo sarebbe stato
**prima** della migrazione: Spring Boot **3.5.16** (ultima 3.5.x), Spring Framework 6.2, Jackson 2, Hibernate 6.6,
Spring Retry, Testcontainers 1.x, Spring Modulith 1.4.

La traccia dell'esercizio è in **[ESERCIZIO.md](ESERCIZIO.md)**: portare questo progetto a Spring Boot **4.1.2**.

## Requisiti

- JDK 25 (Java è già alla versione finale: l'esercizio riguarda solo Spring)
- Docker (PostgreSQL per l'avvio e per i test con Testcontainers)
- Git
- Maven non serve: si usa `./mvnw`

## Avvio e test

```bash
./mvnw verify                   # 35 test, tutti verdi (quelli con Spring usano Testcontainers)
./mvnw spring-boot:run          # avvia anche PostgreSQL da compose.yaml (spring-boot-docker-compose)
```

All'avvio vengono caricati tre articoli di prova: `ABC-1` (100 pezzi), `ABC-2` (20), `ABC-3` (esaurito).

```bash
scripts/cattura-contratto.sh baseline-3.5.txt     # fotografa stati HTTP e JSON dell'API (app avviata)
```

## Com'è fatto

```
com.shopflow
 ├─ comune/      Denaro, Result, formato JSON di Denaro, audit su riga JSON, Spring Retry attivo (modulo OPEN)
 ├─ ordini/      API (CreaOrdine, OrdineId, RichiestaOrdine, OrdineConfermato, StatoOrdine, ErroreOrdine)
 │   ├─ spi/     ScaricoMagazzino, StockInsufficienteException
 │   ├─ client/  MagazzinoClient @HttpExchange (creato a mano) + MagazzinoGateway con @Retryable
 │   ├─ dominio/ Ordine, RigaOrdine, OrdineRepository: niente Spring né JPA
 │   └─ interno/ servizi, adattatori JPA e REST, aspect
 ├─ magazzino/   scarico con @Version, spedizioni da preparare (listener di OrdineConfermato)
 └─ pagamenti/   gestore simulato (rifiuta oltre shopflow.pagamenti.soglia-massima)
```

## Differenze rispetto a `../shopflow` (4.1.2)

| Area | Questo progetto (3.5) | `../shopflow` (4.1.2) |
|---|---|---|
| Starter | `starter-web`, `starter-aop`, `starter-test` | `starter-webmvc`, `-restclient`, `-aspectj`, `-webmvc-test`... |
| JSON | Jackson 2 (`ObjectMapper`, `@JsonComponent`) | Jackson 3 (`tools.jackson`, `@JacksonComponent`) |
| Resilienza | Spring Retry (`@EnableRetry`, `maxAttempts`) + `Semaphore` a mano | `@Retryable` / `@ConcurrencyLimit` del Framework 7 |
| Client HTTP | `HttpServiceProxyFactory` costruito a mano, proprietà `shopflow.magazzino.*` | `@ImportHttpServices`, `spring.http.serviceclient.*` |
| Versionamento API | condizione `headers` sulla mappatura (una versione sconosciuta riceve la v1) | `version = "..."` + `ApiVersionConfigurer` (versione sconosciuta: 400) |
| Null-safety | `org.springframework.lang` (`@NonNullApi`, `@Nullable`) | JSpecify (`@NullMarked`, `@Nullable`) |
| Test | `@MockBean` (in parte), `TestRestTemplate`, Testcontainers 1.x | `@MockitoBean`, `RestTestClient`, Testcontainers 2.x |
