package com.dawidsobczak.tests;

import com.dawidsobczak.ds.Lexeme;
import com.dawidsobczak.ds.Lexer;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.InputStream;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyTest {
    @Test
    public void a_test() throws Lexer.LexerException {
        InputStream s = this.getClass().getResourceAsStream("/test.ds");
        Lexer lexer = new Lexer(s);
        Lexeme l = lexer.nextLexeme();
        while (l != null) {
            System.out.println(l.raw);
            l = lexer.nextLexeme();
        }
    }
}