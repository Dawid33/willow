package com.dawidsobczak.willow.phase_one.willow;

import com.dawidsobczak.willow.phase_one.Lexeme;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class WillowLexerInputStream implements AutoCloseable, Closeable, Iterator<Lexeme> {
    Scanner reader;
    public LexerState state = LexerState.DATA;
    StringBuffer buf = new StringBuffer();

    public enum LexerState {
        DATA,
    }

    public WillowLexerInputStream(InputStream s) {
        this.reader = new Scanner(s);
        reader.useDelimiter("");
    }

    @Override
    public boolean hasNext() {
        return reader.hasNext();
    }

    @Override
    public Lexeme<WillowGrammarSymbols> next() throws NoSuchElementException {
        return null;
//        while (reader.hasNext()) { }
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
