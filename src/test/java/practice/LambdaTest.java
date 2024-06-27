package practice;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LambdaTest {

    @Test
    public void 자바_익명_클래스() {
        Plus plusOperate = new Plus() {
            @Override
            public int operate(int x, int y) {
                return x + y;
            }
        };

        int result = plusOperate.operate(2, 4);

        assertEquals(6, result);
    }

    @Test
    public void 람다식() {
        Plus plusOperate = (x, y) -> x + y;

        int result = plusOperate.operate(2, 4);

        assertEquals(6, result);
    }

    @Test
    public void 프로세스_정의_여러줄() {
        Plus plusOperate = (x, y) -> {
            int result = x + y;
            return result;
        };

        int result = plusOperate.operate(2, 4);

        assertEquals(6, result);
    }

    @Test
    public void 함수를_파라미터로() {
        int result = operator((x, y) -> x + y, 2, 4);
        assertEquals(6, result);

        result = operator((x, y) -> x * y, 2, 4);
        assertEquals(8, result);

        result = operator((x, y) -> x - y, 2, 4);
        assertEquals(-2, result);

    }

    private int operator(Operator oper, int x, int y) {
        return oper.operate(x, y);
    }
}