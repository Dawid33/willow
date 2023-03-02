package com.dawidsobczak.ds.lang;

import java.util.ArrayDeque;

public class Parser {
    ParseTree outputTree;
    record LexemeGrammarTuple(Lexeme lexeme, Associativity associativity) {}
    ArrayDeque<LexemeGrammarTuple> stack = new ArrayDeque<>();
    Grammar g;

    public Parser(Grammar g) {
        this.g = g;
    }

    enum Associativity {
        None,
        Left,
        Right,
        Equal,
        Undefined,
    }

    public void consumeToken(Lexeme l) throws ParserException {
        if (stack.isEmpty()) {
            System.out.println("LEXEME\t" + l.type);
            stack.push(new LexemeGrammarTuple(l, Associativity.Undefined));
            return;
        }

        System.out.println("Precedence of " + stack.getFirst().lexeme().type+ " and " + l.type + " : " + g.getPrecedence(stack.getFirst().lexeme.type, l.type));
        System.out.println("LEXEME\t" + l.type);

        stack.push(new LexemeGrammarTuple(l, g.getPrecedence(stack.getFirst().lexeme().type, l.type)));
    }

    public ParseTree getParseTree() {
        return null;
    }
}
