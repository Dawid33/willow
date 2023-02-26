package com.dawidsobczak.ds.old;

import java.util.ArrayList;

public class Statement {
    public StatementType type;
    public ArrayList<Lexeme> lexemes = new ArrayList();

    enum StatementType {
        NEW_BLOCK,
        NEW_SECTION,
        MOV,
        ADD,
        SUB
    }

    public Statement(StatementType type) {
        this.type = type;
    }
}
