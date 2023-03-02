package com.dawidsobczak.ds.lang;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class Parser {
    ParseTree outputTree;
    ArrayDeque<Lexeme> stack = new ArrayDeque<>();
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
        if (stack.isEmpty()) {
            stack.push(s);
            return;
        }

        switch (s.type) {
            case PLUS -> {

            }
        }

//        switch (s.type) {
//            case NUMBER -> {
//                stack.addLast(s);
//            }
//            case PLUS -> {
//            }
//        }
    }

    public ParseTree getParseTree() {
        return null;
    }
}
