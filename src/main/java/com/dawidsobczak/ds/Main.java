package com.dawidsobczak.ds;

import com.dawidsobczak.ds.phase_one.Lexeme;
import com.dawidsobczak.ds.phase_one.LexerException;
import com.dawidsobczak.ds.phase_one.ParseTree;
import com.dawidsobczak.ds.phase_one.Parser;
import com.dawidsobczak.ds.phase_one.ds.DsGrammarSymbols;
import com.dawidsobczak.ds.phase_one.ds.DsLexerInputStream;
import com.dawidsobczak.ds.phase_one.grammar.GrammarBuilder;

import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        InputStream s = Main.class.getResourceAsStream("/test.ds");
        try (DsLexerInputStream lexemeStream = new DsLexerInputStream(s)) {
            Parser<DsGrammarSymbols> p = new Parser<>(GrammarBuilder.buildDsGrammar());
            while(lexemeStream.hasNext()) {
                Lexeme<DsGrammarSymbols> l = lexemeStream.next();
                System.out.println("LEXER : " + l.type);
                p.consumeToken(l);
            }
            p.consumeToken(new Lexeme<>(DsGrammarSymbols.DELIM, null));
            p.printStack();

            ParseTree<DsGrammarSymbols> tree = p.getParseTree();
            System.out.println();
            System.out.println(tree);
        } catch (LexerException e) {
            throw e;
        }
    }
}