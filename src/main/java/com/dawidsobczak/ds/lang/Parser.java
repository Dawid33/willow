package com.dawidsobczak.ds.lang;

import java.util.Deque;

public class Parser {
    enum Associativity {
        None,
        Left,
        Rigth,
        Equal
    }

    ParseTree outputTree;
    Deque stack;

    public void consumeToken(Lexeme s) throws ParserException {

    }

    public ParseTree getParseTree() {
        return null;
    }
}
