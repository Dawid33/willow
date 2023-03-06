package com.dawidsobczak.willow.phase_two.json;

import com.dawidsobczak.willow.phase_one.json.JsonGrammarSymbols;
import com.dawidsobczak.willow.phase_one.Node;
import com.dawidsobczak.willow.phase_one.ParseTree;
import com.dawidsobczak.willow.phase_two.TransformerException;

import java.util.*;

public class JsonAnalyzer {
    ParseTree<JsonGrammarSymbols> tree;
    record StackNode(Iterator<Node<JsonGrammarSymbols>> nodeIter, JsonElement element) {}
    public JsonAnalyzer (ParseTree<JsonGrammarSymbols> tree) {
        this.tree = tree;
    }

    public JsonObject buildObject() throws TransformerException {
        Node<JsonGrammarSymbols> root = tree.root;
        if (root.symbol != JsonGrammarSymbols.OBJECT) {
            throw new TransformerException("Top of parse tree must be an object.");
        }

        JsonObject rootObject = new JsonObject();
        LinkedList<StackNode> openNodes = new LinkedList<>(List.of(new StackNode(new ArrayList<>(List.of(root)).iterator(), rootObject)));
        do {
            var current = openNodes.pop();
            while (current.nodeIter.hasNext()) {
                var child = current.nodeIter.next();
                System.out.println(child.symbol);
                if (!child.children.isEmpty()) {
                    JsonElement element;
                    switch (child.symbol) {
                        case OBJECT -> {
                            element = new JsonObject();
                            element.parent = current.element;
                        }
                        default -> element = current.element;
                    }
                    openNodes.push(new StackNode(child.children.iterator(), element));
                    break;
                } else {
                    System.out.println("\tTerminal" + child.symbol);
                    switch (child.symbol) {
                        case DELIM, START -> throw new TransformerException("Illegal symbol in tree.");
                        case OBJECT, MEMBERS, PAIR, STRING, VALUE, ARRAY, ELEMENTS, CHARS, CHAR -> {
                            throw new TransformerException("Cannot have non-terminal without children");
                        }
                        case RIGHT_CURLY, LEFT_CURLY -> {
                        }
                        case COLON -> {
                        }
                        case NUMBER -> {
                        }
                        case BOOL -> {
                        }
                        case QUOTE -> {
                        }
                        case RIGHT_QUOTE -> {
                        }
                        case LEFT_SQUARE_BRACKET -> {
                        }
                        case RIGHT_SQUARE_BRACKET -> {
                        }
                        case COMMA -> {
                        }
                        case CHARACTER -> {
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + child.symbol);
                    }
                }
            }
        } while (!openNodes.isEmpty());
        return rootObject;
    }
}
