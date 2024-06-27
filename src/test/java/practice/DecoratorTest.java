package practice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class DecoratorTest {

    @Test
    void Function_함수형_인터페이스의_compose() {
        Function<String, String > stepOne = one -> "one:" + one;
        Function<String, String > stepTwo = two -> "two:" + two;

        String result = stepOne.compose(stepTwo).apply("mattew");
        assertEquals("one:two:mattew", result);
    }

    @Test
    void compose를_이용해_데코레이팅하기() {
        String product = "americano";

        Function<String, String> decoStar = prod -> "**" + prod + "**";
        Function<String, String> decoDash = prod -> "--" + prod + "--";
        Function<String, String> decoUnderscore = prod -> "__" + prod + "__";

        Function<String, String> decorators = Stream.of(decoStar, decoDash, decoUnderscore)
                .reduce((acc, next) -> acc.compose(next)).orElse(string -> string);

        String result = Optional.of(product).map(decorators).get();

        System.out.println("result = " + result);
        assertEquals("**--__americano__--**", result);
    }

    private static Stream<Arguments> filterDataFactory() {
        Function<Color, Color> brighter = Color::brighter;
        Function<Color, Color> darker = Color::darker;
        return Stream.of(
                arguments(
                        /* expected */ new Color(14, 15, 17),
                        /* filters */ List.of(brighter, brighter, darker)
                ),
                arguments(
                        /* expected */ new Color(10, 11, 12),
                        /* filters */ List.of(brighter,  darker)
                )
        );
    }

    @ParameterizedTest(name="filterd: {0}")
    @MethodSource("filterDataFactory")
    public void 카메라에_여러개의_필터적용하기(Color expectedColor, List<Function<Color, Color>> filters) {
        Camera sonyA7R5 = new Camera();
        sonyA7R5.setFilters(filters);

        Color capturedColor = sonyA7R5.capture(new Color(10,12,14));
        System.out.println("capturedColor = " + capturedColor);

        assertEquals(expectedColor, capturedColor);
    }

    public static Stream<Arguments> dataFactory() {
        List<PurchaseItem> purchaseList = List.of(
                new PurchaseItem("cookie",  BigDecimal.valueOf(1500)),
                new PurchaseItem("serial", BigDecimal.valueOf(2500)),
                new PurchaseItem("hambuger",  BigDecimal.valueOf(1530)),
                new PurchaseItem("milk",  BigDecimal.valueOf(1200)),
                new PurchaseItem("bread",  BigDecimal.valueOf(4500)),
                new PurchaseItem("bread",  BigDecimal.valueOf(4500)),
                new PurchaseItem("cookie",  BigDecimal.valueOf(1500)),
                new PurchaseItem("bread",  BigDecimal.valueOf(4500)),
                new PurchaseItem("bread",  BigDecimal.valueOf(4500)),
                new PurchaseItem("milk",  BigDecimal.valueOf(1200))
        );
        return Stream.of(
                arguments(purchaseList, true, true, BigDecimal.valueOf(23452.65).setScale(2)) // vip
                , arguments(purchaseList, true, false, BigDecimal.valueOf(24687.00).setScale(2)) // 회원
                , arguments(purchaseList, false, false, BigDecimal.valueOf(27430.00).setScale(2)) // 비회원
        );
    }

    @ParameterizedTest
    @MethodSource("dataFactory")
    public void 계산을_해보자(List<PurchaseItem> list) {
        BigDecimal totalPrice = list.stream()
                .map(PurchaseItem::getValue)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

        // 비회원
        assertEquals(BigDecimal.valueOf(27430.00).setScale(2), totalPrice.setScale(2));

        // 회원 : 10% 할인
        BigDecimal memberPrice10 = totalPrice.subtract(totalPrice.multiply(BigDecimal.valueOf(0.1)));
        assertEquals(BigDecimal.valueOf(24687.00).setScale(2), memberPrice10.setScale(2));

        // 15% 할인
        BigDecimal memberPrice15 = totalPrice.subtract(totalPrice.multiply(BigDecimal.valueOf(0.15)));
        assertEquals(BigDecimal.valueOf(23315.50).setScale(2), memberPrice15.setScale(2));

        // vip : 회원할인 + 추가 5% 할인
        BigDecimal vipPrice = memberPrice10.subtract(memberPrice10.multiply(BigDecimal.valueOf(0.05)));
        assertEquals(BigDecimal.valueOf(23452.65).setScale(2), vipPrice.setScale(2));
    }

    @ParameterizedTest
    @MethodSource("dataFactory")
    public void compose를_이용하여_데코레이팅하기(List<PurchaseItem> list, boolean isMember, boolean isVip, BigDecimal expected) {
        Function<PurchaseItem, PurchaseItem> memberDiscount = DiscountFilter.discount(BigDecimal.valueOf(0.1));
        Function<PurchaseItem, PurchaseItem> vipDiscount = DiscountFilter.discount(BigDecimal.valueOf(0.05));

        List<Function<PurchaseItem, PurchaseItem>> funcs = new ArrayList<>();

        if (isMember) funcs.add(memberDiscount);
        if (isVip) funcs.add(vipDiscount);

        Function<PurchaseItem, PurchaseItem> totalDiscount = DiscountFilter.totalDiscount(funcs);

        BigDecimal totalPrice = list.stream()
                .map(totalDiscount)
                .map(PurchaseItem::getValue)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

        assertEquals(expected, totalPrice.setScale(2));
    }
}
