package model;

public class Product {
    private String barcode;
    private String name;
    private Double price;

    public Product(String barcode, String name, Double price) {
        this.barcode = barcode;
        this.name = name;
        this.price = price;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }
}
