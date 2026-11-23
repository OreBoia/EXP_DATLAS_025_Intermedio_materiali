import java.util.ArrayList;
import java.util.List;

public class Magazzino {

    sealed interface Movimento {
        String sku();

        record Carico(String sku, int quantita) implements Movimento {}
        record Vendita(String sku, int quantita, String ordineId) implements Movimento {}
        record Reso(String sku, int quantita, String motivo) implements Movimento {}
        record Rettifica(String sku, int differenza) implements Movimento {}
    }

    static int effetto(Movimento m) {
        return switch (m) {
            case Movimento.Carico(_, var q)                                       -> q;
            case Movimento.Vendita(_, var q, _)                                   -> -q;
            case Movimento.Reso(_, _, var motivo) when "DIFETTOSO".equals(motivo) -> 0;
            case Movimento.Reso(_, var q, _)                                      -> q;
            case Movimento.Rettifica(_, var differenza)                           -> differenza;
        };
    }

    static int giacenza(List<Movimento> storico, String sku) {
        return storico.stream()
                .filter(m -> m.sku().equals(sku))
                .mapToInt(Magazzino::effetto)
                .sum();
    }

    static int venduto(List<Movimento> storico, String sku) {
        int totale = 0;
        for (var m : storico) {
            if (m instanceof Movimento.Vendita(var s, var q, _) && s.equals(sku)) {
                totale += q;
            }
        }
        return totale;
    }

    public static void main(String[] args) {
        List<Movimento> storico = new ArrayList<>();
        storico.add(new Movimento.Carico("MAGLIA-M", 20));
        storico.add(new Movimento.Carico("JEANS-42", 10));
        storico.add(new Movimento.Vendita("MAGLIA-M", 3, "ORD-1"));
        storico.add(new Movimento.Reso("MAGLIA-M", 1, "TAGLIA_SBAGLIATA"));
        storico.add(new Movimento.Reso("JEANS-42", 1, "DIFETTOSO"));
        storico.add(new Movimento.Vendita("JEANS-42", 4, "ORD-2"));
        storico.add(new Movimento.Rettifica("MAGLIA-M", -2));
        storico.add(new Movimento.Carico("MAGLIA-M", 5));

        System.out.println("Primo: " + storico.getFirst());
        System.out.println("Ultimo: " + storico.getLast());

        System.out.println("Ultimi 3, dal più recente:");
        storico.reversed().stream().limit(3).forEach(m -> System.out.println("  " + m));

        for (var sku : List.of("MAGLIA-M", "JEANS-42")) {
            System.out.println("Giacenza " + sku + ": " + giacenza(storico, sku));
        }
        System.out.println("Venduto MAGLIA-M: " + venduto(storico, "MAGLIA-M"));
    }
}