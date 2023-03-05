package com.dawidsobczak.ds.phase_one;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class JsonLexerInputStream implements AutoCloseable, Closeable, Iterator<Lexeme> {
    Scanner reader;
    public LexerState state = LexerState.DATA;
    StringBuffer buf = new StringBuffer();

    public enum LexerState {
        DATA,
        IN_IDENTIFIER,
        IN_NUMBER,
    }

    public JsonLexerInputStream(InputStream s) {
        this.reader = new Scanner(s);
        reader.useDelimiter("");
    }

    @Override
    public boolean hasNext() {
        return reader.hasNext();
    }

    @Override
    public Lexeme<JsonGrammarSymbols> next() throws NoSuchElementException {
        while (reader.hasNext()) {
            char c = reader.next().charAt(0);
            if (state == LexerState.DATA) {
                if (Character.isDigit(c)) {
                    buf.append(c);
                    state = LexerState.IN_NUMBER;
                } else if (Character.isAlphabetic(c)) {
                    return new Lexeme<>(JsonGrammarSymbols.CHARACTER, null);
                } else if (c == '{') {
                    return new Lexeme<>(JsonGrammarSymbols.LEFT_CURLY, null);
                } else if (c == '}') {
                    return new Lexeme<>(JsonGrammarSymbols.RIGHT_CURLY, null);
                } else if (c == ',') {
                    return new Lexeme<>(JsonGrammarSymbols.COMMA, null);
                } else if (c == ':') {
                    return new Lexeme<>(JsonGrammarSymbols.COLON, null);
                } else if (c == '[') {
                    return new Lexeme<>(JsonGrammarSymbols.LEFT_SQUARE_BRACKET, null);
                } else if (c == ']') {
                    return new Lexeme<>(JsonGrammarSymbols.RIGHT_SQUARE_BRACKET, null);
                } else if (c == '\"') {
                    return new Lexeme<>(JsonGrammarSymbols.LEFT_QUOTE, null);
                } else if (!Character.isWhitespace(c)) {
                    throw new LexerException("Unrecognized char");
                }
            } else if (state == LexerState.IN_NUMBER) {
                if (Character.isDigit(c)) {
                    buf.append(c);
                } else if (Character.isWhitespace(c) || c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')') {
                    return buildFromBuffer(JsonGrammarSymbols.NUMBER);
                } else {
                    throw new LexerException("Incorrect character in number.");
                }
            }
        }

        if (!buf.isEmpty()) {
            if (state == LexerState.IN_NUMBER) {
                return buildFromBuffer(JsonGrammarSymbols.NUMBER);
            } else {
                throw new LexerException("Lexer has data to output but no valid state.");
            }
        } else {
            return null;
        }
    }

    Lexeme<JsonGrammarSymbols> buildFromBuffer(JsonGrammarSymbols s) {
        Lexeme<JsonGrammarSymbols> l = new Lexeme<>(s, buf.toString());
        buf.setLength(0);
        state = LexerState.DATA;
        return l;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    @Override
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
