import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DiscountTests {

    @Test
    void testSimpleDiscountOnProduct() {
        // Arrange
        Product product = new Product("Apple", new Money(100.0, java.util.Currency.getInstance("SEK")));
        SimpleDiscount discount = new SimpleDiscount(20.0, true); // 20% rabatt

        // Act
        double discountedPrice = discount.applyDiscount(product.getPrice().getAmount());

        // Assert
        assertEquals(80.0, discountedPrice, 0.01, "Simple discount should reduce price by 20%");
    }

    @Test
    void testDiscountValidityPeriod() {
        // Arrange
        Date futureDate = new Date(System.currentTimeMillis() + 86400000); // 1 day from now
        Date pastDate = new Date(System.currentTimeMillis() - 86400000); // 1 day ago
        SimpleDiscount validDiscount = new SimpleDiscount(10.0, true, futureDate); // Giltig rabatt
        SimpleDiscount expiredDiscount = new SimpleDiscount(10.0, true, pastDate); // Ogiltig rabatt

        // Act
        boolean isValidNow = validDiscount.isValid();
        boolean isExpired = expiredDiscount.isValid();

        // Assert
        assertTrue(isValidNow, "Discount should be valid within the specified time period");
        assertFalse(isExpired, "Discount should not be valid if the date has passed");
    }

    @Test
    void testBestDiscountApplied() {
        // Arrange
        Product product = new Product("Apple", new Money(100.0, java.util.Currency.getInstance("SEK")));
        SimpleDiscount discount1 = new SimpleDiscount(10.0, true); // 10% rabatt
        SimpleDiscount discount2 = new SimpleDiscount(20.0, true); // 20% rabatt
        DiscountManager manager = new DiscountManager();
        manager.addDiscount(discount1);
        manager.addDiscount(discount2);

        // Act
        double bestPrice = manager.applyBestDiscount(product);

        // Assert
        assertEquals(80.0, bestPrice, 0.01, "Best discount should reduce price by 20%");
    }

    @Test
    void testMultipleDiscountsOnSameProduct() {
        // Arrange
        Product product = new Product("Apple", new Money(100.0, java.util.Currency.getInstance("SEK")));
        Discount discount1 = new Discount(null) {
            @Override
            public double applyDiscount(double price) {
                return price * 0.9; // 10% rabatt
            }

            @Override
            public boolean isApplicable(Product product) {
                return product.getName().equals("Apple");
            }
        };
        Discount discount2 = new Discount(null) {
            @Override
            public double applyDiscount(double price) {
                return price - 5.0; // Dra av 5 SEK
            }

            @Override
            public boolean isApplicable(Product product) {
                return product.getName().equals("Apple");
            }
        };
        DiscountManager manager = new DiscountManager();
        manager.addDiscount(discount1);
        manager.addDiscount(discount2);

        // Act
        double bestPrice = manager.applyBestDiscount(product);

        // Assert
        assertEquals(85.0, bestPrice, 0.01, "Total should reflect the best discount applied correctly");
    }

    @Test
    void testDiscountWithZeroPercentage() {
        // Arrange
        Product product = new Product("Orange", new Money(50.0, java.util.Currency.getInstance("SEK")));
        SimpleDiscount discount = new SimpleDiscount(0.0, true); // 0% rabatt

        // Act
        double discountedPrice = discount.applyDiscount(product.getPrice().getAmount());

        // Assert
        assertEquals(50.0, discountedPrice, 0.01, "Discount with 0% should not change the price");
    }

    @Test
    void testDiscountWithNegativePercentage() {
        // Arrange
        Product product = new Product("Banana", new Money(30.0, java.util.Currency.getInstance("SEK")));
        SimpleDiscount discount = new SimpleDiscount(-10.0, true); // -10% rabatt

        // Act
        double discountedPrice = discount.applyDiscount(product.getPrice().getAmount());

        // Assert
        assertEquals(33.0, discountedPrice, 0.01, "Negative discount percentage should increase the price");
    }

    @Test
    void testExpiredDiscountDoesNotApply() {
        // Arrange
        Product product = new Product("Grapes", new Money(60.0, java.util.Currency.getInstance("SEK")));
        Date pastDate = new Date(System.currentTimeMillis() - 86400000); // 1 day ago
        SimpleDiscount discount = new SimpleDiscount(10.0, true, pastDate); // Expired rabatt

        // Act
        boolean isValid = discount.isValid();
        double discountedPrice = discount.applyDiscount(product.getPrice().getAmount());

        // Assert
        assertFalse(isValid, "Expired discount should not be valid");
        assertEquals(60.0, discountedPrice, 0.01, "Expired discount should not change the price");
    }

    @Test
    void testDiscountManagerWithoutDiscounts() {
        // Arrange
        Product product = new Product("Peach", new Money(80.0, java.util.Currency.getInstance("SEK")));
        DiscountManager manager = new DiscountManager();

        // Act
        double bestPrice = manager.applyBestDiscount(product);

        // Assert
        assertEquals(80.0, bestPrice, 0.01, "Without discounts, price should remain unchanged");
    }

    @Test
    void testMultipleProductsWithDifferentDiscounts() {
        // Arrange
        Product product1 = new Product("Watermelon", new Money(120.0, java.util.Currency.getInstance("SEK")));
        Product product2 = new Product("Cherry", new Money(200.0, java.util.Currency.getInstance("SEK")));
        SimpleDiscount discount1 = new SimpleDiscount(10.0, true); // 10% rabatt
        SimpleDiscount discount2 = new SimpleDiscount(25.0, true); // 25% rabatt
        DiscountManager manager = new DiscountManager();
        manager.addDiscount(discount1);
        manager.addDiscount(discount2);

        // Act
        double bestPrice1 = manager.applyBestDiscount(product1);
        double bestPrice2 = manager.applyBestDiscount(product2);

        // Assert
        assertEquals(108.0, bestPrice1, 0.01, "Watermelon should reflect the best discount");
        assertEquals(150.0, bestPrice2, 0.01, "Cherry should reflect the best discount");
    }
}
