package model;

public class ScannedItem {
    private Product product;
    private int receiptId;

    public ScannedItem(Product product, int receiptId)
    {
        this.product = product;
        this.receiptId = receiptId;
    }

    public Product getProduct() {
        return product;
    }

    public int getReceiptId() {
        return receiptId;
    }
}
