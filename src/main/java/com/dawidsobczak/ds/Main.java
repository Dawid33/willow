package com.dawidsobczak.ds;

import com.dawidsobczak.ds.phase_one.*;

import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        InputStream s = Main.class.getResourceAsStream("/test.ds");
        try (JsonLexerInputStream lexemeStream = new JsonLexerInputStream(s)) {
            Parser<JsonGrammarSymbols> p = new Parser<>(GrammarBuilder.buildGrammar());
            while(lexemeStream.hasNext()) {
                Lexeme<JsonGrammarSymbols> l = lexemeStream.next();
                System.out.println("LEXER : " + l.type);
                p.consumeToken(l);
            }
            p.consumeToken(new Lexeme<>(JsonGrammarSymbols.DELIM, null));
            p.printStack();

            ParseTree<JsonGrammarSymbols> tree = p.getParseTree();
            System.out.println();
            System.out.println(tree);
        } catch (LexerException e) {
            throw e;
        }
    }
}