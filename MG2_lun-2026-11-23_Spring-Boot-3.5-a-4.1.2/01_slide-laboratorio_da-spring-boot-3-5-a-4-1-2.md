# Da Spring Boot 3.5 a 4.1.2

## Spiegazione teorica

**Perché migrare adesso**
- Spring Boot 3.5 è fuori supporto open source dal 30/06/2026 (ultima release gratuita: 3.5.16); le patch di sicurezza ora sono solo a pagamento
- Spring Boot 4.0 esce dal supporto il 31/12/2026: il bersaglio corretto è la **4.1.x** (supporto fino al 31/07/2027)
- Boot 4.1 si basa su Spring Framework 7, Jakarta EE 11 e Hibernate 7.4 (7.4.x), e funziona con Java da 17 a 27
- Spring Boot 4.2 e Framework 7.1 sono attesi a novembre 2026: il corso resta su 4.1.2

**Ordine consigliato della migrazione**
1. Portare Java da 21 a 25 e avere i test verdi
2. Aggiornare all'ultima 3.5.x e togliere le deprecazioni
3. Eseguire la ricetta OpenRewrite `UpgradeSpringBoot_4_0`
4. Correggere a mano ciò che resta
5. Verificare a runtime (avvio, endpoint, JSON prodotto)

**Cosa cambia in Boot 4 (da gestire in migrazione)**
- **Starter modulari**: uno per tecnologia (es. `spring-boot-starter-web` diventa `spring-boot-starter-webmvc`), con starter di test dedicati; gli starter "classic" fanno da ponte temporaneo ma sono deprecati
- **Jackson 3**: package `tools.jackson` al posto di `com.fasterxml.jackson`, `JsonMapper` immutabile, date in formato ISO-8601 di default; le annotazioni restano in `com.fasterxml.jackson.annotation`
- **Test**: `@MockBean` e `@SpyBean` lasciano il posto a `@MockitoBean` e `@MockitoSpyBean`; arriva `RestTestClient`
- **Rimozioni**: classi e proprietà deprecate in 3.x, annotazioni `javax.*`, Undertow
- **Hibernate 7.x**: non permette più di riassociare un'entità *detached*

**Novità di Spring Framework 7 da usare**
- **Null-safety con JSpecify**: `@NullMarked` rende non nullo tutto un package, `@Nullable` segnala i valori che possono mancare
- **Versionamento nativo delle API**: attributo `version` su `@GetMapping` e simili; la versione si legge da header, path, query o media type
- **Client HTTP dichiarativi**: interfacce con `@HttpExchange`, registrate in gruppi con `@ImportHttpServices` e basate su `RestClient`
- `RestTemplate` sarà deprecato in Framework 7.1: i nuovi client si scrivono con `RestClient` o con interfacce `@HttpExchange`

## Esempi teorici di utilizzo

- **Versione fuori supporto in produzione**: una vulnerabilità scoperta su Boot 3.5 non riceve più patch gratuite; migrare a 4.1.x ripristina gli aggiornamenti di sicurezza
- **Migrazione di molte classi**: cambiare a mano gli import di Jackson e le annotazioni nei test di un'applicazione grande richiede giorni; la ricetta OpenRewrite li cambia in pochi minuti e lascia al team la revisione
- **Passare per gradi**: con un'applicazione complessa si parte dagli starter "classic" per far ripartire il progetto e poi si passa uno alla volta agli starter modulari
- **Front-end separato che non si aggiorna insieme al back-end**: un'app mobile o un front-end React di un altro team usa ancora il vecchio formato; il versionamento permette di pubblicare la v2 senza rompere la v1
- **Contratto JSON che cambia senza che nessuno se ne accorga**: con Jackson 3 il formato di alcune date è diverso, e un front-end che le legge come testo smette di funzionare dopo l'aggiornamento
- **Chiamate tra servizi**: con più servizi che si chiamano, un'interfaccia `@HttpExchange` per servizio sostituisce decine di righe di `RestTemplate` ripetute
- **Meno `NullPointerException`**: con `@NullMarked` il compilatore e gli strumenti di analisi segnalano dove un valore può essere nullo, prima di arrivare in produzione

## Esempi di codice

### Base
Cosa mostra: gli starter modulari nel `pom.xml` e il comando che lancia la ricetta OpenRewrite.

```xml
<!-- Il parent porta la versione 4.1.2 -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>4.1.2</version>
</parent>

<dependencies>
    <!-- Boot 3.5: spring-boot-starter-web. Boot 4: starter dedicato a Spring MVC -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webmvc</artifactId>
    </dependency>
    <!-- Per chiamare altri servizi con RestClient e client @HttpExchange -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-restclient</artifactId>
    </dependency>
    <!-- Boot 4: anche i test hanno starter modulari, uno per tecnologia -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webmvc-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

```bash
# Ricetta OpenRewrite: aggiorna starter, proprietà, @MockBean e Spring Cloud
mvn -U org.openrewrite.maven:rewrite-maven-plugin:run \
  -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-spring:RELEASE \
  -Drewrite.activeRecipes=org.openrewrite.java.spring.boot4.UpgradeSpringBoot_4_0

# Poi si guarda cosa è cambiato prima di fare commit
git diff
```
- Ogni tecnologia ha il suo starter: gli artefatti da dichiarare sono di più, ma più mirati
- Gli starter di test seguono la stessa logica: uno per ogni tecnologia usata nei test
- La ricetta modifica il codice, quindi si lancia su un branch e si rivede il `git diff` riga per riga
- La ricetta non basta: il lavoro rimasto (Jackson 3 personalizzato, codice che usa classi rimosse) si corregge a mano

### Intermedio
Cosa mostra: due versioni dello stesso endpoint con il versionamento nativo di Framework 7, lette da un header.

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer
            .useRequestHeader("X-API-Version")      // il client indica la versione nell'header
            .addSupportedVersions("1.0", "2.0")     // versioni accettate
            .setVersionRequired(false)              // senza header i vecchi client continuano a funzionare
            .setDefaultVersion("1.0");              // ...e ricevono la v1
    }
}

@RestController
@RequestMapping("/ordini")
class OrdineController {

    // v1: il totale è un semplice numero
    @GetMapping(path = "/{id}", version = "1.0")
    OrdineV1 dettaglioV1(@PathVariable long id) {
        return servizio.v1(id);
    }

    // v2: il totale diventa un oggetto con importo e valuta
    @GetMapping(path = "/{id}", version = "2.0")
    OrdineV2 dettaglioV2(@PathVariable long id) {
        return servizio.v2(id);
    }
}

record OrdineV1(long id, BigDecimal totale) {}
record OrdineV2(long id, Denaro totale, Instant creatoIl) {}
```

```bash
curl localhost:8080/ordini/42                           # v1 (versione di default)
curl -H "X-API-Version: 2.0" localhost:8080/ordini/42   # v2
```
- La versione è un attributo della mappatura, non un prefisso da ripetere nei percorsi
- La configurazione sta in un solo punto: cambiare da header a segmento di path è una riga (`usePathSegment`)
- Una versione non supportata produce un errore 400, senza codice scritto a mano
- Per annunciare il ritiro di una versione, Framework 7 può inviare gli header `Deprecation` e `Sunset`

**Test delle due versioni**
```java
// v2: il test invia l'header come farebbe il client reale
mockMvc.perform(get("/ordini/42").header("X-API-Version", "2.0"))
       .andExpect(status().isOk())
       .andExpect(jsonPath("$.totale.valuta").value("EUR"));    // forma della v2

// versione non supportata: errore 400, senza codice scritto a mano
mockMvc.perform(get("/ordini/42").header("X-API-Version", "9.9"))
       .andExpect(status().isBadRequest());
```
- Serve un test per ogni versione pubblicata: una v2 non deve cambiare ciò che riceve la v1
- Senza header la risposta è quella della versione di default (v1)
- Una versione inesistente produce 400

### Avanzato
Cosa mostra: un client dichiarativo verso il servizio di magazzino, con contratto di null-safety JSpecify.

```java
// package-info.java: tutto il package è NON nullo, salvo dove indicato con @Nullable
@NullMarked
package com.shopflow.ordini.client;

import org.jspecify.annotations.NullMarked;
```

```java
// L'interfaccia dichiara le chiamate: Spring genera l'implementazione a runtime
@HttpExchange("/giacenze")
public interface MagazzinoClient {

    @GetExchange("/{sku}")
    Giacenza giacenza(@PathVariable String sku,
                      @RequestParam @Nullable String deposito);   // parametro opzionale

    @PostExchange("/riserve")
    Riserva riserva(@RequestBody RichiestaRiserva richiesta);
}

// Registra il client in un gruppo "magazzino": un client HTTP condiviso dalle sue interfacce
@Configuration
@ImportHttpServices(group = "magazzino", types = MagazzinoClient.class)
class ClientConfig {}

// Uso: il client si inietta come qualsiasi bean
@Service
class OrdineService {
    private final MagazzinoClient magazzino;
    OrdineService(MagazzinoClient magazzino) { this.magazzino = magazzino; }

    Riserva riservaArticoli(RichiestaRiserva richiesta) {
        return magazzino.riserva(richiesta);          // nessun codice HTTP nel servizio
    }
}
```

```properties
# application.properties: configurazione del gruppo
spring.http.serviceclient.magazzino.base-url=http://localhost:8081
spring.http.serviceclient.magazzino.read-timeout=2s
```
- L'interfaccia è l'unico punto che conosce i percorsi e i verbi HTTP del servizio remoto
- Il gruppo condivide indirizzo e timeout tra tutti i client che ne fanno parte
- `@NullMarked` rende non nulli parametri e ritorni del package: `@Nullable` è l'eccezione, scritta nel contratto
- Nell'architettura esagonale il client è un adattatore in uscita; il servizio dipende da un'interfaccia, non da `RestTemplate`

**Cosa succede se il servizio remoto non risponde bene.** Per default le risposte 4xx e 5xx diventano eccezioni.
```java
// Adattatore in uscita: traduce le eccezioni HTTP in errori di dominio
// (ArticoloSconosciuto e MagazzinoNonDisponibile sono nuove varianti di ErroreOrdine)
Result<Riserva, ErroreOrdine> riserva(RichiestaRiserva richiesta) {
    try {
        return new Result.Ok<>(magazzino.riserva(richiesta));
    } catch (HttpClientErrorException.NotFound e) {                      // 404: articolo sconosciuto
        return new Result.Errore<>(new ErroreOrdine.ArticoloSconosciuto(richiesta.sku()));
    } catch (HttpServerErrorException | ResourceAccessException e) {    // 5xx oppure timeout/rete
        return new Result.Errore<>(new ErroreOrdine.MagazzinoNonDisponibile());
    }
}
```
- 4xx → `HttpClientErrorException` (con sottoclassi come `NotFound`); 5xx → `HttpServerErrorException`; timeout e problemi di rete → `ResourceAccessException`
- Tradurre le eccezioni nell'adattatore evita che i dettagli HTTP arrivino al servizio di dominio
- Gli errori tornano come `Result`, come in MG1; i tentativi automatici con `@Retryable` si vedono in MG6

### Dopo la ricetta: Jackson 3 e test
Cosa mostra: il cambio di package di Jackson e la sostituzione di `@MockBean` nei test.

```java
// PRIMA (Boot 3.5, Jackson 2)
import com.fasterxml.jackson.databind.ObjectMapper;       // mapper modificabile
import com.fasterxml.jackson.annotation.JsonProperty;     // annotazioni

// DOPO (Boot 4, Jackson 3)
import tools.jackson.databind.json.JsonMapper;            // nuovo package, mapper immutabile
import com.fasterxml.jackson.annotation.JsonProperty;     // le annotazioni restano nel vecchio package

JsonMapper mapper = JsonMapper.builder().build();         // si configura con il builder, non dopo
String json = mapper.writeValueAsString(ordine);          // lancia JacksonException (non controllata)
```

```java
// PRIMA (Boot 3.5)
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class OrdineServiceTest {
    @MockBean MagazzinoClient magazzino;                  // sostituisce il bean con un mock
}

// DOPO (Boot 4): @MockitoBean esiste già da Framework 6.2, quindi si può cambiare PRIMA
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class OrdineServiceTest {
    @MockitoBean MagazzinoClient magazzino;               // stesso comportamento
}
```
- Cambiano package di mapper, moduli e classi di configurazione; le annotazioni di Jackson (`@JsonProperty`, `@JsonFormat`…) restano dove sono
- `@JsonComponent` è rinominata `@JacksonComponent`
- Il modulo ponte `spring-boot-jackson2` esiste ma serve solo come soluzione temporanea
- `@MockBean` e `@SpyBean` diventano `@MockitoBean` e `@MockitoSpyBean`: adottarli già su 3.x riduce le modifiche del giorno della migrazione

## Esercizi

### Facile
Obiettivo: dato il `pom.xml` di una piccola applicazione "Rubrica contatti" su Spring Boot 3.5 (con `spring-boot-starter-web`, `spring-boot-starter-data-jpa` e `spring-boot-starter-test`), portarlo alla 4.1.2 sostituendo gli starter con quelli modulari. Verificare che il progetto compili e che i test partano, annotando gli errori incontrati e come li si è risolti.

### Medio
Obiettivo: in un'applicazione "Biblioteca" con l'endpoint `GET /libri/{id}` che restituisce l'autore come semplice stringa, introdurre una versione 2 in cui l'autore diventa un oggetto con nome e cognome, senza modificare ciò che ricevono i client esistenti. Scrivere un test per ciascuna delle due versioni e verificare cosa risponde l'applicazione a una versione non supportata.

### Difficile
Obiettivo: scrivere un client dichiarativo `@HttpExchange` per un servizio di conversione valute (cambio tra due valute in una data), registrarlo con `@ImportHttpServices` e configurare indirizzo e timeout da `application.properties`. Marcare il package con `@NullMarked`, indicare con `@Nullable` i parametri opzionali e testare il client contro un server HTTP finto, verificando anche il comportamento quando il servizio risponde 500.

### Laboratorio finale: migrare ShopFlow da Spring Boot 3.5 a 4.1.2
Obiettivo: portare ShopFlow (progetto `shopflow-boot35`, Spring Boot 3.5.16, 35 test verdi) alla 4.1.2 seguendo l'ordine consigliato: pulire le deprecazioni sulla 3.5, ricetta OpenRewrite, correzioni a mano, verifica a runtime. Alla fine tutti i test devono essere verdi, il compilatore non deve segnalare deprecazioni e il contratto HTTP dell'API, fotografato prima e dopo con `scripts/cattura-contratto.sh`, deve essere invariato oppure avere differenze spiegate e motivate.
- Si lavora su un branch, con un commit per passo e senza mai usare `-DskipTests` o `@Disabled`
- Gli errori incontrati si annotano in un file `MIGRAZIONE.md` (messaggio, causa, soluzione), insieme alle versioni di Hibernate, Spring e Jackson prima e dopo
- Fase A (obbligatoria): starter modulari, Jackson 3, Spring Retry → `@Retryable` del Framework 7, client `@ImportHttpServices`, test, proprietà rinominate (con `spring-boot-properties-migrator`)
- Fase B (novità): versionamento nativo delle API e `RestTestClient`
- Traccia passo per passo: `shopflow-boot35/ESERCIZIO.md` (circa 3 ore)
