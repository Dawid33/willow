package com.dawidsobczak.ds.phase_one;

import java.io.IOException;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Parser {
    ParseTree outputTree;
    public record LexemeGrammarTuple(Lexeme lexeme, Associativity associativity) {}
    LinkedList<LexemeGrammarTuple> stack = new LinkedList<>();
    Grammar<GrammarSymbols> g;
    boolean shouldReconsume = false;

    public Parser(Grammar g) {
        this.g = g;
    }

    enum Associativity {
        None,
        Left,
        Right,
        Equal,
        Undefined,
    }

    public void consumeToken(Lexeme l) throws ParserException, IOException {
        if (stack.isEmpty()) {
            stack.add(new LexemeGrammarTuple(l, Associativity.Left));
            return;
        }

        var X = l;

        shouldReconsume = true;
        while (shouldReconsume) {
            shouldReconsume = false;

            if (stack.size() == 1) {
                if (stack.get(0).associativity == Associativity.Undefined && l.type == GrammarSymbols.DELIM) {
                    System.out.println("Done");
                    return;
                }
            }

            LexemeGrammarTuple Y = null;
            for (LexemeGrammarTuple element : stack) {
                if(g.terminals.contains(element.lexeme.type)) {
                    Y = element;
                }
            }
            if (Y == null) {
                stack.add(new LexemeGrammarTuple(l, Associativity.Left));
                return;
            }

            Associativity precedence;
            if (X.type == GrammarSymbols.DELIM) {
                precedence = Associativity.Right;
            } else {
                precedence = g.getPrecedence(Y.lexeme.type, X.type);
            }


            System.out.println("Applying " + X.type + " " + precedence);
            printStack();

            if (precedence == Associativity.Left) {
                stack.add(new LexemeGrammarTuple(X, Associativity.Left));
                System.out.println("Append\n");
                return;
            }

            if (precedence == Associativity.Equal) {
                stack.add(new LexemeGrammarTuple(X, Associativity.Equal));
                System.out.println("Append\n");
                return;
            }

            if (g.nonTerminals.contains(X.type)) {
                stack.add(new LexemeGrammarTuple(X, Associativity.Undefined));
                System.out.println("Append\n");
                return;
            }

            if (precedence == Associativity.Right) {
                int i = -1;
                for (int j = 0; j < stack.size(); j++) {
                    if (stack.get(j).associativity == Associativity.Left) {
                        i = j;
                    }
                }

                if (i < 0) {
                    stack.add(new LexemeGrammarTuple(X, Associativity.Right));
                    System.out.println("Append\n");
                    return;
                } else {
                    LexemeGrammarTuple XiMinusOne = null;
                    boolean isDelim = false;
                    try {
                        XiMinusOne = stack.get(i - 1);
                    } catch (IndexOutOfBoundsException e) {
                        isDelim = true;
                    }

                    if (isDelim) {
                        processTerminal(i);
                    } else {
                        if (g.terminals.contains(XiMinusOne.lexeme.type)) {
                            processTerminal(i);
                        } else if (g.nonTerminals.contains(XiMinusOne.lexeme.type)) {
                            processNonTerminal(i);
                        } else {
                            throw new ParserException("Should be able to reduce but cannot.");
                        }
                    }
                }
            }
        }
    }

    void processTerminal(int i) {
        for(Rule<GrammarSymbols> r : g.rules) {
            boolean ruleApplies = true;
            for (int j = 0; j < r.right.length; j++) {
                GrammarSymbols curr;
                try {
                    curr = stack.get(i + j).lexeme.type;
                } catch (NoSuchElementException e) {
                    ruleApplies = false;
                    break;
                }

                if (curr != r.right[j]) {
                    ruleApplies = false;
                    break;
                }
            }
            if (ruleApplies) {
                for (int j = 0; j < r.right.length; j++)
                    stack.remove(i);

                stack.add(i, new LexemeGrammarTuple(new Lexeme(r.left, null), Associativity.Undefined));
                System.out.println("Reduce\n");
                shouldReconsume = true;
            }
        }
    }

    void processNonTerminal(int i) {
        Rule<GrammarSymbols> rule = null;
        for (Rule<GrammarSymbols> r : g.rules) {
            boolean ruleApplies = true;
            for (int j = 0; j < r.right.length; j++) {
                GrammarSymbols curr;
                try {
                    curr = stack.get(i + j - 1).lexeme.type;
                } catch (IndexOutOfBoundsException e) {
                    ruleApplies = false;
                    break;
                }

                if (curr != r.right[j]) {
                    ruleApplies = false;
                    break;
                }
            }
            if (ruleApplies) {
                if (rule != null) {
                    if (r.priority > rule.priority) {
                        rule = r;
                    }
                } else {
                    rule = r;
                }
            }
        }
        if (rule != null) {
            for (int j = 0; j < rule.right.length; j++)
                stack.remove(i - 1);

            stack.add(i - 1 , new LexemeGrammarTuple(new Lexeme(rule.left, null), Associativity.Undefined));
            System.out.println("Reduce\n");
            shouldReconsume = true;
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
