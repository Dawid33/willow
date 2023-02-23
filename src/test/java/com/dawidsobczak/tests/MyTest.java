package com.dawidsobczak.tests;

import com.dawidsobczak.ds.Lexer;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyTest {
    @Test
    public void a_test() {
        InputStream s = this.getClass().getResourceAsStream("/test.ds");
        Lexer l = new Lexer(s);
        l.nextLexeme();
    }
}