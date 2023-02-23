package com.dawidsobczak.ds;

public class Lexeme {
    public enum LexemeType {
        CHARACTER,
        LEFT_CURLY_BRACKET,
        RIGHT_CURLY_BRACKET,
        COMMA,
        PERIOD,
    }

    LexemeType type;

    public Lexeme(LexemeType type, String raw) {
        this.type = type;
    }
}
