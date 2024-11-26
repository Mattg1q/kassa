import java.util.Date;

public class SimpleDiscount extends Discount {
    private double discountValue; // Kan vara procentsats eller belopp
    private boolean isPercentage;

    // Konstruktor för procentuell rabatt utan giltighetstid
    public SimpleDiscount(double discountValue, boolean isPercentage) {
        super(null); // Ingen giltighetstid
        this.discountValue = discountValue;
        this.isPercentage = isPercentage;
    }

    // Konstruktor för rabatt med giltighetstid
    public SimpleDiscount(double discountValue, boolean isPercentage, Date validityPeriod) {
        super(validityPeriod);
        this.discountValue = discountValue;
        this.isPercentage = isPercentage;
    }

    @Override
    public double applyDiscount(double price) {
        if (isPercentage) {
            return price * (1 - discountValue / 100);
        } else {
            return Math.max(price - discountValue, 0); // Ingen negativt pris
        }
    }
}