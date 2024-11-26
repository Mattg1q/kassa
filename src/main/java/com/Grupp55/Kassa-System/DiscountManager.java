import java.util.ArrayList;
import java.util.List;

public class DiscountManager {
    private List<Discount> discounts;

    // Konstruktor
    public DiscountManager() {
        this.discounts = new ArrayList<>();
    }

    // Lägg till rabatt
    public void addDiscount(Discount discount) {
        discounts.add(discount);
    }

    // Applicera bästa rabatt
    public double applyBestDiscount(Product product) {
        double originalPrice = product.getPrice().getAmount();
        double bestPrice = originalPrice;

        for (Discount discount : discounts) {
            if (discount.isApplicable(product)) {
                double discountedPrice = discount.applyDiscount(originalPrice);
                if (discountedPrice < bestPrice) {
                    bestPrice = discountedPrice;
                }
            }
        }

        return bestPrice;
    }
}
