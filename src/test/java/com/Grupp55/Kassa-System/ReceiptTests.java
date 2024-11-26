import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

class ReceiptTests {

    @Test
    void testAddAndRemoveProducts() {
        // Arrange
        Product product1 = new Product("Apple", new Money(10.0, java.util.Currency.getInstance("SEK")));
        Product product2 = new Product("Banana", new Money(15.0, java.util.Currency.getInstance("SEK")));
        Receipt receipt = new Receipt();

        // Act
        receipt.addProduct(product1);
        receipt.addProduct(product2);
        receipt.removeProduct(product1);

        // Assert
        assertEquals(1, receipt.getProducts().size(), "Receipt should contain only 1 product after removal");
        assertEquals(15.0, receipt.calculateTotal(), 0.01, "Total price should be updated after removal");
    }

    @Test
    void testCalculateTotalWithDiscounts() {
        // Arrange
        Product product = new Product("Apple", new Money(100.0, java.util.Currency.getInstance("SEK")));
        Discount discount = new Discount(null) {
            @Override
            public double applyDiscount(double price) {
                return price * 0.8; // 20% rabatt
            }

            @Override
            public boolean isApplicable(Product product) {
                return product.getName().equals("Apple");
            }
        };
        Receipt receipt = new Receipt();
        receipt.addProduct(product);
        receipt.addDiscount(discount);

        // Act
        double total = receipt.calculateTotal();

        // Assert
        assertEquals(80.0, total, 0.01, "Total price should reflect applied discount");
    }

    @Test
    void testGenerateReceiptContent() {
        // Arrange
        Product product1 = new Product("Apple", new Money(10.0, java.util.Currency.getInstance("SEK")));
        Product product2 = new Product("Banana", new Money(15.0, java.util.Currency.getInstance("SEK")));
        Receipt receipt = new Receipt();
        receipt.addProduct(product1);
        receipt.addProduct(product2);

        // Act
        String content = receipt.generateReceiptContent();

        // Assert
        assertTrue(content.contains("Apple"), "Receipt content should include 'Apple'");
        assertTrue(content.contains("Banana"), "Receipt content should include 'Banana'");
        assertTrue(content.contains("25.00"), "Receipt content should show correct total price");
    }

    @Test
    void testSaveReceiptToFile() {
        // Arrange
        Product product = new Product("Apple", new Money(10.0, java.util.Currency.getInstance("SEK")));
        Receipt receipt = new Receipt();
        receipt.addProduct(product);
        String filename = "test_receipt.txt";

        // Act
        receipt.saveReceiptToFile(filename);

        // Assert
        assertTrue(new java.io.File(filename).exists(), "Receipt file should be created");
        // Cleanup
        new java.io.File(filename).delete();
    }

    @Test
    void testEmptyProductList() {
        // Arrange
        Receipt receipt = new Receipt();

        // Act
        double total = receipt.calculateTotal();

        // Assert
        assertEquals(0.0, total, 0.01, "Total should be 0.0 for an empty product list");
    }

    @Test
    void testProductWithoutPrice() {
        // Arrange
        Product product = new Product("Unknown Item", null);
        Receipt receipt = new Receipt();

        // Act
        try {
            receipt.addProduct(product);
            double total = receipt.calculateTotal();

            // Assert
            assertEquals(0.0, total, 0.01, "Total should not include products without a price");
        } catch (Exception e) {
            fail("The system should handle products without a price gracefully.");
        }
    }

    @Test
    void testSaveReceiptWithInvalidFilename() {
        // Arrange
        Product product = new Product("Apple", new Money(10.0, java.util.Currency.getInstance("SEK")));
        Receipt receipt = new Receipt();
        receipt.addProduct(product);
        String invalidFilename = "/invalid-path/test_receipt.txt";

        // Act
        try {
            receipt.saveReceiptToFile(invalidFilename);
            fail("Expected an IOException or error to be logged");
        } catch (Exception e) {
            assertTrue(e instanceof IOException, "Should throw IOException for invalid file paths");
        }
    }

    @Test
    void testLargeDataHandling() {
        // Arrange
        Receipt receipt = new Receipt();
        for (int i = 0; i < 10_000; i++) {
            receipt.addProduct(new Product("Product" + i, new Money(1.0, java.util.Currency.getInstance("SEK"))));
        }
        Discount discount = new Discount(null) {
            @Override
            public double applyDiscount(double price) {
                return price * 0.95; // 5% rabatt
            }

            @Override
            public boolean isApplicable(Product product) {
                return true;
            }
        };
        for (int i = 0; i < 1_000; i++) {
            receipt.addDiscount(discount);
        }

        // Act
        long startTime = System.currentTimeMillis();
        double total = receipt.calculateTotal();
        long endTime = System.currentTimeMillis();

        // Assert
        assertEquals(9_500.0, total, 0.01, "Total should be correct for large data set with discounts");
        assertTrue((endTime - startTime) < 5000, "Performance issue: calculateTotal should complete in under 5 seconds");
    }

    @Test
    void testReceiptWithOnlyDiscounts() {
        // Arrange
        Receipt receipt = new Receipt();
        Discount discount = new Discount(null) {
            @Override
            public double applyDiscount(double price) {
                return price * 0.5; // 50% rabatt
            }

            @Override
            public boolean isApplicable(Product product) {
                return true;
            }
        };
        receipt.addDiscount(discount);

        // Act
        double total = receipt.calculateTotal();

        // Assert
        assertEquals(0.0, total, 0.01, "Total should remain 0.0 when there are no products, even with discounts");
    }

    @Test
    void testMultipleDiscountsAndProducts() {
        // Arrange
        Receipt receipt = new Receipt();
        Product product1 = new Product("Item1", new Money(100.0, java.util.Currency.getInstance("SEK")));
        Product product2 = new Product("Item2", new Money(50.0, java.util.Currency.getInstance("SEK")));
        Discount discount1 = new Discount(null) {
            @Override
            public double applyDiscount(double price) {
                return price - 10.0;
            }

            @Override
            public boolean isApplicable(Product product) {
                return product.getName().equals("Item1");
            }
        };
        Discount discount2 = new Discount(null) {
            @Override
            public double applyDiscount(double price) {
                return price * 0.9;
            }

            @Override
            public boolean isApplicable(Product product) {
                return product.getName().equals("Item2");
            }
        };
        receipt.addProduct(product1);
        receipt.addProduct(product2);
        receipt.addDiscount(discount1);
        receipt.addDiscount(discount2);

        // Act
        double total = receipt.calculateTotal();

        // Assert
        assertEquals(115.0, total, 0.01, "Total should reflect the correct discounts applied to both products");
    }
}
