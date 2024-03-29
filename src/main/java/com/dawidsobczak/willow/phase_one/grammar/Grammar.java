package com.dawidsobczak.willow.phase_one.grammar;

import com.dawidsobczak.willow.phase_one.Parser.Associativity;

import java.util.*;

public class Grammar<T extends Enum<T>> {
    public ArrayList<T> nonTerminals;
    public ArrayList<T> terminals;
    public T delim;
    public HashMap<T, ArrayList<T>> inverseRewriteRules;
    T axiom;
    public ArrayList<Rule<T>> rules;
    HashMap<T, HashMap<T, Associativity>> opTable = new HashMap<>();

    public Associativity getPrecedence(T left, T right) {
        return opTable.get(left).get(right);
    }

    public Grammar(ArrayList<Rule<T>> rules, ArrayList<T> terminals, ArrayList<T> nonTerminals, T axiom, T delim) throws GrammarException {
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;
        this.rules = rules;
        this.axiom = axiom;
        this.delim = delim;

        // Create re-write rules
        // TODO : Figure out how this actually works.
        HashMap<T, ArrayList<T>> rewriteRules = new HashMap<>();
        for (T nonTerminal : nonTerminals) {
            rewriteRules.put(nonTerminal, new ArrayList<>());
        }
        boolean modified = true;
        while (modified) {
            modified = false;
            for (Rule<T> r : rules) {
                T token = r.right[0];
                if (r.right.length != 1 || terminals.contains(token)) {
                    continue;
                }
                if (!rewriteRules.get(r.left).contains(token)) {
                    modified = true;
                    rewriteRules.get(r.left).add(token);
                } else {
                    for (T ttoken : rewriteRules.get(token)) {
                        if (!rewriteRules.get(r.left).contains(ttoken)) {
                            modified = true;
                            rewriteRules.get(r.left).add(ttoken);
                        }
                    }
                }
            }
        }

        // Create inverse rewrite rules
        // TODO: Figure out what this is.
        inverseRewriteRules = new HashMap<>();
        for (T t : nonTerminals) {
            inverseRewriteRules.put(t, new ArrayList<>(List.of(t)));
        }
        for (T t : nonTerminals) {
            for (T t1 : rewriteRules.get(t)) {
                inverseRewriteRules.get(t1).add(t);
            }
        }

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

        System.out.printf("%-14s", "");
        for (T row : terminals) {
            System.out.printf("%-14s ", row);
        }
        System.out.println();

        for (T row : terminals) {
            System.out.printf("%-14s", row);
            var currRow = opTable.get(row);
            for (T col : terminals) {
                System.out.printf("%-14s ", currRow.get(col));
            }
            System.out.println();
        }
        System.out.println();
    }
}
