import java.util.Date;

public class BuyXGetYDiscount extends Discount {
    private int requiredQuantity;
    private int discountQuantity;

    public BuyXGetYDiscount(int requiredQuantity, int discountQuantity, Date validityPeriod) {
        super(validityPeriod);
        this.requiredQuantity = requiredQuantity;
        this.discountQuantity = discountQuantity;
    }

    @Override
    public double applyDiscount(double price) {
        return price * requiredQuantity / (requiredQuantity + discountQuantity);
    }
}
