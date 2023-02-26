package com.dawidsobczak.ds.lang;

import java.util.NoSuchElementException;

public class LexerException extends NoSuchElementException {
    public LexerException(String s) {
        super(s);
    }
}
