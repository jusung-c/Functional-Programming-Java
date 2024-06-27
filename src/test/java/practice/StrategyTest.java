package practice;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class StrategyTest {

    public static Stream<Arguments> dataFactory() {
        return Stream.of(
                arguments(
                        List.of(
                                new PurchaseItem("cookie",  BigDecimal.valueOf(1500)),
                                new PurchaseItem("serial", BigDecimal.valueOf(2500)),
                                new PurchaseItem("hambuger",  BigDecimal.valueOf(1530)),
                                new PurchaseItem("bread",  BigDecimal.valueOf(4500)),
                                new PurchaseItem("milk",  BigDecimal.valueOf(1200))
                        ),
                        BigDecimal.valueOf(11230)
                ),
                arguments(
                        List.of(
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
                        ),
                        BigDecimal.valueOf(27430)
                )
        );
    }
    public static Stream<Arguments> dataByItemFactory() {
        return Stream.of(
                arguments(
                        List.of(
                                new PurchaseItem("cookie",  BigDecimal.valueOf(1500)),
                                new PurchaseItem("serial", BigDecimal.valueOf(2500)),
                                new PurchaseItem("hamburger",  BigDecimal.valueOf(1530)),
                                new PurchaseItem("bread",  BigDecimal.valueOf(4500)),
                                new PurchaseItem("milk",  BigDecimal.valueOf(1200))
                        ),
                        BigDecimal.valueOf(1500), "cookie"
                ),
                arguments(
                        List.of(
                                new PurchaseItem("cookie",  BigDecimal.valueOf(1500)),
                                new PurchaseItem("serial", BigDecimal.valueOf(2500)),
                                new PurchaseItem("hamburger",  BigDecimal.valueOf(1530)),
                                new PurchaseItem("milk",  BigDecimal.valueOf(1200)),
                                new PurchaseItem("bread",  BigDecimal.valueOf(4500)),
                                new PurchaseItem("bread",  BigDecimal.valueOf(4500)),
                                new PurchaseItem("cookie",  BigDecimal.valueOf(1500)),
                                new PurchaseItem("bread",  BigDecimal.valueOf(4500)),
                                new PurchaseItem("bread",  BigDecimal.valueOf(4500)),
                                new PurchaseItem("milk",  BigDecimal.valueOf(1200))
                        ),
                        BigDecimal.valueOf(18000), "bread"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("dataFactory")
    public void 구매내역_총_지불금액_구하기(List<PurchaseItem> purchaseItemList, BigDecimal expectedTotal) {
        BigDecimal total = purchaseItemList.stream()
                .map(PurchaseItem::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertEquals(expectedTotal, total);
    }

    @ParameterizedTest
    @MethodSource("dataByItemFactory")
    public void 아이템별_구매내역_총_지불금액_구하기(List<PurchaseItem> purchaseItemList, BigDecimal expectedTotal, String item) {
        BigDecimal total = purchaseItemList.stream()
                .filter(p -> p.getItem().equals(item))
                .map(PurchaseItem::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertEquals(expectedTotal, total);
    }

    @ParameterizedTest
    @MethodSource("dataByItemFactory")
    public void 아이템별_구매내역_총_지불금액_구하기_메서드_분리_버전(List<PurchaseItem> purchaseItemList, BigDecimal expectedTotal, String item) {
        BigDecimal total = getTotal(purchaseItemList, (p) -> item.equals(p.getItem()));

        assertEquals(expectedTotal, total);
    }

    private <T extends PurchaseItem> BigDecimal getTotal(List<T> list, Predicate<T> itemSelector) {
        return list.stream()
                .filter(itemSelector)
                .map(PurchaseItem::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @ParameterizedTest
    @MethodSource("dataByItemFactory")
    public void 아이템별_구매내역_총_지불금액_구하기_유틸_버전(List<PurchaseItem> purchaseItemList, BigDecimal expectedTotal, String item) {
        BigDecimal total = getTotal(purchaseItemList, PurchaseItemUtil.itemSelector(item));
        assertEquals(expectedTotal, total);
    }


}
