import java.util.Currency;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        // Creating products
        Product product1 = new Product("Kaffe", new Money(50, Currency.getInstance("SEK")));
        Product product2 = new Product("Muffin", new Money(30, Currency.getInstance("SEK")));

        // Creating discounts
        Discount buy2Get1 = new BuyXGetYDiscount(2, 1, new Date(System.currentTimeMillis() + 86400000)); // valid for 1 day

        // Creating and filling the receipt
        Receipt receipt = new Receipt();
        receipt.addProduct(product1);
        receipt.addProduct(product2);
        receipt.addDiscount(buy2Get1);

        // Print and save receipt
        System.out.println(receipt.generateReceiptContent());
        receipt.saveReceiptToFile("receipt.txt");

        // Optionally, print to printer (if a printer is connected)
        // receipt.printReceiptToPrinter();
    }
}
