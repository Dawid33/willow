package com.dawidsobczak.ds.phase_one;

import java.util.ArrayList;
import java.util.List;

public class GrammarBuilder {
    public static Grammar<JsonGrammarSymbols> buildGrammar() {
        ArrayList<Rule<JsonGrammarSymbols>> rules = new ArrayList<>();

        var start = JsonGrammarSymbols.START;
        var object = JsonGrammarSymbols.OBJECT;
        var members= JsonGrammarSymbols.MEMBERS;
        var pair= JsonGrammarSymbols.PAIR;
        var string = JsonGrammarSymbols.STRING;
        var value = JsonGrammarSymbols.VALUE;
        var array = JsonGrammarSymbols.ARRAY;
        var elements= JsonGrammarSymbols.ELEMENTS;
        var chars= JsonGrammarSymbols.CHARS;

        var comma = JsonGrammarSymbols.COMMA;
        var colon= JsonGrammarSymbols.COLON;
        var rcurly = JsonGrammarSymbols.RIGHT_CURLY;
        var lcurly = JsonGrammarSymbols.LEFT_CURLY;
        var rSqrBracket = JsonGrammarSymbols.RIGHT_SQUARE_BRACKET;
        var lSqrBracket= JsonGrammarSymbols.LEFT_SQUARE_BRACKET;
        var number = JsonGrammarSymbols.NUMBER;
        var bool = JsonGrammarSymbols.BOOL;
        var lquote= JsonGrammarSymbols.LEFT_QUOTE;
        var rquote= JsonGrammarSymbols.RIGHT_QUOTE;
        var character = JsonGrammarSymbols.CHARACTER;

        var terminals = new ArrayList<>(List.of(new JsonGrammarSymbols[]{comma, colon, rcurly, lcurly, number, bool,lquote, character}));
        var nonTerminals = new ArrayList<>(List.of(new JsonGrammarSymbols[]{start, object, members, pair, string, value, array, elements, chars}));

        rules.add(new Rule<>(start, new JsonGrammarSymbols[]{object}, 1));

        rules.add(new Rule<>(object, new JsonGrammarSymbols[]{lcurly, rcurly}, 4));
        rules.add(new Rule<>(object, new JsonGrammarSymbols[]{lcurly, pair, rcurly}, 3));

        rules.add(new Rule<>(members, new JsonGrammarSymbols[]{pair}, 4));
        rules.add(new Rule<>(members, new JsonGrammarSymbols[]{pair, comma, members}, 4));

        rules.add(new Rule<>(pair, new JsonGrammarSymbols[]{string, colon, value}, 21));

        rules.add(new Rule<>(value, new JsonGrammarSymbols[]{string}, 10));
        rules.add(new Rule<>(value, new JsonGrammarSymbols[]{number}, 9));
        rules.add(new Rule<>(value, new JsonGrammarSymbols[]{object}, 8));
        rules.add(new Rule<>(value, new JsonGrammarSymbols[]{array}, 7));
        rules.add(new Rule<>(value, new JsonGrammarSymbols[]{bool}, 6));

        rules.add(new Rule<>(string, new JsonGrammarSymbols[]{lquote, lquote}, 12));
        rules.add(new Rule<>(string, new JsonGrammarSymbols[]{lquote, chars, lquote}, 11));

//        rules.add(new Rule<>(array, new JsonGrammarSymbols[]{lSqrBracket, rSqrBracket}, 14));
//        rules.add(new Rule<>(array, new JsonGrammarSymbols[]{lSqrBracket, elements, rSqrBracket}, 13));

        rules.add(new Rule<>(elements, new JsonGrammarSymbols[]{value}, 16));
        rules.add(new Rule<>(elements, new JsonGrammarSymbols[]{value, comma, elements}, 15));

        rules.add(new Rule<>(chars, new JsonGrammarSymbols[]{character}, 19));
        rules.add(new Rule<>(chars, new JsonGrammarSymbols[]{character, chars}, 20));

        return new Grammar<>(rules, terminals, nonTerminals);
    }
    public static Grammar<ArithemticGrammarSymbols> buildArithmeticGrammar() {
        ArrayList<Rule<ArithemticGrammarSymbols>> rules = new ArrayList<>();

        var plus = ArithemticGrammarSymbols.PLUS;
        var multiply = ArithemticGrammarSymbols.MULTIPLY;

        var start = ArithemticGrammarSymbols.START;
        var A = ArithemticGrammarSymbols.A;
        var B = ArithemticGrammarSymbols.B;
        var number = ArithemticGrammarSymbols.NUMBER;

        var terminals = new ArrayList<>(List.of(new ArithemticGrammarSymbols[]{number, plus, multiply}));
        var nonTerminals = new ArrayList<>(List.of(new ArithemticGrammarSymbols[]{start, A, B}));

        rules.add(new Rule<>(start, new ArithemticGrammarSymbols[]{A}, 2));
        rules.add(new Rule<>(start, new ArithemticGrammarSymbols[]{B}, 1));

        rules.add(new Rule<>(A, new ArithemticGrammarSymbols[]{A, plus, B}, 3));
        rules.add(new Rule<>(A, new ArithemticGrammarSymbols[]{B, plus, B}, 2));

        rules.add(new Rule<>(B, new ArithemticGrammarSymbols[]{B, multiply, number}, 3));
        rules.add(new Rule<>(B, new ArithemticGrammarSymbols[]{number}, 2));

        return new Grammar<>(rules, terminals, nonTerminals);
    }
}
