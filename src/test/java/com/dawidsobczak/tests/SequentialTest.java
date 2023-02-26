package com.dawidsobczak.tests;

import com.dawidsobczak.ds.lang.*;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

public class SequentialTest {
    @Test
    public void a_test() throws Exception {
        InputStream s = this.getClass().getResourceAsStream("/test.ds");
        try (LexerInputStream lexemeStream = new LexerInputStream(s)) {
            Parser p = new Parser();
            while(lexemeStream.hasNext()) {
                Lexeme l = lexemeStream.next();
                System.out.println("LEXEME\t" + l.type);
                p.consumeToken(l);
            }

            ParseTree tree = Parser.parse(lexemeStream);

        } catch (LexerException e) {
            throw e;
        }
    }
}