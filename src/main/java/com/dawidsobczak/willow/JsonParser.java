package com.dawidsobczak.willow;

import com.dawidsobczak.willow.phase_one.*;
import com.dawidsobczak.willow.phase_one.grammar.GrammarBuilder;
import com.dawidsobczak.willow.phase_one.json.JsonGrammarSymbols;
import com.dawidsobczak.willow.phase_one.json.JsonLexerInputStream;
import com.dawidsobczak.willow.phase_two.json.JsonAnalyzer;
import com.dawidsobczak.willow.phase_two.json.JsonObject;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JsonParser {
    InputStream s;

    public JsonParser(String text) {
        this.s = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    }

    public JsonParser(InputStream s) {
        this.s = s;
    }

    public void parse() throws Exception {
        try (JsonLexerInputStream lexemeStream = new JsonLexerInputStream(s)) {
            Parser<JsonGrammarSymbols> p = new Parser<>(GrammarBuilder.buildJsonGrammar());
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
            JsonAnalyzer analyzer = new JsonAnalyzer(tree);
            JsonObject json = analyzer.buildObject();
        } catch (LexerException e) {
            throw e;
        }
    }
}
