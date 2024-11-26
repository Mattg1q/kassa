import java.awt.*;
import java.awt.print.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Receipt implements Printable {
    private List<Product> products;
    private List<Discount> discounts;
    private Date date;

    public Receipt() {
        this.products = new ArrayList<>();
        this.discounts = new ArrayList<>();
        this.date = new Date();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public void addDiscount(Discount discount) {
        discounts.add(discount);
    }

    public double calculateTotal() {
        double total = 0;
        for (Product product : products) {
            double productPrice = product.getPrice().getAmount();
            for (Discount discount : discounts) {
                if (discount.isApplicable(product)) {
                    productPrice = discount.applyDiscount(productPrice);
                }
            }
            total += productPrice;
        }
        return total;
    }

    public String generateReceiptContent() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("-------- KVITTO --------\n");
        receipt.append("Datum: ").append(date).append("\n\n");
        receipt.append(String.format("%-20s %-10s %-10s\n", "Produkt", "Pris", "Rabatt"));
        receipt.append("-------------------------------\n");

        for (Product product : products) {
            double originalPrice = product.getPrice().getAmount();
            double discountedPrice = originalPrice;

            for (Discount discount : discounts) {
                if (discount.isApplicable(product)) {
                    discountedPrice = discount.applyDiscount(originalPrice);
                }
            }

            String discountStr = discountedPrice < originalPrice ? String.format("-%.2f", originalPrice - discountedPrice) : "-";
            receipt.append(String.format("%-20s %-10.2f %-10s\n", product.getName(), discountedPrice, discountStr));
        }

        receipt.append("\n-------------------------------\n");
        receipt.append(String.format("Totalt: %.2f\n", calculateTotal()));
        receipt.append("-------------------------------\n");

        return receipt.toString();
    }

    public void saveReceiptToFile(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(generateReceiptContent());
            System.out.println("Kvitto sparat till filen: " + filename);
        } catch (IOException e) {
            System.err.println("Kunde inte spara kvitto till fil: " + e.getMessage());
        }
    }

    public void printReceiptToPrinter(String filename) {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("System saknar GUI: Kvittot sparas som PDF i stället.");
            saveReceiptToFile("receipt.pdf");
        } else {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable(this);
            if (job.printDialog()) {
                try {
                    job.print();
                } catch (PrinterException e) {
                    System.err.println("Utskrift misslyckades: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        double widthCm = 8.0;
        double heightCm = 18.0;
        double widthInches = widthCm / 2.54;
        double heightInches = heightCm / 2.54;

        Paper paper = new Paper();
        paper.setSize(widthInches * 72, heightInches * 72);
        paper.setImageableArea(0, 0, widthInches * 72, heightInches * 72);
        pf.setPaper(paper);

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        String[] lines = generateReceiptContent().split("\n");
        int y = 20;
        for (String line : lines) {
            g2d.drawString(line, 10, y);
            y += 15;
        }

        return PAGE_EXISTS;
    }
}
