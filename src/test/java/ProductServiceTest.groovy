import io.inputImpl.BarcodeInput
import io.outputImpl.LcdOutput
import io.outputImpl.PrinterOutput
import model.Product
import model.ScannedItem
import org.mockito.AdditionalAnswers
import org.mockito.Mockito
import repository.history.HistoryRepository
import repository.product.ProductRepository
import service.ProductService
import spock.lang.Specification


class ProductServiceTest extends Specification {
    def "Should successfully return message on empty barcode case" () {
        given:

            BarcodeInput barcodeInput = Mockito.mock(BarcodeInput.class)
            LcdOutput lcdDisplay = Mockito.mock(LcdOutput.class)
            PrinterOutput printer = Mockito.mock(PrinterOutput.class)
            ProductRepository productRepository = Mockito.mock(ProductRepository.class)
            HistoryRepository historyRepository = Mockito.mock(HistoryRepository.class)

            ProductService productService = new ProductService(productRepository, historyRepository, lcdDisplay, printer, barcodeInput)

            Mockito.when(barcodeInput.read()).thenReturn("")

        when:

            String response = productService.sale()

        then:

            response == "Invalid bar-code"
    }

    def "Should successfully return message on null product case" () {
        given:

            BarcodeInput barcodeInput = Mockito.mock(BarcodeInput.class)
            LcdOutput lcdOutput = Mockito.mock(LcdOutput.class)
            PrinterOutput printerOutput = Mockito.mock(PrinterOutput.class)
            ProductRepository productRepository = Mockito.mock(ProductRepository.class)
            HistoryRepository historyRepository = Mockito.mock(HistoryRepository.class)

            ProductService productService = new ProductService(productRepository, historyRepository, lcdOutput, printerOutput, barcodeInput)

            Product product = null

            Mockito.when(barcodeInput.read()).thenReturn("barcodeOfNonExistingProduct")
            Mockito.when(productRepository.findProductByBarcode(Mockito.anyString())).thenReturn(product)
            Mockito.when(lcdOutput.write(Mockito.anyString())).thenAnswer(AdditionalAnswers.<String>returnsFirstArg())

        when:

            String response = productService.sale()

        then:

            response == "Product not found"
    }

    def "Should successfully return message on correct product case" () {
        given:

            BarcodeInput barcodeInput = Mockito.mock(BarcodeInput.class)
            LcdOutput lcdOutput = Mockito.mock(LcdOutput.class)
            PrinterOutput printerOutput = Mockito.mock(PrinterOutput.class)
            ProductRepository productRepository = Mockito.mock(ProductRepository.class)
            HistoryRepository historyRepository = Mockito.mock(HistoryRepository.class)

            ProductService productService = new ProductService(productRepository, historyRepository, lcdOutput, printerOutput, barcodeInput)

            Product product = new Product("barcode", "name", 100)

            Mockito.when(barcodeInput.read()).thenReturn("barcode")
            Mockito.when(productRepository.findProductByBarcode(Mockito.anyString())).thenReturn(product)
            Mockito.when(lcdOutput.write(Mockito.anyString())).thenAnswer(AdditionalAnswers.<String>returnsFirstArg())

        when:

            String response = productService.sale()

        then:

            response == product.toString()
    }

    def "Should successfully return message on exit code case" () {
        given:

            BarcodeInput barcodeInput = Mockito.mock(BarcodeInput.class)
            LcdOutput lcdOutput = Mockito.mock(LcdOutput.class)
            PrinterOutput printerOutput = Mockito.mock(PrinterOutput.class)
            ProductRepository productRepository = Mockito.mock(ProductRepository.class)
            HistoryRepository historyRepository = Mockito.mock(HistoryRepository.class)


            ProductService productService = new ProductService(productRepository, historyRepository, lcdOutput, printerOutput, barcodeInput)

            def scannedItemHistory = [new ScannedItem(new Product("barcode_1", "name_1", 100),1),
                                      new ScannedItem(new Product("barcode_2", "name_2", 200),1),
                                      new ScannedItem(new Product("barcode_3", "name_3", 300),1)]

            Mockito.when(barcodeInput.read()).thenReturn("exit")
            Mockito.when(historyRepository.findByReceiptId(Mockito.anyInt())).thenReturn(scannedItemHistory)

        when:

            String response = productService.sale()

        then:

            response == "600.0"
    }
}
