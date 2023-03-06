package com.dawidsobczak.willow.phase_one;

import java.util.NoSuchElementException;

public class LexerException extends NoSuchElementException {
    public LexerException(String s) {
        super(s);
    }
}
