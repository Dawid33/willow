package com.dawidsobczak.ds.old;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class Lexeme {
    public LexemeType type;
    public String raw;
    public IdentifierType identifierType;
    public enum LexemeType {
        IDENTIFIER,
        LEFT_CURLY_BRACKET,
        RIGHT_CURLY_BRACKET,
        LEFT_PAREN,
        RIGHT_PAREN,
        SEMICOLON,
        COMMA,
        PERIOD,
    }

    enum IdentifierType {
        Unidentified,
        Section,
        Mov,
        Add,
        Sub
    }

    public Lexeme(LexemeType type, String raw) {
        this.type = type;
        this.raw = raw;
        if (this.type == LexemeType.IDENTIFIER) {
            this.identifierType = tryMatchKeyword(raw);
        }
    }

    public IdentifierType tryMatchKeyword(String input) {
        return IdentifierType.Unidentified;
    }
}
