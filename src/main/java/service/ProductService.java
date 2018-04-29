package service;

import io.inputImpl.BarCodeScanner;
import io.outputImpl.LCDDisplay;
import io.outputImpl.Printer;
import model.Product;
import repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductService {
    private ProductRepository productRepository;
    private LCDDisplay lcdDisplay;
    private Printer printer;
    private BarCodeScanner barCodeScanner;
    private ArrayList<Product> productsHistory;

    public ProductService(ProductRepository productRepository, LCDDisplay lcdDisplay, Printer printer, BarCodeScanner barCodeScanner) {
        this.productRepository = productRepository;
        this.lcdDisplay = lcdDisplay;
        this.printer = printer;
        this.barCodeScanner = barCodeScanner;
        productsHistory = new ArrayList<>();
    }

    public List<Product> getProductsHistory() {
        return productsHistory;
    }

    public String sale() {
        return saleProduct(barCodeScanner.read());
    }

    private String saleProduct(String barcode) {
        Optional<Product> productOptional = Optional.ofNullable(productRepository.findProductByBarcode(barcode));

        try {
            return productOptional.map(this::displayProduct)
                                  .map(message -> lcdDisplay.write(message))
                                  .orElseGet(() -> lcdDisplay.write("Product not found"));

        } catch (SaleException saleException) {
            Optional<Double> optionalTotalPrice = sumPrice();

            if(optionalTotalPrice.isPresent()) {
                lcdDisplay.write(optionalTotalPrice.get().toString());
                printer.write(optionalTotalPrice.get().toString() + productsHistory.toString());

                return optionalTotalPrice.get().toString();
            }
            else
            {
                return "0.00";
            }
        }
    }

    private String displayProduct(Product product) {
        if(product.getBarcode().isEmpty()) {
            return "Invalid bar-code";
        }
        else if(product.getBarcode().equals("exit")) {
            throw new SaleException();
        }
        else {
            productsHistory.add(product);
            return product.toString();
        }
    }

    private Optional<Double> sumPrice() {
        return productsHistory.stream()
                              .map(Product::getPrice)
                              .reduce((firstPrice, secondPrice) -> firstPrice + secondPrice);

    }
}
