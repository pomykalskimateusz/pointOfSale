package repository;

import model.Product;

public interface Repository {
    default Product getProductByBarcode(String barcode) {
        return new Product("default barcode", "default name", 0.00);
    }
}
