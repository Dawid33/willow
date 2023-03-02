package com.dawidsobczak.ds;

import com.dawidsobczak.ds.lang.*;

import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        InputStream s = Main.class.getResourceAsStream("/test.ds");
        try (LexerInputStream lexemeStream = new LexerInputStream(s)) {
            Parser p = new Parser(new Grammar());
            while(lexemeStream.hasNext()) {
                Lexeme l = lexemeStream.next();
                if (l != null) {
                    p.consumeToken(l);
                }
            }
            p.consumeToken(new Lexeme(GrammarSymbols.DELIM, null));
            p.printStack();

            ParseTree tree = p.getParseTree();

        } catch (LexerException e) {
            throw e;
        }
    }
}