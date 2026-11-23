# Esercizio finale MG2 – Migrare ShopFlow da Spring Boot 3.5 a 4.1.2

**Durata indicativa:** 3 ore (Fase A, obbligatoria: 2 ore · Fase B, novità di Boot 4: 1 ora)
**Si lavora:** da soli o in coppia, sul progetto `shopflow-boot35`
**Si consegna:** il branch git `migrazione-4.1` e il file `MIGRAZIONE.md` (modello a fine pagina)

---

## Lo scenario

Siete il team di ShopFlow. Spring Boot 3.5 è fuori supporto open source dal 30/06/2026: le patch di sicurezza sono
ormai solo a pagamento. Il responsabile vi chiede di portare l'applicazione alla **4.1.2** (supporto fino al
31/07/2027) senza cambiare ciò che vedono i clienti dell'API, **salvo dove la differenza è decisa e motivata**.

Il progetto è ShopFlow alla fine di MG5: ordini, magazzino, pagamenti, audit, resilienza, test con PostgreSQL vero.
Parte da una situazione **tipica**: i test sono verdi, ma il codice usa tutto ciò che in Boot 4 cambia.

## Regole del gioco

1. Si lavora su un branch, **un commit per passo** (il messaggio dice cosa avete cambiato e perché).
2. Dopo ogni passo si lancia ciò che il passo indica. **Non si va avanti con la build rossa** senza sapere perché.
3. Vietati `-DskipTests` e `@Disabled`. Un test che fallisce si capisce; non si cancella e non si indebolisce
   (salvo che il passo dica espressamente di cambiarlo).
4. Annotate man mano nel `MIGRAZIONE.md` ogni errore incontrato: serve alla consegna.
5. Il codice di dominio (`ordini.dominio`, `Denaro`, `Result`, ...) **non** si tocca: se serve modificarlo, vi siete persi.

## Cosa serve

JDK 25 · Docker acceso · Git · connessione a Internet (Maven Central). Maven è già incluso (`./mvnw`).

---

# FASE A – Migrazione (obbligatoria)

Obiettivo: ShopFlow su Boot 4.1.2 con **tutti i 35 test verdi**, nessun warning di deprecazione e contratto HTTP
invariato (o con differenze che sapete spiegare).

## Passo 0 – Fotografare il punto di partenza (15 min)

```bash
cd shopflow-boot35
git init && git add -A && git commit -m "ShopFlow su Spring Boot 3.5.16 (punto di partenza)"
git switch -c migrazione-4.1

./mvnw verify                                   # 1) 35 test verdi?
./mvnw dependency:tree | grep -E "hibernate-core|spring-core|jackson-databind|spring-retry|testcontainers:|spring-modulith-core"
```

Riportate nel `MIGRAZIONE.md` le versioni trovate (colonna "Prima"). Dovreste leggere: Spring Framework 6.2.19,
Hibernate 6.6.53.Final, Jackson 2.21.4, Spring Retry 2.0.13, Testcontainers 1.21.4, Spring Modulith 1.4.14.

Ora **fotografate il comportamento dell'API**, prima di toccare qualsiasi cosa:

```bash
# terminale 1: avvia l'applicazione (e PostgreSQL da compose.yaml)
./mvnw spring-boot:run

# terminale 2: crea ordini, legge v1/v2, provoca errori 404/409/422/400 e salva stati e JSON in un file
scripts/cattura-contratto.sh baseline-3.5.txt
```

Fermate l'applicazione (`Ctrl+C`). Aprite `baseline-3.5.txt`: è il **contratto da non rompere**
(gli UUID e le date sono mascherati, così il confronto finale mostra solo differenze vere).

Infine contate i warning del compilatore:

```bash
./mvnw clean test-compile -Dmaven.compiler.showDeprecation=true | grep WARNING
```

✅ **Controllo:** 35 test verdi; il compilatore segnala 4 warning di deprecazione (in 2 file di test). Quali?

```bash
git add baseline-3.5.txt && git commit -m "Baseline del contratto HTTP su Boot 3.5"
```

## Passo 1 – Ripulire le deprecazioni *sulla 3.5* (10 min)

Si fa **prima** di salire di versione: così il giorno della migrazione c'è una cosa in meno da cambiare.
`@MockBean` e `@SpyBean` sono deprecate: `@MockitoBean` esiste già dal Framework 6.2 e funziona anche su Boot 3.5.

1. Sostituite `@MockBean` con `@MockitoBean` nei due file segnalati (cambia anche l'import).
2. `./mvnw verify` → ancora 35 test verdi, **0 warning**.

✅ **Controllo:** `git diff --stat` mostra solo 2 file di test.

## Passo 2 – La ricetta OpenRewrite (20 min)

Si fa **su un branch** e si rilegge **tutto** il `git diff`.

```bash
mvn -U org.openrewrite.maven:rewrite-maven-plugin:run \
  -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-spring:RELEASE \
  -Drewrite.activeRecipes=org.openrewrite.java.spring.boot4.UpgradeSpringBoot_4_0
git diff
```

(Se `mvn` non è installato, usate `./mvnw` al suo posto.)

Nel `MIGRAZIONE.md` annotate:

- quali file ha toccato la ricetta e che cosa ha cambiato;
- **a quale versione di Boot ha portato il `parent`** (il nome della ricetta è `..._4_0`: il bersaglio è la 4.1.2!);
- quali dei passi 3–8 qui sotto la ricetta **ha già fatto** e quali no.

> **Senza rete o senza permessi?** Saltate questo passo e dichiaratelo nel `MIGRAZIONE.md`: i passi seguenti
> fanno a mano tutto ciò che serve. Se la ricetta ha già fatto qualcosa, saltate i punti corrispondenti.

Committate il risultato. **Dopo la ricetta il progetto, quasi sicuramente, non compila ancora o non è verde:** è normale.

## Passo 3 – Il `pom.xml`: parent e starter (25 min)

1. Portate il `parent` a **4.1.2** (se la ricetta non l'ha già fatto) e lanciate `./mvnw -q compile`.
   Maven si ferma **prima di compilare**: ci sono 4 dipendenze senza versione. Annotate quali.
2. Correggete il `pom.xml`:

| Prima (3.5) | Dopo (4.1.2) | Perché |
|---|---|---|
| `spring-boot-starter-web` | `spring-boot-starter-webmvc` **e** `spring-boot-starter-restclient` | starter modulari; il codice usa `RestClient` e `@HttpExchange`, quindi serve anche il secondo |
| `spring-boot-starter-aop` | `spring-boot-starter-aspectj` | rinominato |
| `spring-retry` | **eliminare** (si sostituisce nel Passo 5) | Boot non gestisce più Spring Retry |
| `spring-modulith.version` `1.4.14` | `2.1.3` | la linea 2.1.x è quella costruita su Boot 4.1 |
| `org.testcontainers:junit-jupiter` | `org.testcontainers:testcontainers-junit-jupiter` | Testcontainers 2.x: nuovi nomi |
| `org.testcontainers:postgresql` | `org.testcontainers:testcontainers-postgresql` | idem |

> Il vecchio `spring-boot-starter-web` esiste ancora come "ponte" deprecato: potreste usarlo per far ripartire il
> progetto in fretta. In questo esercizio **non** lo fate: gli starter si passano subito a quelli modulari.
> Gli starter di test si sistemano al Passo 7.

3. `./mvnw -q compile`

✅ **Controllo:** ora Maven compila e fallisce con errori in **7 file** di produzione:
`ResilienzaConfig`, `RegistroAudit`, `DenaroJson`, `JsonConfig`, `ClientConfig`, `MagazzinoGateway`,
`CreaOrdineConRetry`. Se ne vedete di più o di meno, controllate il `pom.xml`.

## Passo 4 – Jackson 3 (30 min)

I file coinvolti: `RegistroAudit`, `DenaroJson`, `JsonConfig`. Regole:

| Jackson 2 / Boot 3.5 | Jackson 3 / Boot 4 |
|---|---|
| `com.fasterxml.jackson.databind.*`, `com.fasterxml.jackson.core.*` | `tools.jackson.databind.*`, `tools.jackson.core.*` (le **annotazioni** restano in `com.fasterxml.jackson.annotation`) |
| `JsonProcessingException` (controllata) | `tools.jackson.core.JacksonException` (**non** controllata) |
| `JsonSerializer`, `JsonDeserializer`, `SerializerProvider` | `ValueSerializer`, `ValueDeserializer`, `SerializationContext` |
| `writeNumberField`, `writeStringField` | `writeNumberProperty`, `writeStringProperty` |
| `parser.getCodec().readTree(parser)`, `nodo.asText()` | `parser.readValueAsTree()`, `nodo.asString()` |
| `@JsonComponent` | `@JacksonComponent` (`org.springframework.boot.jackson`) |
| `Jackson2ObjectMapperBuilderCustomizer`, `builder.featuresToEnable(...)` | `JsonMapperBuilderCustomizer` (`org.springframework.boot.jackson.autoconfigure`), `builder.enable(...)` |
| `new ObjectMapper().registerModule(new JavaTimeModule())` | `JsonMapper.builder().build()`: il mapper è **immutabile** (si configura col builder) e `java.time` è già incluso |

Applicate le regole ai tre file e fate compilare il codice di produzione (gli altri file del Passo 3 restano da fare).

✅ **Controllo:** `RegistroAudit`, `DenaroJson`, `JsonConfig` non danno più errori.

❓ **Prima di proseguire, prevedete** (scrivetelo nel `MIGRAZIONE.md`): quali dei test JSON
(`ContrattoJsonTest`, `RegistroAuditTest`) falliranno dopo la migrazione, e perché? Verificherete la previsione al Passo 7.

## Passo 5 – Resilienza: da Spring Retry al Framework 7 (25 min)

I file: `ResilienzaConfig`, `MagazzinoGateway`, `CreaOrdineConRetry`.

| Spring Retry (3.5) | Framework 7 (4.1.2) |
|---|---|
| `@EnableRetry` | `@EnableResilientMethods` (`org.springframework.resilience.annotation`) |
| `@Retryable(retryFor = X.class, ...)` | `@Retryable(includes = X.class, ...)` |
| `maxAttempts = 4` (tentativi **totali**) | `maxRetries = 3` (ripetizioni **dopo il primo**) |
| `backoff = @Backoff(delay = 200, multiplier = 2, maxDelay = 2000, random = true)` | `delay = 200, multiplier = 2, maxDelay = 2000, jitter = 50` |
| `Semaphore(10)` scritto a mano | `@ConcurrencyLimit(10)` sul metodo (eliminate il semaforo) |

❓ **Domanda:** se per distrazione scrivete `maxRetries = 4`, quale test se ne accorge? Provate (e rimettete a posto).

✅ **Controllo:** `./mvnw -q compile` → il codice di produzione compila **senza errori**.

## Passo 6 – Client HTTP, null-safety e codici di stato (25 min)

1. **`ClientConfig`** costruisce il client a mano (`HttpServiceProxyFactory`) e usa `ClientHttpRequestFactorySettings`,
   classe che in Boot 4 non esiste più. Sostituite tutta la classe con la registrazione dichiarativa:
   `@ImportHttpServices(group = "magazzino", types = MagazzinoClient.class)`.
2. Eliminate `MagazzinoProperties` e in `application.properties` passate da `shopflow.magazzino.*` alle chiavi del
   gruppo: `spring.http.serviceclient.magazzino.base-url` e `spring.http.serviceclient.magazzino.read-timeout`.
3. **Null-safety:** il package `ordini.client` usa `@NonNullApi` e `@Nullable` di `org.springframework.lang`
   (deprecate nel Framework 7). Passate a JSpecify: `@NullMarked` (nel `package-info.java`) e `org.jspecify.annotations.Nullable`.
4. **`ErroriOrdineHttp`**: `HttpStatus.UNPROCESSABLE_ENTITY` è deprecato, il nome corrente è `UNPROCESSABLE_CONTENT`.

✅ **Controllo:**

```bash
./mvnw clean compile -Dmaven.compiler.showDeprecation=true | grep -E "WARNING|ERROR"      # nessuna riga
```

## Passo 7 – I test (30 min)

```bash
./mvnw -q test-compile
```

Vedrete errori in 3 file di test. Nel `pom.xml` aggiungete lo starter di test mirato
`spring-boot-starter-webmvc-test` e il modulo `spring-boot-resttestclient` (scope `test`), poi correggete:

| File | Problema | Correzione |
|---|---|---|
| `OrdineControllerVersioniTest` | `@WebMvcTest` ha cambiato package | `org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest` |
| `FlussoOrdineTest` | `TestRestTemplate` ha cambiato package e non si registra più da solo | `org.springframework.boot.resttestclient.TestRestTemplate` + `@AutoConfigureTestRestTemplate` (`org.springframework.boot.resttestclient.autoconfigure`) |
| `ContrattoJsonTest` | Jackson 2 | import `tools.jackson...` (stesse regole del Passo 4) |
| `TestcontainersConfiguration` | compila, ma con 2 warning: `PostgreSQLContainer` del vecchio package è deprecato | `org.testcontainers.postgresql.PostgreSQLContainer` (non è più generico: niente `<?>` né `<>`) |

Poi:

```bash
./mvnw verify
```

**Se avete fatto tutto giusto, non sarà verde.** Esaminate i fallimenti uno alla volta, **leggendo il messaggio**:

- `MagazzinoGatewayTest` fallisce in **tutti e 6** i test (conteggi a zero, risposte sbagliate, e in due casi
  `I/O error on GET request for "http://localhost:8081/..."`). Il server finto del test è su un'altra porta:
  perché il client non lo trova? (Suggerimento: guardate `@DynamicPropertySource` nel test e il Passo 6.)
- `RegistroAuditTest` fallisce: confrontate `expected` e `but was`. **Che cosa è cambiato nel JSON?**
  La riga di audit è letta da strumenti esterni, che si aspettano ancora il numero: **non** modificate il test.
  Correggete il mapper in `RegistroAudit` (suggerimento: `tools.jackson.databind.cfg.DateTimeFeature`).
- `ContrattoJsonTest` resta verde. È una buona o una cattiva notizia? Perché il mapper di Boot e quello di
  `RegistroAudit` si comportano in modo diverso? Annotatelo.

✅ **Controllo:** `./mvnw verify` → **35 test verdi**.

## Passo 8 – Le proprietà che non si vedono (10 min)

Una proprietà non più riconosciuta **non fa fallire l'avvio**: viene ignorata. Lo strumento per scovarle:

1. Aggiungete al `pom.xml` (`scope` runtime) `org.springframework.boot:spring-boot-properties-migrator`.
2. `./mvnw spring-boot:run` e leggete il log di avvio: cercate `renamed`.
3. Correggete `application.properties` con la chiave nuova suggerita.
4. **Togliete** la dipendenza dal `pom.xml` (si usa solo durante la migrazione) e riavviate: il messaggio non c'è più.

✅ **Controllo:** annotate nel `MIGRAZIONE.md` la chiave vecchia e quella nuova.

## Passo 9 – Verifica a runtime e confronto del contratto (15 min)

I test verdi non bastano. Avviate ShopFlow sulla 4.1.2 e rifate la fotografia:

```bash
./mvnw spring-boot:run                                   # terminale 1
scripts/cattura-contratto.sh dopo-4.1.2.txt              # terminale 2
diff baseline-3.5.txt dopo-4.1.2.txt
```

Controllate anche:

```bash
./mvnw dependency:tree | grep -E "hibernate-core|spring-core|jackson-databind|testcontainers:|spring-modulith-core"
```

e, nel log dell'applicazione, la riga `AUDIT {...}` della creazione ordine.

✅ **Controllo:** il `diff` mostra **2 differenze**, entrambe nel corpo JSON degli errori 409 e 422
(`application/problem+json`). Descrivete esattamente che cosa è cambiato, poi **decidete** e motivate nel
`MIGRAZIONE.md`: accettate la differenza (e a chi lo comunicate) o la neutralizzate? (Suggerimento: leggete che cosa dice
la RFC 9457 sul campo che manca.) Annotate anche le nuove versioni di Hibernate, Spring, Jackson.

Committate: **fine Fase A**.

---

# FASE B – Adottare le novità di Boot 4 (opzionale ma consigliata)

La Fase A fa funzionare il progetto. La Fase B usa ciò che Boot 4 aggiunge. Si fa **dopo** la migrazione, mai insieme.

## Passo 10 – Versionamento nativo delle API (30 min)

Oggi `OrdineController` sceglie la versione con la condizione `headers = "X-API-Version=2.0"`: una versione
sconosciuta (`9.9`) finisce, in silenzio, sulla v1. Nel contratto della Fase A questa è la riga
`GET /ordini/{id} (X-API-Version: 9.9)` → `200`.

1. Create `comune.web.WebConfig` (`WebMvcConfigurer`) con `configureApiVersioning`: versione letta dall'header
   `X-API-Version`, versioni supportate `1.0` e `2.0`, versione non obbligatoria, default `1.0`.
2. In `OrdineController` sostituite le due condizioni con `@GetMapping(path = "/{id}", version = "1.0")` e `version = "2.0"`.
3. In `OrdineControllerVersioniTest` il test `versioneSconosciutaRiceveLaV1` non descrive più il comportamento:
   riscrivetelo con il nome `versioneNonSupportataDa400` e verificate **400**.
4. `./mvnw verify` e un nuovo `scripts/cattura-contratto.sh` → `diff` con la baseline.

✅ **Controllo:** nel `diff` ora compare anche la riga della versione `9.9`: da `200` a `400`.
❓ Per un'app mobile ancora in produzione che manda `X-API-Version: 3.0`: è una buona o una cattiva notizia?

## Passo 11 – `RestTestClient` al posto di `TestRestTemplate` (20 min)

`TestRestTemplate` in Boot 4.1 funziona ancora, ma è destinato alla deprecazione. Riscrivete `FlussoOrdineTest` con
`RestTestClient` (`org.springframework.test.web.servlet.client.RestTestClient`, annotazione `@AutoConfigureRestTestClient`
nello stesso package di `@AutoConfigureTestRestTemplate`): `client.post().uri(...).body(...).exchange().expectStatus()...`
e `.expectBody().jsonPath(...)` al posto di `ResponseEntity` e `Map`.

✅ **Controllo:** 35 test verdi, `FlussoOrdineTest` senza più `TestRestTemplate`.

## Passo 12 – Chiusura (15 min)

```bash
./mvnw clean verify -Dmaven.compiler.showDeprecation=true       # 35 verdi, 0 warning
git log --oneline                                               # un commit per passo
```

Completate il `MIGRAZIONE.md` (modello qui sotto) e consegnate il branch.

---

# Modello di `MIGRAZIONE.md` (da compilare)

```markdown
# Migrazione ShopFlow 3.5.16 → 4.1.2

## Versioni
| Componente | Prima | Dopo |
|---|---|---|
| Spring Boot | 3.5.16 | 4.1.2 |
| Spring Framework | | |
| Hibernate ORM | | |
| Jackson | | |
| Testcontainers | | |
| Spring Modulith | | |

## OpenRewrite (Passo 2)
- Eseguita: sì / no (perché)
- Versione di Boot a cui ha portato il parent:
- Cosa ha fatto / cosa NON ha fatto:

## Errori incontrati
| # | Passo | Messaggio (o sintomo) | Causa | Come l'ho risolto |
|---|---|---|---|---|

## Previsioni (Passo 4) e risultato
- Previsione sui test JSON:
- Cosa è successo davvero e perché:

## Differenze nel contratto HTTP (Passo 9)
| Cosa è cambiato | Decisione (accetto / neutralizzo) | Motivazione |
|---|---|---|

## Proprietà rinominate (Passo 8)
| Chiave vecchia | Chiave nuova |
|---|---|

## Checklist
- [ ] Parent 4.1.2; starter modulari; starter di test per tecnologia
- [ ] `@MockBean` → `@MockitoBean`
- [ ] Jackson: import `tools.jackson`, niente `catch (JsonProcessingException)`, test JSON non basati su stringhe intere
- [ ] `spring-boot-properties-migrator` usato **e rimosso**
- [ ] Spring Retry sostituito da `@Retryable` del Framework (conteggio tentativi verificato dai test)
- [ ] Client HTTP con `@ImportHttpServices`; null-safety con JSpecify
- [ ] Avvio reale, contratto HTTP confrontato prima/dopo
- [ ] Hibernate verificato con `dependency:tree`
- [ ] 0 warning di deprecazione

## Cosa mi ha sorpreso
```

---

# Come viene valutato

| Criterio | Peso |
|---|---|
| Fase A completata: 35 test verdi, 0 warning, nessun `@Disabled` né `-DskipTests` | 40% |
| Contratto HTTP confrontato prima/dopo e differenze spiegate con una decisione motivata | 20% |
| `MIGRAZIONE.md`: errori diagnosticati (causa, non solo rimedio), previsioni confrontate col risultato | 20% |
| Commit piccoli e con messaggi chiari; `properties-migrator` rimosso | 10% |
| Fase B (anche parziale) | 10% |

## Se siete bloccati

1. Leggete il messaggio **per intero**, dall'alto: la prima riga è quasi sempre quella che conta.
2. Il riferimento del corso (`02_riferimento_spring-boot-3-5-4-1-4-2-confronto.md`, sezioni 2.1–2.8) spiega ogni voce
   con "Cosa si vede".
3. Cercate il nome della classe vecchia: spesso la nuova ha lo stesso nome in un altro package.
4. Un errore che non capite dopo 10 minuti si chiede al docente: non è tempo perso, è il mestiere.
