package com.dawidsobczak.ds.lang;

import java.util.ArrayList;
import java.util.Deque;

public class Parser {
    ParseTree outputTree;
    ArrayList<Lexeme> stack = new ArrayList<>();
    Grammar g;

    public Parser(Grammar g) {
        this.g = g;
    }

    enum Associativity {
        None,
        Left,
        Rigth,
        Equal
    }

    public void consumeToken(Lexeme s) throws ParserException {
    }

    public ParseTree getParseTree() {
        return null;
    }
}
