# Da Java 17 a Java 25, e cosa arriva con Java 27

## 1. Le differenze, versione per versione

**Il punto di partenza: cosa c'era già in Java 17 (LTS, settembre 2021)**

- Record (16), classi e interfacce `sealed` (17), text block (15), `switch` come espressione (14), pattern matching per `instanceof` (16), `var` (10), `Stream.toList()` (16), messaggi chiari sulle `NullPointerException` (14)
- Tutto ciò che segue è stato aggiunto **dopo**: è il lavoro di quattro anni, tra Java 17 e Java 25 (settembre 2025), entrambe LTS. In mezzo c'è Java 21 (LTS, settembre 2023)

**Come nasce una funzionalità**

- Prima esce in **preview** (linguaggio) o **incubator** (librerie): si prova solo con `--enable-preview` e può cambiare o sparire
- Diventa **finale** dopo uno o più giri; da quel momento è stabile e si può usare in produzione
- Le tabelle che seguono elencano le funzionalità nella versione in cui sono diventate finali

### Java 18 (marzo 2022)

| JEP | Novità | Area | Esempio |
| --- | --- | --- | --- |
| 400 | UTF-8 come codifica predefinita | Strumenti e default | 2.17 |
| 408 | Server web semplice (`jwebserver`) | Strumenti | 2.18 |
| 413 | Frammenti di codice nella Javadoc (`@snippet`) | Strumenti | 2.19 |
| 416 | Reflection reimplementata con i method handle | JVM | nessun cambiamento nel codice |
| 418 | SPI per la risoluzione degli indirizzi Internet | Librerie | uso raro, solo citato |
| 421 | Finalizzazione (`finalize()`) deprecata per la rimozione | Rimozioni | 2.26 |

### Java 19 (settembre 2022) e Java 20 (marzo 2023)

- Nessuna funzionalità finale del linguaggio: sono le versioni delle **preview** (virtual thread, record pattern, pattern matching per `switch`, scoped value)
- Piccole API di Java 19: `HashMap.newHashMap(n)`, `ExecutorService` in try-with-resources, `Future.state()` e `Future.resultNow()` (2.11–2.12)

### Java 21 (LTS, settembre 2023)

| JEP | Novità | Area | Esempio |
| --- | --- | --- | --- |
| 441 | Pattern matching per `switch` | Linguaggio | 2.1 |
| 440 | Record pattern | Linguaggio | 2.2 |
| 431 | Sequenced collections | Librerie | 2.7 |
| 444 | Virtual thread | Librerie | 2.8 |
| 452 | API per il Key Encapsulation Mechanism (KEM) | Sicurezza | 2.15 |
| 439 | ZGC generazionale | JVM | 2.21 |
| 451 | Avviso sul caricamento dinamico degli agent | Rimozioni e avvisi | 2.28 |
| 449 | Port Windows 32-bit deprecato | Rimozioni | 2.31 |
| — | `StringBuilder.repeat`, `Math.clamp`, `String.splitWithDelimiters`, `Character.isEmoji`, `HttpClient` chiudibile | Librerie | 2.11–2.12 |

### Java 22 (marzo 2024)

| JEP | Novità | Area | Esempio |
| --- | --- | --- | --- |
| 456 | Variabili e pattern senza nome (`_`) | Linguaggio | 2.3 |
| 454 | Foreign Function & Memory API | Librerie | 2.13 |
| 458 | Avvio di programmi composti da più file sorgente | Strumenti | 2.20 |
| 423 | Region pinning per G1 (meno pause con JNI) | JVM | nessun cambiamento nel codice |

### Java 23 (settembre 2024)

| JEP | Novità | Area | Esempio |
| --- | --- | --- | --- |
| 467 | Commenti di documentazione in Markdown (`///`) | Strumenti | 2.19 |
| 474 | ZGC generazionale di default | JVM | 2.21 |
| 471 | Metodi di accesso alla memoria di `sun.misc.Unsafe` deprecati | Rimozioni | 2.29 |

### Java 24 (marzo 2025)

| JEP | Novità | Area | Esempio |
| --- | --- | --- | --- |
| 485 | Stream Gatherers | Librerie | 2.9 |
| 484 | Class-File API | Librerie | 2.14 |
| 491 | Virtual thread senza *pinning* su `synchronized` | Librerie | 2.8 |
| 496, 497 | Algoritmi post-quantistici ML-KEM e ML-DSA | Sicurezza | 2.15 |
| 483 | Caricamento e linking anticipati delle classi (AOT cache) | JVM | 2.22 |
| 490 | ZGC solo generazionale | JVM | 2.21 |
| 475 | G1: espansione tardiva delle barriere (compilazione più veloce) | JVM | nessun cambiamento nel codice |
| 493 | `jlink` senza file JMOD (immagini più piccole) | Strumenti | solo citato |
| 486 | Security Manager disabilitato per sempre | Rimozioni | 2.27 |
| 472 | Avvisi sull'uso di JNI | Rimozioni e avvisi | 2.30 |
| 498 | Avviso sull'uso della memoria tramite `sun.misc.Unsafe` | Rimozioni e avvisi | 2.29 |
| 479, 501 | Rimosso il port Windows 32-bit; deprecato x86 32-bit | Rimozioni | 2.31 |
| — | `Reader.of(CharSequence)` | Librerie | 2.11–2.12 |

### Java 25 (LTS, settembre 2025)

| JEP | Novità | Area | Esempio |
| --- | --- | --- | --- |
| 513 | Flexible constructor bodies (istruzioni prima di `super(...)`) | Linguaggio | 2.4 |
| 511 | Import di moduli (`import module`) | Linguaggio | 2.5 |
| 512 | File sorgente compatti, `main` d'istanza e classe `IO` | Linguaggio | 2.6 |
| 506 | Scoped value | Librerie | 2.10 |
| 510 | API per la derivazione di chiavi (KDF) | Sicurezza | 2.16 |
| 514, 515 | AOT cache in un solo passo e profilazione anticipata dei metodi | JVM | 2.22 |
| 519 | Compact object headers (non più sperimentali) | JVM | 2.23 |
| 521 | Shenandoah generazionale | JVM | 2.24 |
| 518, 520 | JFR: campionamento cooperativo, tempi e tracciamento dei metodi | JVM | 2.25 |
| 503 | Rimosso il port x86 32-bit | Rimozioni | 2.31 |
| — | `Reader.readAllAsString()`, `readAllLines()`, `ForkJoinPool` con scheduling, `BodyHandlers.limiting` | Librerie | 2.11–2.12 |

### Il percorso delle funzionalità in preview

| Funzionalità | Prima preview o incubator | Finale |
| --- | --- | --- |
| Pattern matching per `switch` | 17 | 21 |
| Record pattern, virtual thread | 19 | 21 |
| Variabili senza nome `_` | 21 | 22 |
| Foreign Function & Memory API | 14 (incubator) | 22 |
| Stream Gatherers, Class-File API | 22 | 24 |
| File sorgente compatti e `main` d'istanza | 21 | 25 |
| Flexible constructor bodies | 22 | 25 |
| Import di moduli | 23 | 25 |
| Scoped value | 20 (incubator) | 25 |
| String template | 21 | **ritirate** in 23: non esistono più |
| Structured Concurrency | 19 (incubator) | ancora preview in 27 |
| Tipi primitivi nei pattern | 23 | ancora preview in 27 |
| Stable Values, poi Lazy Constants | 25 | ancora preview in 27 |
| PEM encodings | 25 | ancora preview in 27 |
| Vector API | 16 (incubator) | ancora incubator in 27 |

## 2. Prima e dopo: un esempio per ogni novità

### Linguaggio

#### 2.1 Pattern matching per `switch` (Java 21)

```java
// PRIMA (17): catena di if con instanceof
if (o == null) return "nessun valore";
if (o instanceof Integer i && i > 0) return "positivo " + i;
if (o instanceof Integer i) return "intero " + i;
if (o instanceof String s) return "testo di " + s.length() + " caratteri";
return "altro";
```

```java
// DOPO (21): switch sul tipo, con null e guardie
return switch (o) {
    case null                 -> "nessun valore";
    case Integer i when i > 0 -> "positivo " + i;
    case Integer i            -> "intero " + i;
    case String s             -> "testo di " + s.length() + " caratteri";
    default                   -> "altro";
};
```

- `case null` evita la `NullPointerException` che lo `switch` lancerebbe
- Su una gerarchia `sealed` il compilatore verifica che i casi siano tutti coperti e il `default` non serve

**Approfondimento**

- **Cosa succede davvero:** i `case` si valutano dall'alto in basso e vince il primo che corrisponde; il compilatore verifica che nessun caso sia già coperto da uno precedente
- **Trappole:**
  - ordine: `case Integer i` scritto prima di `case Integer i when i > 0` **non compila** («etichetta dominata»): prima le guardie, poi il caso generale
  - una guardia `when` non conta per l'esaustività: serve almeno un caso senza guardia per lo stesso tipo (in 4.1 è il secondo `Rifiutato`)
  - uno switch espressione deve coprire tutti i casi: su `Object` serve il `default`, su una `sealed` no
  - senza `case null` un valore `null` lancia `NullPointerException`; si può scrivere anche `case null, default -> ...`
  - se aggiungi un tipo a una `sealed` e non ricompili lo switch, a runtime esce `MatchException`; ricompilando, il compilatore indica il punto
- **Cosa si vede:** con `Object o = -5;` l'esempio restituisce `intero -5`; con `o = null` e senza `case null` si ottiene `NullPointerException`

#### 2.2 Record pattern (Java 21)

```java
record Punto(int x, int y) {}

// PRIMA (17)
if (o instanceof Punto p) {
    int x = p.x();
    int y = p.y();
    distanza = Math.hypot(x, y);
}
```

```java
// DOPO (21): il pattern estrae direttamente i componenti, anche annidati
if (o instanceof Punto(int x, int y)) {
    distanza = Math.hypot(x, y);
}
// case Linea(Punto(var x1, var y1), Punto(var x2, var y2)) -> ...
```

- Funziona in `instanceof` e nei `case`; con `var` il tipo viene dedotto

**Approfondimento**

- **Cosa succede davvero:** il pattern controlla il tipo, poi chiama gli accessor (`x()`, `y()`) e assegna i valori alle variabili, che esistono solo nel ramo in cui il pattern ha corrisposto
- **Trappole:**
  - un valore `null` non corrisponde mai a un record pattern: `null instanceof Punto(int x, int y)` è `false`
  - nel pattern annidato `Linea(Punto(var x1, var y1), ...)`, se il componente `Punto` è `null` il pattern non corrisponde; con `Linea(var p1, var p2)` invece sì, e `p1` vale `null`
  - se un accessor lancia un'eccezione, questa arriva incapsulata in una `MatchException`
- **Rispetto a Java 17:** servivano il cast, le chiamate agli accessor e due variabili d'appoggio per ogni livello di annidamento

#### 2.3 Variabili e pattern senza nome `_` (Java 22)

```java
// PRIMA (17): variabili dichiarate e mai usate
try { quantita = Integer.parseInt(testo); }
catch (NumberFormatException e) { quantita = 0; }   // "e" non serve
prezzi.forEach((sku, prezzo) -> totale.add(prezzo)); // "sku" non serve
```

```java
// DOPO (22)
try { quantita = Integer.parseInt(testo); }
catch (NumberFormatException _) { quantita = 0; }
prezzi.forEach((_, prezzo) -> totale.add(prezzo));
// anche nei pattern: case Spedito(var corriere, _) -> ...
```

- Dice al lettore (e al compilatore) che il valore è ignorato di proposito

**Approfondimento**

- **Cosa succede davvero:** `_` dichiara «qui c'è un valore, ma non mi serve»: il compilatore non crea nessuna variabile
- **Dove si può usare:** variabili locali, parametri di `catch` e di lambda, variabili di pattern, `for (var _ : lista)`, `try (var _ = ...)`; si può ripetere più volte nello stesso blocco
- **Trappole:**
  - non si può leggere: `_ + 1` non compila
  - non può essere il nome di un campo, di un metodo o di una classe
  - in Java 17 (e da Java 9) usare `_` come nome è già un errore; in Java 21 era preview

#### 2.4 Flexible constructor bodies (Java 25)

```java
// PRIMA (17): super(...) doveva essere la prima istruzione, serviva un metodo statico
class Sconto extends Promozione {
    Sconto(String codice, int percentuale) {
        super(codice, verifica(percentuale));
    }
    private static int verifica(int p) {
        if (p < 0 || p > 100) throw new IllegalArgumentException("percentuale non valida");
        return p;
    }
}
```

```java
// DOPO (25): si valida prima di super(...)
class Sconto extends Promozione {
    Sconto(String codice, int percentuale) {
        if (percentuale < 0 || percentuale > 100)
            throw new IllegalArgumentException("percentuale non valida");
        super(codice, percentuale);
    }
}
```

- Prima di `super(...)` non si può usare `this`, ma si possono assegnare i campi della classe: la superclasse li trova già valorizzati (esempio 4.5)

**Approfondimento**

- **Cosa succede davvero:** il codice prima di `super(...)` (il «prologo») gira prima che la superclasse sia costruita: l'oggetto non esiste ancora del tutto
- **Cosa si può fare nel prologo:** usare parametri e variabili locali, chiamare metodi statici, lanciare eccezioni, assegnare i campi della classe
- **Cosa non si può fare:** leggere `this` o un campo, chiamare metodi d'istanza (errore di compilazione)
- **Trappola:** un campo con inizializzatore nella dichiarazione non rientra tra quelli assegnabili (da verificare con una prova)
- **Rispetto a Java 17:** il metodo statico `verifica(...)` dentro `super(...)` era l'unico modo; ora la validazione si scrive in ordine, prima della chiamata
- **Cosa si vede:** `new Sconto("X", 150)` lancia `IllegalArgumentException: percentuale non valida` prima che la superclasse faccia qualsiasi cosa

#### 2.5 Import di moduli (Java 25)

```java
// PRIMA (17)
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.Path;
```

```java
// DOPO (25): tutti i package esportati dal modulo
import module java.base;
```

- Utile negli script e negli esempi; nel codice applicativo gli import singoli restano più leggibili
- Se due moduli esportano lo stesso nome (`List` in `java.base` e `java.desktop`), un import singolo risolve l'ambiguità

**Approfondimento**

- **Cosa succede davvero:** `import module java.base;` importa a richiesta tutti i package esportati dal modulo **e** dai moduli che esso dichiara `requires transitive` (per esempio `import module java.sql;` porta anche `java.logging` e `java.xml`)
- Funziona anche nei progetti non modulari (con classpath)
- **Trappola, ambiguità:** se due package importati hanno un nome uguale (`List` in `java.util` e in `java.awt`), l'errore compare solo quando usi il nome. Si risolve con un import singolo (`import java.util.List;`), che ha la precedenza
- In Java 17 non esiste; in Java 23 e 24 era preview

#### 2.6 File sorgente compatti, `main` d'istanza e `IO` (Java 25)

```java
// PRIMA (17): Ciao.java
public class Ciao {
    public static void main(String[] args) {
        System.out.println("Ciao!");
    }
}
```

```java
// DOPO (25): Ciao.java, si esegue con "java Ciao.java"
void main() {
    String nome = IO.readln("Come ti chiami? ");
    IO.println("Ciao, " + nome);
}
```

- Un file sorgente compatto importa automaticamente tutto il modulo `java.base`
- `IO` (in `java.lang`) offre `print`, `println` e `readln`; la classe implicita non si può usare da altre classi
- Pensato per script e primi programmi, non per il codice applicativo

**Approfondimento**

- **Cosa succede davvero:** il compilatore genera una classe implicita con il nome del file, nel package senza nome (un file compatto non può avere la riga `package`)
- `java Ciao.java` compila **in memoria**: non resta nessun `.class`; con `javac Ciao.java` il `.class` invece si crea
- **Il `main`:** serve un costruttore senza argomenti non privato; vale anche `void main(String[] args)`; se esistono entrambi, un `static void main` ha la precedenza sul `main` d'istanza
- I metodi di `IO` non sono importati in automatico: si scrive `IO.println(...)`
- **Trappola, `readln`:** restituisce `null` quando l'input è finito (input chiuso, cron, `< /dev/null`): conviene confrontare partendo dalla costante, `"s".equalsIgnoreCase(IO.readln(...))`
- **Cosa si vede:** `java Ciao.java` mostra `Come ti chiami?` e attende; con `echo Ore | java Ciao.java` stampa `Ciao, Ore`

### Librerie

#### 2.7 Sequenced collections (Java 21)

```java
// PRIMA (17)
var primo = ordini.get(0);
var ultimo = ordini.get(ordini.size() - 1);
var inverso = new ArrayList<>(ordini);
Collections.reverse(inverso);
var primaVoce = mappa.entrySet().iterator().next();     // LinkedHashMap
```

```java
// DOPO (21): gli stessi metodi su List, Deque, LinkedHashSet, LinkedHashMap
var primo = ordini.getFirst();
var ultimo = ordini.getLast();
var inverso = ordini.reversed();          // una VISTA, non una copia
var primaVoce = mappa.firstEntry();
```

- Anche `addFirst`, `addLast`, `removeLast`, `putFirst`, `pollFirstEntry`
- Su una collezione vuota `getFirst()` lancia `NoSuchElementException`

**Approfondimento**

- **Quali tipi:** `List`, `Deque`, `LinkedHashSet`, `TreeSet` (e `SortedSet`), `LinkedHashMap`, `TreeMap`; **non** `HashSet` e `HashMap`
- **Cosa succede davvero:** `reversed()` restituisce una **vista**: una modifica all'originale si vede nella vista e viceversa; per una copia: `new ArrayList<>(ordini.reversed())`
- **Casi limite:**
  - `getFirst()` su una collezione vuota lancia `NoSuchElementException`; `firstEntry()` su una mappa vuota restituisce `null`
  - su una lista immutabile (`List.of`) `addFirst` lancia `UnsupportedOperationException`; lo stesso su `TreeSet`, perché l'ordine lo decide il confronto
  - le entry restituite da `firstEntry()` non si possono modificare con `setValue`

#### 2.8 Virtual thread (Java 21, senza pinning da Java 24)

```java
// PRIMA (17): un pool di thread di sistema, dimensionato a mano
ExecutorService pool = Executors.newFixedThreadPool(200);
try {
    for (var f : fornitori) pool.submit(() -> chiediPrezzo(f));
} finally {
    pool.shutdown();
}
```

```java
// DOPO (21): un virtual thread per ogni attività bloccante
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (var f : fornitori) executor.submit(() -> chiediPrezzo(f));
}   // close() aspetta la fine delle attività
```

- Adatti a chiamate che **aspettano** (HTTP, database); non velocizzano i calcoli
- Fino a Java 23 un `synchronized` bloccava il thread di sistema sottostante (*pinning*); da Java 24 no
- In Spring Boot: `spring.threads.virtual.enabled=true`

**Approfondimento**

- **Cosa succede davvero:** ogni `submit` crea un virtual thread nuovo: è leggero, quindi non si mettono in un pool. Quando si blocca in attesa (HTTP, database) la JVM lo stacca dal thread di sistema e lo riprende dopo
- **Differenza nascosta:** in Java 17 `pool.shutdown()` **non aspetta** la fine delle attività; in Java 21 `close()` nel try-with-resources **aspetta**
- **Trappole:**
  - il pool da 200 era anche un limite di concorrenza verso il servizio chiamato: con un virtual thread per attività quel limite sparisce. Per proteggere un servizio o un database si usa un `Semaphore` (o il pool di connessioni)
  - con `submit` le eccezioni restano nel `Future`: se non chiami `get()` si perdono in silenzio
  - per il codice che calcola (CPU) non c'è nessun guadagno
  - il pinning di `synchronized` esiste in Java 21–23; da 24 non c'è più, ma le chiamate native lo causano ancora
- **Cosa si vede:** `Thread.currentThread()` stampa qualcosa come `VirtualThread[#22]/runnable@ForkJoinPool-1-worker-1`
- **In Spring Boot:** `spring.threads.virtual.enabled=true` richiede Java 21 o superiore e vale per le richieste web e per gli esecutori gestiti da Spring Boot

#### 2.9 Stream Gatherers (Java 24)

```java
// PRIMA (17): lotti da 50 con un ciclo e gli indici
List<List<Ordine>> lotti = new ArrayList<>();
for (int i = 0; i < ordini.size(); i += 50)
    lotti.add(ordini.subList(i, Math.min(i + 50, ordini.size())));
```

```java
// DOPO (24): un'operazione intermedia personalizzata nello stream
List<List<Ordine>> lotti = ordini.stream()
        .gather(Gatherers.windowFixed(50))
        .toList();

// Totale progressivo: [10, 30, 35]
Stream.of(10, 20, 5).gather(Gatherers.scan(() -> 0, Integer::sum)).toList();
```

- Pronti all'uso: `windowFixed`, `windowSliding`, `fold`, `scan`, `mapConcurrent`
- Se ne scrivono di propri con `Gatherer.ofSequential(...)`

**Approfondimento**

- **Cosa succede davvero:** `gather` è un'operazione intermedia lazy: `windowFixed(50)` tiene in memoria al più una finestra alla volta e la passa a valle quando è piena (o quando lo stream finisce)
- **Differenze rispetto al ciclo con `subList`:** ogni finestra è una lista a sé, non una vista sull'originale; l'ultima finestra può essere più corta, ed è restituita
- **Altri gatherer:** `windowSliding(3)` produce finestre sovrapposte; `scan` emette un valore per ogni elemento (3 in ingresso, 3 in uscita), `fold` emette solo il risultato finale; `mapConcurrent(n, f)` esegue su virtual thread e mantiene l'ordine
- **Trappola:** un gatherer sequenziale come `windowFixed` in uno stream `parallel()` viene eseguito in sequenza

#### 2.10 Scoped value (Java 25)

```java
// PRIMA (17): ThreadLocal, da pulire a mano
static final ThreadLocal<String> NEGOZIO = new ThreadLocal<>();
NEGOZIO.set("MI-01");
try {
    chiusura.esegui();               // in profondità: NEGOZIO.get()
} finally {
    NEGOZIO.remove();                // dimenticarlo = valore che resta sul thread
}
```

```java
// DOPO (25): valore immutabile, valido solo dentro run(...)
static final ScopedValue<String> NEGOZIO = ScopedValue.newInstance();
ScopedValue.where(NEGOZIO, "MI-01").run(() -> chiusura.esegui());   // in profondità: NEGOZIO.get()
```

- Non si può modificare né dimenticare: alla fine di `run` il valore sparisce
- Non passa ai thread di un `ExecutorService`; passa ai sottotask di Structured Concurrency (sezione 3)

**Approfondimento**

- **Cosa succede davvero:** `where(...).run(...)` associa il valore al thread **solo** per la durata della `run` e di tutto ciò che essa chiama; all'uscita il valore sparisce, anche se c'è un'eccezione
- **Trappole:**
  - fuori da `run`, `NEGOZIO.get()` lancia `NoSuchElementException`: si controlla con `isBound()` o si usa `orElse(...)`
  - un `where(...)` annidato ridefinisce il valore solo nel blocco interno; all'uscita torna quello di prima
  - `run` accetta un `Runnable`: per un blocco che lancia eccezioni controllate si usa `call(...)`
- **Rispetto a ThreadLocal:** non c'è `set`, non c'è `remove`, e un valore non resta appeso a un thread riusato di un pool

#### 2.11 Piccole API utili (Java 19–25)

```java
// PRIMA (17)                                          // DOPO
new HashMap<>((int) Math.ceil(n / 0.75));              HashMap.newHashMap(n);                    // 19
for (int i = 0; i < 40; i++) sb.append('-');           sb.repeat('-', 40);                       // 21
Math.max(1, Math.min(100, dimensione));                Math.clamp(dimensione, 1, 100);           // 21
new StringReader(testo);                               Reader.of(testo);                         // 24
new BufferedReader(r).lines()                          r.readAllAsString();                      // 25
        .collect(Collectors.joining("\n"));
```

- `"a,b;c".splitWithDelimiters("[,;]", 0)` restituisce anche i separatori: `[a, ",", b, ";", c]` (21)
- `readAllAsString()` conserva i terminatori di riga; `readAllLines()` restituisce una lista

**Approfondimento**

- **Trappole:**
  - `new HashMap<>(n)` per `n` elementi **ridimensiona comunque**, perché la soglia è al 75% della capacità; `HashMap.newHashMap(n)` calcola la capacità giusta
  - `Math.clamp(valore, min, max)` mette il valore **per primo**, al contrario di `max(min(...))`; con `min > max` lancia `IllegalArgumentException`
  - `sb.repeat('-', 40)` accetta un carattere (code point) o una stringa: `sb.repeat("ab", 3)`
  - `lines().collect(joining("\n"))` normalizza i fine riga e perde l'ultimo; `readAllAsString()` restituisce il testo **identico**

#### 2.12 Risorse che si chiudono da sole e future più comodi (Java 19–25)

```java
// PRIMA (17)
var client = HttpClient.newHttpClient();          // nessun close()
var risposta = client.send(richiesta, BodyHandlers.ofString());   // nessun limite alla dimensione
if (futuro.isDone() && !futuro.isCancelled()) valore = futuro.get();
```

```java
// DOPO
try (var client = HttpClient.newHttpClient()) {                                   // 21
    var risposta = client.send(richiesta,
            BodyHandlers.limiting(BodyHandlers.ofString(), 64 * 1024));         // 25: al massimo 64 KB
}
if (futuro.state() == Future.State.SUCCESS) valore = futuro.resultNow();          // 19: niente eccezioni checked
```

- Anche `ExecutorService` (19) si usa in try-with-resources
- Da Java 25 `ForkJoinPool` implementa `ScheduledExecutorService` e offre `submitWithTimeout`

**Approfondimento**

- **Cosa succede davvero:** `close()` su `HttpClient` aspetta la fine delle richieste in corso (per interrompere subito: `shutdownNow()`); prima di Java 21 non si poteva chiudere
- **`limiting`:** se i byte ricevuti superano la capacità, `send()` lancia `IOException`: il corpo non viene troncato in silenzio. Il limite riguarda solo i byte del corpo
- **`Future.state()`:** restituisce `RUNNING`, `SUCCESS`, `FAILED` o `CANCELLED`. Il vecchio controllo `isDone() && !isCancelled()` seguito da `get()` poteva comunque lanciare `ExecutionException` se il futuro era fallito. `resultNow()` lancia `IllegalStateException` se lo stato non è `SUCCESS`; per un futuro fallito c'è `exceptionNow()`

#### 2.13 Foreign Function & Memory API (Java 22)

```java
// PRIMA (17): JNI, con codice C da scrivere e compilare
public class Nativo {
    static { System.loadLibrary("nativo"); }   // libnativo.so scritta in C, con il suo header
    static native long lunghezza(String s);
}
```

```java
// DOPO (22): solo Java
Linker linker = Linker.nativeLinker();
MethodHandle strlen = linker.downcallHandle(
        linker.defaultLookup().find("strlen").orElseThrow(),
        FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS));
// invokeExact dichiara "throws Throwable": il metodo che contiene questo codice deve dichiararlo (o usare try/catch)
try (Arena arena = Arena.ofConfined()) {
    MemorySegment testo = arena.allocateFrom("Ciao");
    long lunghezza = (long) strlen.invokeExact(testo);    // 4
}   // alla chiusura dell'Arena la memoria è liberata
```

- Serve per usare librerie native (crittografia hardware, modelli di AI, driver) senza C
- Da Java 24 l'accesso nativo dà un avviso: si autorizza con `--enable-native-access=ALL-UNNAMED`

**Approfondimento**

- **Trappole:**
  - `invokeExact` richiede che il tipo del cast `(long)` coincida con il descrittore: altrimenti a runtime si ottiene `WrongMethodTypeException`
  - `Arena.ofConfined()` è usabile solo dal thread che l'ha creata (altrimenti `WrongThreadException`); dopo `close()` l'accesso al segmento lancia `IllegalStateException`; per condividere tra thread c'è `Arena.ofShared()`
  - senza `--enable-native-access` Java 24 e successivi stampano un avviso, non un errore
- **Rispetto a JNI:** niente codice C da scrivere e compilare; il prezzo è il codice che descrive i tipi (`FunctionDescriptor`)

#### 2.14 Class-File API (Java 24)

```java
// PRIMA (17): libreria esterna ASM, con un visitor
new ClassReader(bytes).accept(new ClassVisitor(Opcodes.ASM9) {
    @Override public MethodVisitor visitMethod(int acc, String nome, String d, String s, String[] e) {
        System.out.println(nome);
        return null;
    }
}, 0);
```

```java
// DOPO (24): API standard del JDK
ClassModel classe = ClassFile.of().parse(Path.of("Ordine.class"));
classe.methods().forEach(m -> System.out.println(m.methodName().stringValue()));
```

- Riguarda chi scrive framework e strumenti (Spring, Hibernate, agent): la vedrete nelle dipendenze, raramente nel vostro codice

**Approfondimento**

- **Perché ti riguarda comunque:** ogni versione di Java alza la versione dei class file (Java 17 = 61, Java 21 = 65, Java 25 = 69). Se un framework (Spring, Mockito, Hibernate) non conosce ancora la versione, l'avvio fallisce con `Unsupported class file major version 69`
- È una delle ragioni per cui, passando a un Java nuovo, vanno aggiornati anche i framework

### Sicurezza

#### 2.15 KEM e algoritmi post-quantistici (Java 21 e 24)

```java
// PRIMA (17): accordo di chiave a mano con KeyAgreement, molte righe e scelte delicate
KeyAgreement ka = KeyAgreement.getInstance("X25519");
ka.init(mieChiavi.getPrivate());
ka.doPhase(chiavePubblicaAltrui, true);
byte[] segreto = ka.generateSecret();      // poi va derivata una chiave AES a mano
```

```java
// DOPO (21): incapsulamento di una chiave segreta
KeyPair destinatario = KeyPairGenerator.getInstance("X25519").generateKeyPair();
KEM kem = KEM.getInstance("DHKEM");
KEM.Encapsulated inc = kem.newEncapsulator(destinatario.getPublic()).encapsulate();
SecretKey chiave = inc.key();                     // il mittente la usa subito
byte[] daInviare = inc.encapsulation();           // il destinatario ricava la stessa chiave:
SecretKey stessa = kem.newDecapsulator(destinatario.getPrivate()).decapsulate(daInviare);

// DOPO (24): resistente ai computer quantistici, con la stessa API
KeyPair coppia = KeyPairGenerator.getInstance("ML-KEM").generateKeyPair();
KEM kemPq = KEM.getInstance("ML-KEM");
// firme: Signature.getInstance("ML-DSA")
```

**Approfondimento**

- **Cosa succede davvero:** un KEM non cifra messaggi: serve a ottenere un **segreto condiviso**. Il mittente ottiene `chiave` e `encapsulation` e spedisce solo l'`encapsulation`; il destinatario la passa al suo decapsulatore e ottiene la stessa chiave
- La chiave si usa poi con un algoritmo simmetrico (per esempio AES-GCM)
- **ML-KEM e ML-DSA:** gli algoritmi post-quantistici hanno più livelli di sicurezza (i nomi esatti vanno controllati nella Javadoc di Java 25); `getInstance("ML-KEM")` usa quello predefinito
- Nel lavoro di tutti i giorni queste API le usano TLS e le librerie, non il codice applicativo (da Java 27 TLS usa già `X25519MLKEM768`)

#### 2.16 Derivazione di chiavi (KDF) (Java 25)

```java
// PRIMA (17): HKDF non è nel JDK, serviva Bouncy Castle o un'implementazione con Mac/HMAC
```

```java
// DOPO (25)
KDF hkdf = KDF.getInstance("HKDF-SHA256");
AlgorithmParameterSpec parametri = HKDFParameterSpec.ofExtract()
        .addIKM(segretoCondiviso)
        .addSalt(sale)
        .thenExpand("chiave-sessione".getBytes(StandardCharsets.UTF_8), 32);
SecretKey chiaveAes = hkdf.deriveKey("AES", parametri);
```

- Trasforma un segreto (per esempio quello del KEM) in una o più chiavi per scopi diversi

**Approfondimento**

- **Cosa succede davvero:** HKDF ha due fasi: *extract* concentra il segreto (con un sale opzionale), *expand* produce una chiave della lunghezza voluta
- L'etichetta (`"chiave-sessione"`) separa gli usi: con etichette diverse dallo stesso segreto escono chiavi diverse (per esempio una per cifrare e una per autenticare)
- `32` è la lunghezza in byte: 32 byte = 256 bit, adatti ad AES-256

### Strumenti e comportamenti predefiniti

#### 2.17 UTF-8 come codifica predefinita (Java 18)

```java
// PRIMA (17): la codifica dipende dal sistema operativo (Windows: windows-1252)
try (var r = new FileReader("clienti.csv")) { ... }   // "Città" letto male se il file è UTF-8
try (var r = new FileReader("clienti.csv", StandardCharsets.UTF_8)) { ... }   // l'unica strada sicura
```

```java
// DOPO (18): UTF-8 ovunque, salvo diversa indicazione
try (var r = new FileReader("clienti.csv")) { ... }
```

- Riguarda `FileReader`, `FileWriter`, `PrintStream`, `Scanner`; `Files` usava già UTF-8
- Per il comportamento vecchio: `-Dfile.encoding=COMPAT`. La console continua a usare la codifica del terminale

**Approfondimento**

- **Trappola:** un file `windows-1252` letto con `FileReader` da Java 18 in poi **non dà errore**: le lettere accentate diventano `�`. `Files.readString` invece lancia `MalformedInputException`. Per file di origine sconosciuta la codifica si dichiara sempre
- La proprietà `native.encoding` mostra la codifica del sistema; la console usa `stdout.encoding`
- **Cosa si vede:** un `clienti.csv` salvato da Excel mostra `Citt�` invece di `Città`

#### 2.18 Server web semplice (Java 18)

```bash
# PRIMA (17): un'applicazione apposta, oppure un altro linguaggio
python3 -m http.server 8000
```

```bash
# DOPO (18): incluso nel JDK, solo file statici
jwebserver -p 8000 -d /home/ore/sito
```

- Per prove e demo (una pagina, un JSON di esempio); non per la produzione

**Approfondimento**

- Ascolta solo su `127.0.0.1` (si cambia con `-b`) e risponde solo a richieste in lettura (GET e HEAD)
- Se la cartella contiene un `index.html`, lo serve; altrimenti mostra l'elenco dei file
- **Cosa si vede:** aprendo `http://localhost:8000` nel browser compare la pagina o l'elenco della cartella

#### 2.19 Javadoc: `@snippet` (Java 18) e Markdown (Java 23)

```java
// PRIMA (17)
/**
 * Somma due importi.
 * <p>Esempio:
 * <pre>{@code
 * var totale = a.somma(b);
 * }</pre>
 * @return un <b>nuovo</b> importo
 */
```

```java
// DOPO (23): commento in Markdown con ///, e snippet verificabili (18)
/// Somma due importi.
///
/// Esempio:
/// {@snippet :
/// var totale = a.somma(b);   // @highlight substring="somma"
/// }
///
/// @return un **nuovo** importo
```

**Approfondimento**

- **Cosa succede davvero:** `///` introduce un commento in Markdown (Java 23): `**nuovo**` al posto di `<b>nuovo</b>`; i vecchi commenti `/** */` funzionano come prima e i due stili possono convivere
- `@snippet` mostra un esempio di codice nella documentazione; con `@highlight` si evidenzia una parte; se l'esempio sta in un file separato (`file=...`) può essere compilato nei test e quindi non diventa obsoleto
- **Cosa si vede:** generando la documentazione con `javadoc`, l'esempio compare in un riquadro con la parola `somma` in evidenza

#### 2.20 Programmi su più file senza compilarli (Java 22)

```bash
# PRIMA (17): "java" esegue un solo file; con più file si compila prima
javac -d out *.java && java -cp out Main
```

```bash
# DOPO (22): Main.java usa Util.java nella stessa cartella, e viene compilato al volo
java Main.java
```

**Approfondimento**

- **Cosa succede davvero:** il lanciatore compila in memoria il file indicato e le classi che trova nei sorgenti della stessa cartella; non scrive nessun `.class` (la regola esatta di ricerca è nella JEP 458)
- **Limiti:** per usare librerie `.jar` serve `--class-path`; per un progetto vero restano Maven o Gradle; la compilazione si ripaga a ogni esecuzione

### JVM e prestazioni (nessuna modifica al codice)

#### 2.21 ZGC generazionale (Java 21, default da 23, unico da 24)

```bash
# PRIMA (17): ZGC senza generazioni
java -XX:+UseZGC -jar app.jar
# Java 21: generazioni da attivare
java -XX:+UseZGC -XX:+ZGenerational -jar app.jar
# DOPO (24 e 25): stesso comando di prima, ma ZGC è sempre generazionale
java -XX:+UseZGC -jar app.jar
```

- Pause sotto il millisecondo con meno memoria e meno CPU rispetto a ZGC senza generazioni
- In Java 24 e 25 `-XX:+ZGenerational` è un'opzione obsoleta: la JVM parte lo stesso ma la segnala; si può togliere
- ZGC è pensato per heap grandi e pause minime; G1 resta il predefinito. Il passaggio si decide **misurando** pause, memoria e CPU

#### 2.22 AOT cache: avvio più rapido (Java 24 e 25)

```bash
# PRIMA (17): AppCDS, solo le classi
java -XX:ArchiveClassesAtExit=app.jsa -jar app.jar
java -XX:SharedArchiveFile=app.jsa -jar app.jar
```

```bash
# DOPO (25): 1) si estrae l'applicazione (la cache funziona solo con la forma estratta)
java -Djarmode=tools -jar app.jar extract --destination application
cd application
# 2) esecuzione di addestramento: avvia il contesto Spring e si ferma appena è pronto
java -XX:AOTCacheOutput=app.aot -Dspring.context.exit=onRefresh -jar app.jar
# 3) gli avvii successivi riusano la cache
java -XX:AOTCache=app.aot -jar app.jar
```

- In Java 24 servivano due passi (`-XX:AOTMode=record`, poi `create`); da 25 uno solo
- **Cosa succede davvero:** la JVM salva in `app.aot` le classi caricate e collegate (da Java 25 anche i profili dei metodi); agli avvii successivi le legge dal file invece di ricaricarle. `-Dspring.context.exit=onRefresh` fa terminare l'applicazione appena il contesto Spring è pronto: senza, l'addestramento avvia il server e non finisce
- **Trappole:** la cache vale solo con la **stessa JVM**, lo stesso classpath nello stesso ordine e JAR con gli stessi timestamp; vanno evitate directory e `*` nel classpath. Con il jar «fat» non estratto non ha effetto
- **Come verificare:** avviare con `-Xlog:class+load:file=aot.log`: le classi prese dalla cache hanno come sorgente `shared objects file`
- Con `onRefresh` si ottimizza solo l'avvio; per sfruttare anche i profili dei metodi si può addestrare con un carico simile alla produzione (preparazione più lunga)
- Utile per i container che si avviano spesso; ogni nuova versione dell'applicazione richiede una nuova cache

#### 2.23 Compact object headers (Java 25)

```bash
# PRIMA (17): intestazione di ogni oggetto da 12 byte (16 senza puntatori compressi)
# DOPO (25): 8 byte, attivando un'opzione non più sperimentale
java -XX:+UseCompactObjectHeaders -jar app.jar
```

- Meno memoria per le applicazioni con molti oggetti piccoli (DTO, entità, record); in Java 27 è attivo di default

**Approfondimento**

- **Cosa succede davvero:** ogni oggetto ha un'intestazione che contiene il tipo e dati di servizio; passa da 12 a 8 byte. Con milioni di oggetti piccoli il risparmio è visibile
- **Versioni:** in Java 24 era sperimentale (serviva `-XX:+UnlockExperimentalVMOptions`), in Java 25 basta `-XX:+UseCompactObjectHeaders`, in Java 27 è attivo di default (`-XX:-UseCompactObjectHeaders` per spegnerlo)
- **Trappola:** ha dei limiti (per esempio sul numero di classi caricate, da verificare) e le librerie che leggono l'intestazione con `Unsafe` possono non funzionare; il guadagno si verifica misurando memoria e latenza prima e dopo

#### 2.24 Shenandoah generazionale (Java 25)

```bash
java -XX:+UseShenandoahGC -XX:ShenandoahGCMode=generational -jar app.jar
```

**Da sapere**

- Funziona solo con una JVM che include Shenandoah: le build di Oracle (Oracle JDK e OpenJDK di Oracle) in genere non lo contengono, mentre Eclipse Temurin sì. Con una build senza Shenandoah l'opzione non è disponibile
- Per scegliere tra G1, ZGC e Shenandoah si prova con il carico reale: pause, memoria e CPU

#### 2.25 JFR: tempi dei singoli metodi (Java 25)

```bash
# PRIMA (17): un profiler esterno o log con System.nanoTime() nel codice
# DOPO (25): JFR misura il metodo indicato, senza toccare il codice
java -XX:StartFlightRecording:method-timing='com.shopflow.ordini.CreaOrdineService::esegui',filename=ordini.jfr -jar app.jar
jfr view method-timing ordini.jfr
```

- Anche `method-trace` (chi chiama il metodo) e, su Linux, il profilo sul tempo di CPU (sperimentale)

**Approfondimento**

- **Cosa succede davvero:** JFR strumenta il metodo indicato e registra durata e numero di chiamate. Si indicano pochi metodi e non interi package: ogni metodo misurato ha un costo
- **Cosa si vede:** `jfr view method-timing ordini.jfr` stampa una tabella con il metodo, il numero di chiamate e i tempi
- La sintassi esatta dell'opzione va verificata con una prova su Java 25

### Rimozioni e avvisi: cosa può rompersi nella migrazione

#### 2.26 Finalizzazione deprecata (Java 18)

```java
// PRIMA (17)
class FileTemporaneo {
    @Override protected void finalize() { cancella(percorso); }   // forse, un giorno
}
```

```java
// DOPO: try-with-resources, con Cleaner come rete di sicurezza
class FileTemporaneo implements AutoCloseable {
    private static final Cleaner CLEANER = Cleaner.create();
    private final Cleaner.Cleanable pulizia;

    FileTemporaneo(Path percorso) {
        pulizia = CLEANER.register(this, () -> cancella(percorso));   // la lambda NON deve usare this
    }
    @Override public void close() { pulizia.clean(); }                // eseguita una volta sola
}
```

- Per scoprire chi dipende ancora da `finalize()`: avviare con `--finalization=disabled`

**Approfondimento**

- **Trappole:**
  - la lambda della pulizia **non deve** usare `this`: l'oggetto resterebbe raggiungibile e la pulizia non partirebbe mai
  - il `Cleaner` non garantisce quando (né se) la pulizia gira: il percorso normale è `close()`
  - `clean()` esegue la pulizia una sola volta, anche se chiamato due volte
- **Cosa si vede:** con `--finalization=disabled` gli oggetti con `finalize()` non vengono più finalizzati: così si scopre chi dipende ancora dal metodo

#### 2.27 Security Manager disabilitato (Java 24)

```bash
# PRIMA (17): deprecato, ma funzionante
java -Djava.security.manager=allow -jar app.jar
# DOPO (24): la JVM non parte; System.setSecurityManager lancia UnsupportedOperationException
```

- L'isolamento si ottiene con container e permessi del sistema operativo

**Approfondimento**

- **Cosa si vede:** con `-Djava.security.manager=allow` la JVM **non parte** e segnala l'errore; `System.setSecurityManager(...)` lancia `UnsupportedOperationException`

#### 2.28 Agent caricati a runtime (avviso da Java 21)

```text
WARNING: A Java agent has been loaded dynamically ...
WARNING: Dynamic loading of agents will be disallowed by default in a future release
```

- Il caso tipico sono i **test con Mockito**, che carica un agent per creare i mock
- Soluzione: dichiarare l'agent all'avvio (`-javaagent:` nell'`argLine` di Surefire) oppure, in via temporanea, `-XX:+EnableDynamicAgentLoading`

**Approfondimento**

- **Cosa si vede:** l'avviso compare alla prima creazione di un mock; non è un errore e i test passano
- **Come si risolve:** si dichiara l'agent nell'`argLine`. Esempio da adattare (versione di Mockito presa dalla propria BOM):

```xml
<plugin>
  <artifactId>maven-surefire-plugin</artifactId>
  <configuration>
    <argLine>-javaagent:${settings.localRepository}/org/mockito/mockito-core/${mockito.version}/mockito-core-${mockito.version}.jar</argLine>
  </configuration>
</plugin>
```

- `-XX:+EnableDynamicAgentLoading` toglie l'avviso ma è solo una soluzione temporanea

#### 2.29 `sun.misc.Unsafe` (deprecato in 23, avviso da 24)

- I metodi di accesso alla memoria danno un avviso al primo uso: riguarda librerie vecchie (versioni datate di Netty, Kryo, serializzatori)
- Si risolve aggiornando la libreria; in attesa: `--sun-misc-unsafe-memory-access=allow`

**Approfondimento**

- **Cosa si vede:** un avviso su `stderr` al primo uso, con il nome della classe che lo chiama: da lì si capisce quale libreria aggiornare

#### 2.30 JNI (avviso da Java 24)

- Caricare una libreria nativa dà un avviso; si autorizza con `--enable-native-access=ALL-UNNAMED` (o il nome del modulo)

#### 2.31 Port a 32 bit

- Windows 32-bit rimosso in 24, x86 32-bit rimosso in 25: servono un sistema e una JVM a 64 bit

## 3. Cosa ci aspetta: Java 26 e Java 27

**Il calendario**

- Java 26 (marzo 2026) e Java 27 (settembre 2026) **non sono LTS**: ricevono aggiornamenti per sei mesi
- In produzione si resta su Java 25; la prossima LTS è Java 29 (settembre 2027)
- Le preview si provano così: `javac --release 27 --enable-preview` e `java --enable-preview`, oppure `java --enable-preview Prova.java`

### Le novità finali di Java 26

| JEP | Novità | Effetto pratico |
| --- | --- | --- |
| 500 | Prepararsi a rendere `final` davvero definitivo | Avviso se la reflection modifica un campo `final` |
| 517 | HTTP/3 nell'`HttpClient` | Si chiede con una riga, con ripiego automatico su HTTP/2 |
| 516 | AOT cache con qualunque garbage collector | Avvio rapido anche con ZGC |
| 522 | G1 con meno sincronizzazione | Più throughput, senza modifiche |
| 504 | Rimossa l'API Applet | Nessun effetto su applicazioni server |

### Le novità finali di Java 27

| JEP | Novità | Effetto pratico |
| --- | --- | --- |
| 523 | G1 è il garbage collector predefinito ovunque | Anche nei container piccoli (prima si sceglieva Serial) |
| 534 | Compact object headers attivi di default | Meno memoria; si disattivano con `-XX:-UseCompactObjectHeaders` |
| 527 | Scambio di chiavi ibrido post-quantistico in TLS 1.3 | `X25519MLKEM768` usato per primo, senza toccare il codice |
| 536 | JFR nasconde i dati sensibili nelle registrazioni | Password e token negli argomenti e nelle variabili d'ambiente diventano `[REDACTED]` |

### In preview e incubator in Java 27

| JEP | Funzionalità | Giro |
| --- | --- | --- |
| 533 | Structured Concurrency | settima preview |
| 531 | Lazy Constants (prima Stable Values) | terza preview |
| 532 | Tipi primitivi nei pattern, `instanceof` e `switch` | quinta preview |
| 538 | PEM encodings di chiavi e certificati | terza preview |
| 537 | Vector API | dodicesimo incubator |

#### 3.1 Final davvero final (Java 26)

```java
// Codice che oggi funziona in silenzio, e da Java 26 dà un avviso
Field campo = Ordine.class.getDeclaredField("id");     // private final String id
campo.setAccessible(true);
campo.set(ordine, "ORD-99");
```

- Nei test con Spring è frequente: `ReflectionTestUtils.setField(...)` su un campo `final` produce lo stesso avviso
- Il comportamento si regola con `--illegal-final-field-mutation=allow|warn|debug|deny`: `warn` è il predefinito di Java 26 (un avviso per modulo), `debug` aggiunge la traccia a ogni tentativo, `deny` fa lanciare `IllegalAccessException`; in futuro il default diventerà `deny`
- Per autorizzare esplicitamente un modulo (o il classpath): `--enable-final-field-mutation=ALL-UNNAMED` oppure l'elenco dei moduli
- Nei test conviene provare con `deny`, per scoprire i casi prima che diventi il default
- Meglio iniettare i valori dal costruttore e lasciare la reflection su `final` alle sole librerie autorizzate

**Approfondimento**

- **Cosa si vede:** con il default `warn` compare, una volta per modulo, una riga del tipo `Final field ... has been mutated reflectively by class ...`; con `debug` anche la traccia dello stack per ogni tentativo; con `deny` un'eccezione `IllegalAccessException`. Con JFR l'evento si chiama `jdk.FinalFieldMutation` (da verificare)

#### 3.2 HTTP/3 (Java 26)

```java
// PRIMA (25): al massimo HTTP/2
var richiesta = HttpRequest.newBuilder(URI.create("https://api.esempio.it/prezzi"))
        .version(HttpClient.Version.HTTP_2).build();
```

```java
// DOPO (26): si prova HTTP/3; se il server non lo supporta, si torna a HTTP/2 o 1.1
var richiesta = HttpRequest.newBuilder(URI.create("https://api.esempio.it/prezzi"))
        .version(HttpClient.Version.HTTP_3).build();
```

**Approfondimento**

- **Cosa succede davvero:** HTTP/3 usa UDP (non TCP): il firewall deve lasciar passare il traffico UDP; se il server non lo supporta, il client ripiega su HTTP/2 o 1.1 senza errore
- Come il client scopre che un server parla HTTP/3, e come forzarlo (`HttpOption.H3_DISCOVERY`), va controllato sulla Javadoc di Java 26
- **Cosa si vede:** `risposta.version()` dice quale protocollo è stato usato davvero

#### 3.3 G1 ovunque e header compatti (Java 27)

```bash
# Un container con 1 CPU
java -XX:ActiveProcessorCount=1 -Xlog:gc -version
# Java 25: Using Serial      Java 27: Using G1
```

- Chi vuole il comportamento di prima lo indica esplicitamente: `-XX:+UseSerialGC`
- Vale la pena **misurare** memoria e latenza dei servizi dopo il passaggio

**Approfondimento**

- **Cosa succede davvero:** la JVM sceglie il garbage collector in base alla macchina: in Java 25, su un container con poca CPU o poca memoria, usa Serial; in Java 27 usa G1 (le soglie esatte vanno verificate)
- **Trappola:** G1 usa più memoria di Serial sui container piccoli: dopo il passaggio vanno controllati il consumo di memoria e i limiti del container
- **Cosa si vede:** con `-Xlog:gc -version` compare `Using Serial` in Java 25 e `Using G1` in Java 27

#### 3.4 Structured Concurrency (preview in 27)

```java
// OGGI (25): futuri indipendenti; se uno fallisce, gli altri continuano
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    Future<Cliente> cliente = executor.submit(() -> anagrafica.trova(id));
    Future<List<Ordine>> ordini = executor.submit(() -> storico.ultimi(id));
    return new Profilo(cliente.get(), ordini.get());   // se cliente fallisce, ordini gira fino in fondo
}
```

```java
// DOMANI (27, preview): i sottotask vivono e muoiono con il blocco
try (var scope = StructuredTaskScope.open()) {
    Subtask<Cliente> cliente = scope.fork(() -> anagrafica.trova(id));
    Subtask<List<Ordine>> ordini = scope.fork(() -> storico.ultimi(id));
    scope.join();        // un errore cancella gli altri sottotask; in 27 lancia ExecutionException
    return new Profilo(cliente.get(), ordini.get());
}
```

- I sottotask ereditano gli scoped value del thread che li crea
- L'API cambia ancora da una versione all'altra (in 27: `join()` lancia `ExecutionException`, `onTimeout()` diventa `timeout()`)

**Approfondimento**

- **Cosa succede davvero:** i sottotask vivono dentro lo scope e non possono sopravvivere alla chiusura del blocco: se `join()` fallisce, gli altri sottotask vengono interrotti
- **Trappole:**
  - `subtask.get()` prima di `join()` lancia `IllegalStateException`
  - lo scope va chiuso dal thread che lo ha aperto (try-with-resources)
  - la politica predefinita è «tutti riusciti, altrimenti fallisce»; altre politiche (per esempio «il primo che risponde») si scelgono con un parametro di `open(...)`: da controllare nell'API di Java 27
- **Preview:** una classe compilata con `--enable-preview` gira solo sulla stessa versione di Java con cui è stata compilata

#### 3.5 Lazy Constants (preview in 27)

```java
// OGGI (17–25): inizializzazione pigra a mano (holder idiom)
class Tariffe {
    private static class Holder { static final TabellaIva TABELLA = TabellaIva.caricaDaFile(); }
    static TabellaIva tabella() { return Holder.TABELLA; }
}
```

```java
// DOMANI (27, preview): una costante calcolata al primo uso, trattata dalla JVM come final
class Tariffe {
    static final LazyConstant<TabellaIva> TABELLA = LazyConstant.of(TabellaIva::caricaDaFile);
    static TabellaIva tabella() { return TABELLA.get(); }   // la prima chiamata carica, le altre leggono
}
// Anche per le collezioni: List.ofLazy(20, _ -> new Connessione()), Map.ofLazy(...), Set.ofLazy(...)
```

- Avvio più rapido: si carica solo ciò che serve davvero, e in modo sicuro tra thread
- In Java 25 si chiamava `StableValue` con un'altra API: è l'esempio di quanto una preview possa cambiare

**Approfondimento**

- **Cosa succede davvero:** `get()` esegue la funzione alla prima chiamata e conserva il risultato; se più thread chiamano insieme, gli altri aspettano; la funzione viene eseguita una volta sola
- **Trappole (comportamento in Java 27):**
  - se la funzione lancia un'eccezione non controllata, la costante passa a uno stato di errore: `get()` lancia `NoSuchElementException` (con l'eccezione originale come causa) e le chiamate successive lanciano senza richiamare la funzione. In Java 26 invece veniva riprovata
  - anche un risultato `null` manda la costante in errore
  - il vantaggio rispetto al *holder idiom* è che la JVM può trattarla come una costante `final` e ottimizzarla
- **Cosa si vede:** dopo un errore in `caricaDaFile`, ogni `TABELLA.get()` lancia `NoSuchElementException` finché l'applicazione non viene riavviata

#### 3.6 Tipi primitivi nei pattern (preview in 27)

```java
// OGGI (25): controlli di intervallo a mano
if (valore >= Byte.MIN_VALUE && valore <= Byte.MAX_VALUE) {
    byte b = (byte) valore;
    ...
}
```

```java
// DOMANI (27, preview): il pattern corrisponde solo se la conversione non perde informazione
if (valore instanceof byte b) { ... }

String testo = switch (stato) {          // stato è un int (codice HTTP)
    case 200                       -> "OK";
    case int c when c >= 500       -> "errore del server " + c;
    case int c                     -> "errore del client " + c;
};
```

**Approfondimento**

- **Cosa succede davvero:** `valore instanceof byte b` è vero solo se il valore `int` sta in un `byte` **senza perdere informazione**: non c'è nessun cast silenzioso
- **Trappola:** la funzionalità è ancora in preview (richiede `--enable-preview`) e il comportamento sugli altri tipi primitivi nei `switch` può cambiare

#### 3.7 PEM encodings (preview in 27)

```java
// OGGI (25): togliere intestazioni, decodificare Base64, scegliere la KeySpec giusta
String base64 = pem.replace("-----BEGIN PUBLIC KEY-----", "")
                   .replace("-----END PUBLIC KEY-----", "").replaceAll("\\s", "");
var spec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64));
PublicKey chiave = KeyFactory.getInstance("RSA").generatePublic(spec);
```

```java
// DOMANI (27, preview): un'API dedicata per leggere e scrivere chiavi e certificati in PEM
PublicKey chiave = PEMDecoder.of().decode(pem, PublicKey.class);
String testo = PEMEncoder.of().encodeToString(chiave);
```

- Nella terza preview alcuni nomi sono cambiati: verificare la Javadoc di Java 27 prima di usarla

**Approfondimento**

- **Cosa succede davvero:** `PEMDecoder` legge l'intestazione, decodifica il Base64 e sceglie la `KeySpec` giusta; `decode(pem, PublicKey.class)` verifica anche il tipo atteso
- **Trappola:** i nomi delle classi e dei metodi sono cambiati tra una preview e l'altra e possono cambiare ancora: il codice va riscritto dopo aver letto la Javadoc di Java 27

## 4. Esempi in contesto reale

### 4.1 Gestire le notifiche di un gestore dei pagamenti (Java 21–22)

Cosa mostra: `sealed`, record pattern, `switch` esaustivo, guardia `when` e `_` per smistare gli eventi di un webhook.

```java
// Gli eventi del webhook, già letti dal JSON
public sealed interface EventoPagamento {
    record Autorizzato(String ordineId, BigDecimal importo) implements EventoPagamento {}
    record Incassato(String ordineId, BigDecimal importo, Instant quando) implements EventoPagamento {}
    record Rifiutato(String ordineId, String codice, String messaggio) implements EventoPagamento {}
    record Rimborsato(String ordineId, BigDecimal importo, String motivo) implements EventoPagamento {}
}

// Che cosa fa l'applicazione in risposta
public sealed interface Azione {
    record Attendi(String ordineId) implements Azione {}
    record ConfermaOrdine(String ordineId, BigDecimal importo) implements Azione {}
    record ChiediAltraCarta(String ordineId) implements Azione {}
    record AnnullaOrdine(String ordineId, String motivo) implements Azione {}
    record RegistraRimborso(String ordineId, BigDecimal importo) implements Azione {}
}

// nel servizio: import com.shopflow.pagamenti.EventoPagamento.*;
Azione decidi(EventoPagamento evento) {
    return switch (evento) {
        case Autorizzato(var id, _)                    -> new Azione.Attendi(id);
        case Incassato(var id, var importo, _)         -> new Azione.ConfermaOrdine(id, importo);
        case Rifiutato(var id, var codice, _)
                when codice.equals("FONDI_INSUFFICIENTI") -> new Azione.ChiediAltraCarta(id);
        case Rifiutato(var id, _, var messaggio)       -> new Azione.AnnullaOrdine(id, messaggio);
        case Rimborsato(var id, var importo, _)        -> new Azione.RegistraRimborso(id, importo);
    };   // nessun default: se il gestore aggiunge un evento, il compilatore indica dove gestirlo
}
```

- In Java 17 sarebbe stata una catena di `instanceof` con un `else` finale che nasconde i casi dimenticati
- La decisione è una funzione pura: si prova con un test senza server né JSON

**Approfondimento**

- **Cosa succede davvero:** il compilatore controlla che i cinque tipi di evento siano tutti coperti; per questo non c'è `default`
- **Trappole:**
  - `codice.equals("FONDI_INSUFFICIENTI")` lancia `NullPointerException` se `codice` è `null`: più sicuro `"FONDI_INSUFFICIENTI".equals(codice)`
  - un `evento` `null` lancia `NullPointerException` (non c'è `case null`): in un webhook si controlla prima
  - se si aggiunge un nuovo record a `EventoPagamento` e si ricompila, il compilatore segnala `decidi`; se `decidi` non viene ricompilato, a runtime esce `MatchException`
  - l'ordine dei due `Rifiutato` conta: il caso senza guardia deve venire **dopo** quello con la guardia, altrimenti il compilatore segnala un errore
- **Cosa si vede:** `decidi(new Rifiutato("42", "FONDI_INSUFFICIENTI", "..."))` restituisce `ChiediAltraCarta`; con qualsiasi altro codice restituisce `AnnullaOrdine`

### 4.2 Importare un listino da CSV a lotti (Java 21–24)

Cosa mostra: `Math.clamp`, Gatherers, `_` e sequenced collections in un import notturno.

```java
void importa(Path file, int dimensioneLotto) throws IOException {
    int lotto = Math.clamp(dimensioneLotto, 100, 5_000);     // configurazione fuori misura? la riporto nei limiti
    var scartate = new ArrayList<String>();

    try (var righe = Files.lines(file)) {
        righe.skip(1)                                         // intestazione
             .map(riga -> leggi(riga, scartate))
             .flatMap(Optional::stream)                       // solo le righe valide
             .gather(Gatherers.windowFixed(lotto))            // lotti per l'inserimento nel database
             .forEach(listino::salvaTutti);
    }
    if (!scartate.isEmpty()) {
        log.warn("{} righe scartate, la prima: '{}', l'ultima: '{}'",
                 scartate.size(), scartate.getFirst(), scartate.getLast());
    }
}

Optional<RigaListino> leggi(String riga, List<String> scartate) {
    var campi = riga.split(";");
    try {
        return Optional.of(new RigaListino(campi[0].strip(), new BigDecimal(campi[1].replace(',', '.'))));
    } catch (ArrayIndexOutOfBoundsException | NumberFormatException _) {    // l'eccezione non serve
        scartate.add(riga);
        return Optional.empty();
    }
}
```

- Lo stream legge il file una riga alla volta: anche un listino molto grande non riempie la memoria
- L'ultimo lotto può essere più corto: `windowFixed` lo restituisce comunque
- Lo stream è sequenziale, quindi aggiungere a `scartate` dentro `map` è sicuro; con `parallel()` non lo sarebbe

**Approfondimento**

- **Trappole:**
  - ogni chiamata a `listino::salvaTutti` è un lotto a sé: se l'import fallisce a metà, i lotti già salvati restano salvati (a meno che una transazione avvolga tutto)
  - `Files.lines` legge in UTF-8: un CSV esportato da Excel in `windows-1252` fa lanciare `UncheckedIOException` (`MalformedInputException`); in quel caso `Files.lines(file, Charset.forName("windows-1252"))`
  - `scartate.getFirst()` su una lista vuota lancia `NoSuchElementException`: per questo c'è l'`if (!scartate.isEmpty())`
  - `Math.clamp(valore, min, max)` mette il valore **per primo** e, con questi argomenti, restituisce un `int`

### 4.3 Chiedere il prezzo a più fornitori in parallelo (Java 19–25)

Cosa mostra: virtual thread, timeout complessivo, `Future.state()` e una risposta HTTP con dimensione limitata.

```java
record Offerta(String fornitore, BigDecimal prezzo) {}

Optional<Offerta> miglioreOfferta(String sku) throws InterruptedException {
    List<Callable<Offerta>> richieste = fornitori.stream()
            .map(f -> (Callable<Offerta>) () -> f.quota(sku))
            .toList();

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        return executor.invokeAll(richieste, 2, TimeUnit.SECONDS).stream()   // chi non risponde in 2 s è cancellato
                .filter(f -> f.state() == Future.State.SUCCESS)                 // ignoro errori e cancellazioni
                .map(Future::resultNow)
                .min(Comparator.comparing(Offerta::prezzo));
    }
}

// Nella classe Fornitore
Offerta quota(String sku) throws IOException, InterruptedException {
    var richiesta = HttpRequest.newBuilder(URI.create(baseUrl + "/prezzi/" + sku))
            .timeout(Duration.ofSeconds(2)).build();
    var risposta = client.send(richiesta,
            BodyHandlers.limiting(BodyHandlers.ofString(), 64 * 1024));        // un fornitore impazzito non ci manda 1 GB
    return new Offerta(nome, new BigDecimal(risposta.body().strip()));
}
```

- Dieci fornitori significano dieci virtual thread, senza dimensionare un pool
- Un fornitore lento o guasto non blocca la risposta: si sceglie tra quelli che hanno risposto
- Con Structured Concurrency (3.4) la stessa logica diventerà più esplicita su errori e cancellazioni

**Approfondimento**

- **Cosa succede davvero:** `invokeAll(..., 2, SECONDS)` torna quando finiscono tutti i task **o** scade il tempo: quelli non finiti sono **cancellati** (stato `CANCELLED`)
- **Trappole:**
  - i fornitori in errore (`FAILED`) vengono scartati senza lasciare traccia: conviene loggare `f.exceptionNow()`
  - manca il controllo del codice HTTP: una risposta 500 con un testo fa lanciare `NumberFormatException` in `new BigDecimal(...)`, che finisce nello stato `FAILED`; in `quota` si può controllare `risposta.statusCode()` prima di leggere il corpo
  - superare il limite di 64 KB fa lanciare `IOException` (anche questo `FAILED`)
  - non c'è un limite alle richieste contemporanee: con molti SKU in parallelo si può sovraccaricare il fornitore (vedi `Semaphore` in 2.8)
  - `HttpClient` è thread-safe: se ne usa uno solo, condiviso tra i task
- Se tutti i fornitori falliscono, il metodo restituisce `Optional.empty()`: il chiamante deve decidere cosa fare

### 4.4 Il negozio corrente in una chiusura giornaliera (Java 25)

Cosa mostra: uno scoped value al posto di un parametro passato attraverso molti metodi.

```java
public final class Contesto {
    public static final ScopedValue<Negozio> NEGOZIO = ScopedValue.newInstance();
}

// Job notturno: un giro per ogni negozio
@Scheduled(cron = "0 0 2 * * *")
void chiusuraNotturna() {
    for (var negozio : negozi.attivi()) {
        ScopedValue.where(Contesto.NEGOZIO, negozio)
                   .run(() -> chiusura.esegui());              // incassi, inventario, report...
    }
}

// Sei chiamate più in basso, senza che nessuno abbia passato il negozio come parametro
@Component
class RegistroAudit {
    void scrivi(String evento) {
        var codice = Contesto.NEGOZIO.isBound() ? Contesto.NEGOZIO.get().codice() : "-";
        log.info("[{}] {}", codice, evento);
    }
}
```

- Il valore non si può modificare per sbaglio a metà del giro e non resta appeso al thread
- Va usato per il **contesto** (negozio, utente, id della richiesta), non per passare dati di business
- Attenzione: un `@Async` o un `ExecutorService` usati all'interno **non** vedono il valore

**Approfondimento**

- **Cosa succede davvero:** durante `run` il valore è legato al thread del job; in `RegistroAudit` si legge senza nessun parametro
- **Trappole:**
  - se `chiusura.esegui()` lancia un'eccezione, il `for` si ferma e gli altri negozi non vengono chiusi: serve un `try/catch` per ogni giro (sotto)
  - un `@Async` o un `ExecutorService` non vedono il valore: lì `isBound()` è `false` e il log mostra `-`
  - Spring Security (`SecurityContextHolder`) usa ancora i `ThreadLocal`: i due meccanismi convivono
- **Cosa si vede:** nel log `[MI-01] ...` per le chiamate dentro `run`, `[-] ...` per quelle fuori o su altri thread

```java
for (var negozio : negozi.attivi()) {
    try {
        ScopedValue.where(Contesto.NEGOZIO, negozio).run(() -> chiusura.esegui());
    } catch (RuntimeException e) {
        log.error("Chiusura fallita per {}", negozio.codice(), e);   // gli altri negozi proseguono
    }
}
```

### 4.5 Un bonifico validato prima di nascere (Java 25)

Cosa mostra: flexible constructor bodies per validare e preparare i dati prima del costruttore della superclasse.

```java
abstract class Movimento {
    private final String iban;
    private final BigDecimal importo;

    protected Movimento(String iban, BigDecimal importo) {
        this.iban = iban;
        this.importo = importo;
        log.info("Nuovo movimento: {}", descrizione());     // chiama un metodo ridefinito dalla sottoclasse
    }
    abstract String descrizione();
}

final class Bonifico extends Movimento {
    private final String causale;

    Bonifico(String iban, BigDecimal importo, String causale) {
        var ibanPulito = iban.replace(" ", "").toUpperCase();
        if (!ibanPulito.matches("IT\\d{2}[A-Z]\\d{10}[0-9A-Z]{12}"))
            throw new IllegalArgumentException("IBAN non valido: " + iban);
        if (importo.signum() <= 0)
            throw new IllegalArgumentException("importo non positivo");
        this.causale = causale.strip();      // assegnato PRIMA di super: descrizione() lo vede già
        super(ibanPulito, importo);
    }

    @Override String descrizione() { return "bonifico: " + causale; }
}
```

- Con Java 17 l'IBAN andava pulito in un metodo statico dentro la chiamata a `super(...)`
- Con Java 17 `descrizione()`, chiamata dal costruttore di `Movimento`, avrebbe letto `causale` ancora a `null`

**Approfondimento**

- **Cosa succede davvero:** l'ordine è: validazione, assegnazione di `causale`, poi `super(...)`. Quando il costruttore di `Movimento` chiama `descrizione()`, il campo `causale` è già valorizzato
- **Cosa si vede:** con `new Bonifico("IT60X0542811101000000123456", new BigDecimal("10"), " Saldo luglio ")` il log stampa `Nuovo movimento: bonifico: Saldo luglio`; in Java 17 stampava `bonifico: null`, perché il campo non era ancora assegnato
- **Errori di compilazione:** leggere `causale` o chiamare `descrizione()` prima di `super(...)` non compila
- Un IBAN non valido lancia `IllegalArgumentException` prima che `Movimento` venga costruito: nessun oggetto a metà
- **Trappola:** chiamare dal costruttore un metodo ridefinibile resta una cattiva pratica: l'esempio mostra cosa è cambiato, non come scrivere il codice

### 4.6 Uno script di manutenzione senza progetto (Java 25)

Cosa mostra: file sorgente compatto, `main` d'istanza, `IO`, sequenced collections e `_`.

```java
// PulisciLog.java — si esegue con: java PulisciLog.java /var/log/shopflow 30
// Niente classe, niente import: un file sorgente compatto importa tutto java.base

void main(String[] args) throws IOException {
    var cartella = Path.of(args[0]);
    var limite = Instant.now().minus(Duration.ofDays(Integer.parseInt(args[1])));

    List<Path> vecchi;
    try (var file = Files.list(cartella)) {
        vecchi = file.filter(p -> p.toString().endsWith(".log"))
                     .filter(p -> modificato(p).isBefore(limite))
                     .sorted(Comparator.comparing(this::modificato))
                     .toList();
    }
    if (vecchi.isEmpty()) {
        IO.println("Niente da cancellare");
        return;
    }
    IO.println(vecchi.size() + " file, dal " + vecchi.getFirst().getFileName()
               + " al " + vecchi.getLast().getFileName());
    if ("s".equalsIgnoreCase(IO.readln("Confermi la cancellazione? (s/n) "))) {   // readln restituisce null se l'input è finito
        for (var p : vecchi) Files.delete(p);
    }
}

Instant modificato(Path p) {
    try {
        return Files.getLastModifiedTime(p).toInstant();
    } catch (IOException _) {
        return Instant.MAX;              // non leggibile: non lo considero vecchio
    }
}
```

- Sostituisce uno script di shell senza installare nulla oltre al JDK
- Se lo script cresce, si trasforma in una classe normale aggiungendo la dichiarazione della classe e gli import

**Approfondimento**

- **Cosa succede davvero:** il file è un `main` d'istanza: per questo `this::modificato` funziona, e `modificato` è un metodo della classe implicita
- **Trappole:**
  - `getFirst()` è il file più vecchio e `getLast()` il più recente, perché la lista è ordinata per data di modifica crescente
  - senza argomenti lo script termina con `ArrayIndexOutOfBoundsException` (`args[0]`, `args[1]`): in uno script vero conviene controllare `args.length`
  - se `Files.delete` fallisce a metà, i file già cancellati restano cancellati
  - `readln` restituisce `null` se l'input è chiuso (vedi 2.6): per questo il confronto parte da `"s"`
- **Come si prova:** `echo s | java PulisciLog.java /tmp/log 30` risponde «sì» senza tastiera

### 4.7 Uno sguardo a Java 27: il cruscotto del cliente (preview)

Cosa mostra: Structured Concurrency e Lazy Constants insieme. Si compila solo con `--enable-preview` e l'API può cambiare.

```java
record Cruscotto(Cliente cliente, List<Ordine> ultimiOrdini, int puntiFedelta, BigDecimal ivaMedia) {}

class CruscottoService {
    // La tabella IVA è grande: si carica alla prima richiesta, non all'avvio
    private static final LazyConstant<TabellaIva> IVA = LazyConstant.of(TabellaIva::caricaDaFile);

    Cruscotto carica(String idCliente) throws InterruptedException, ExecutionException {
        try (var scope = StructuredTaskScope.open()) {
            var cliente = scope.fork(() -> anagrafica.trova(idCliente));
            var ordini  = scope.fork(() -> storico.ultimi(idCliente, 10));
            var punti   = scope.fork(() -> fedelta.saldo(idCliente));
            scope.join();      // un servizio in errore cancella gli altri due e la richiesta fallisce subito
            return new Cruscotto(cliente.get(), ordini.get(), punti.get(),
                                 IVA.get().media(ordini.get()));
        }
    }
}
```

- Tre chiamate in parallelo su virtual thread, ma con una regola chiara: nessun sottotask sopravvive al blocco
- Gli scoped value (4.4) passano ai sottotask: il registro di audit funziona anche qui
- Per un servizio in produzione oggi si usa la versione di 4.3; questa serve a capire dove sta andando Java

**Approfondimento**

- **Cosa succede davvero:** `scope.join()` aspetta i tre sottotask; se uno fallisce, gli altri due vengono interrotti e `join()` lancia `ExecutionException`: `ordini.get()` non viene mai raggiunto
- **Trappole:**
  - se `caricaDaFile()` fallisce, la costante `IVA` resta in errore fino al riavvio (vedi 3.5): per un file che può essere temporaneamente indisponibile non è adatta
  - compilazione ed esecuzione richiedono `--enable-preview --release 27`
- **Cosa si vede:** senza il flag di preview, `javac` segnala che `StructuredTaskScope` e `LazyConstant` sono API di preview

## 5. Esercizi

Due esercizi ispirati ai casi reali della sezione 4. Servono **Java 25** (`javac --release 25`): le variabili senza nome `_` e lo scoped value sono finali da 22 e 25, quindi non serve nessun flag di preview. Le soluzioni sono in fondo a ciascun esercizio, dentro un blocco ripiegabile.

### Esercizio 1: registro dei movimenti di magazzino

**Ispirato a:** 4.1 (eventi e `switch` esaustivo) e 2.7 (sequenced collections)

**Cosa mette in pratica:** record pattern, `_`, pattern matching per `switch` e per `instanceof`, sequenced collections

**Scenario.** Un piccolo magazzino registra quattro tipi di movimento: carico di merce, vendita, reso del cliente e rettifica dell'inventario. Devi calcolare la giacenza di ogni articolo e leggere lo storico.

**Consegna**

1. Crea una `sealed interface Movimento` con il metodo `String sku();` e quattro record annidati che la implementano:
   - `Carico(String sku, int quantita)`
   - `Vendita(String sku, int quantita, String ordineId)`
   - `Reso(String sku, int quantita, String motivo)`
   - `Rettifica(String sku, int differenza)`
2. Scrivi `static int effetto(Movimento m)`: restituisce di quanto il movimento cambia la giacenza. Usa uno `switch` espressione con record pattern:
   - carico: `+quantita`; vendita: `-quantita`; rettifica: `differenza`
   - reso: `+quantita`, ma se il motivo è `"DIFETTOSO"` il pezzo non torna a scaffale, quindi `0`. Serve una guardia `when`
   - non metterci `default`: se aggiungi un quinto tipo di movimento, il compilatore deve segnalare lo `switch`
   - usa `_` per ogni componente che non ti serve
3. Scrivi `static int giacenza(List<Movimento> storico, String sku)` con uno stream, sommando gli effetti dei movimenti di quello SKU.
4. Scrivi `static int venduto(List<Movimento> storico, String sku)` con un ciclo `for` e un `instanceof` con record pattern: somma le quantità vendute di quello SKU.
5. Nel `main`, usando uno `ArrayList` come storico:
   - stampa il primo e l'ultimo movimento con `getFirst()` e `getLast()`
   - stampa gli ultimi 3 movimenti, dal più recente, con `reversed()`
   - stampa la giacenza di `MAGLIA-M` e `JEANS-42` e le vendite di `MAGLIA-M`

**Dati di prova** (in quest'ordine, con `add`):

```text
Carico("MAGLIA-M", 20)
Carico("JEANS-42", 10)
Vendita("MAGLIA-M", 3, "ORD-1")
Reso("MAGLIA-M", 1, "TAGLIA_SBAGLIATA")
Reso("JEANS-42", 1, "DIFETTOSO")
Vendita("JEANS-42", 4, "ORD-2")
Rettifica("MAGLIA-M", -2)
Carico("MAGLIA-M", 5)
```

**Output atteso**

```text
Primo: Carico[sku=MAGLIA-M, quantita=20]
Ultimo: Carico[sku=MAGLIA-M, quantita=5]
Ultimi 3, dal più recente:
  Carico[sku=MAGLIA-M, quantita=5]
  Rettifica[sku=MAGLIA-M, differenza=-2]
  Vendita[sku=JEANS-42, quantita=4, ordineId=ORD-2]
Giacenza MAGLIA-M: 21
Giacenza JEANS-42: 6
Venduto MAGLIA-M: 3
```

**Prova.** Scambia l'ordine dei due casi `Reso` nello `switch`. Il compilatore protesta? Perché? (Indizio: sezione 2.1, «etichetta dominata».)

**Soluzione:** vedi [04_soluzioni-esercizi_java-25.md](04_soluzioni-esercizi_java-25.md), Esercizio 1

### Esercizio 2: chiusura dei negozi in parallelo

**Ispirato a:** 4.3 (virtual thread) e 4.4 (scoped value)

**Cosa mette in pratica:** virtual thread con `Executors.newVirtualThreadPerTaskExecutor()`, try-with-resources sull'executor, scoped value

**Scenario.** A fine giornata tre negozi vanno chiusi. Ogni chiusura richiede circa 200 ms di lavoro (incassi, inventario) e non dipende dalle altre, quindi le fai in parallelo, una per virtual thread. Ogni riga di log deve dire quale negozio la scrive, senza passare il codice del negozio come parametro.

**Punto di partenza**

```java
record Negozio(String codice, String citta) {}

static final ScopedValue<Negozio> NEGOZIO = ScopedValue.newInstance();

static final Map<String, Integer> INCASSI = Map.of("MI-01", 1250, "RM-02", 980, "TO-03", 1600);
```

**Consegna**

1. Scrivi `static void log(String messaggio)`: stampa `[codice] messaggio` leggendo lo scoped value. Se non è associato, stampa `[-]`.
2. Scrivi `static int chiudi(Negozio negozio)`: registra «inizio chiusura», aspetta 200 ms con `Thread.sleep(Duration.ofMillis(200))`, prende l'incasso da `INCASSI`, registra «chiusura completata, incasso X» e restituisce l'incasso.
3. Scrivi `static int chiudiTutti(List<Negozio> negozi)`:
   - apri un `ExecutorService` con `Executors.newVirtualThreadPerTaskExecutor()` dentro un try-with-resources
   - per ogni negozio invia un task e salva il `Future<Integer>`
   - chiudi il try: `close()` aspetta la fine di tutti i task
   - somma gli incassi con `future.get()` e restituisci il totale
4. **Prima prova senza associare lo scoped value.** Nel task chiama direttamente `chiudi(negozio)`. Osserva che tutte le righe mostrano `[-]`: lo scoped value non passa ai thread di un `ExecutorService` (sezione 2.10).
5. **Correggi.** Dentro il task associa il valore: `ScopedValue.where(NEGOZIO, negozio).call(() -> chiudi(negozio))`. Ora ogni riga mostra il codice del suo negozio.
6. Nel `main` misura il tempo con `System.nanoTime()`. Deve essere circa 200 ms e non 600 ms, perché i tre negozi lavorano insieme.

**Output atteso** (l'ordine delle righe di log può cambiare da un'esecuzione all'altra; il resto è fisso)

```text
[MI-01] inizio chiusura
[RM-02] inizio chiusura
[TO-03] inizio chiusura
[MI-01] chiusura completata, incasso 1250
[RM-02] chiusura completata, incasso 980
[TO-03] chiusura completata, incasso 1600
Incasso totale: 3830
Tempo: 215 ms (in serie sarebbero circa 600 ms)
```

**Prova.** Chiama `NEGOZIO.get()` fuori da `call(...)`. Quale eccezione ottieni, e perché `isBound()` evita il problema? (Sezione 2.10.)

**Soluzione:** vedi [04_soluzioni-esercizi_java-25.md](04_soluzioni-esercizi_java-25.md), Esercizio 2
