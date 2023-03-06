package com.dawidsobczak.tests;

import com.dawidsobczak.ds.phase_one.*;
import com.dawidsobczak.ds.phase_one.arithmetic.ArithemticGrammarSymbols;
import com.dawidsobczak.ds.phase_one.arithmetic.ArithemticLexerInputStream;
import com.dawidsobczak.ds.phase_one.grammar.GrammarBuilder;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

public class ArithmeticTest {
    @Test
    public void test() throws Exception {
        InputStream s = this.getClass().getResourceAsStream("/arithmetic.txt");
        try (ArithemticLexerInputStream lexemeStream = new ArithemticLexerInputStream(s)) {
            Parser p = new Parser(GrammarBuilder.buildArithmeticGrammar());
            while(lexemeStream.hasNext()) {
                Lexeme l = lexemeStream.next();
                if (l != null) {
                    p.consumeToken(l);
                }
            }
            p.consumeToken(new Lexeme(ArithemticGrammarSymbols.DELIM, null));
            p.printStack();

            ParseTree tree = p.getParseTree();
            System.out.println(tree);

        } catch (LexerException e) {
            throw e;
        }
    }
}
