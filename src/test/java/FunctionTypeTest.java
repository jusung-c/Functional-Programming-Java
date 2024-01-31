import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class FunctionTypeTest {

    @Test
    public void 입력값만() {
        // Consumer
        Consumer<Integer> consumer = (x) -> System.out.println(x);

        consumer.accept(3);
     }

    @Test
    public void 출력만() {
        // Supplier
        Supplier<String> supplier = () -> "made in korea";

        String result = supplier.get();
        assertEquals(result, "made in korea");
    }

    @Test
    public void 입출력_모두() {
        // Function
        Function<String, List<String>> function = (string) -> {
            return List.of(string);
        };

        assertEquals("korea", function.apply("korea").get(0));
    }

    @Test
    public void 참거짓을_판단하는_경우() {
        // Predicate
        Predicate<String> predicate = (string) -> "true".equals(string);

        assertFalse(predicate.test("false"));
    }

    @Test
    public void 입출력이_없는_인터페이스() {
        // Runnable
        Runnable runnable = () -> System.out.println("run");

        runnable.run();

    }

}
