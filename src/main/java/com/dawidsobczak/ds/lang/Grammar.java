package com.dawidsobczak.ds.lang;

import com.dawidsobczak.ds.lang.Parser.Associativity;

import java.util.*;
/*
Arithmetic expr grammar

START -> E
E     -> E + T
E     -> T
T     -> T * f
T     -> F
F     -> NUMBER
F     -> ( E )

Operator Precedence Table from grammar

For such grammars, setting up the precedence table is relatively easy. First we
compute for each non-terminal A the set FIRST OP (A), which is the set of all operators
that can occur as the first operator in sentential forms deriving from A. Note that such
a first operator can be preceded by at most one non-terminal in an operator grammar.
The FIRST OP s of all non-terminals are constructed simultaneously as follows:

    1. For each non-terminal A, find all right-hand sides of all rules for A; now for each
    right-hand side R we insert the first operator in R (if any) into FIRST OP (A). This
    gives us the initial values of all FIRST OP s.

    2. For each non-terminal A, find all right-hand sides of all rules for A; now for each
    right-hand side R that starts with a non-terminal, say B, we add the elements of
    FIRST OP (B) to FIRST OP (A). This is reasonable, since a sentential form of A may
    start with B, so all operators in FIRST OP (B) should also be in FIRST OP (A).

    3. Repeat step 2 above until no FIRST OP changes any more. We have now found
    the FIRST OP of all non-terminals.

We will also need the set LAST OP (A), which is defined similarly, and a similar
algorithm, using the last operator in R in step 1 and a B which ends A in step 2
provides it.
*/


public class Grammar {
    ArrayList<Rule> rules = new ArrayList<>();
    record Rule(GrammarSymbols left, GrammarSymbols[] right) {}

    public Grammar() {
        // Symbols in the grammar.
        var plus = GrammarSymbols.PLUS;
        var multiply = GrammarSymbols.MULTIPLY;
        var lparen = GrammarSymbols.LPAREN;
        var rparen =GrammarSymbols.RPAREN;

        var start = GrammarSymbols.START;
        var term = GrammarSymbols.TERM;
        var factor = GrammarSymbols.FACTOR;
        var expr = GrammarSymbols.EXPR;
        var number = GrammarSymbols.NUMBER;

        var terminals = new ArrayList<>(List.of(new GrammarSymbols[]{plus, multiply, lparen, rparen}));
        var nonTerminals = new ArrayList<>(List.of(new GrammarSymbols[]{start, term, factor, expr, number}));

        rules.add(new Rule(start, new GrammarSymbols[]{expr}));

        rules.add(new Rule(expr, new GrammarSymbols[]{expr, plus, term}));
        rules.add(new Rule(expr, new GrammarSymbols[]{term}));

        rules.add(new Rule(term, new GrammarSymbols[]{term, multiply, factor}));
        rules.add(new Rule(term, new GrammarSymbols[]{factor}));
        rules.add(new Rule(factor, new GrammarSymbols[]{number}));
        rules.add(new Rule(factor, new GrammarSymbols[]{lparen, expr, rparen}));

        var firstOps = new HashMap<GrammarSymbols, Set<GrammarSymbols>>();
        var lastOps = new HashMap<GrammarSymbols, Set<GrammarSymbols>>();

        // Build operator precedence table
        // From Parsing Techniques, A practical Guide 2008 By Dick Grune p.288

        for (Rule r: rules) {
            if (nonTerminals.contains(r.left())) {
                if (r.right.length > 0) {
                    for(GrammarSymbols s : r.right) {
                        if (terminals.contains(s)) {
                            if (!firstOps.containsKey(r.left)) {
                                firstOps.put(r.left , new HashSet<>(Set.of(s)));
                            } else {
                                var right = firstOps.get(r.left);
                                right.add(s);
                            }
                            break;
                        }
                    }

                    for(int i = r.right.length - 1; i >= 0; i--) {
                        if (terminals.contains(r.right[i])) {
                            if (!lastOps.containsKey(r.left)) {
                                lastOps.put(r.left , new HashSet<>(Set.of(r.right[i])));
                            } else {
                                var right = lastOps.get(r.left);
                                right.add(r.right[i]);
                            }
                            break;
                        }
                    }

                }
            }
        }

        boolean didSomething;
        do {
            didSomething = false;
            for (Rule r: rules) {
                if (nonTerminals.contains(r.left())) {
                    if (r.right.length > 0) {
                        if (nonTerminals.contains(r.right[0])) {
                            if (firstOps.containsKey(r.right[0])) {
                                var B = firstOps.get(r.right[0]);
                                if (!firstOps.containsKey(r.left)) {
                                    didSomething = true;
                                    firstOps.put(r.left , new HashSet<>(B));
                                } else if (!firstOps.get(r.left).containsAll(B)) {
                                    firstOps.get(r.left).addAll(B);
                                    didSomething = true;
                                }
                            }
                        }

                        if (nonTerminals.contains(r.right[r.right.length - 1])) {
                            if (lastOps.containsKey(r.right[r.right.length - 1])) {
                                var B = lastOps.get(r.right[r.right.length - 1]);
                                if (!lastOps.containsKey(r.left)) {
                                    didSomething = true;
                                    lastOps.put(r.left , new HashSet<>(B));
                                } else if (!lastOps.get(r.left).containsAll(B)) {
                                    lastOps.get(r.left).addAll(B);
                                    didSomething = true;
                                }
                            }
                        }
                    }
                }
            }
        } while(didSomething);

        HashMap<GrammarSymbols, HashMap<GrammarSymbols, Associativity>> opTable = new HashMap<>();

        HashMap<GrammarSymbols, Associativity> template = new HashMap<>();
        template.put(plus, Associativity.None);
        template.put(multiply, Associativity.None);
        template.put(lparen, Associativity.None);
        template.put(rparen, Associativity.None);
        for (GrammarSymbols terminal : terminals) {
            opTable.put(terminal, (HashMap<GrammarSymbols, Associativity>) template.clone());
        }



        System.out.println();
    }
}
