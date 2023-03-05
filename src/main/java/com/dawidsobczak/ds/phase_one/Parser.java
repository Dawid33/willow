package com.dawidsobczak.ds.phase_one;

import java.util.HashMap;
import java.util.LinkedList;

public class Parser<T extends Enum<T>> {
    ParseTree<T> outputTree;
    public record LexemeGrammarTuple<T extends Enum<T>>(Lexeme<T> lexeme, Associativity associativity) {}
    HashMap<LexemeGrammarTuple<T>, Node<T>> openNodes = new HashMap<>();
    LinkedList<LexemeGrammarTuple<T>> stack = new LinkedList<>();
    Grammar<T> g;
    boolean shouldReconsume = false;

    public Parser(Grammar<T> g) {
        this.g = g;
        this.outputTree = null;
    }

    enum Associativity {
        None,
        Left,
        Right,
        Equal,
        Undefined,
    }

    public void consumeToken(Lexeme<T> l) throws ParserException {
        if (stack.isEmpty()) {
            stack.add(new LexemeGrammarTuple<>(l, Associativity.Left));
            return;
        }

        var X = l;

        shouldReconsume = true;
        while (shouldReconsume) {
            shouldReconsume = false;

            if (stack.size() == 1) {
                if (stack.get(0).associativity == Associativity.Undefined && l.type == g.delim) {
                    System.out.println("Done");
                    return;
                }
            }

            LexemeGrammarTuple<T> Y = null;
            for (LexemeGrammarTuple<T> element : stack) {
                if(g.terminals.contains(element.lexeme.type)) {
                    Y = element;
                }
            }
            if (Y == null) {
                stack.add(new LexemeGrammarTuple<>(l, Associativity.Left));
                return;
            }

            Associativity precedence;
            if (X.type == g.delim) {
                precedence = Associativity.Right;
            } else {
                precedence = g.getPrecedence(Y.lexeme.type, X.type);
            }

            if (precedence == Associativity.None) {
                throw new ParserException("No precedence == user grammar error.");
            }

            System.out.println("Open nodes");
            openNodes.forEach((key, value) -> {
                System.out.println("\t" + key.lexeme.type);
            });
            System.out.println("Applying " + X.type + " " + precedence);
            printStack();

            if (precedence == Associativity.Left) {
                stack.add(new LexemeGrammarTuple<>(X, Associativity.Left));
                System.out.println("Append\n");
                return;
            }

            if (precedence == Associativity.Equal) {
                stack.add(new LexemeGrammarTuple<>(X, Associativity.Equal));
                System.out.println("Append\n");
                return;
            }

            if (g.nonTerminals.contains(X.type)) {
                stack.add(new LexemeGrammarTuple<>(X, Associativity.Undefined));
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
                    stack.add(new LexemeGrammarTuple<>(X, Associativity.Right));
                    System.out.println("Append\n");
                    return;
                } else {
                    LexemeGrammarTuple<T> XiMinusOne = null;
                    boolean isDelim = false;
                    if (i - 1 >= 0) {
                        XiMinusOne = stack.get(i - 1);
                    } else {
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
        reduceStack(i, 0);
    }

    void processNonTerminal(int i) {
        reduceStack(i, -1);
    }

    void reduceStack(int i, int offset) {
        Rule<T> rule = null;
        HashMap<T, T> applyRewrites = new HashMap<>();
        int longest = 0;
        for (Rule<T> r : g.rules) {
            HashMap<T, T> rewrites = new HashMap<>();
            boolean ruleApplies = true;
            for (int j = 0; j < r.right.length; j++) {
                T curr;
                if (i + j + offset >= 0 && i + j + offset < stack.size()) {
                    curr = stack.get(i + j + offset).lexeme.type;
                } else {
                    ruleApplies = false;
                    break;
                }

                if (g.nonTerminals.contains(curr)) {
                    boolean rewriteApplies = false;
                    T token = null;
                    for (T t : g.inverseRewriteRules.get(curr)) {
                        if (t == r.right[j])  {
                            rewriteApplies = true;
                            token = t;
                        }
                    }
                    if (rewriteApplies) {
                        rewrites.put(r.right[j], token);
                    } else {
                        ruleApplies = false;
                    }
                } else if (curr != r.right[j]) {
                    ruleApplies = false;
                    break;
                }
            }
            if (ruleApplies) {
                if (r.right.length > longest) {
                    longest = r.right.length;

                    if (rewrites.isEmpty()) {
                        rule = r;
                    } else {
                        rule = r;
                        applyRewrites = (HashMap<T, T>) rewrites.clone();
                        rewrites.clear();
                    }
                }
            }
        }

        if (rule != null) {
           if (!applyRewrites.isEmpty()) {
               for (int j = 0; j < rule.right.length; j++) {
                   LexemeGrammarTuple<T> current = stack.get(i + offset);
                   T token = applyRewrites.get(current.lexeme.type);
                   if (token != null) {
                       current.lexeme.type = token;

                       if (openNodes.containsKey(current)){
                           openNodes.get(current).symbol = token;
                       }
                   }
               }
           }

            var parent = new Node<>(rule.left);
            for (int j = 0; j < rule.right.length; j++) {
                var current = stack.get(i + offset);
                if (openNodes.containsKey(current)) {
                    var subTree = openNodes.get(current);
                    parent.appendChild(subTree);
                    openNodes.remove(current);
                } else {
                    Node<T> leaf = new Node<>(current.lexeme().type);
                    parent.appendChild(leaf);
                }
                stack.remove(i + offset);
            }

            var left = new LexemeGrammarTuple<>(new Lexeme<>(rule.left, null), Associativity.Undefined);
            openNodes.put(left, parent);

            stack.add(i + offset , left);
            System.out.println("Reduce\n");
            shouldReconsume = true;
        }
    }

    public void printStack() {
        for (LexemeGrammarTuple<T> i : stack) {
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

    public ParseTree<T> getParseTree() throws ParserException {
        if (openNodes.size() == 1) {
            return new ParseTree<>((Node<T>) openNodes.values().toArray()[0]);
        } else {
            throw new ParserException("Either hasn't finished parsing input or encountered and error.");
        }
    }
}
