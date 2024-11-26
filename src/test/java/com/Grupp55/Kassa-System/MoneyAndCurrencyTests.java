import org.junit.jupiter.api.Test;

import java.util.Currency;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MoneyAndCurrencyTests {

    @Test
    void testCurrencyConversion() {
        // Arrange
        Money sekMoney = new Money(100.0, Currency.getInstance("SEK"));

        // Act
        Money convertedToUsd = sekMoney.convertToCurrency(Currency.getInstance("USD"));

        // Assert
        assertNotNull(convertedToUsd, "Converted money should not be null");
        assertEquals(11.0, convertedToUsd.getAmount(), 0.01, "100 SEK should convert to 11 USD");
        assertEquals(Currency.getInstance("USD"), convertedToUsd.getCurrency(), "Currency should be USD");
    }

    @Test
    void testMoneyAddition() {
        // Arrange
        Money usd1 = new Money(50.0, Currency.getInstance("USD"));
        Money usd2 = new Money(30.0, Currency.getInstance("USD"));

        // Act
        Money result = usd1.add(usd2);

        // Assert
        assertNotNull(result, "Resulting money should not be null");
        assertEquals(80.0, result.getAmount(), 0.01, "50 USD + 30 USD should equal 80 USD");
        assertEquals(Currency.getInstance("USD"), result.getCurrency(), "Currency should remain USD");
    }

    @Test
    void testMoneySubtractionWithConversion() {
        // Arrange
        Money sekMoney = new Money(200.0, Currency.getInstance("SEK"));
        Money usdMoney = new Money(10.0, Currency.getInstance("USD"));

        // Act
        Money result = sekMoney.subtract(usdMoney);

        // Assert
        assertNotNull(result, "Resulting money should not be null");
        assertEquals(109.0, result.getAmount(), 0.01, "200 SEK - (10 USD converted to SEK) should equal 109 SEK");
        assertEquals(Currency.getInstance("SEK"), result.getCurrency(), "Currency should remain SEK");
    }

    @Test
    void testCalculateChange() {
        // Arrange
        Money total = new Money(75.0, Currency.getInstance("EUR"));
        double payment = 100.0;

        // Act
        Money change = total.calculateChange(payment);

        // Assert
        assertNotNull(change, "Change should not be null");
        assertEquals(25.0, change.getAmount(), 0.01, "Change should be 25 EUR");
        assertEquals(Currency.getInstance("EUR"), change.getCurrency(), "Currency should remain EUR");
    }

    @Test
    void testCalculateFewestCoins() {
        // Arrange
        Money sekMoney = new Money(87.0, Currency.getInstance("SEK"));

        // Act
        Map<Integer, Integer> coins = sekMoney.calculateFewestCoins();

        // Assert
        assertNotNull(coins, "Coins map should not be null");
        assertEquals(4, coins.size(), "There should be 4 different coin denominations");
        assertEquals(4, coins.get(20), "There should be 4 coins of 20 SEK");
        assertEquals(1, coins.get(5), "There should be 1 coin of 5 SEK");
        assertEquals(2, coins.get(1), "There should be 2 coins of 1 SEK");
    }
}