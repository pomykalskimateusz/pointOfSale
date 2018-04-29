package service;

import io.inputImpl.BarcodeInput;
import io.outputImpl.LcdOutput;
import io.outputImpl.PrinterOutput;
import model.Product;
import model.ScannedItem;
import repository.history.HistoryRepository;
import repository.product.ProductRepository;

import java.util.Optional;

public class ProductService {
    private ProductRepository productRepository;
    private LcdOutput lcdOutput;
    private PrinterOutput printerOutput;
    private BarcodeInput barcodeInput;
    private HistoryRepository historyRepository;
    private int receiptId;

    public ProductService(ProductRepository productRepository, HistoryRepository historyRepository, LcdOutput lcdOutput, PrinterOutput printerOutput, BarcodeInput barcodeInput)
    {
        this.productRepository = productRepository;
        this.historyRepository = historyRepository;
        this.lcdOutput = lcdOutput;
        this.printerOutput = printerOutput;
        this.barcodeInput = barcodeInput;
        receiptId = 1;
    }

    public String sale()
    {
        return saleProduct(barcodeInput.read());
    }

    private String saleProduct(String barcode)
    {
       if(barcode.isEmpty())
       {
           return "Invalid bar-code";
       }
       else if(barcode.equals("exit"))
       {
           return totalPrice().map(this::totalPriceOutput)
                              .orElseThrow(SaleException::new);
       }
       else
       {
           return Optional.ofNullable(productRepository.findProductByBarcode(barcode))
                          .map(this::scannedItemOutput)
                          .orElseGet(() -> lcdOutput.write("Product not found"));
       }
    }

    private String scannedItemOutput(Product product)
    {
        historyRepository.add(new ScannedItem(product, receiptId));

        return product.toString();
    }

    private String totalPriceOutput(Double totalPrice)
    {
        lcdOutput.write(totalPrice.toString());
        printerOutput.write(totalPrice.toString() + "\n All products : " + historyRepository.findByReceiptId(receiptId).toString());

        receiptId++;

        return totalPrice.toString();
    }

    private Optional<Double> totalPrice()
    {
        return historyRepository.findByReceiptId(receiptId)
                                .stream()
                                .map(ScannedItem::getProduct)
                                .map(Product::getPrice)
                                .reduce((firstPrice, secondPrice) -> firstPrice + secondPrice);

    }
}
