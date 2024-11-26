import java.util.ArrayList;
import java.util.List;

public class ProductGroup {
    private String groupName;
    private List<Product> products;

    public ProductGroup(String groupName) {
        this.groupName = groupName;
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    public double calculateGroupDiscount(double discountPercentage) {
        double totalDiscountedPrice = 0;

        for (Product product : products) {
            double originalPrice = product.getPrice().getAmount();
            double discountedPrice = originalPrice * (1 - discountPercentage / 100);
            totalDiscountedPrice += discountedPrice;
        }

        return totalDiscountedPrice;
    }
}
