package com.dawidsobczak.willow.phase_one.json;

public enum JsonGrammarSymbols {
    // NonTerminal
    DELIM,
    START,
    OBJECT,
    MEMBERS,
    PAIR,
    STRING,
    VALUE,
    ARRAY,
    ELEMENTS,
    CHARS,
    CHAR,

    // Terminal
    RIGHT_CURLY,
    LEFT_CURLY,
    COLON,
    NUMBER,
    BOOL,
    QUOTE,
    RIGHT_QUOTE,
    LEFT_SQUARE_BRACKET,
    RIGHT_SQUARE_BRACKET,
    COMMA,
    CHARACTER
}
