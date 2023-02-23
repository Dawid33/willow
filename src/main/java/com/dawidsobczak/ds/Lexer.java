package com.dawidsobczak.ds;

import java.io.*;
import java.util.Scanner;

public class Lexer {
    Scanner reader;
    public Lexer (InputStream stream) {
        reader = new Scanner(stream);
        reader.useDelimiter("");
    }

    public Lexeme nextLexeme() {
        while (reader.hasNext()) {
            char c = reader.next().charAt(0);
            System.out.println(c);
//            switch (c) {
//                case 'a' -> {
//                }
//                default -> {}
//            }

        }
        return null;
    }
}
