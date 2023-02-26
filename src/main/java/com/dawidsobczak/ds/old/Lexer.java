package com.dawidsobczak.ds.old;

import java.io.*;
import java.util.Scanner;

public class Lexer {
    public static class LexerException extends Exception {
        public LexerException(String s) {
            super(s);
        }
    }
    public enum LexerState {
        AWAITING_LEXEME,
        IN_IDENTIFIER,
    }

    StringBuffer buffer = new StringBuffer();
    LexerState state = LexerState.AWAITING_LEXEME;
    Scanner reader;
    boolean reconsume = false;
    char c;

    public Lexer (InputStream stream) {
        reader = new Scanner(stream);
        reader.useDelimiter("");
    }

    public Lexeme nextLexeme() throws LexerException{
        while (reader.hasNext()) {
            if (reconsume) {
                reconsume = false;
            } else {
                c = reader.next().charAt(0);
            }


            switch (state) {
                case AWAITING_LEXEME -> {
                    if (Character.isLetter(c) || c == '_' || c == '-') {
                        buffer.append(c);
                        state = LexerState.IN_IDENTIFIER;
                    } else if (c == '{') {
                        return new Lexeme(Lexeme.LexemeType.LEFT_CURLY_BRACKET, "{");
                    } else if (c == '}') {
                        return new Lexeme(Lexeme.LexemeType.RIGHT_CURLY_BRACKET, "{");
                    } else if (c == ',') {
                        return new Lexeme(Lexeme.LexemeType.COMMA, ",");
                    } else if (c == '.') {
                        return new Lexeme(Lexeme.LexemeType.PERIOD, ".");
                    } else if (c == ';') {
                        return new Lexeme(Lexeme.LexemeType.SEMICOLON, ";");
                    } else if (c == '(') {
                        return new Lexeme(Lexeme.LexemeType.LEFT_PAREN, "(");
                    } else if (c == ')') {
                        return new Lexeme(Lexeme.LexemeType.RIGHT_PAREN, ")");
                    } else if (!Character.isWhitespace(c)) {
                        throw new LexerException("Illegal character : " + c);
                    }
                }
                case IN_IDENTIFIER -> {
                    if (Character.isLetter(c)) {
                        buffer.append(c);
                    } else if (Character.isWhitespace(c) || c == '(' || c == ')' || c == ';' || c == ',' || c == '.') {
                        state = LexerState.AWAITING_LEXEME;
                        String raw = buffer.toString();
                        buffer.setLength(0);
                        reconsume = true;
                        return new Lexeme(Lexeme.LexemeType.IDENTIFIER, raw);
                    } else {
                        throw new LexerException("Illegal character in suspected identifier : " + c);
                    }
                }
            }
        }

        return null;
    }
}
