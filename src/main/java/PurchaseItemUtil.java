import java.util.function.Predicate;

public class PurchaseItemUtil {
    public static Predicate<PurchaseItem> itemSelector(String itemName) {
        return (p) -> itemName.equals(p.getItem());
    }
}
