import java.util.Date;

public abstract class Discount {
    protected Date validityPeriod;

    public Discount(Date validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public abstract double applyDiscount(double price);

    public boolean isValid() {
        Date currentDate = new Date();
        return validityPeriod == null || currentDate.before(validityPeriod);
    }

    public boolean isApplicable(Product product) {
        return isValid();
    }
}
