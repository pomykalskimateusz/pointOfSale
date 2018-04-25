package io;

public interface Output {
    default String write(String text) {
        return "Default output text. If you want to print something real, implement this method please.";
    }
}
