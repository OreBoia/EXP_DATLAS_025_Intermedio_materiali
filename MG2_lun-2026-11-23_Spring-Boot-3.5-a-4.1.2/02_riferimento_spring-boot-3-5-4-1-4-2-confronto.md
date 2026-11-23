# Spring Boot 3.5 → 4.1.2 → 4.2: confronto tra le versioni

Materiale di approfondimento per il corso **Java e Spring Boot Intermedio (EXP_DATLAS_025)**, modulo **MG2 – Migrazione da Spring Boot 3.5 a 4.1.2**. Il file `slide-da-spring-boot-3-5-a-4-1-2.md` resta il percorso operativo del laboratorio; questo file è il **riferimento di dettaglio**: per ogni novità mostra com'era in 3.5, com'è in 4.1.2, cosa cambierà in 4.2 e cosa succede davvero sotto il cofano.

Progetto di riferimento: **ShopFlow** (monolite Spring Boot 3.5 / Java 21 che durante il corso diventa Spring Boot 4.1.2 / Java 25).

---

## 0. Come leggere questo file

### 0.1 Struttura

| Sezione | Contenuto |
|---|---|
| 1 | Tabella delle versioni: 3.5 → 4.0 → 4.1.2 → 4.2 (date, supporto, librerie) |
| 2 | Da 3.5 a 4.1.2: ogni differenza con esempio *prima / dopo* e spiegazione approfondita |
| 3 | Da 4.1.2 a 4.2: il futuro, con le novità già documentate nelle milestone e nelle RC1/RC2 |
| 4 | Confronto a tre colonne (3.5 / 4.1.2 / 4.2) e cosa **non** cambia |
| 5 | Esempi reali su ShopFlow, checklist di migrazione, piano di verifica |
| Fonti | Documentazione ufficiale consultata |

Ogni voce delle sezioni 2 e 3 segue lo stesso schema:

- **Cosa cambia**: la differenza in due righe.
- **Prima (3.5)** e **Dopo (4.1.2 o 4.2)**: esempio di codice o configurazione molto breve.
- **Cosa succede davvero**: il meccanismo, non solo la sintassi.
- **Trappole**: gli errori tipici che si vedono migrando.
- **Cosa si vede**: l'effetto concreto (errore di avvio, test rosso, output diverso).
- **Stato verifica**: una delle tre etichette qui sotto.

### 0.2 Etichette di affidabilità

| Etichetta | Significato |
|---|---|
| **[V]** | Verificato su documentazione ufficiale (release notes, migration guide, reference) consultata durante la preparazione |
| **[?]** | Da verificare: informazione ricavata da fonti secondarie o non confermata in modo univoco. Questi punti sono stati spostati in `02b_punti-da-verificare.md` |
| **[GA]** | Riguarda Spring Boot 4.2 / Framework 7.1: si basa sulle note di milestone M1/M2 e delle release candidate RC1/RC2. La pagina ufficiale delle note 4.2 dichiara che le note complete usciranno con il rilascio. **Da ricontrollare dopo il GA previsto per il 19/11/2026** |

> **Nota importante.** Nessun esempio di questo file è stato compilato o eseguito: gli esempi sono costruiti sulla documentazione. Prima di mostrarli a lezione vanno provati su ShopFlow (vedi sezione 5.6).

> **Nota sulle date.** Il corso parte il 16/11/2026 e il GA di Spring Boot 4.2 è previsto per il 19/11/2026, cioè **durante la prima settimana**. Il corso lavora su 4.1.2 (versione supportata); la sezione 3 serve a dare agli studenti una mappa di ciò che arriva, non a migrare ShopFlow a 4.2.

---

## 1. Le versioni a confronto

### 1.1 Tabella generale

| | **3.5.x** | **4.0.x** | **4.1.2** (target del corso) | **4.2** (futuro) |
|---|---|---|---|---|
| Rilascio | 2025 (ultima OSS: 3.5.16) | novembre 2025 | 10/06/2026 | GA previsto 19/11/2026 **[GA]** |
| Spring Framework | 6.2 | 7.0 | 7.0.10 (BOM di 4.1.2) | 7.1 (7.1.0-RC2) **[GA]** |
| Spring Security | 6.5.0 | 7.0 | 7.1.0 | 7.2 (milestone) **[GA]** |
| Jakarta EE | 10 | 11 (Servlet 6.1) | 11 | 11 |
| Server Tomcat | 10.1 | 11 | 11 (11.0.27 sulla pagina delle coordinate) | 11 |
| Jackson | 2 | 3.0 (Jackson 2 deprecato) | 3.x | 3.1 (BOM 3.1.8 nella RC2) **[GA]** |
| Hibernate ORM | – | 7.1 (GA 4.0.0), poi 7.2 da 4.0.1 | 7.4.13.Final (verificato su ShopFlow con `dependency:tree`) | 7.4.14.Final (RC2) **[GA]** |
| JUnit | Jupiter 5.12 | 6 | 6 | Jupiter 6.1.3 **[GA]** |
| Micrometer | 1.15.0 | – | 1.17.0 (Tracing 1.7.0) | 1.18 (Tracing 1.8) **[GA]** |
| Kafka client | 3.9 | – | 4.1.0 | 4.2 **[GA]** |
| Java minimo | 17 | 17 | 17 | – |
| Supporto open source fino a | **terminato il 30/06/2026** | 31/12/2026 | **31/07/2027** | 31/12/2027 **[GA]** |
| Supporto commerciale fino a | 30/06/2032 | – | – | 31/12/2028 **[GA]** |

**Nota 1 – Hibernate.** Le fonti consultate non concordano con il materiale di progetto, ed è importante saperlo:

- il materiale di progetto (`MG2` e la tabella delle novità) parla di Hibernate **7.1**: è un dato **superato**;
- Boot 4.0.0 (GA) gestisce Hibernate **7.1**; dalla 4.0.1 la versione gestita è **7.2** (le note 4.0.1 riportano l'upgrade a 7.2.0.Final);
- Boot 4.1.x gestisce Hibernate **7.4.x**: le note 4.1.0 riportano l'upgrade a 7.4.1.Final e le note 4.1.1 a 7.4.5.Final; la pagina delle coordinate di Boot 4.1 riporta `hibernate-core` **7.4.13.Final**, e le note della RC2 di Boot 4.2 riportano **7.4.14.Final**;
- il Framework 7.1 dichiara come baseline minima ORM 7.3 (quindi 7.4.x è compatibile).

Conclusione (verificata il 23/11/2026): con Boot 4.1.2 la versione gestita è Hibernate **7.4.13.Final**. Lo dicono sia il BOM `spring-boot-dependencies-4.1.2` (`hibernate.version`) sia `./mvnw dependency:tree | grep hibernate-core` sul progetto ShopFlow (cartella `shopflow/` accanto ai materiali). La slide `MG2` è stata corretta (7.1 → 7.4).

### 1.2 Cosa significa il calendario di supporto

| Data | Evento |
|---|---|
| 10/06/2026 | Rilascio Spring Boot 4.1 |
| 30/06/2026 | Fine supporto open source di Boot 3.5 |
| 16/11/2026 | Inizio del corso |
| 19/11/2026 | GA previsto di Spring Boot 4.2 **[GA]** |
| 31/12/2026 | Fine supporto open source di Boot 4.0 |
| 31/07/2027 | Fine supporto open source di Boot 4.1 |
| 31/12/2027 | Fine supporto open source di Boot 4.2 **[GA]** |

**Come funziona?** Spring Boot rilascia una minor ogni sei mesi circa e supporta ciascuna versione in open source per circa 13 mesi (la 4.1 fino al 31/07/2027). Una versione che esce dal supporto OSS non riceve più nemmeno le correzioni di sicurezza gratuite: l'ultima 3.5 OSS è la 3.5.16, e dopo quella chi resta su 3.5 deve pagare il supporto commerciale (fino al 30/06/2032) oppure migrare.

**Perché il corso migra a 4.1.2 e non a 4.0.** La 4.0 esce dal supporto OSS il 31/12/2026, cioè a un mese e mezzo dall'inizio del corso: migrare ShopFlow a una versione che scade subito vanificherebbe lo scopo. La 4.1 dà tempo fino a luglio 2027. **[V]**

"Siamo su 3.5, la 4.0 è la successiva, passiamo alla 4.0": errore tipico. In produzione, dal giugno 2026, si salta direttamente alla 4.1.x (la 4.0 è già in scadenza).

**Stato verifica:** date di supporto **[V]**; date 4.2 **[GA]**.

### 1.3 L'ordine di migrazione consigliato

1. **Java 21 → 25** (il tema della slide `slide-java-17-25-27-confronto.md`): si fa per primo perché è indipendente da Spring.
2. **Ultima 3.5.x** (3.5.16): così le deprecazioni di 3.x sono già segnalate come warning e non come errori.
3. **OpenRewrite** `UpgradeSpringBoot_4_0` per la parte meccanica.
4. **Correzioni manuali** (test, Jackson, proprietà).
5. **Controlli a runtime**: avvio, test, output JSON, metriche, sicurezza.
6. Solo dopo, e con calma: salire alla 4.1.x e adottare le novità (API versioning, HTTP service client, resilienza).

> Il percorso 3.5 → 4.1.2 passa **comunque** dalle regole di migrazione della 4.0: la 4.1 aggiunge poche rotture, la 4.0 è dove sta il grosso del lavoro.

---

## 2. Da Spring Boot 3.5 a 4.1.2

Le voci 2.1–2.9 vengono quasi tutte da **Boot 4.0 / Framework 7.0** (è lì che si rompe il codice). Le voci 2.10–2.12 sono le novità e le rotture introdotte dalla **4.1**.

### 2.1 Starter modulari (e starter "ponte")

**Cosa cambia.** In 3.5 un solo `spring-boot-starter-web` portava dentro MVC, JSON, validazione e client. In 4.0 Boot è diviso in moduli: ogni tecnologia ha il proprio starter, e ogni starter ha il proprio starter di test.

**Prima (3.5) – `pom.xml`:**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

**Dopo (4.1.2) – `pom.xml`:**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-restclient</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc-test</artifactId>
    <scope>test</scope>
</dependency>
```

**Cosa succede davvero.**

- Lo starter `spring-boot-starter-web` esiste ancora ma è **deprecato**: serve da ponte, e punta a `webmvc`. Lo stesso vale per `aop` → `aspectj` e per gli starter `oauth2-*` → `security-oauth2-*`.
- I nuovi starter sono più piccoli: un'applicazione che non usa il client HTTP non si porta più dietro `RestClient`. Per questo, chi chiamava `RestClient` o `RestTemplate` deve aggiungere esplicitamente `spring-boot-starter-restclient` (o `-webclient` per il client reattivo).
- Esistono anche gli starter "classici" come soluzione temporanea: permettono di compilare ed eseguire subito, rimandando la divisione in moduli a un secondo momento.
- Per i test, ogni tecnologia ha lo starter `-<tecnologia>-test` (es. `spring-boot-starter-webmvc-test`, `spring-boot-starter-data-jpa-test`): le annotazioni di slice (`@WebMvcTest`, `@DataJpaTest`) vivono lì.

**Trappole.**

- Compila tutto ma a runtime manca un bean (`RestClient.Builder`, `JsonMapper`…): quasi sempre manca uno starter.
- Aggiungere lo starter classico "per far passare tutto" e dimenticarlo lì: si perde il vantaggio dei moduli piccoli.
- I test di slice smettono di compilare: manca `-<tecnologia>-test`.

**Cosa si vede.** `package org.springframework.boot.test.autoconfigure.web.servlet does not exist`, oppure `NoSuchBeanDefinitionException` per `RestClient.Builder` all'avvio.

**Stato verifica:** **[V]** (Migration Guide 4.0)

---

### 2.2 Jackson 3 al posto di Jackson 2

**Cosa cambia.** Il motore JSON passa da Jackson 2 a Jackson 3. Cambiano i package, la classe principale, il modello delle eccezioni e **due default che modificano l'output senza toccare il codice**.

**Prima (3.5):**

```java
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrdineJson {

    private final ObjectMapper mapper;

    public OrdineJson(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public String serializza(Ordine ordine) {
        try {
            return mapper.writeValueAsString(ordine);
        } catch (JsonProcessingException e) {          // eccezione CHECKED
            throw new IllegalStateException(e);
        }
    }
}
```

**Dopo (4.1.2):**

```java
import tools.jackson.databind.json.JsonMapper;

public class OrdineJson {

    private final JsonMapper mapper;

    public OrdineJson(JsonMapper mapper) {
        this.mapper = mapper;
    }

    public String serializza(Ordine ordine) {
        return mapper.writeValueAsString(ordine);      // JacksonException: UNCHECKED
    }
}
```

**Cosa succede davvero.**

| Aspetto | Jackson 2 (Boot 3.5) | Jackson 3 (Boot 4.x) |
|---|---|---|
| Package | `com.fasterxml.jackson.*` | `tools.jackson.*` |
| Eccezione base | `JsonProcessingException` (checked) | `JacksonException` (unchecked) |
| Mapper | `ObjectMapper` mutabile | `JsonMapper` immutabile, creato con un builder |
| Date | numeri (timestamp) | stringhe ISO-8601 |
| Ordine delle proprietà | ordine di dichiarazione | **alfabetico** |
| Token finali dopo il JSON | ignorati | errore (`FAIL_ON_TRAILING_TOKENS` attivo) |
| Moduli | solo quelli "noti" (es. `JavaTimeModule`) | rilevati automaticamente dal classpath |
| Bean di Boot | `ObjectMapper` | `JsonMapper` (e `XmlMapper`) |

Le **annotazioni** (`@JsonProperty`, `@JsonIgnore`, `@JsonFormat`…) restano nel package `com.fasterxml.jackson.annotation`: il codice dei modelli non cambia. Cambiano gli import di tutto il resto.

Gli hook di Boot hanno nomi nuovi:

| Boot 3.5 / Jackson 2 | Boot 4.x / Jackson 3 |
|---|---|
| `@JsonComponent` | `@JacksonComponent` |
| `@JsonMixin` | `@JacksonMixin` |
| `Jackson2ObjectMapperBuilderCustomizer` | `JsonMapperBuilderCustomizer` |

**Esempio – personalizzare il mapper:**

```java
@Bean
JsonMapperBuilderCustomizer jacksonCustomizer() {
    return builder -> builder.enable(SerializationFeature.INDENT_OUTPUT);
}
```

**Come tornare al vecchio comportamento (per migrare con calma):**

```properties
# riproduce i default di Jackson 2: date come timestamp, ordine non alfabetico, ecc.
spring.jackson.use-jackson2-defaults=true
```

Con questa proprietà il `JsonMapper` si comporta come un `ObjectMapper` di Jackson 2, **anche senza Jackson 2 sul classpath**. È pensata come ponte, non come configurazione definitiva.

**L'esempio che spiega tutto – stesso codice, JSON diverso:**

```java
public record OrdineDto(Long id, String cliente, LocalDateTime creatoIl) {}
```

```json
// Boot 3.5 (Jackson 2): ordine di dichiarazione del record
{"id":42,"cliente":"Rossi","creatoIl":"2026-11-16T09:30:00"}

// Boot 4.1.2 (Jackson 3): ordine alfabetico delle proprietà
{"cliente":"Rossi","creatoIl":"2026-11-16T09:30:00","id":42}
```

**Trappole.**

- **Test con confronto di stringhe JSON**: `assertEquals("{\"id\":42,\"cliente\":\"Rossi\"}", json)` diventa rosso solo per l'ordine delle chiavi. Soluzione: confrontare JSON strutturalmente (`jsonPath`, `.json(...)` di `RestTestClient`/`MockMvc` in modalità non strict), mai come testo.
- **Il catch inutile**: `catch (JsonProcessingException e)` non compila più (la classe non esiste in Jackson 3 sotto quel package). Va tolto, non sostituito.
- **Librerie terze ancora su Jackson 2**: restano supportate come dipendenze transitive, ma Boot configura automaticamente **solo Jackson 3**. Chi ha bisogno di Jackson 2 usa il modulo ponte `spring-boot-jackson2` (da usare solo se indispensabile: Jackson 2 è deprecato nel portfolio Spring).
- **Moduli auto-rilevati**: un modulo Jackson presente nel classpath ora viene registrato da solo e può cambiare la forma del JSON. Si controlla con `spring.jackson.find-and-add-modules`.
- **Nomi delle proprietà di configurazione**: vedi sotto.

**Proprietà `spring.jackson.*` – cosa è cambiato in due tappe:**

| Boot | Proprietà per lettura/scrittura JSON |
|---|---|
| 3.5 | `spring.jackson.read.*` e `spring.jackson.write.*` (caratteristiche di lettura/scrittura del JSON) |
| 4.0 | le stesse impostazioni si spostano sotto il formato: `spring.jackson.json.read.*` e `spring.jackson.json.write.*` |
| 4.1 | `spring.jackson.read.*` e `spring.jackson.write.*` tornano come proprietà **comuni** a più formati (JSON, CBOR, XML); si aggiungono le proprietà `spring.jackson.factory.*` e tre customizer: `JsonFactoryBuilderCustomizer`, `CborFactoryBuilderCustomizer`, `XmlFactoryBuilderCustomizer` |

**Cosa si vede.** Un `application.yml` con `spring.jackson.read.*` scritto per 3.5 che, migrando a 4.0, smette di avere effetto **senza errori**: con `spring-boot-properties-migrator` (vedi 2.4) l'avvio segnala la chiave rinominata. Le chiavi hanno cambiato posto due volte tra 3.5 e 4.1: vanno sempre confrontate con la reference della versione in uso.

**Stato verifica:** differenze Jackson 2/3, default, bean e hook **[V]**; proprietà 4.0 e 4.1 **[V]** (Migration Guide 4.0 e Release Notes 4.1).

---

### 2.3 Test: `@MockBean`, `@AutoConfigureMockMvc`, `RestTestClient`

**Cosa cambia.** Tre rotture ai test, tutte ben visibili al primo `mvn test`.

**Prima (3.5):**

```java
@SpringBootTest
@AutoConfigureMockMvc
class OrdineControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean MagazzinoClient magazzino;         // ← rimossa in 4.0

    @Test
    void creaOrdine() throws Exception {
        mockMvc.perform(post("/api/ordini")...)
               .andExpect(status().isCreated());
    }
}
```

**Dopo (4.1.2):**

```java
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@AutoConfigureMockMvc                            // ora indispensabile con @SpringBootTest
class OrdineControllerTest {

    @Autowired MockMvc mockMvc;
    @MockitoBean MagazzinoClient magazzino;      // del Framework, non di Boot

    @Test
    void creaOrdine() throws Exception {
        mockMvc.perform(post("/api/ordini")...)
               .andExpect(status().isCreated());
    }
}
```

**Cosa succede davvero.**

- `@MockBean` e `@SpyBean` erano annotazioni di **Spring Boot**; sono state **rimosse**. Le sostituiscono `@MockitoBean` e `@MockitoSpyBean`, che appartengono al **Spring Framework** (meccanismo "bean override") e funzionano con tutti i `TestContext`, non solo con quelli di Boot.
- `@SpringBootTest` non configura più `MockMvc` da solo: serve `@AutoConfigureMockMvc`.
- `TestRestTemplate` richiede `@AutoConfigureTestRestTemplate` per essere iniettato.
- Nasce `RestTestClient`: un client di test con API fluente (`.get().uri(...).exchange().expectStatus()...`) basato su `RestClient`. Si usa con `@AutoConfigureRestTestClient` e può puntare a un'applicazione reale (`bindToServer`) o a un MockMvc (nessun server avviato).

**Esempio con `RestTestClient`:**

```java
@WebMvcTest(OrdineController.class)
@AutoConfigureRestTestClient
class OrdineControllerRestTestClientTest {

    @Autowired RestTestClient client;
    @MockitoBean OrdineService service;

    @Test
    void leggeUnOrdine() {
        when(service.trova(42L)).thenReturn(new OrdineDto(42L, "Rossi", LocalDateTime.of(2026, 11, 16, 9, 30)));

        client.get().uri("/api/ordini/42")
              .exchange()
              .expectStatus().isOk()
              .expectBody()
              .jsonPath("$.cliente").isEqualTo("Rossi");
    }
}
```

**Trappole.**

- Sostituire `@MockBean` con `@MockitoBean` e dimenticare l'import: il nuovo package è `org.springframework.test.context.bean.override.mockito`.
- Il corpo della risposta va **consumato** (`expectBody(...)`) in test con rilevamento di leak dei buffer: altrimenti il tool segnala buffer non rilasciati.
- `TestRestTemplate` verrà **deprecato in 4.2** (vedi 3.1): per i nuovi test conviene già usare `RestTestClient`.

**Cosa si vede.** `cannot find symbol: class MockBean` (compilazione), oppure `NoSuchBeanDefinitionException: MockMvc` / `TestRestTemplate` (esecuzione).

**Stato verifica:** rimozioni e sostituzioni **[V]**.

---

### 2.4 Rimozioni e cambiamenti di default

**Cosa cambia.** Alcune funzioni sono state eliminate, non solo deprecate.

| Rimosso o cambiato in 4.0 | Cosa fare |
|---|---|
| **Undertow** (server) | usare Tomcat (default) o Jetty 12.1 |
| **Pulsar Reactive** | usare Pulsar non reattivo |
| **Script di avvio "launch scripts"** (file jar eseguibile come servizio) | usare `systemd` o un container |
| **Spock** (supporto di test) | rimosso in 4.0, **ripristinato in 4.1** (con Spock 2.4, che supporta Groovy 5) |
| Gestione di **Spring Retry** da parte delle dipendenze di Boot | usare i nuovi `@Retryable` del Framework 7 (vedi 2.7) |
| **Loader classico** dei fat jar | usare il loader standard |
| Alcune proprietà rinominate | vedi tabella sotto |

**Proprietà rinominate (esempi):**

| Prima (3.5) | Dopo (4.x) |
|---|---|
| `management.tracing.enabled` | `management.tracing.export.enabled` |
| `spring.dao.exceptiontranslation.enabled` | `spring.persistence.exceptiontranslation.enabled` |
| `spring.data.mongodb.*` (molte chiavi: `uri`, `host`, `port`, `database`, `username`…) | `spring.mongodb.*` (le chiavi che richiedono ancora Spring Data MongoDB, es. `auto-index-creation`, restano sotto `spring.data.mongodb.*`) |
| `spring.session.redis.*`, `spring.session.mongodb.*` | `spring.session.data.redis.*`, `spring.session.data.mongodb.*` |

> Righe persistenza, MongoDB e sessioni: **[V]** sulla Migration Guide 4.0.

**Cosa succede davvero.** Una proprietà non più riconosciuta **non fa fallire l'avvio**: viene ignorata. È uno dei motivi per cui una migrazione "funziona" ma si comporta diversamente (tracing che non esporta, sessioni che non si salvano nello store giusto).

**Lo strumento che risolve il problema:**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-properties-migrator</artifactId>
    <scope>runtime</scope>
</dependency>
```

All'avvio stampa un elenco di proprietà deprecate o rinominate, con la chiave nuova. Si usa **solo durante la migrazione**: va rimosso a lavoro finito (rallenta l'avvio e non serve in produzione).

**Trappole.**

- Lasciare `spring-boot-properties-migrator` nel `pom` di produzione.
- Considerare il log "properties migrator" un elenco completo: segnala solo ciò che Boot sa mappare.

**Cosa si vede.** Un log di avvio con `The use of configuration keys that have been renamed was found in the environment: ... Replacement: ...`.

**Stato verifica:** **[V]** (Migration Guide 4.0).

---

### 2.5 Versionamento delle API (`version`)

**Cosa cambia.** Il Framework 7 introduce il versionamento delle API come funzione di prima classe; in 3.5 si usavano URL con `/v1/`, `/v2/` o logica fatta a mano.

**Prima (3.5):**

```java
@RestController
class OrdineControllerV1 {
    @GetMapping("/api/v1/ordini/{id}")
    OrdineV1 leggi(@PathVariable Long id) { ... }
}

@RestController
class OrdineControllerV2 {
    @GetMapping("/api/v2/ordini/{id}")
    OrdineV2 leggi(@PathVariable Long id) { ... }
}
```

**Dopo (4.1.2):**

```properties
# come si legge la versione: da un header
spring.mvc.apiversion.use.header=API-Version
```

```java
@RestController
@RequestMapping("/api/ordini")
class OrdineController {

    @GetMapping(path = "/{id}", version = "1.0")
    OrdineV1 leggiV1(@PathVariable Long id) { ... }

    @GetMapping(path = "/{id}", version = "2.0")
    OrdineV2 leggiV2(@PathVariable Long id) { ... }
}
```

Chiamata: `GET /api/ordini/42` con header `API-Version: 2.0`.

**Cosa succede davvero.**

- Il mapping `@GetMapping` ha un nuovo attributo **`version`**. La stessa URL può avere più metodi, scelti in base alla versione richiesta.
- La versione può arrivare da **header, parametro, segmento del path o media type**: si sceglie con la configurazione (`spring.mvc.apiversion.use.*`, oppure `configureApiVersioning` in `WebMvcConfigurer`, come nell'esempio di MG2).
- Tre componenti si possono personalizzare: `ApiVersionResolver` (estrae la versione dalla richiesta), `ApiVersionParser` (la interpreta: di default come versione semantica) e `ApiVersionDeprecationHandler` (aggiunge le intestazioni `Deprecation`, `Sunset` e `Link` per avvisare i client).
- Un valore come `version = "1.2+"` significa "dalla 1.2 in avanti": è una **baseline**, non una versione esatta.
- Sul lato **client** `RestClient` può inserire la versione da solo (`ApiVersionInserter`), e `@HttpExchange` accetta l'attributo `version`. In configurazione: `spring.http.client.restclient.apiversion.insert.header=API-Version`.

**Trappole.**

- Con `setVersionRequired(false)` e nessun default configurato, una richiesta senza header **non** trova un mapping versionato: serve `setDefaultVersion("1.0")` (come in MG2).
- Mescolare metodi con e senza `version` sulla stessa URL: il comportamento va provato, non dato per scontato.

**Cosa si vede.** Un test `MockMvc` che chiama `/api/ordini/42` con `API-Version: 2.0` e legge il corpo della V2; senza header, la V1 (se è la versione di default).

**Stato verifica:** meccanismo, proprietà e componenti **[V]**.

---

### 2.6 HTTP service client dichiarativi (`@ImportHttpServices`)

**Cosa cambia.** In 3.5 un'interfaccia `@HttpExchange` richiedeva una `@Bean` con `HttpServiceProxyFactory` scritta a mano. In 4.x basta un'annotazione e due righe di configurazione.

**Prima (3.5):**

```java
@Configuration
class MagazzinoClientConfig {

    @Bean
    MagazzinoClient magazzinoClient(RestClient.Builder builder) {
        RestClient restClient = builder.baseUrl("http://magazzino:8081").build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient)).build();
        return factory.createClient(MagazzinoClient.class);
    }
}
```

**Dopo (4.1.2):**

```java
@Configuration
@ImportHttpServices(group = "magazzino", types = MagazzinoClient.class)
class MagazzinoClientConfig { }
```

```properties
spring.http.serviceclient.magazzino.base-url=http://magazzino:8081
spring.http.serviceclient.magazzino.read-timeout=2s
```

```java
@HttpExchange("/giacenze")
interface MagazzinoClient {

    @GetExchange("/{sku}")
    @Nullable Giacenza leggi(@PathVariable String sku);

    @PostExchange("/prenotazioni")
    Prenotazione prenota(@RequestBody RichiestaPrenotazione richiesta);
}
```

**Cosa succede davvero.**

- `@ImportHttpServices` registra come bean le interfacce elencate; il **gruppo** (`group = "magazzino"`) è la chiave per la configurazione `spring.http.serviceclient.<gruppo>.*`.
- Il client sotto è un `RestClient` configurato da Boot: eredita i convertitori JSON (Jackson 3), il tracing e le metriche.
- Il valore `@Nullable` e `@NullMarked` (vedi 2.8) dicono esplicitamente se il client può restituire `null`.

**Trappole.**

- Gruppo scritto diversamente nel codice e nelle proprietà (`magazzino` / `Magazzino`): le proprietà vengono ignorate senza errore, il client punta a un URL vuoto.
- Dimenticare lo starter `spring-boot-starter-restclient` (vedi 2.1).
- Errori HTTP 4xx/5xx diventano eccezioni: nel codice di ShopFlow si traducono in `Result` / `ErroreOrdine` al confine del client (come in MG2), senza far uscire eccezioni HTTP dal dominio.

**Cosa si vede.** Con il timeout a `2s` e un magazzino lento, `ResourceAccessException` dopo 2 secondi; senza la proprietà, nessun timeout di lettura (rischio di thread bloccati).

**Stato verifica:** **[V]** (reference Boot "REST clients").

---

### 2.7 Resilienza nel Framework 7: `@Retryable` e `@ConcurrencyLimit`

**Cosa cambia.** Le funzioni di ritentativo prima erano nella libreria separata **Spring Retry** (`spring-retry`, `@EnableRetry`). Nel Framework 7 sono nel core: `@Retryable`, `@ConcurrencyLimit`, `RetryTemplate`, `RetryPolicy`.

**Prima (3.5, Spring Retry):**

```java
@Configuration
@EnableRetry
class RetryConfig { }

@Service
class PagamentoService {

    @Retryable(retryFor = PagamentoNonDisponibile.class,
               maxAttempts = 4,
               backoff = @Backoff(delay = 200, multiplier = 2))
    Ricevuta addebita(Ordine ordine) { ... }
}
```

**Dopo (4.1.2, Framework 7):**

```java
@Configuration
@EnableResilientMethods
class ResilienzaConfig { }

@Service
class PagamentoService {

    @Retryable(includes = PagamentoNonDisponibile.class,
               maxRetries = 3,
               delay = 200, multiplier = 2, maxDelay = 2000, jitter = 50)
    Ricevuta addebita(Ordine ordine) { ... }

    @ConcurrencyLimit(10)
    Ricevuta addebitaConLimite(Ordine ordine) { ... }
}
```

**Cosa succede davvero.**

| Attributo | Significato |
|---|---|
| `includes` / `excludes` | quali eccezioni fanno ritentare (o non ritentare) |
| `maxRetries` | numero di **ritentativi**, default 3 |
| `delay` | attesa tra i tentativi, default 1 secondo (in millisecondi nell'annotazione) |
| `multiplier` | fattore di crescita dell'attesa (backoff esponenziale) |
| `maxDelay` | tetto all'attesa |
| `jitter` | variazione casuale, per evitare che molti client ritentino nello stesso istante |
| `predicate` | condizione personalizzata |

- **Attenzione al conteggio.** In Spring Retry `maxAttempts = 4` voleva dire 4 tentativi in tutto. Nel Framework 7 `maxRetries = 3` vuol dire 1 tentativo iniziale **+ 3 ritentativi = 4 tentativi in tutto**. Il numero cambia di uno: errore tipico di traduzione.
- `@EnableResilientMethods` abilita sia `@Retryable` sia `@ConcurrencyLimit`.
- `@ConcurrencyLimit(10)` fa aspettare le chiamate in eccesso (un'undicesima chiamata contemporanea attende un posto libero); non le scarta.
- Per codice non annotato esistono `RetryTemplate` e `RetryPolicy`: `invoke` rilancia l'**ultima eccezione originale**, `execute` lancia una `RetryException` che incapsula le cause.
- Il ritentativo avviene **in un proxy**: l'auto-chiamata (`this.addebita(...)`) non passa dal proxy e non ritenta (stesso problema già visto per `@Transactional` in MG5).

**Trappole.**

- Ritentare operazioni **non idempotenti** (un addebito): un ritentativo dopo un timeout può duplicare il pagamento. Prima del retry servono una chiave di idempotenza o una verifica dello stato (tema di MG9).
- `@Retryable` senza `includes`: ritenta qualsiasi eccezione, anche errori di validazione che non guariranno mai.
- Dimenticare `@EnableResilientMethods`: le annotazioni sono accettate ma **non fanno nulla**.

**Cosa si vede.** Nei log, tre tentativi distanziati 200 ms, 400 ms, 800 ms (con jitter) e poi l'eccezione originale; nei test, `MethodRetryEvent` pubblicato a ogni tentativo.

**Stato verifica:** attributi, semantica di `maxRetries` e `RetryTemplate` **[V]** (reference Framework 7).

---

### 2.8 Null-safety con JSpecify

**Cosa cambia.** Le annotazioni `@Nullable` di Spring (e quelle di altre librerie) vengono sostituite da **JSpecify**: uno standard unico per dire "questo valore può essere `null`".

**Prima (3.5):**

```java
import org.springframework.lang.Nullable;     // annotazione di Spring (deprecata)

@Nullable
Giacenza leggi(String sku);
```

**Dopo (4.1.2):**

```java
// package-info.java
@NullMarked
package it.formminds.shopflow.magazzino;

import org.jspecify.annotations.NullMarked;
```

```java
import org.jspecify.annotations.Nullable;

@Nullable Giacenza leggi(String sku);          // può restituire null
Giacenza leggiObbligatoria(String sku);        // per default NON restituisce null
```

**Cosa succede davvero.**

- `@NullMarked` su un package (o classe) dichiara: **tutto qui dentro è non-null, salvo dove c'è `@Nullable`**. È l'inverso del Java di sempre, dove tutto può essere `null`.
- Le annotazioni non cambiano il comportamento a runtime: servono agli strumenti (IDE, NullAway, Kotlin) per segnalare errori **prima** dell'esecuzione.
- Tutte le API pubbliche di Spring 7 sono annotate: chiamando un metodo che può restituire `null`, l'IDE lo segnala.

**Trappole.**

- Aspettarsi un `NullPointerException` "migliore" a runtime: non c'è. Senza un controllore (NullAway, IntelliJ) le annotazioni non fanno nulla.
- Mischiare `org.springframework.lang.Nullable` (vecchio) e `org.jspecify.annotations.Nullable`: gli strumenti li trattano in modo diverso.

**Cosa si vede.** Un avviso nell'IDE ("dereference of possibly-null reference") sul codice che usa `leggi(...)` senza controllo.

**Stato verifica:** **[V]**.

---

### 2.9 Spring Security 7 (cenni)

**Cosa cambia.** Boot 4.0 porta Security 7.0, la 4.1 la 7.1. Alcuni **default** cambiano e possono rompere API che funzionavano.

**Cosa succede davvero.** Il tema è trattato in MG8 (sicurezza di servizi e API). In questo file basta ricordare:

- le proprietà OAuth2 sono state riorganizzate e gli starter rinominati (`oauth2-*` → `security-oauth2-*`, vedi 2.1);
- la 4.1 aggiunge `authorities-claim-expressions` per i JWT, con espressioni **SpEL** per ricavare le autorità dai claim.

**Stato verifica:** **[V]** per renaming starter e `authorities-claim-expressions`.

---

### 2.10 Novità di Boot 4.1: protezione SSRF con `InetAddressFilter`

**Cosa cambia.** La 4.1 introduce un filtro sugli indirizzi IP che i client HTTP possono raggiungere, per ridurre il rischio di **SSRF** (Server-Side Request Forgery: un utente induce il server a chiamare indirizzi interni).

**Prima (3.5):** nessuna protezione integrata; chi voleva bloccare indirizzi interni scriveva un `ClientHttpRequestInterceptor`.

**Dopo (4.1.2):**

```java
@Configuration
class SicurezzaClientConfig {

    @Bean
    InetAddressFilter filtroIndirizzi() {
        return InetAddressFilter.externalAddresses();    // solo indirizzi pubblici
    }
}
```

**Cosa succede davvero.**

- `InetAddressFilter.externalAddresses()` accetta solo indirizzi **pubblici**: blocca loopback, reti private e simili.
- Si possono comporre regole: `InetAddressFilter.of("192.168.1.0/24").andNot(...)`.
- Se nel contesto esiste un bean `InetAddressFilter`, **si applica a tutti i client auto-configurati** (`RestClient`, `WebClient`, `HttpServiceProxyFactory`), non solo a uno.

**Trappole.**

- In ShopFlow `MagazzinoClient` chiama `http://magazzino:8081`, che è un indirizzo **interno**: con `externalAddresses()` la chiamata verrebbe bloccata. Il filtro va scritto per far passare le reti interne ammesse.
- Se tra client e destinazione c'è un **proxy**, il filtro valuta l'indirizzo del proxy, non quello finale: limite documentato.

**Cosa si vede.** Una chiamata verso `127.0.0.1` o verso la rete interna che fallisce prima di aprire la connessione, con un'eccezione che nomina il filtro.

**Stato verifica:** **[V]** (reference Boot 4.1).

---

### 2.11 Altre novità di Boot 4.1 (tabella)

| Novità 4.1 | Dettaglio | Stato |
|---|---|---|
| **gRPC** | moduli `spring-boot-grpc-*` per server e client | **[V]** |
| **Proprietà Jackson condivise** | `spring.jackson.read.*`, `write.*`, `factory.*` (vedi 2.2) | **[V]** |
| **Cookie dei client HTTP** | `spring.http.clients.cookie-handling` | **[V]** |
| **OpenTelemetry** | configurazione tramite variabili d'ambiente e `management.opentelemetry.*` | **[V]** |
| **Propagazione del contesto in `@Async`** | il contesto di osservabilità segue il thread asincrono | **[V]** |
| **Connessione al DB "pigra"** | `spring.datasource.connection-fetch=lazy` | **[V]** |
| **Bootstrap JPA** | proprietà `spring.jpa.bootstrap` (modalità di avvio di JPA) | **[V]** |
| **`@RedisListener`** | listener Redis dichiarativi | **[V]** |
| **Spring Batch + MongoDB** | starter dedicato | **[V]** |
| **Docker Compose** | log dei container e supporto Elasticsearch | **[V]** |
| **Log4j** | rotazione dei file di log | **[V]** |
| **Spock** | supporto ripristinato (Spock 2.4 / Groovy 5) | **[V]** |
| **Endpoint `info`**, **OAuth2 Resource Server**, **LDAP embedded**, **SSL per RabbitMQ Streams** | miglioramenti minori elencati nelle note 4.1 | **[V]** |
| **`spring.config.import`** | gestione della codifica dei file | **[V]** |

**Cosa succede davvero (le due che contano per ShopFlow).**

- **`connection-fetch=lazy`**: con `@Transactional` il framework di solito apre la connessione all'**inizio** della transazione, anche se il metodo poi legge solo dalla cache. In modalità lazy la connessione viene presa solo alla **prima query vera**. Meno connessioni tenute occupate inutilmente.
- **Propagazione in `@Async`**: i metodi `@Async` mantengono `traceId` e contesto di osservabilità. Prima la traccia si "spezzava" al passaggio al thread del pool.

**Stato verifica:** **[V]** (release notes 4.1).

---

### 2.12 Rotture e deprecazioni di Boot 4.1

| Cosa | Effetto | Rimedio |
|---|---|---|
| **`layertools` rimosso** | il comando `java -Djarmode=layertools` non esiste più | usare la modalità **`tools`** |
| **`-DskipTests` non salta più l'AOT dei test** | la build continua a eseguire la fase di *process-test-aot* | usare `-Dmaven.test.skip=true` (salta anche la compilazione dei test) |
| **jOOQ 3.20 richiede Java 21** | con Java 17 jOOQ non va aggiornato | restare su Java 21+ (nel corso: Java 25) |
| **Cambi a `bootstrap-mode`** | impostazioni di avvio JPA ridefinite | rivedere le proprietà, vedi `spring.jpa.bootstrap` |
| **Deprecati**: Derby, Dynatrace V1, LiveReload di DevTools | funzionano ma verranno rimossi | pianificare l'abbandono |

**Trappola.** `mvn package -DskipTests` in CI continua a "funzionare" ma ora richiede più tempo e può fallire sull'AOT dei test: chi lo usa per accelerare la pipeline vede la build rallentare e, in alcuni casi, rompersi.

**Cosa si vede.** `Error: Unrecognized option layertools` nei `Dockerfile` che usano la vecchia estrazione dei layer.

**Stato verifica:** **[V]** (release notes 4.1).

---

## 3. Da Spring Boot 4.1.2 a 4.2 (il futuro)

> **Stato delle informazioni.** Questa sezione si basa sulle note delle milestone (M1, M2) e delle release candidate (**RC1** e **RC2**) di Spring Boot 4.2 e di Spring Framework 7.1. La pagina ufficiale "Spring Boot 4.2 Release Notes" è ancora un avviso: le note complete usciranno con il rilascio. Le note della RC1 avevano sezioni "TBD" (dipendenze e deprecazioni); quelle della RC2 sono incrementali (raccontano solo cosa è cambiato dopo la RC1/M1) e non hanno più TBD. Tutte le voci portano l'etichetta **[GA]**: **vanno ricontrollate dopo il rilascio del 19/11/2026**. Quello che segue è la mappa di ciò che *probabilmente* si troverà, non una specifica.

> **Perché metterlo nel corso.** Gli studenti lavorano su 4.1.2 (supportata fino al 31/07/2027), ma entro pochi giorni dall'inizio del corso esce la 4.2. Sapere **cosa cambia al prossimo salto** serve a scrivere oggi codice che non andrà riscritto domani (in particolare: niente `RestTemplate`).

### 3.1 `RestTemplate` e `TestRestTemplate` deprecati

**Cosa cambia.** In 4.2 (con il Framework 7.1) `RestTemplate` viene **deprecato**, insieme a `RestTemplateBuilder`, `RestTemplateCustomizer` e `TestRestTemplate`. Sono le classi più usate per chiamare servizi HTTP dal 2009.

| Era (3.5 e 4.0/4.1) | Sostituto |
|---|---|
| `RestTemplate` | `RestClient` |
| `RestTemplateBuilder` | `RestClient.Builder` |
| `RestTemplateCustomizer` | `RestClientCustomizer` |
| `TestRestTemplate` | `RestTestClient` |

**Prima (3.5, ma funziona ancora in 4.1.2):**

```java
@Service
class PagamentoGateway {

    private final RestTemplate restTemplate;

    PagamentoGateway(RestTemplateBuilder builder) {
        this.restTemplate = builder
                .rootUri("http://pagamenti:8082")
                .readTimeout(Duration.ofSeconds(2))
                .build();
    }

    Ricevuta addebita(RichiestaPagamento richiesta) {
        return restTemplate.postForObject("/addebiti", richiesta, Ricevuta.class);
    }
}
```

**Dopo (da scrivere già in 4.1.2, valido anche in 4.2):**

```java
@Service
class PagamentoGateway {

    private final RestClient restClient;

    PagamentoGateway(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://pagamenti:8082")
                .build();
    }

    Ricevuta addebita(RichiestaPagamento richiesta) {
        return restClient.post()
                .uri("/addebiti")
                .body(richiesta)
                .retrieve()
                .body(Ricevuta.class);
    }
}
```

```properties
# in alternativa al codice, i timeout dei client HTTP si possono impostare in configurazione
# (chiave esatta da verificare sulla reference di 4.1.2: ramo spring.http.client(s).*)
spring.http.clients.read-timeout=2s
```

**Cosa succede davvero.**

- `RestTemplate` non sparisce: in 4.2 diventa **deprecato** (warning di compilazione), non rimosso. Una deprecazione dà tempo per migrare; la data di rimozione, se verrà annunciata, è da verificare.
- `RestClient` (dal Framework 6.1) ha un'API fluente e usa gli stessi convertitori; è la base anche di `@ImportHttpServices` (vedi 2.6) e di `RestTestClient`.
- `RestClient.Builder` nel Framework 7.1 registra i convertitori di default; per partire da zero si usa `disableDefaults()`.

**Trappole.**

- Il `RestTemplate` con **interceptor** e `ResponseErrorHandler` personalizzati non si traduce 1:1: in `RestClient` la gestione errori è `defaultStatusHandler(...)` / `.onStatus(...)`.
- Con `RestTemplate` un `4xx` generava subito `HttpClientErrorException`; con `RestClient.retrieve()` succede lo stesso, ma con `.exchange(...)` l'errore **non** viene lanciato automaticamente.
- Il Framework 7.1 cambia anche il significato dei **timeout**: valore `0` = nessun timeout, valori negativi rifiutati (vedi 3.8).

**Cosa si vede.** Dopo l'upgrade a 4.2, `warning: [deprecation] RestTemplate in org.springframework.web.client has been deprecated` su tutte le classi che lo usano; con `-Werror` la build fallisce.

**Stato verifica:** **[GA]**.

---

### 3.2 Header `Forwarded`: default `X-Forwarded-*` e formato configurabile

**Cosa cambia.** I proxy inversi (nginx, un ingress, un load balancer) comunicano l'indirizzo e il protocollo originali del client con header di inoltro. In 4.2 Spring MVC cambia il formato letto per default e lo rende configurabile.

```properties
# 4.2: scelta del formato degli header di inoltro
spring.mvc.forwarded-headers.header-format=x-forwarded
```

**Cosa succede davvero.**

- Esistono due formati: l'header standard `Forwarded` (RFC 7239) e la famiglia non standard ma diffusissima `X-Forwarded-For`, `X-Forwarded-Proto`, `X-Forwarded-Host`.
- In 4.2 il **default diventa `X-Forwarded-*`**, perché è quello che quasi tutti i proxy inviano davvero. La proprietà `spring.mvc.forwarded-headers.header-format` permette di scegliere l'altro.
- Collegato: il costruttore senza argomenti di `ForwardedHeaderFilter` è **deprecato** nel Framework 7.1.

**Trappole.**

- Applicazione dietro un proxy che invia solo `Forwarded`: con il nuovo default Spring non vede più host e protocollo reali, e genera URL (redirect, link HATEOAS) con `http://` e con l'host interno.
- Applicazione **non** dietro un proxy ma esposta direttamente: gli header di inoltro in arrivo da un client malevolo possono essere falsificati. Va abilitata solo dove c'è davvero un proxy fidato.

**Cosa si vede.** `Location: http://shopflow-interno:8080/api/ordini/42` nelle risposte `201 Created`, invece dell'URL pubblico `https://...`.

**Stato verifica:** **[GA]** (valore esatto della proprietà `header-format` da confermare).

---

### 3.3 Configurazione: `Set` e `Map` mantengono l'ordine

**Cosa cambia.** Quando una proprietà di tipo `Set` o `Map` viene letta in un `@ConfigurationProperties`, Boot 4.2 **preserva l'ordine** in cui i valori sono scritti.

**Prima (4.1.2):**

```java
@ConfigurationProperties("shopflow.sconti")
record ScontiProperties(Set<String> codiciAttivi, Map<String, BigDecimal> percentuali) {}
```

```yaml
shopflow:
  sconti:
    codici-attivi: [NATALE, ESTATE, VIP]
    percentuali: { NATALE: 10, ESTATE: 5, VIP: 15 }
```

In 4.1.2 l'ordine di iterazione di `codiciAttivi` non era garantito; in 4.2 è `NATALE, ESTATE, VIP`.

**Cosa succede davvero.** Boot ora usa strutture ordinate (`LinkedHashSet`, `LinkedHashMap`) quando crea queste collezioni. Cambia solo l'ordine, non quali valori ci sono.

**Trappole.** Test o log che dipendevano (per caso) dall'ordine vecchio: si possono rompere in entrambe le direzioni. Un codice che *richiede* un ordine deve dichiararlo (`List`, `LinkedHashMap`), non sperarlo.

**Cosa si vede.** Un elenco in un endpoint di diagnostica (`/actuator/configprops`) con l'ordine diverso rispetto a prima.

**Stato verifica:** **[GA]**.

---

### 3.4 Tomcat: proprietà `server.tomcat.servlet.*`

**Cosa cambia.** Il gruppo `server.tomcat.servlet.*` raccoglie impostazioni del contenitore servlet; tra queste `use-relative-redirects`, che in 4.2 ha come default `true`.

```properties
server.tomcat.servlet.use-relative-redirects=true
```

**Cosa succede davvero.** Un redirect (`302`/`303`) può essere scritto con URL assoluto (`Location: https://host/ordini/42`) o **relativo** (`Location: /ordini/42`). Con `true` Tomcat non ricalcola l'URL assoluto: il browser risolve il percorso sull'host che ha usato. È più robusto dietro un proxy che cambia l'host.

**Trappole.** Client non-browser (script, vecchie app mobile) che si aspettano un URL assoluto nel `Location`.

**Stato verifica:** **[GA]**.

---

### 3.5 AMQP 1.0 e due starter per RabbitMQ

**Cosa cambia.** Boot 4.2 aggiunge il supporto a **AMQP 1.0** (libreria QPid Proton), in aggiunta al vecchio **AMQP 0.9.1** di RabbitMQ. Gli starter sono distinti:

| Protocollo | Starter |
|---|---|
| AMQP 0.9.1 (RabbitMQ classico) | `spring-boot-starter-rabbitmq` |
| AMQP 1.0 | `spring-boot-starter-amqp-rabbitmq` |

**Cosa succede davvero.** I due protocolli non sono compatibili tra loro: AMQP 1.0 è uno standard ISO diverso da 0.9.1, e lo stesso broker (RabbitMQ 4.x) può parlarli entrambi. La scelta dello starter decide quale client si configura.

**Trappole.** Cambiare starter "per aggiornare" e trovarsi con un client che parla l'altro protocollo: i listener `@RabbitListener` non funzionano con AMQP 1.0.

**Cambi introdotti dalla RC2 (rispetto a M1)** per il client AMQP 1.0:

- `spring.amqp.rabbitmq.username` e `password` **non** hanno più il default `guest`;
- se è impostato `spring.amqp.rabbitmq.ssl.bundle`, la porta di default diventa `5671`;
- `spring.amqp.rabbitmq.template.exchange` e `routing-key` non hanno più come default la stringa vuota;
- la chiave dell'health indicator passa da `rabbit` a `amqprabbit` (per convivere con quello AMQP 0.9; si disattiva con `management.health.amqprabbit.enabled`).

**Rilevanza per il corso.** MG7 usa Kafka e ActiveMQ; RabbitMQ è fuori programma. La voce è qui per completezza.

**Stato verifica:** **[GA]** (nome esatto dello starter `amqp-rabbitmq` da confermare).

---

### 3.6 Osservabilità: convenzioni OpenTelemetry, OTLP comune, MDC

**Cosa cambia.** Tre novità sulla stessa area:

```properties
# 1) nomi di metriche e span secondo le convenzioni semantiche di OpenTelemetry
management.observations.conventions=open-telemetry

# 2) configurazione OTLP comune (traces, metriche, log)
management.opentelemetry.otlp.endpoint=http://collector:4318
management.opentelemetry.otlp.headers.Authorization=Bearer ...
management.opentelemetry.otlp.compression=gzip

# 3) proprietà di tracing per il MDC dei log (gruppo management.tracing.mdc.*)
```

**Cosa succede davvero.**

- **Convenzioni:** secondo le note RC2, impostare `management.observations.conventions=open-telemetry` abilita le convenzioni semantiche OpenTelemetry (prima nessuno dei due insiemi di convenzioni era applicato). Finora Micrometer usava nomi propri (per esempio `http.server.requests`). Con `conventions=open-telemetry` gli stessi dati escono con i nomi standard OpenTelemetry (per esempio `http.server.request.duration`, esempio indicativo da confermare). Dashboard e allarmi basati sui vecchi nomi **si rompono**.
- **OTLP comune:** una sola sezione per l'endpoint, le intestazioni e la compressione, valida per tutti i segnali; prima si ripeteva per tracce, metriche e log.
- **MDC:** il gruppo `management.tracing.mdc.*` governa le chiavi (`traceId`, `spanId`) copiate nel MDC dei log.
- Le classi di Micrometer per le metriche JVM cambiano nome (le vecchie convenzioni sono sostituite): vedi dettagli nella RC finale.
- Il Framework 7.1 registra gli errori `5xx` nel tag `error` dell'osservazione.

**Trappole.** Cambiare solo la proprietà delle convenzioni senza aggiornare i pannelli Grafana: i grafici diventano vuoti senza errori.

**Stato verifica:** **[GA]**.

---

### 3.7 Kafka: `KafkaAdmin` dedicato e arresto ordinato

**Cosa cambia.** Due modifiche per chi usa Spring for Apache Kafka (tema di MG7):

- un **`KafkaAdmin` dedicato** (un bean di amministrazione separato dagli altri client; i dettagli di configurazione sono da confermare);
- la proprietà **`await-async-results-on-stop`**: all'arresto dell'applicazione i risultati degli invii asincroni in corso vengono attesi, invece di perderli;
- un nuovo **`KafkaConfigBuilder`** per costruire le configurazioni dei client.

**Cosa succede davvero.** L'invio asincrono (`kafkaTemplate.send(...)` restituisce un `CompletableFuture`) può essere ancora in volo quando il contesto si chiude: senza attesa, un messaggio già "accettato" dal codice di business non arriva mai al broker.

**Trappole.** Attendere troppo allunga l'arresto (un pod Kubernetes ha un tempo massimo di terminazione).

**Stato verifica:** **[GA]** (nome esatto e valore di default della proprietà da confermare).

---

### 3.8 Spring Framework 7.1: altre differenze rilevanti

| Novità / cambio | Cosa significa | Stato |
|---|---|---|
| **Jackson 3.1** | aggiornamento minore del motore JSON | **[GA]** |
| **Hibernate ORM 7.3** come baseline minima del Framework (Boot 4.2 RC2 usa 7.4.14.Final); supporto a **JPA 4.0** / Hibernate 8.0 in arrivo | allineare la versione di Hibernate (vedi nota 1, sezione 1) | **[GA]** |
| **Metodo HTTP `QUERY`** | richiesta con corpo, sicura e idempotente, pensata per ricerche complesse | **[GA]** |
| **`configureProblemDetails`** + `renderUnhandledExceptions(true)` | configurazione centrale delle risposte `application/problem+json` | **[GA]** |
| **`ProblemDetail.instance` non automatico** | il campo `instance` non viene più riempito da solo | **[GA]** |
| **`MultipartHttpMessageConverter`** | conversione di corpi `multipart` | **[GA]** |
| **Timeout `0` = nessun timeout; valori negativi rifiutati** | un `0` ora significa "mai"; un valore negativo genera un errore | **[GA]** |
| **JAXB non rilevato in automatico sui server** | chi serve XML via JAXB deve registrarlo | **[GA]** |
| **Supporto a JsonUnit** nei test | asserzioni JSON più comode | **[GA]** |
| **Deprecazioni** | `RestTemplate`, costruttore vuoto di `ForwardedHeaderFilter`, `AllEncompassingFormHttpMessageConverter`, `DataBinder#disallowedFields`, supporto Derby, `PropertyAccessorUtils` | **[GA]** |

**Esempio – `ProblemDetail` (rilevante per MG8 e per il capitolo errori REST):**

```java
@RestControllerAdvice
class ErroriRest {

    @ExceptionHandler(OrdineNonTrovato.class)
    ProblemDetail nonTrovato(OrdineNonTrovato e) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        p.setInstance(URI.create("/api/ordini/" + e.getId()));   // in 7.1 va impostato a mano
        return p;
    }
}
```

**Trappole.**

- **Timeout `0`**: un codice che passava `0` per dire "scadenza immediata" ora significa "mai". Controllare tutti i punti dove i timeout arrivano da configurazione.
- **`ProblemDetail.instance`**: i client che leggono `instance` si ritrovano `null` se non lo impostiamo.
- **JAXB**: un endpoint che rispondeva in XML smette di farlo senza errori di compilazione.

**Stato verifica:** **[GA]** per l'intera tabella.

---

### 3.9 Build e container: buildpacks, Ubuntu 26.04, modalità `tools`

**Cosa cambia.**

| Novità | Dettaglio |
|---|---|
| **Cache delle immagini** | la build dell'immagine con i buildpacks usa una cache basata su immagine (non su volume) |
| **Builder predefinito su Ubuntu 26.04** | `paketobuildpacks/ubuntu-resolute-builder`; l'immagine di *run* "tiny" **non contiene una shell** |
| **Modalità `tools`** | in 4.2 il jar `tools` può produrre anche un **SBOM** (`sbom`) |
| **`RetryPolicySettings`** | opzione di *timeout* nella configurazione dei ritentativi |

**Cosa succede davvero.** Con l'immagine "tiny" non si può fare `docker exec -it … sh`: manca la shell. Per fare diagnostica serve un'immagine di debug o un container effimero collegato.

**Trappole.** Script di `HEALTHCHECK` o `ENTRYPOINT` che usano `sh -c ...`: non funzionano sull'immagine senza shell.

**Stato verifica:** **[GA]**.

---

### 3.10 Cambi della RC2 che possono fermare l'avvio ("Upgrading from 4.1")

Le note RC2 hanno una sezione di aggiornamento da 4.1 con tre comportamenti che cambiano **senza modifiche al codice**:

| Cambio | Effetto | Rimedio |
|---|---|---|
| **Redis**: `spring.data.redis.database` (o la parte database di `spring.data.redis.url`) ora si applica anche con `spring.data.redis.masterreplica.nodes` | prima veniva ignorata: i dati stavano nel database 0 | rimuovere la proprietà o impostarla a `0` |
| **WebFlux**: `X-Forwarded-Prefix` non imposta più il context path con `server.forward-headers-strategy=native` (o `framework` con WebFlux) | gli URL generati perdono il prefisso del proxy | `server.netty.forwarded-headers.use-forwarded-prefix=true` (native) oppure `spring.webflux.forwarded-headers.use-forwarded-prefix=true` (framework) |
| **SSL bundle** con `options.ciphers` o `options.enabled-protocols` usato da client/server gRPC, client LDAP e LDAP embedded, esportatori OTLP | **l'avvio fallisce** (prima l'opzione era ignorata in silenzio) | togliere le opzioni o usare un bundle separato |

ShopFlow usa Spring MVC, non WebFlux, e non ha Redis in configurazione master/replica: questi tre punti servono come **esempi del tipo di rottura** che arriva con una minor, non come lavoro da fare.

**Trappola.** Il terzo caso è l'opposto di una proprietà ignorata: Boot ora è *più severo* e rifiuta di avviarsi. È una tendenza da ricordare agli studenti: ogni minor può rendere errore ciò che prima era un avviso muto.

**Stato verifica:** **[GA]** (da note RC2).

---

### 3.11 Cosa NON si sa ancora

Dopo la RC2 restano da confermare con le note complete del GA:

- l'elenco **definitivo** delle dipendenze (nella RC2: Framework 7.1.0-RC2, Micrometer 1.18.0-RC2, Tracing 1.8.0-RC2, Hibernate 7.4.14.Final, Jackson BOM 3.1.8, Netty 4.2.20.Final, AspectJ 1.9.27; gli intervalli attesi per le milestone erano Kafka 4.2, Security 7.2, Spring Data 2026.1, Kotlin 2.4.x, JUnit Jupiter 6.1.3, OpenTelemetry 1.64.0);
- l'elenco **completo** delle deprecazioni di Boot 4.2 (la RC2 non ne aggiunge, ma quelle di M1/RC1 vanno rilette);
- eventuali rimozioni o breaking change aggiunti tra RC2 e GA.

**Come ricontrollare dopo il 19/11/2026:** leggere le *Release Notes* di Spring Boot 4.2 e le *Upgrade Notes* del Framework 7.1; ripercorrere questa sezione voce per voce, togliendo l'etichetta **[GA]** da ogni voce confermata e correggendo le altre.

---

## 4. Confronto a tre colonne

### 4.1 Tabella riassuntiva 3.5 → 4.1.2 → 4.2

| Area | **Boot 3.5** | **Boot 4.1.2** (corso) | **Boot 4.2** **[GA]** |
|---|---|---|---|
| **Starter web** | `spring-boot-starter-web` | `spring-boot-starter-webmvc` (+ `-restclient`, `-webclient`); il vecchio starter è un ponte deprecato | invariato rispetto a 4.1 |
| **Starter di test** | `spring-boot-starter-test` | `-<tecnologia>-test` per ogni tecnologia | invariato |
| **JSON** | Jackson 2 (`ObjectMapper`, `com.fasterxml.jackson.*`) | Jackson 3 (`JsonMapper`, `tools.jackson.*`); ordine alfabetico; date ISO | Jackson 3.1 |
| **Proprietà Jackson** | `spring.jackson.read/write.*` | `spring.jackson.json.read/write.*` (4.0) → `spring.jackson.read/write/factory.*` comuni (4.1) | invariato |
| **Mock nei test** | `@MockBean`, `@SpyBean` | `@MockitoBean`, `@MockitoSpyBean` | invariato |
| **Test HTTP** | `TestRestTemplate`, `MockMvc` | `RestTestClient` (+ `@AutoConfigureRestTestClient`), `MockMvc`, `MockMvcTester` | `TestRestTemplate` **deprecato** |
| **Client HTTP** | `RestTemplate`, `RestClient`, `WebClient` | `RestClient` consigliato; `@ImportHttpServices` per client dichiarativi | `RestTemplate` **deprecato** |
| **Versionamento API** | a mano (URL `/v1/`, `/v2/`) | `version` su `@GetMapping`, `spring.mvc.apiversion.*`, `Deprecation`/`Sunset`/`Link` | invariato |
| **Resilienza** | Spring Retry (`@EnableRetry`, `maxAttempts`) | `@Retryable` / `@ConcurrencyLimit` del Framework (`@EnableResilientMethods`, `maxRetries`) | `RetryPolicySettings` con timeout |
| **Null-safety** | `org.springframework.lang.Nullable` | JSpecify (`@NullMarked`, `@Nullable`) | invariato |
| **Sicurezza di rete** | interceptor scritti a mano | `InetAddressFilter` (SSRF) dalla 4.1 | invariato |
| **Osservabilità** | Micrometer 1.15, nomi Micrometer | Micrometer 1.17; OTel via env var e `management.opentelemetry.*`; propagazione in `@Async` | convenzioni OpenTelemetry opzionali; OTLP comune; MDC (`management.tracing.mdc.*`) |
| **Header di inoltro** | `Forwarded` / `X-Forwarded-*` secondo la strategia | invariato | default `X-Forwarded-*`, `spring.mvc.forwarded-headers.header-format` |
| **Tomcat** | 10.1 (Jakarta EE 10) | 11 (Jakarta EE 11, Servlet 6.1) | 11; `server.tomcat.servlet.*` |
| **Persistenza** | – | Hibernate 7.1 (GA), 7.2 da 4.0.1; `spring.datasource.connection-fetch=lazy`; `spring.jpa.bootstrap` | Hibernate 7.4.13.Final; RC2 di 4.2: 7.4.14.Final |
| **Messaggistica** | Kafka client 3.9; RabbitMQ AMQP 0.9.1 | Kafka client 4.1; AMQP 0.9.1 | AMQP 1.0 (starter dedicato); `KafkaAdmin` dedicato |
| **Container** | `layertools`, buildpacks Jammy | `layertools` rimosso → `tools` | builder Ubuntu 26.04, run image tiny **senza shell**; `tools` con SBOM |
| **Config. properties** | `Set`/`Map` senza ordine garantito | invariato | ordine preservato |
| **Supporto OSS** | terminato il 30/06/2026 | fino al 31/07/2027 | fino al 31/12/2027 |

> Le celle con **[GA]** e quelle della colonna 4.2 vanno ricontrollate dopo il 19/11/2026.

### 4.2 Cosa **non** cambia (e rassicura gli studenti)

Per evitare che la migrazione sembri una riscrittura totale, ecco ciò che resta uguale:

- `@SpringBootApplication`, `@RestController`, `@Service`, `@Repository`, `@Component`, `@Configuration`, `@Bean`;
- **iniezione dal costruttore**, profili (`@Profile`), `application.properties` / `application.yml`;
- `@Transactional` e il modello delle transazioni (MG5);
- Spring Data JPA: repository, `findBy...`, `@Query`, `Pageable`;
- Bean Validation (`@Valid`, `@NotBlank`…);
- le annotazioni Jackson (`@JsonProperty`, `@JsonIgnore`);
- Actuator e i suoi endpoint di base;
- JUnit (`@Test`, `@BeforeEach`), anche se la versione del motore è la 6.

**Regola pratica da dire in aula.** Il 90% del codice di ShopFlow non cambia: si rompono i **test**, le **dipendenze**, il **JSON**, le **proprietà** e le chiamate a **client HTTP** e a **Retry**.

### 4.3 Che cosa fare adesso in ShopFlow (3.5 → 4.1.2) e che cosa lasciare al dopo

| Voce | Adottare nel corso (4.1.2) | Non adottare ancora / solo spiegare |
|---|---|---|
| Starter modulari | ✔ obbligatorio | – |
| Jackson 3 | ✔ obbligatorio | `use-jackson2-defaults` solo come ponte transitorio |
| `@MockitoBean` | ✔ obbligatorio | – |
| `RestClient` al posto di `RestTemplate` | ✔ già da ora (evita il lavoro con la 4.2) | – |
| `RestTestClient` | ✔ per i nuovi test | `TestRestTemplate` ancora ammesso in 4.1.2 |
| API versioning | ✔ laboratorio MG2 | – |
| `@ImportHttpServices` | ✔ per `MagazzinoClient` | – |
| `@Retryable` / `@ConcurrencyLimit` | ✔ per `PagamentoService` | Spring Retry: dismettere |
| `InetAddressFilter` | spiegare, provare con un test | adozione dipende dalla rete |
| gRPC | – | solo cenno |
| Migrare a 4.2 | – | dopo il GA e dopo una settimana di assestamento |

---

## 5. Esempi reali su ShopFlow

Gli esempi sono costruiti sul progetto del corso: `Ordine`, `OrdineController`, `OrdineService`, `PagamentoService`, `MagazzinoClient`. **Nessuno è stato eseguito**: vanno provati prima della lezione (vedi 5.6).

### 5.1 Il `pom.xml` di ShopFlow, prima e dopo

**Prima (3.5.16):**

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.16</version>
</parent>

<properties>
    <java.version>21</java.version>
</properties>

<dependencies>
    <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-web</artifactId></dependency>
    <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-data-jpa</artifactId></dependency>
    <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-validation</artifactId></dependency>
    <dependency><groupId>org.springframework.retry</groupId><artifactId>spring-retry</artifactId></dependency>
    <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-test</artifactId><scope>test</scope></dependency>
</dependencies>
```

**Dopo (4.1.2):**

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>4.1.2</version>
</parent>

<properties>
    <java.version>25</java.version>
</properties>

<dependencies>
    <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-webmvc</artifactId></dependency>
    <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-restclient</artifactId></dependency>
    <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-data-jpa</artifactId></dependency>
    <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-validation</artifactId></dependency>
    <!-- spring-retry eliminata: @Retryable è nel Framework 7 -->
    <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-webmvc-test</artifactId><scope>test</scope></dependency>
    <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-data-jpa-test</artifactId><scope>test</scope></dependency>
    <!-- solo durante la migrazione -->
    <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-properties-migrator</artifactId><scope>runtime</scope></dependency>
</dependencies>
```

**Cosa succede davvero.** `spring-boot-starter-validation` resta con lo stesso nome: non tutti gli starter sono stati rinominati. Le differenze sono: `web` → `webmvc`, comparsa di `restclient`, gli starter di test per tecnologia, via `spring-retry`.

**Trappole.** Cambiare solo la versione del parent e lasciare `starter-web`: compila con avvisi di deprecazione, ma non è una migrazione completa.

**Cosa si vede.** `./mvnw dependency:tree` mostra `tools.jackson.core:jackson-databind` al posto di `com.fasterxml.jackson.core:jackson-databind`; e un solo `hibernate-core`, nella versione 7.4.13.Final.

### 5.2 Test di regressione del JSON (cattura Jackson 3)

**Obiettivo.** Un test che avvisa se l'output JSON di un DTO cambia senza che nessuno se ne accorga.

```java
@JsonTest                                              // starter: spring-boot-starter-jackson-test
class OrdineDtoJsonTest {

    @Autowired JacksonTester<OrdineDto> json;

    @Test
    void serializzaConChiaviEDateAttese() throws Exception {
        OrdineDto dto = new OrdineDto(42L, "Rossi", LocalDateTime.of(2026, 11, 16, 9, 30));

        JsonContent<OrdineDto> risultato = json.write(dto);

        assertThat(risultato).extractingJsonPathStringValue("$.creatoIl")
                             .isEqualTo("2026-11-16T09:30:00");
        assertThat(risultato).extractingJsonPathNumberValue("$.id").isEqualTo(42);
    }

    @Test
    void leggeUnJsonSenzaOrdineFisso() throws Exception {
        String testo = """
            {"id":42,"cliente":"Rossi","creatoIl":"2026-11-16T09:30:00"}
            """;

        assertThat(json.parse(testo)).isEqualTo(
                new OrdineDto(42L, "Rossi", LocalDateTime.of(2026, 11, 16, 9, 30)));
    }
}
```

**Cosa succede davvero.** Il test controlla i **valori** con JSONPath e non confronta stringhe intere: l'ordine alfabetico delle chiavi non può farlo cadere. `@JsonTest` avvia solo la parte JSON del contesto (non l'intera applicazione) e fornisce un `JacksonTester`.

**Trappole.** Un test che confronta `json.write(dto).getJson()` con una stringa: è esattamente il tipo di test che si rompe per l'ordine alfabetico.

**Cosa si vede.** Test verde con Jackson 3; se qualcuno attiva `use-jackson2-defaults` e il formato della data cambia, il test lo segnala.

### 5.3 `PagamentoService` con `@Retryable` e controllo di idempotenza

```java
@Service
class PagamentoService {

    private final PagamentoClient pagamenti;
    private final RicevuteRepository ricevute;

    PagamentoService(PagamentoClient pagamenti, RicevuteRepository ricevute) {
        this.pagamenti = pagamenti;
        this.ricevute = ricevute;
    }

    @Retryable(includes = PagamentoTemporaneamenteNonDisponibile.class,
               maxRetries = 2, delay = 200, multiplier = 2, jitter = 50)
    Ricevuta addebita(Ordine ordine) {
        return ricevute.findByOrdineId(ordine.getId())            // idempotenza: già pagato?
                       .orElseGet(() -> pagamenti.addebita(ordine.getId(), ordine.getTotale()));
    }
}
```

```java
@Configuration
@EnableResilientMethods
class ResilienzaConfig { }
```

**Test (conta i tentativi):**

```java
@SpringBootTest
@AutoConfigureMockMvc
class PagamentoServiceRetryTest {

    @Autowired PagamentoService service;
    @MockitoBean PagamentoClient pagamenti;
    @MockitoBean RicevuteRepository ricevute;

    @Test
    void ritentaDueVolteEPoiRilanciaLEccezioneOriginale() {
        when(ricevute.findByOrdineId(1L)).thenReturn(Optional.empty());
        when(pagamenti.addebita(eq(1L), any()))
                .thenThrow(new PagamentoTemporaneamenteNonDisponibile());

        assertThatThrownBy(() -> service.addebita(ordineDiProva(1L)))
                .isInstanceOf(PagamentoTemporaneamenteNonDisponibile.class);

        verify(pagamenti, times(3)).addebita(eq(1L), any());      // 1 tentativo + 2 ritentativi
    }
}
```

**Cosa succede davvero.**

- `maxRetries = 2` significa **tre** chiamate in tutto. Il `times(3)` nel test è proprio ciò che gli studenti sbagliano più spesso.
- Quando i tentativi finiscono, arriva l'**eccezione originale** (il metodo annotato rilancia l'ultima causa), non un'eccezione "di retry" generica.
- Il controllo `findByOrdineId` rende l'operazione **idempotente**: se il primo tentativo è andato a buon fine ma la risposta si è persa, il secondo non addebita due volte.

**Trappole.**

- `PagamentoService` chiamato da un altro metodo della stessa classe: il proxy viene saltato e il retry **non** scatta.

**Cosa si vede.** Nei log, tre righe "addebito in corso" distanziate; nel test, `times(3)` verde. Con `maxRetries = 3` il test deve diventare `times(4)`.

### 5.4 `MagazzinoClient` con versione dell'API

```java
@HttpExchange("/giacenze")
interface MagazzinoClient {

    @GetExchange("/{sku}")
    @Nullable GiacenzaV2 leggi(@PathVariable String sku);   // la versione la inserisce RestClient
}
```

```properties
spring.http.serviceclient.magazzino.base-url=http://magazzino:8081
# il client invia automaticamente l'header con la versione
spring.http.client.restclient.apiversion.insert.header=API-Version
spring.http.client.restclient.apiversion.default=2.0        # chiave da verificare
```

**Cosa succede davvero.** Il servizio `magazzino` espone due versioni della stessa URL (come in 2.5). Il client di ShopFlow dichiara la versione **una volta sola**, in configurazione, e `RestClient` aggiunge l'header a ogni richiesta; così il codice non deve ripeterla.


**Cosa si vede.** Un test con `MockRestServiceServer` che verifica `header("API-Version", "2.0")` sulla richiesta in uscita.

### 5.5 Pulizia in vista della 4.2: trovare `RestTemplate` e `TestRestTemplate`

**Passo 1 – quanti usi ci sono (comando shell):**

```bash
grep -rln "RestTemplate\|TestRestTemplate" src/
```

**Passo 2 – far emergere i warning di deprecazione (già in 4.1.2 compilano senza avvisi per `RestTemplate`; l'avviso arriva con la 4.2):**

```xml
<plugin>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <compilerArgs>
            <arg>-Xlint:deprecation</arg>
        </compilerArgs>
        <showDeprecation>true</showDeprecation>
    </configuration>
</plugin>
```

**Passo 3 – esempio di migrazione di un test:**

*Prima:*

```java
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureTestRestTemplate
class OrdineApiTest {

    @Autowired TestRestTemplate rest;

    @Test
    void leggeUnOrdine() {
        ResponseEntity<OrdineDto> risposta = rest.getForEntity("/api/ordini/42", OrdineDto.class);
        assertThat(risposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(risposta.getBody().cliente()).isEqualTo("Rossi");
    }
}
```

*Dopo:*

```java
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureRestTestClient
class OrdineApiTest {

    @Autowired RestTestClient client;

    @Test
    void leggeUnOrdine() {
        client.get().uri("/api/ordini/42")
              .exchange()
              .expectStatus().isOk()
              .expectBody(OrdineDto.class)
              .value(dto -> assertThat(dto.cliente()).isEqualTo("Rossi"));
    }
}
```

**Cosa succede davvero.** Il nuovo test non estrae più `ResponseEntity` per poi asserire a mano: le asserzioni stanno nella catena. Con `webEnvironment = RANDOM_PORT` il client punta al server reale; con `@AutoConfigureMockMvc` + `@AutoConfigureRestTestClient` punta a MockMvc senza aprire porte.

**Trappole.** Il Framework 7 documenta che il corpo va consumato (`expectBody`) nei test con rilevamento dei leak dei buffer.

**Cosa si vede.** Test verdi; `grep` del passo 1 che restituisce zero file.

### 5.6 Piano di verifica prima di spiegare in aula

Il materiale è costruito sulla documentazione, non su un'esecuzione. Prima delle lezioni, su un clone di ShopFlow:

| # | Da verificare | Come |
|---|---|---|
| 1 | Versione di Hibernate con 4.1.2 | `./mvnw dependency:tree \| grep hibernate-core` |
| 2 | Stato HTTP per versione API non supportata | test `MockMvc` con `API-Version: 9.9` |
| 3 | Chiavi `spring.jackson.*` e `management.tracing.*` | avvio con `spring-boot-properties-migrator` |
| 4 | Nome dello starter di test per `RestTestClient` | `pom` generato da start.spring.io (4.1.2) |
| 5 | Chiave `apiversion.default` del client | reference di Boot 4.1.2 "REST clients" |
| 6 | `delay` in ms e `delayString` con segnaposto | test su `@Retryable` |
| 7 | Default dei test su date e ordine JSON | eseguire 5.2 con e senza `use-jackson2-defaults` |
| 8 | Intera sezione 3 | dopo il 19/11/2026: release notes GA di 4.2 e *upgrade notes* del Framework 7.1 |
| 9 | Default di Spring Security 7 | *Migration Guide* di Spring Security 7 + test di sicurezza di ShopFlow |
| 10 | Correggere MG2 | Hibernate 7.1 → 7.4.x (versione reale da `dependency:tree`); stato 400 per versione non supportata |

### 5.7 Checklist di migrazione 3.5 → 4.1.2

- [ ] Java 25 installato e CI aggiornata
- [ ] Ultima 3.5.x (3.5.16) e tutti i warning di deprecazione risolti
- [ ] OpenRewrite `UpgradeSpringBoot_4_0` eseguito, diff riletto
- [ ] Parent a 4.1.2; `starter-web` → `starter-webmvc`; aggiunto `starter-restclient` se serve
- [ ] Starter di test per tecnologia; `@MockBean` → `@MockitoBean`; `@AutoConfigureMockMvc` dove serve
- [ ] Import `com.fasterxml.jackson.databind` → `tools.jackson.databind`; rimossi `catch (JsonProcessingException)`
- [ ] Test JSON non basati su stringhe intere
- [ ] `spring-boot-properties-migrator` eseguito e poi rimosso
- [ ] `spring-retry` sostituito da `@Retryable` del Framework (conteggio tentativi controllato)
- [ ] `RestTemplate` sostituito da `RestClient` dove possibile (anticipo sulla 4.2)
- [ ] Dockerfile senza `layertools` (→ `tools`); CI senza `-DskipTests` per saltare l'AOT dei test (→ `maven.test.skip`)
- [ ] Avvio, test, endpoint, metriche e tracing verificati a runtime
- [ ] Hibernate verificato con `dependency:tree`

---

## Fonti consultate

**Documentazione ufficiale Spring**

- Spring Boot 4.0 Migration Guide: https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Migration-Guide
- Spring Boot 4.1 Release Notes: https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.1-Release-Notes
- Spring Boot 4.2 Release Notes (pagina di avviso) e note della RC2: https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.2-Release-Notes · https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.2.0-RC2-Release-Notes
- Introducing Jackson 3 support in Spring: https://spring.io/blog/2025/10/07/introducing-jackson-3-support-in-spring/
- Spring Framework – RestTestClient: https://docs.spring.io/spring-framework/reference/7.1/testing/resttestclient.html
- Altre pagine ufficiali consultate durante la preparazione (reference del Framework 7 su resilienza e API versioning; reference di Boot su REST clients con `@ImportHttpServices` e `InetAddressFilter`; Release Notes di Boot 3.5; pagina delle coordinate delle dipendenze di Boot 4.1): gli indirizzi esatti vanno completati nella versione finale delle slide.

**Fonti secondarie** (usate per i punti da verificare, ora in `02b_punti-da-verificare.md`)

- Moritz Halbritter, slide *Inside Spring Boot 4* (default di Jackson 3): https://mhalbritter.github.io/slides/2026-04%20Inside%20Spring%20Boot%204%20Restructuring%20for%20the%20Future.pdf
- Dan Vega, *MockMvcTester vs RestTestClient*: https://www.danvega.dev/blog/mock-vs-rest
- Dan Vega, *Jackson 3 in Spring Boot 4*: https://www.danvega.dev/blog/jackson-3-spring-boot-4
