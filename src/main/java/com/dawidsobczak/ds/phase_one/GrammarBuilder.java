package com.dawidsobczak.ds.phase_one;

import java.util.ArrayList;
import java.util.List;

public class GrammarBuilder {
    public static Grammar<GrammarSymbols> buildArithmeticGrammar() {
        ArrayList<Rule<GrammarSymbols>> rules = new ArrayList<>();

        var plus = GrammarSymbols.PLUS;
        var multiply = GrammarSymbols.MULTIPLY;
        var lparen = GrammarSymbols.LPAREN;
        var rparen = GrammarSymbols.RPAREN;
        var delim = GrammarSymbols.DELIM;

        var start = GrammarSymbols.START;
        var program = GrammarSymbols.PROGRAM;
        var A = GrammarSymbols.A;
        var B = GrammarSymbols.B;
        var term = GrammarSymbols.TERM;
        var factor = GrammarSymbols.FACTOR;
        var expr = GrammarSymbols.EXPR;
        var number = GrammarSymbols.NUMBER;

        var terminals = new ArrayList<>(List.of(new GrammarSymbols[]{number, plus, multiply}));
        var nonTerminals = new ArrayList<>(List.of(new GrammarSymbols[]{start, A, B}));

        rules.add(new Rule<>(start, new GrammarSymbols[]{A}, 0));
        rules.add(new Rule<>(start, new GrammarSymbols[]{B}, 0));

        rules.add(new Rule<>(A, new GrammarSymbols[]{A, plus, B}, 2));
        rules.add(new Rule<>(A, new GrammarSymbols[]{B, plus, B}, 1));

        rules.add(new Rule<>(B, new GrammarSymbols[]{B, multiply, number}, 2));
        rules.add(new Rule<>(B, new GrammarSymbols[]{number}, 1));

        return new Grammar<>(rules, terminals, nonTerminals);
    }
}
