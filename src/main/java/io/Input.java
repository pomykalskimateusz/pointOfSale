package io;

public interface Input {
    default String read() {
        return "Default input text. If you really want to scan something, implement this method please.";
    }
}
