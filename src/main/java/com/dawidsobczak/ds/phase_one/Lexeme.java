package com.dawidsobczak.ds.phase_one;

public final class Lexeme {
    public final GrammarSymbols type;
    public final String content;

    public Lexeme(GrammarSymbols type, String content) {
        this.type = type;
        this.content = content;
    }
}
