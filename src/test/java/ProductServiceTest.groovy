import io.inputImpl.BarCodeScanner
import io.outputImpl.LCDDisplay
import io.outputImpl.Printer
import model.Product
import org.mockito.AdditionalAnswers
import org.mockito.Mockito
import repository.ProductRepository
import service.ProductService
import spock.lang.Specification


class ProductServiceTest extends Specification {
    def "Empty barcode case" () {
        given:

            BarCodeScanner barCodeScanner = Mockito.mock(BarCodeScanner.class)
            LCDDisplay lcdDisplay = Mockito.mock(LCDDisplay.class)
            Printer printer = Mockito.mock(Printer.class)
            ProductRepository productRepository = Mockito.mock(ProductRepository.class)

            ProductService productService = new ProductService(productRepository, lcdDisplay, printer, barCodeScanner)

            Product product = new Product("", "", 0.00)

            Mockito.when(productRepository.findProductByBarcode(Mockito.anyString())).thenReturn(product)
            Mockito.when(lcdDisplay.write(Mockito.anyString())).thenAnswer(AdditionalAnswers.<String>returnsFirstArg())

        when:

            String response = productService.sale()

        then:

            response == "Invalid bar-code"
    }

    def "Null product case" () {
        given:

            BarCodeScanner barCodeScanner = Mockito.mock(BarCodeScanner.class)
            LCDDisplay lcdDisplay = Mockito.mock(LCDDisplay.class)
            Printer printer = Mockito.mock(Printer.class)
            ProductRepository productRepository = Mockito.mock(ProductRepository.class)

            ProductService productService = new ProductService(productRepository, lcdDisplay, printer, barCodeScanner)

            Product product = null

            Mockito.when(productRepository.findProductByBarcode(Mockito.anyString())).thenReturn(product)
            Mockito.when(lcdDisplay.write(Mockito.anyString())).thenAnswer(AdditionalAnswers.<String>returnsFirstArg())

        when:

            String response = productService.sale()

        then:

            response == "Product not found"
    }

    def "Correct product case" () {
        given:

            BarCodeScanner barCodeScanner = Mockito.mock(BarCodeScanner.class)
            LCDDisplay lcdDisplay = Mockito.mock(LCDDisplay.class)
            Printer printer = Mockito.mock(Printer.class)
            ProductRepository productRepository = Mockito.mock(ProductRepository.class)

            ProductService productService = new ProductService(productRepository, lcdDisplay, printer, barCodeScanner)

            Product product = new Product("barcode", "name", 100)

            Mockito.when(productRepository.findProductByBarcode(Mockito.anyString())).thenReturn(product)
            Mockito.when(lcdDisplay.write(Mockito.anyString())).thenAnswer(AdditionalAnswers.<String>returnsFirstArg())

        when:

            String response = productService.sale()

        then:

            response == product.toString()
    }

    def "Exit code case" () {
        given:

            BarCodeScanner barCodeScanner = Mockito.mock(BarCodeScanner.class)
            LCDDisplay lcdDisplay = Mockito.mock(LCDDisplay.class)
            Printer printer = Mockito.mock(Printer.class)
            ProductRepository productRepository = Mockito.mock(ProductRepository.class)

            ProductService productService = new ProductService(productRepository, lcdDisplay, printer, barCodeScanner)
            productService.getProductsHistory().add(new Product("barcode", "name", 100))

            Product product = new Product( "exit", "empty", 0)

            Mockito.when(productRepository.findProductByBarcode(Mockito.anyString())).thenReturn(product)
            Mockito.when(lcdDisplay.write(Mockito.anyString())).thenAnswer(AdditionalAnswers.<String>returnsFirstArg())

        when:

            String response = productService.sale()

        then:

            response == "100.0"
    }
}
