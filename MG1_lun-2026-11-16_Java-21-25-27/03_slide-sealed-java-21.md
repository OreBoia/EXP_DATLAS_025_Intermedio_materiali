# Sealed classes e interfacce in Java 21

## Spiegazione teorica

- Una classe o interfaccia `sealed` dichiara quali tipi possono estenderla o implementarla, con la clausola `permits`.
- Ogni sottotipo permesso deve scegliere un modificatore:
  - `final`: nessuna ulteriore estensione.
  - `sealed`: a sua volta limitata da un proprio `permits`.
  - `non-sealed`: aperta di nuovo a qualsiasi estensione.
- Il compilatore conosce l'insieme completo dei sottotipi, quindi può verificare che uno `switch` li gestisca tutti.
- Introdotte come standard in Java 17 (JEP 409); in Java 21 si combinano con il pattern matching per `switch` (JEP 441).

## Esempi di codice

### Base

Cosa mostra: un'interfaccia sealed con tre record permessi.

```java
sealed interface Notifica permits Email, Sms, Push {}      // solo questi tre tipi

record Email(String indirizzo, String oggetto) implements Notifica {}
record Sms(String numero, String testo) implements Notifica {}
record Push(String dispositivo, String titolo) implements Notifica {}
```

- Nessun altro tipo può implementare `Notifica`.
- I record sono `final` per natura, quindi soddisfano il vincolo sui modificatori.

### Gerarchia

Cosa mostra: una gerarchia con i tre modificatori dei sottotipi.

```java
sealed abstract class Veicolo permits Auto, Moto, Bici {}

final class Auto extends Veicolo {}                    // non estendibile
sealed class Moto extends Veicolo permits Scooter {}   // estendibile solo da Scooter
final class Scooter extends Moto {}

non-sealed class Bici extends Veicolo {}               // estendibile liberamente
class BiciElettrica extends Bici {}
```

- `Auto` chiude il ramo; `Moto` lo limita a `Scooter`.
- `Bici` riapre il ramo: qualsiasi classe può estenderla.

### Avanzato

Cosa mostra: `switch` esaustivo con pattern e guardia `when`, senza `default`.

```java
String descrivi(Notifica n) {
    return switch (n) {
        case Email e when e.oggetto().isBlank() -> "Email senza oggetto a " + e.indirizzo();
        case Email e                            -> "Email: " + e.oggetto();
        case Sms s                              -> "SMS a " + s.numero();
        case Push p                             -> "Push su " + p.dispositivo();
        // senza il caso Push la compilazione fallisce: lo switch non è esaustivo
    };
}
```

- La guardia `when` specializza un caso del pattern.
- Se si aggiunge un tipo a `permits` senza gestirlo, il compilatore segnala l'errore.
