package service;

class SaleException extends RuntimeException {
    SaleException() {
        super("Not found any scanned items");
    }
}
