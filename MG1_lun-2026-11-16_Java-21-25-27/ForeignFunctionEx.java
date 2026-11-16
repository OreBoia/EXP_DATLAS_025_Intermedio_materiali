import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class ForeignFunctionEx {
    public static void main(String[] args) {
        try(Arena arena = Arena.ofConfined()){
            MemorySegment numeri = arena.allocateFrom(ValueLayout.JAVA_INT, 1, 2, 3, 4);
            numeri.setAtIndex(ValueLayout.JAVA_INT, 2, 30);

            int somma = 0;
            for (long i = 0; i < 4; i++){
                somma += numeri.getAtIndex(ValueLayout.JAVA_INT, i);
            }
            System.out.println(somma);
        }

    }
}
