package com.dawidsobczak.ds.lang;

import java.util.ArrayList;
import java.util.Deque;

public class Parser {
    ParseTree outputTree;
    ArrayList<Lexeme> stack = new ArrayList<>();

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
