package service;

class SaleException extends RuntimeException {
    SaleException() {
        super("Found 'exit' code. End of running Point Of Sale.");
    }
}
