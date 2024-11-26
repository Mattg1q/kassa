import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

public class Money {
    private double amount;
    private Currency currency;

    // Statisk växelkurskarta som exempel (fasta växelkurser)
    private static final Map<String, Double> exchangeRates = new HashMap<>();

    static {
        exchangeRates.put("SEK-USD", 0.11);
        exchangeRates.put("USD-SEK", 9.1);
        exchangeRates.put("SEK-EUR", 0.095);
        exchangeRates.put("EUR-SEK", 10.5);
        exchangeRates.put("USD-EUR", 0.86);
        exchangeRates.put("EUR-USD", 1.16);
    }

    public Money(double amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    // Metod för att konvertera belopp till en annan valuta
    public Money convertToCurrency(Currency targetCurrency) {
        if (this.currency.equals(targetCurrency)) {
            return new Money(this.amount, this.currency);
        }

        String exchangeKey = this.currency.getCurrencyCode() + "-" + targetCurrency.getCurrencyCode();
        Double rate = exchangeRates.get(exchangeKey);

        if (rate == null) {
            throw new IllegalArgumentException("Växelkurs ej tillgänglig för " + this.currency + " till " + targetCurrency);
        }

        return new Money(this.amount * rate, targetCurrency);
    }

    // Metod för att addera pengar med valutakonvertering
    public Money add(Money other) {
        if (!this.currency.equals(other.getCurrency())) {
            Money convertedOther = other.convertToCurrency(this.currency);
            return new Money(this.amount + convertedOther.getAmount(), this.currency);
        }
        return new Money(this.amount + other.getAmount(), this.currency);
    }

    // Metod för att subtrahera pengar med valutakonvertering
    public Money subtract(Money other) {
        if (!this.currency.equals(other.getCurrency())) {
            Money convertedOther = other.convertToCurrency(this.currency);
            return new Money(this.amount - convertedOther.getAmount(), this.currency);
        }
        return new Money(this.amount - other.getAmount(), this.currency);
    }

    // Metod för att beräkna växel
    public Money calculateChange(double paymentAmount) {
        if (paymentAmount < amount) {
            throw new IllegalArgumentException("Betalningen är mindre än totalbeloppet.");
        }

        double changeAmount = paymentAmount - amount;
        return new Money(changeAmount, this.currency);
    }

    // Metod för att beräkna få den minsta mängden mynt för hela beloppet
    public Map<Integer, Integer> calculateFewestCoins() {
        Map<Integer, Integer> coins = new HashMap<>();
        int[] denominations = {20, 10, 5, 1}; // example denominations
        int remainingAmount = (int) this.amount;

        for (int coin : denominations) {
            int count = remainingAmount / coin;
            if (count > 0) {
                coins.put(coin, count);
                remainingAmount -= count * coin;
            }
        }
        return coins;
    }

    // Metod för att beräkna få den minsta mängden mynt för decimala värden (t.ex. 0.5 SEK)
    public Map<Double, Integer> calculateFewestCoinsForDecimals() {
        Map<Double, Integer> coins = new HashMap<>();
        double[] denominations = {1.0, 0.5, 0.2, 0.1, 0.05, 0.01}; // example denominations
        double remainingAmount = this.amount;

        for (double coin : denominations) {
            int count = (int) (remainingAmount / coin);
            if (count > 0) {
                coins.put(coin, count);
                remainingAmount -= count * coin;
            }
        }
        return coins;
    }
}
