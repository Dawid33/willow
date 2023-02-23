package com.dawidsobczak.ds;

public class Lexeme {
    public enum LexemeType {
        IDENTIFIER,
        LEFT_CURLY_BRACKET,
        RIGHT_CURLY_BRACKET,
        COMMA,
        PERIOD,
    }

    public LexemeType type;
    public String raw;

    public Lexeme(LexemeType type, String raw) {
        this.type = type;
        this.raw = raw;
    }
}
