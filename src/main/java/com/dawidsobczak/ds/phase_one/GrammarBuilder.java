package com.dawidsobczak.ds.phase_one;

import java.util.ArrayList;
import java.util.List;

public class GrammarBuilder {
    public static Grammar<JsonGrammarSymbols> buildGrammar() throws GrammarException {
        ArrayList<Rule<JsonGrammarSymbols>> rules = new ArrayList<>();

        var start = JsonGrammarSymbols.START;
        var object = JsonGrammarSymbols.OBJECT;
        var members = JsonGrammarSymbols.MEMBERS;
        var pair = JsonGrammarSymbols.PAIR;
        var string = JsonGrammarSymbols.STRING;
        var value = JsonGrammarSymbols.VALUE;
        var array = JsonGrammarSymbols.ARRAY;
        var elements = JsonGrammarSymbols.ELEMENTS;
        var chars = JsonGrammarSymbols.CHARS;

        var comma = JsonGrammarSymbols.COMMA;
        var colon = JsonGrammarSymbols.COLON;
        var rcurly = JsonGrammarSymbols.RIGHT_CURLY;
        var lcurly = JsonGrammarSymbols.LEFT_CURLY;
        var rSqrBracket = JsonGrammarSymbols.RIGHT_SQUARE_BRACKET;
        var lSqrBracket = JsonGrammarSymbols.LEFT_SQUARE_BRACKET;
        var number = JsonGrammarSymbols.NUMBER;
        var bool = JsonGrammarSymbols.BOOL;
        var quote = JsonGrammarSymbols.QUOTE;
        var character = JsonGrammarSymbols.CHARACTER;

        var terminals = new ArrayList<>(List.of(lcurly, rcurly, colon, comma, number, bool, quote, character, lSqrBracket, rSqrBracket));
        var nonTerminals = new ArrayList<>(List.of(new JsonGrammarSymbols[]{object, members, pair, string, value, array, elements, chars}));

        rules.add(new Rule<>(object, new JsonGrammarSymbols[]{lcurly, rcurly}));
        rules.add(new Rule<>(object, new JsonGrammarSymbols[]{lcurly, members, rcurly}));

        rules.add(new Rule<>(members, new JsonGrammarSymbols[]{pair}));
        rules.add(new Rule<>(members, new JsonGrammarSymbols[]{pair, comma, members}));

        rules.add(new Rule<>(pair, new JsonGrammarSymbols[]{string, colon, value}));

        rules.add(new Rule<>(value, new JsonGrammarSymbols[]{string}));
        rules.add(new Rule<>(value, new JsonGrammarSymbols[]{number}));
        rules.add(new Rule<>(value, new JsonGrammarSymbols[]{object}));
        rules.add(new Rule<>(value, new JsonGrammarSymbols[]{array}));
        rules.add(new Rule<>(value, new JsonGrammarSymbols[]{bool}));

        rules.add(new Rule<>(string, new JsonGrammarSymbols[]{quote, quote}));
        rules.add(new Rule<>(string, new JsonGrammarSymbols[]{quote, chars, quote}));

        rules.add(new Rule<>(array, new JsonGrammarSymbols[]{lSqrBracket, rSqrBracket}));
        rules.add(new Rule<>(array, new JsonGrammarSymbols[]{lSqrBracket, elements, rSqrBracket}));

        rules.add(new Rule<>(elements, new JsonGrammarSymbols[]{value}));
        rules.add(new Rule<>(elements, new JsonGrammarSymbols[]{value, comma, elements}));

        rules.add(new Rule<>(chars, new JsonGrammarSymbols[]{character}));
        rules.add(new Rule<>(chars, new JsonGrammarSymbols[]{character, chars}));

        Grammar<JsonGrammarSymbols> g = new Grammar<>(rules, terminals, nonTerminals, object, JsonGrammarSymbols.DELIM);

        return g;
    }

    public static Grammar<ArithemticGrammarSymbols> buildArithmeticGrammar() throws GrammarException {
        ArrayList<Rule<ArithemticGrammarSymbols>> rules = new ArrayList<>();

        var plus = ArithemticGrammarSymbols.PLUS;
        var multiply = ArithemticGrammarSymbols.MULTIPLY;

        var start = ArithemticGrammarSymbols.START;
        var A = ArithemticGrammarSymbols.A;
        var B = ArithemticGrammarSymbols.B;
        var number = ArithemticGrammarSymbols.NUMBER;

        var terminals = new ArrayList<>(List.of(new ArithemticGrammarSymbols[]{number, plus, multiply}));
        var nonTerminals = new ArrayList<>(List.of(new ArithemticGrammarSymbols[]{start, A, B}));

        rules.add(new Rule<>(start, new ArithemticGrammarSymbols[]{A}));
        rules.add(new Rule<>(start, new ArithemticGrammarSymbols[]{B}));

        rules.add(new Rule<>(A, new ArithemticGrammarSymbols[]{A, plus, B}));
        rules.add(new Rule<>(A, new ArithemticGrammarSymbols[]{B, plus, B}));

        rules.add(new Rule<>(B, new ArithemticGrammarSymbols[]{B, multiply, number}));
        rules.add(new Rule<>(B, new ArithemticGrammarSymbols[]{number}));

        return new Grammar<>(rules, terminals, nonTerminals, start, ArithemticGrammarSymbols.DELIM);
    }
}
