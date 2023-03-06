package com.dawidsobczak.ds.phase_one.json;

import com.dawidsobczak.ds.phase_one.Lexeme;
import com.dawidsobczak.ds.phase_one.LexerException;

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
    Character c;
    boolean reconsume = false;
    boolean exitingString = false;

    public enum LexerState {
        DATA,
        IN_STRING,
        IN_NUMBER,
    }

    public JsonLexerInputStream(InputStream s) {
        this.reader = new Scanner(s);
        reader.useDelimiter("");
        c = reader.next().charAt(0);
    }

    @Override
    public boolean hasNext() {
        return reader.hasNext();
    }

    @Override
    public Lexeme<JsonGrammarSymbols> next() throws NoSuchElementException {
        Lexeme<JsonGrammarSymbols> l = null;
        while (c != null || reconsume) {
            reconsume = false;
            if (state == LexerState.DATA) {
                if (Character.isDigit(c)) {
                    buf.append(c);
                    state = LexerState.IN_NUMBER;
                } else if (Character.isAlphabetic(c)) {
                    l = new Lexeme<>(JsonGrammarSymbols.CHARACTER, null);
                } else if (c == '{') {
                    l = new Lexeme<>(JsonGrammarSymbols.LEFT_CURLY, null);
                } else if (c == '}') {
                    l = new Lexeme<>(JsonGrammarSymbols.RIGHT_CURLY, null);
                } else if (c == ',') {
                    l = new Lexeme<>(JsonGrammarSymbols.COMMA, null);
                } else if (c == ':') {
                    l = new Lexeme<>(JsonGrammarSymbols.COLON, null);
                } else if (c == '[') {
                    l = new Lexeme<>(JsonGrammarSymbols.LEFT_SQUARE_BRACKET, null);
                } else if (c == ']') {
                    l = new Lexeme<>(JsonGrammarSymbols.RIGHT_SQUARE_BRACKET, null);
                } else if (c == '\"') {
                    if (exitingString) {
                        exitingString = false;
                    } else {
                        state = LexerState.IN_STRING;
                    }
                    l = new Lexeme<>(JsonGrammarSymbols.QUOTE, null);
                } else if (!Character.isWhitespace(c)) {
                    throw new LexerException("Unrecognized char");
                }
            } else if (state == LexerState.IN_NUMBER) {
                if (Character.isDigit(c)) {
                    buf.append(c);
                } else if (!Character.isDigit(c)) {
                    state = LexerState.DATA;
                    reconsume = true;
                    l = buildFromBuffer(JsonGrammarSymbols.NUMBER);
                } else {
                    throw new LexerException("Incorrect character in number.");
                }
            } else if (state == LexerState.IN_STRING) {
                if (c == '\"') {
                    state = LexerState.DATA;
                    reconsume = true;
                    l = buildFromBuffer(JsonGrammarSymbols.CHARACTER);
                    exitingString = true;
                } else {
                    buf.append(c);
                }
            }

            if (!reconsume) {
                if (reader.hasNext()) {
                    c = reader.next().charAt(0);
                } else {
                    c = null;
                }
            }

            if (l != null) {
                return l;
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
