package com.dawidsobczak.ds.lang;

public final class Lexeme {
    public final LexemeType type;
    public final String content;

    public Lexeme(LexemeType type, String content) {
        this.type = type;
        this.content = content;
    }
}
