package repository.product;

import model.Product;

import java.util.Collections;
import java.util.List;

public interface Repository {
    default Product findProductByBarcode(String barcode) {
        return new Product("default barcode", "default name", 0.00);
    }

    default List<Product> findAll() {
        return Collections.emptyList();
    }
}
