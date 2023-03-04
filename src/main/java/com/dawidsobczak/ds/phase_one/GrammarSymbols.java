package com.dawidsobczak.ds.phase_one;

public enum GrammarSymbols {
    // Other
    PARSE_TREE_ROOT,

    // Nonterminals
    START,
    PROGRAM,
    EXPR,
    TERM,
    FACTOR,

    // Terminals
    PLUS,
    MULTIPLY,
    NUMBER,
    LPAREN,
    RPAREN,
    DELIM,
    A,
    B,
}
