package com.dawidsobczak.ds.lang;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Parser {
    ParseTree outputTree;
    record LexemeGrammarTuple(Lexeme lexeme, Associativity associativity) {}
    LinkedList<LexemeGrammarTuple> stack = new LinkedList<>();
    Grammar g;

    public Parser(Grammar g) {
        this.g = g;
        stack.push(new LexemeGrammarTuple(new Lexeme(GrammarSymbols.DELIM, null), Associativity.Right));
    }

    enum Associativity {
        None,
        Left,
        Right,
        Equal,
        Undefined,
    }

    public void consumeToken(Lexeme l) throws ParserException, IOException {
//        if (stack.isEmpty()) {
////            System.out.println("LEXEME\t" + l.type);
//            stack.push(new LexemeGrammarTuple(l, Associativity.Undefined));
//            return;
//        }

        // Y = stack.getFirst().lexeme().type
        // X = l.type
        // Y < X
        boolean isRunning = true;
        boolean didNothing = true;
        int cnt = 0;
        while(isRunning) {
            System.out.println();
            LexemeGrammarTuple topMost = null;
            int topMostIndex = 0;
            for (int i = 0; i < stack.size(); i++) {
                if (g.terminals.contains(stack.get(i).lexeme.type)) {
                    topMost = stack.get(i);
                    topMostIndex = i;
                    break;
                }
            }
            LexemeGrammarTuple Y;
            if (topMost != null) {
                Associativity a =  g.getPrecedence(topMost.lexeme().type, l.type);
                if (l.type == GrammarSymbols.DELIM) {
                    Y = new LexemeGrammarTuple(l, Associativity.Left);
                } else {
                    Y = new LexemeGrammarTuple(l, a);
                }
            } else {
                throw new ParserException("No terminal in stack");
            }

            System.out.println("TOP : " + Y.lexeme.type + " " + Y.associativity);
            printStack();
            if (Y.lexeme.type == GrammarSymbols.DELIM && stack.get(0).lexeme.type == GrammarSymbols.DELIM) {
                return;
            }

            if ((Y.associativity == Associativity.Left || Y.associativity == Associativity.Equal)
                && (Y.lexeme.type != GrammarSymbols.DELIM)) {
                stack.push(Y);
                break;
            } else if (g.nonTerminals.contains(Y.lexeme.type)) {
                stack.push(new LexemeGrammarTuple(Y.lexeme, Associativity.Undefined));
                break;
            } else if (Y.associativity == Associativity.Right || Y.lexeme.type == GrammarSymbols.DELIM){
                boolean hasLeft = false;
                for (int i = topMostIndex; i < stack.size(); i++) {
                    var Xi = stack.get(i);
//                    System.out.println(i);
                    if (Xi.associativity == Associativity.Left) {
                        System.out.printf("Attempting to reduce after recieving %s %s\n", l.type, Y.associativity);
                        hasLeft = true;
                        LexemeGrammarTuple XiMinusOne;
                        if (i > 0) {
                            XiMinusOne = stack.get(i - 1);
                        } else {
                            XiMinusOne = Y;
                        }

                        if (g.nonTerminals.contains(XiMinusOne.lexeme.type)) {
                            for (Grammar.Rule r : g.rules) {
                                boolean ruleApplies = true;
                                for (int j = 0; j < r.right().length; j++) {
                                    GrammarSymbols curr;
                                    try {
                                        curr = stack.get(i + j - 1).lexeme.type;
                                    } catch (NoSuchElementException e) {
                                        ruleApplies = false;
                                        break;
                                    }

                                    if (curr != r.right()[j]) {
                                        ruleApplies = false;
                                        break;
                                    }
                                }
                                if (ruleApplies) {
//                                    System.out.println(r.right().length);
                                    for (int j = 0; j < r.right().length; j++) {
                                        stack.remove(i - 1);
//                                        printStack();
                                    }

                                    stack.add(i - 1, new LexemeGrammarTuple(new Lexeme(r.left(), null), Associativity.Undefined));
                                    didNothing = false;
                                }
                            }

                        } else if (g.terminals.contains(XiMinusOne.lexeme.type) || Y.lexeme.type == GrammarSymbols.DELIM){
                            for (Grammar.Rule r : g.rules) {
                                boolean ruleApplies = true;
                                for (int j = 0; j < r.right().length; j++) {
                                    GrammarSymbols curr;
                                    try {
                                        curr = stack.get(i + j).lexeme.type;
                                    } catch (NoSuchElementException e) {
                                        ruleApplies = false;
                                        break;
                                    }

                                    if (curr != r.right()[j]) {
                                        ruleApplies = false;
                                        break;
                                    }
                                }
                                if (ruleApplies) {
                                    for (int j = 0; j < r.right().length; j++)
                                        stack.remove(i);
                                    stack.add(i, new LexemeGrammarTuple(new Lexeme(r.left(), null), Associativity.Undefined));
                                    didNothing = false;
                                }
                            }
                        } else {
                            throw new ParserException("Symbol does not exist in grammar... ?");
                        }
                        break;
                    }
                }
                if (!hasLeft) {
                    stack.push(Y);
                    didNothing = false;
                }
            }

//            if (top.lexeme.type == GrammarSymbols.DELIM) {
//                isRunning = true;
//            } else {
//                isRunning = false;
//            }

            if (didNothing) {
                break;
            } else {
                if (cnt > 50){
                    break;
                } else {
                    cnt++;
                }
            }
        }
    }

    public void printStack() {
        for (LexemeGrammarTuple i : stack) {
            char x = '!';
            if (i.associativity == Associativity.Left)
                x = '<';
            else if (i.associativity == Associativity.Right)
                x = '>';
            else if (i.associativity == Associativity.Equal)
                x = '=';
            else if (i.associativity == Associativity.Undefined)
                x = '?';
            System.out.printf("(%s, %c) ", i.lexeme.type, x);
        }
        System.out.println();
    }

    public ParseTree getParseTree() {
        return null;
    }
}
