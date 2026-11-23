# Punti da verificare [?] — Spring Boot 3.5 → 4.1.2 → 4.2

Estratti da `02_riferimento_spring-boot-3-5-4-1-4-2-confronto.md`: tutte le parti marcate **[?]** (da verificare: informazione da fonti secondarie o non confermata in modo univoco; va controllata prima di spiegarla in aula).

Il numero di riga si riferisce al file originale.

---

## 1.1 Tabella generale

**1.** *(riga 57)*

| | **3.5.x** | **4.0.x** | **4.1.2** (target del corso) | **4.2** (futuro) |
|---|---|---|---|---|
| Hibernate ORM | 6.6 **[?]** | 7.1 (GA 4.0.0), poi 7.2 da 4.0.1 | 7.4.13.Final (verificato su ShopFlow con `dependency:tree`) | 7.4.14.Final (RC2) **[GA]** |

**2.** *(riga 59)*

| | **3.5.x** | **4.0.x** | **4.1.2** (target del corso) | **4.2** (futuro) |
|---|---|---|---|---|
| Micrometer | 1.15.0 | 1.16 **[?]** | 1.17.0 (Tracing 1.7.0) | 1.18 (Tracing 1.8) **[GA]** |

**3.** *(riga 61)*

| | **3.5.x** | **4.0.x** | **4.1.2** (target del corso) | **4.2** (futuro) |
|---|---|---|---|---|
| Java minimo | 17 | 17 | 17 | 17 **[?]** |

---

## 2.1 Starter modulari (e starter "ponte")

**4.** *(riga 162)*

**Stato verifica:** **[V]** (Migration Guide 4.0; i nomi esatti degli starter vanno confermati sul `pom` generato da start.spring.io per 4.1.2 **[?]**)

---

## 2.2 Jackson 3 al posto di Jackson 2

**5.** *(riga 268)*

> **Sulle date.** Nelle fonti il cambio di `WRITE_DATES_AS_TIMESTAMPS` (da `true` a `false`) è presentato come uno dei due default che rompono i test. Nelle applicazioni Boot 3.x, però, la configurazione automatica di Boot di solito scriveva già date ISO: il caso "timestamp numerico" riguarda soprattutto i mapper creati a mano con `new ObjectMapper()`. Per questo motivo l'esempio qui sopra mostra **solo l'ordine** delle proprietà. **Il test giusto è confrontare l'output reale di ShopFlow prima e dopo la migrazione**, non fidarsi della regola generale. **[?]**

**6.** *(riga 288)*

**Stato verifica:** differenze Jackson 2/3, default, bean e hook **[V]**; proprietà 4.0 e 4.1 **[V]** (Migration Guide 4.0 e Release Notes 4.1); date in Boot 3.x **[?]**.

---

## 2.3 Test: `@MockBean`, `@AutoConfigureMockMvc`, `RestTestClient`

**7.** *(riga 367)*

- `@MockitoBean` **non** va su campi di classi `@Configuration` e non sostituisce bean ancora da creare nello stesso modo: leggere la documentazione dei bean override prima di usarlo con casi particolari. **[?]**

**8.** *(riga 373)*

**Stato verifica:** rimozioni e sostituzioni **[V]**; nome dello starter di test per `RestTestClient` (circola `spring-boot-starter-restclient-test`) **[?]**.

---

## 2.4 Rimozioni e cambiamenti di default

**9.** *(riga 400)*

> Righe persistenza, MongoDB e sessioni: **[V]** sulla Migration Guide 4.0. La riga sul tracing (`management.tracing.*`) proviene dalle note di migrazione riassunte nel materiale di progetto e **non è stata ritrovata nella pagina** consultata: confermarla con il *properties migrator*. **[?]**

---

## 2.5 Versionamento delle API (`version`)

**10.** *(riga 481)*

- Il codice di risposta per una versione **non supportata** (nel materiale MG2 è indicato `400`): **non** l'ho trovato scritto nelle fonti ufficiali consultate. **[?]** Va verificato con un test reale su ShopFlow prima di insegnarlo.

**11.** *(riga 485)*

**Stato verifica:** meccanismo, proprietà e componenti **[V]**; stato HTTP per versione non supportata **[?]**.

---

## 2.6 HTTP service client dichiarativi (`@ImportHttpServices`)

**12.** *(riga 548)*

**Stato verifica:** **[V]** (reference Boot "REST clients"); durata `2s` come formato `Duration` **[?]** (provare con `2s` e con `2000ms`).

---

## 2.7 Resilienza nel Framework 7: `@Retryable` e `@ConcurrencyLimit`

**13.** *(riga 619)*

**Stato verifica:** attributi, semantica di `maxRetries` e `RetryTemplate` **[V]** (reference Framework 7); unità di misura di `delay` nell'annotazione **[?]** (esiste anche la forma stringa `delayString`).

---

## 2.8 Null-safety con JSpecify

**14.** *(riga 663)*

- `@Nullable` con tipi generici e array: la posizione dell'annotazione cambia significato (`@Nullable String[]` vs `String @Nullable []`). **[?]** controllare la guida JSpecify.

---

## 2.9 Spring Security 7 (cenni)

**15.** *(riga 680)*

**Trappola.** Migrare con `@EnableWebSecurity` e configurazioni scritte per Security 6.5 senza leggere la guida di migrazione di Security 7: l'elenco puntuale dei default cambiati **non è stato verificato** in questa preparazione. **[?]** Prima della lezione: leggere la *Migration Guide* di Spring Security 7 e provare i test di sicurezza di ShopFlow.

**16.** *(riga 682)*

**Stato verifica:** **[?]** per i default; **[V]** per renaming starter e `authorities-claim-expressions`.

---

## 2.10 Novità di Boot 4.1: protezione SSRF con `InetAddressFilter`

**17.** *(riga 718)*

**Stato verifica:** **[V]** (reference Boot 4.1); firma esatta di `of(...)` / `andNot(...)` **[?]**.

---

## 2.11 Altre novità di Boot 4.1 (tabella)

**18.** *(riga 746)*

**Stato verifica:** **[V]** (release notes 4.1); il comportamento esatto di `connection-fetch=lazy` va provato sul pool di ShopFlow **[?]**.

---

## 4.1 Tabella riassuntiva 3.5 → 4.1.2 → 4.2

**19.** *(riga 1107)*

| Area | **Boot 3.5** | **Boot 4.1.2** (corso) | **Boot 4.2** **[GA]** |
|---|---|---|---|
| **Persistenza** | Hibernate 6.6 **[?]** | Hibernate 7.1 (GA), 7.2 da 4.0.1; `spring.datasource.connection-fetch=lazy`; `spring.jpa.bootstrap` | Hibernate 7.4.13.Final; RC2 di 4.2: 7.4.14.Final |

**20.** *(riga 1113)*

> Le celle con **[GA]** e quelle della colonna 4.2 vanno ricontrollate dopo il 19/11/2026. La riga "buildpacks Jammy" per Boot 3.5 è ricostruita dalla storia di Paketo e **non** è stata verificata: **[?]**.

---

## 5.3 `PagamentoService` con `@Retryable` e controllo di idempotenza

**21.** *(riga 1309)*

- Il test dura circa 600 ms (attese 200 + 400): in un test vero conviene ridurre `delay` con una proprietà (`delayString = "${shopflow.pagamento.retry.delay:200}"`). **[?]** (verificare che `delayString` accetti il segnaposto).

---

## 5.4 `MagazzinoClient` con versione dell'API

**22.** *(riga 1336)*

- La chiave `apiversion.default` non è stata trovata nelle fonti consultate (si è trovata solo `insert.header`): **[?]**. In alternativa si può usare l'attributo `version` su `@GetExchange`/`@HttpExchange`.

**23.** *(riga 1337)*

- Un client che *non* invia la versione e un server con `setVersionRequired(true)` → la richiesta viene rifiutata (lo stato esatto è **[?]**, vedi 2.5).

---

## Fonti secondarie (usate per le etichette [?])

- Moritz Halbritter, slide *Inside Spring Boot 4* (default di Jackson 3): https://mhalbritter.github.io/slides/2026-04%20Inside%20Spring%20Boot%204%20Restructuring%20for%20the%20Future.pdf
- Dan Vega, *MockMvcTester vs RestTestClient*: https://www.danvega.dev/blog/mock-vs-rest
- Dan Vega, *Jackson 3 in Spring Boot 4*: https://www.danvega.dev/blog/jackson-3-spring-boot-4
