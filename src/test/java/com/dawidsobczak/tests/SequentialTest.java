package com.dawidsobczak.tests;

import com.dawidsobczak.ds.phase_one.*;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

public class SequentialTest {
    @Test
    public void a_test() throws Exception {
        InputStream s = this.getClass().getResourceAsStream("/test.ds");
        try (LexerInputStream lexemeStream = new LexerInputStream(s)) {
            Parser p = new Parser(GrammarBuilder.buildArithmeticGrammar());
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