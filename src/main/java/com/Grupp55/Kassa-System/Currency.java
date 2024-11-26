import java.util.HashMap;
import java.util.Map;

public enum Currency {
    SEK, USD, EUR;

    // En statisk tabell som lagrar växelkurser mellan valutor
    private static final Map<Currency, Map<Currency, Double>> exchangeRates = new HashMap<>();

    // Initiera växelkurserna
    static {
        // SEK-växelkurser
        Map<Currency, Double> sekRates = new HashMap<>();
        sekRates.put(USD, 0.11); // 1 SEK = 0.11 USD
        sekRates.put(EUR, 0.095); // 1 SEK = 0.095 EUR
        exchangeRates.put(SEK, sekRates);

        // USD-växelkurser
        Map<Currency, Double> usdRates = new HashMap<>();
        usdRates.put(SEK, 9.1); // 1 USD = 9.1 SEK
        usdRates.put(EUR, 0.86); // 1 USD = 0.86 EUR
        exchangeRates.put(USD, usdRates);

        // EUR-växelkurser
        Map<Currency, Double> eurRates = new HashMap<>();
        eurRates.put(SEK, 10.5); // 1 EUR = 10.5 SEK
        eurRates.put(USD, 1.16); // 1 EUR = 1.16 USD
        exchangeRates.put(EUR, eurRates);
    }

    // Metod för att konvertera ett belopp från en valuta till en annan
    public double convertTo(double amount, Currency targetCurrency) {
        if (this == targetCurrency) {
            return amount; // Ingen konvertering behövs om samma valuta
        }

        Map<Currency, Double> rates = exchangeRates.get(this);
        if (rates != null && rates.containsKey(targetCurrency)) {
            return amount * rates.get(targetCurrency); // Konvertera beloppet
        } else {
            throw new IllegalArgumentException("Växelkurs ej tillgänglig för " + this + " till " + targetCurrency);
        }
    }
// Metod för att uppdatera en specifik växelkurs
    public static void setExchangeRate(Currency fromCurrency, Currency toCurrency, double rate) {
        exchangeRates.computeIfAbsent(fromCurrency, k -> new HashMap<>()).put(toCurrency, rate);
    }

    // Metod för att hämta en specifik växelkurs
    public static double getExchangeRate(Currency fromCurrency, Currency toCurrency) {
        Map<Currency, Double> rates = exchangeRates.get(fromCurrency);
        if (rates != null && rates.containsKey(toCurrency)) {
            return rates.get(toCurrency);
        } else {
            throw new IllegalArgumentException("Växelkurs ej tillgänglig för " + fromCurrency + " till " + toCurrency);
        }
    }
}