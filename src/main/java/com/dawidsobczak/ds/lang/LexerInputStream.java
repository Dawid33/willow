package com.dawidsobczak.ds.lang;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Consumer;

public class LexerInputStream implements AutoCloseable, Closeable, Iterator<Lexeme> {
    Scanner reader;
    public LexerState state = LexerState.DATA;
    StringBuffer buf = new StringBuffer();

    public enum LexerState {
        DATA,
        IN_IDENTIFIER,
        IN_NUMBER,
    }

    public LexerInputStream(InputStream s) {
        this.reader = new Scanner(s);
        reader.useDelimiter("");
    }

    @Override
    public boolean hasNext() {
        return reader.hasNext();
    }

    @Override
    public Lexeme next() throws NoSuchElementException {
        while (reader.hasNext()) {
            char c = reader.next().charAt(0);
            if (state == LexerState.DATA) {
                if (Character.isDigit(c)) {
                    buf.append(c);
                    state = LexerState.IN_NUMBER;
                } else if (c == '+') {
                    return new Lexeme(GrammarSymbols.PLUS, null);
                } else if (c == '*') {
                    return new Lexeme(GrammarSymbols.MULTIPLY, null);
                } else if (c == '(') {
                    return new Lexeme(GrammarSymbols.LPAREN, null);
                } else if (c == ')') {
                    return new Lexeme(GrammarSymbols.RPAREN, null);
                } else if (!Character.isWhitespace(c)) {
                    throw new LexerException("Unrecognized char");
                }
            } else if (state == LexerState.IN_NUMBER) {
                if (Character.isDigit(c)) {
                    buf.append(c);
                } else if (Character.isWhitespace(c) || c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')') {
                    Lexeme l = new Lexeme(GrammarSymbols.NUMBER, buf.toString());
                    buf.setLength(0);
                    state = LexerState.DATA;
                    return l;
                } else {
                    throw new LexerException("Incorrect character in number.");
                }
            }
        }
        return null;
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
