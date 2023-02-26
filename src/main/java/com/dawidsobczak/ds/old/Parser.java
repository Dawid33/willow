package com.dawidsobczak.ds.old;

import java.util.ArrayList;
import java.util.HashMap;

/*
    mov rds, len
    mov = terminal
    mov rax, .data.hello
 */

public class Parser {
    HashMap<String, String> symbolTable = new HashMap<>();
    int depth = 0;
    ParserState state = ParserState.AWAITING_STMT;
    Statement currentStatement = null;
    public ArrayList<Statement> output;

    enum ParserState {
        AWAITING_STMT,
        IN_STATEMENT
    }
    public static class ParserExeception extends Exception {
        ParserExeception(String str) {
            super(str);
        }

    }

    public Parser() {}

    public void consumeToken(Lexeme lexeme) throws ParserExeception {
        switch (this.state) {
            case AWAITING_STMT -> {
                if (lexeme.type == Lexeme.LexemeType.IDENTIFIER) {
                    switch (lexeme.identifierType) {
                        case Section -> this.currentStatement = new Statement(Statement.StatementType.NEW_SECTION);
                        case Unidentified -> this.currentStatement = new Statement(Statement.StatementType.NEW_BLOCK);
                        case Mov -> this.currentStatement = new Statement(Statement.StatementType.MOV);
                        case Add -> this.currentStatement = new Statement(Statement.StatementType.ADD);
                        case Sub -> this.currentStatement = new Statement(Statement.StatementType.SUB);
                        default -> throw new ParserExeception("Unexpected value: " + lexeme.identifierType);
                    }
                    this.currentStatement.lexemes.add(lexeme);
                } else {
                    throw new ParserExeception("Unexpected lexeme while awaiting statement. Expected Identifier, recieved " + lexeme.type);
                }
            }
            case IN_STATEMENT -> {
                if (currentStatement == null) {
                    throw new ParserExeception("Current statement cannot be null while inside a statement.");
                }
                switch (currentStatement.type) {
                    case NEW_BLOCK -> {
                        Lexeme.LexemeType[] expected = { Lexeme.LexemeType.IDENTIFIER, Lexeme.LexemeType.LEFT_CURLY_BRACKET};
                    }
                    case NEW_SECTION -> {
                        Lexeme.LexemeType[] expected = { Lexeme.LexemeType.IDENTIFIER, Lexeme.LexemeType.IDENTIFIER, Lexeme.LexemeType.LEFT_CURLY_BRACKET};
                        int current = currentStatement.lexemes.size() - 1;
                        if (expected[current] != lexeme.type) {
                            throw new ParserExeception("Expected " + expected[current] + " and recieved " + lexeme.type);
                        }
                        if (currentStatement.lexemes.size() == expected.length) {
                            state = ParserState.AWAITING_STMT;
                            currentStatement = null;
                        }
                    }
                    case MOV -> {
                    }
                    case ADD -> {
                    }
                    case SUB -> {
                    }
                }

            }
        }
    }

}
