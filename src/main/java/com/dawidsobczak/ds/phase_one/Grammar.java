package com.dawidsobczak.ds.phase_one;

import com.dawidsobczak.ds.phase_one.Parser.Associativity;

import java.util.*;
/*
Arithmetic expr grammar

START -> E
E     -> E + T
E     -> T
T     -> T * F
T     -> F
F     -> NUMBER
F     -> ( E )

S -> A
S -> B
A -> A + B
A -> B + B
B -> B * NUMBER
B -> NUMBER

E -> T
T -> T * f
T -> F
F -> NUMBER
F -> ( E )

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


public class Grammar<T extends Enum<T>> {
    public ArrayList<T> nonTerminals;
    public ArrayList<T> terminals;
    ArrayList<Rule<T>> rules;
    HashMap<T, HashMap<T, Associativity>> opTable = new HashMap<>();

    public Associativity getPrecedence(T left, T right) {
        return opTable.get(left).get(right);
    }

    public Grammar(ArrayList<Rule<T>> rules, ArrayList<T> terminals, ArrayList<T> nonTerminals) {
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;
        this.rules = rules;

        var firstOps = new HashMap<T, Set<T>>();
        var lastOps = new HashMap<T, Set<T>>();

        for (Rule<T> r : rules) {
            if (nonTerminals.contains(r.left)) {
                if (r.right.length > 0) {
                    for (T s : r.right) {
                        if (terminals.contains(s)) {
                            if (!firstOps.containsKey(r.left)) {
                                firstOps.put(r.left, new HashSet<>(Set.of(s)));
                            } else {
                                var right = firstOps.get(r.left);
                                right.add(s);
                            }
                            break;
                        }
                    }

                    for (int i = r.right.length - 1; i >= 0; i--) {
                        if (terminals.contains(r.right[i])) {
                            if (!lastOps.containsKey(r.left)) {
                                lastOps.put(r.left, new HashSet<>(Set.of(r.right[i])));
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
            for (Rule<T> r : rules) {
                if (nonTerminals.contains(r.left)) {
                    if (r.right.length > 0) {
                        if (nonTerminals.contains(r.right[0])) {
                            if (firstOps.containsKey(r.right[0])) {
                                var Bs = firstOps.get(r.right[0]);
                                if (!firstOps.containsKey(r.left)) {
                                    didSomething = true;
                                    firstOps.put(r.left, new HashSet<>(Bs));
                                } else if (!firstOps.get(r.left).containsAll(Bs)) {
                                    firstOps.get(r.left).addAll(Bs);
                                    didSomething = true;
                                }
                            }
                        }

                        if (nonTerminals.contains(r.right[r.right.length - 1])) {
                            if (lastOps.containsKey(r.right[r.right.length - 1])) {
                                var Bs = lastOps.get(r.right[r.right.length - 1]);
                                if (!lastOps.containsKey(r.left)) {
                                    didSomething = true;
                                    lastOps.put(r.left, new HashSet<>(Bs));
                                } else if (!lastOps.get(r.left).containsAll(Bs)) {
                                    lastOps.get(r.left).addAll(Bs);
                                    didSomething = true;
                                }
                            }
                        }
                    }
                }
            }
        } while (didSomething);

        System.out.println("FIRST OP");
        for (var row : lastOps.keySet()) {
            System.out.println(row + ": " + firstOps.get(row));
        }
        System.out.println();

        System.out.println("LAST OP");
        for (var row : lastOps.keySet()) {
            System.out.println(row + ": " + lastOps.get(row));
        }
        System.out.println();

        HashMap<T, Associativity> template = new HashMap<>();
        for (T e : terminals) {
            template.put(e, Associativity.None);
        }

        for (T terminal : terminals) {
            opTable.put(terminal, (HashMap<T, Associativity>) template.clone());
        }

        for (Rule<T> r : rules) {
            for (int i = 0; i < r.right.length; i++) {
                if (i + 1 < r.right.length) {
                    if (terminals.contains(r.right[i]) && terminals.contains(r.right[i + 1])) {
                        opTable.get(r.right[i]).put(r.right[i + 1], Associativity.Equal);
                    }
                    if (terminals.contains(r.right[i]) && nonTerminals.contains(r.right[i + 1])) {
                        if (firstOps.containsKey(r.right[i + 1])) {
                            var firstOpA = firstOps.get(r.right[i + 1]);
                            for (var q2 : firstOpA) {
                                opTable.get(r.right[i]).put(q2, Associativity.Left);
                            }
                        }
                    }
                    if (nonTerminals.contains(r.right[i]) && terminals.contains(r.right[i + 1])) {
                        if (lastOps.containsKey(r.right[i])) {
                            var lastOpA= lastOps.get(r.right[i]);
                            for (var q2 : lastOpA) {
                                opTable.get(q2).put(r.right[i + 1], Associativity.Right);
                            }
                        }
                    }
                }
                if (i + 2 < r.right.length) {
                    if (terminals.contains(r.right[i]) && nonTerminals.contains(r.right[i + 1]) && terminals.contains(r.right[i + 2])) {
                        opTable.get(r.right[i]).put(r.right[i + 2], Associativity.Equal);
                    }
                }
            }
        }

        System.out.printf("%-10s", "");
        for (T row : terminals) {
            System.out.printf("%-10s ", row);
        }
        System.out.println();

        for (T row : terminals) {
            System.out.printf("%-10s", row);
            var currRow = opTable.get(row);
            for (T col : terminals) {
                System.out.printf("%-10s ", currRow.get(col));
            }
            System.out.println();
        }
        System.out.println();
    }
}
