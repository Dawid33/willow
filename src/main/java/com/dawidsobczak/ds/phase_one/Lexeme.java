package com.dawidsobczak.ds.phase_one;

public final class Lexeme<T extends Enum<T>> {
    public T type;
    public final String content;

    public Lexeme(T type, String content) {
        this.type = type;
        this.content = content;
    }
}
