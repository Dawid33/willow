package com.dawidsobczak.willow;

import com.dawidsobczak.willow.phase_one.Lexeme;
import com.dawidsobczak.willow.phase_one.LexerException;
import com.dawidsobczak.willow.phase_one.ParseTree;
import com.dawidsobczak.willow.phase_one.Parser;
import com.dawidsobczak.willow.phase_one.willow.WillowGrammarSymbols;
import com.dawidsobczak.willow.phase_one.willow.WillowLexerInputStream;
import com.dawidsobczak.willow.phase_one.grammar.GrammarBuilder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class WillowParser {
    InputStream s;

    public WillowParser(String text) {
        this.s = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    }

    public WillowParser(InputStream s) {
        this.s = s;
    }

    public void parse() throws Exception {
        InputStream s = Main.class.getResourceAsStream("/test.ds");
        try (WillowLexerInputStream lexemeStream = new WillowLexerInputStream(s)) {
            Parser<WillowGrammarSymbols> p = new Parser<>(GrammarBuilder.buildDsGrammar());
            while(lexemeStream.hasNext()) {
                Lexeme<WillowGrammarSymbols> l = lexemeStream.next();
                System.out.println("LEXER : " + l.type);
                p.consumeToken(l);
            }
            p.consumeToken(new Lexeme<>(WillowGrammarSymbols.DELIM, null));
            p.printStack();

            ParseTree<WillowGrammarSymbols> tree = p.getParseTree();
            System.out.println();
            System.out.println(tree);
        } catch (LexerException e) {
            throw e;
        }
    }
}
