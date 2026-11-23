public class ScopedValueEx{
    static final ScopedValue<String> RUOLO = ScopedValue.newInstance();

    static String ruolo() {
        return RUOLO.orElse("Ospite");
    }

    public static void main(String[] args) {
        System.out.println(ruolo());

        ScopedValue.where(RUOLO, "Admin").run(()->{
            System.out.println(ruolo());
            ScopedValue.where(RUOLO, "lettore").run(() ->
                System.out.println(ruolo()));
            System.out.println(ruolo());
        });
    }
}