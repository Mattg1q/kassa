import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class ProductAndMembershipTests {

    @Test
    void testProductDisplay() {
        // Arrange
        Money price = new Money(100.0, java.util.Currency.getInstance("SEK"));
        Product product = new Product("Apple", price);

        // Act
        String display = product.display();

        // Assert
        assertEquals("Apple: 100.0 kr", display, "Product display should show name and price correctly");
    }

    @Test
    void testProductGroupHandling() {
        // Arrange
        Product product1 = new Product("Apple", new Money(100.0, java.util.Currency.getInstance("SEK")));
        Product product2 = new Product("Banana", new Money(50.0, java.util.Currency.getInstance("SEK")));
        ProductGroup group = new ProductGroup("Fruits");

        // Act
        group.addProduct(product1);
        group.addProduct(product2);
        double discountedPrice = group.calculateGroupDiscount(10.0); // 10% rabatt
        group.removeProduct(product1);

        // Assert
        assertEquals(1, group.getProducts().size(), "Group should contain only 1 product after removal");
        assertEquals(135.0, discountedPrice, 0.01, "Discounted total should be calculated correctly");
    }

    @Test
    void testMembershipPoints() {
        // Arrange
        Customer customer = new Customer(
            "John Doe",               // Namn
            "123 Main St",            // Adress
            "123456789",              // Personnummer
            "555-1234",               // Telefonnummer
            "john.doe@example.com"    // E-post
        );
        Membership membership = new Membership(customer);

        // Act
        membership.addPoints(100);
        boolean redeemed = membership.redeemPoints(50);
        boolean failedRedemption = membership.redeemPoints(100);

        // Assert
        assertEquals(50, membership.getPoints(), "Points should update correctly after redemption");
        assertTrue(redeemed, "Redemption should succeed when sufficient points are available");
        assertFalse(failedRedemption, "Redemption should fail when insufficient points are available");
    }

    @Test
    void testBuyXGetYDiscount() {
        // Arrange
        Date futureDate = new Date(System.currentTimeMillis() + 86400000); // 1 day from now
        BuyXGetYDiscount discount = new BuyXGetYDiscount(2, 1, futureDate);

        // Act
        double discountedPrice = discount.applyDiscount(300.0); // Price for 3 items

        // Assert
        assertEquals(200.0, discountedPrice, 0.01, "Discount should reduce price correctly for 'Buy 2, Get 1 Free'");
        assertTrue(discount.isValid(), "Discount should be valid when within validity period");
    }

    @Test
    void testEmptyProductGroup() {
        // Arrange
        ProductGroup group = new ProductGroup("EmptyGroup");

        // Act
        double totalDiscountedPrice = group.calculateGroupDiscount(10.0);

        // Assert
        assertEquals(0.0, totalDiscountedPrice, 0.01, "Discounted price for an empty group should be 0.0");
    }

    @Test
    void testNegativeDiscountPercentage() {
        // Arrange
        Product product = new Product("Pineapple", new Money(200.0, java.util.Currency.getInstance("SEK")));
        ProductGroup group = new ProductGroup("Tropical");
        group.addProduct(product);

        // Act
        double discountedPrice = group.calculateGroupDiscount(-10.0); // Negative discount

        // Assert
        assertEquals(220.0, discountedPrice, 0.01, "Negative discount percentage should increase the price");
    }

    @Test
    void testAddMultipleSameProductToGroup() {
        // Arrange
        Product product = new Product("Orange", new Money(30.0, java.util.Currency.getInstance("SEK")));
        ProductGroup group = new ProductGroup("Citrus");

        // Act
        group.addProduct(product);
        group.addProduct(product);
        double discountedPrice = group.calculateGroupDiscount(10.0);

        // Assert
        assertEquals(2, group.getProducts().size(), "Group should allow adding the same product multiple times");
        assertEquals(54.0, discountedPrice, 0.01, "Discounted price should consider all products, even duplicates");
    }

    @Test
    void testMembershipRedeemExactPoints() {
        // Arrange
        Customer customer = new Customer("Alice", "456 Wonderland St", "987654321", "555-6789", "alice@example.com");
        Membership membership = new Membership(customer);

        // Act
        membership.addPoints(100);
        boolean redeemed = membership.redeemPoints(100);

        // Assert
        assertTrue(redeemed, "Redemption should succeed when points match exactly");
        assertEquals(0, membership.getPoints(), "Points should be 0 after redeeming all points");
    }

    @Test
    void testMembershipRedeemMoreThanAvailablePoints() {
        // Arrange
        Customer customer = new Customer("Bob", "789 Nowhere Rd", "1122334455", "555-1122", "bob@example.com");
        Membership membership = new Membership(customer);

        // Act
        membership.addPoints(50);
        boolean redeemed = membership.redeemPoints(100);

        // Assert
        assertFalse(redeemed, "Redemption should fail when attempting to redeem more points than available");
        assertEquals(50, membership.getPoints(), "Points should remain unchanged if redemption fails");
    }
}
