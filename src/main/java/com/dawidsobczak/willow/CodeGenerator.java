package com.dawidsobczak.willow;

import com.dawidsobczak.willow.phase_one.json.JsonGrammarSymbols;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Random;

public class CodeGenerator {
    PrintWriter p;
    public static long gigabyteMinSize = 1000000000L;
    long generatedSize = 0;
    long minSizeBytes;
    int maxDepth = 5;
    int depth = 1;
    String availableCharacters;
    Random rng;
    JsonGrammarSymbols[] options = new JsonGrammarSymbols[]{JsonGrammarSymbols.STRING, JsonGrammarSymbols.NUMBER, JsonGrammarSymbols.ARRAY, JsonGrammarSymbols.OBJECT};
    public void GenerateJson(Writer out, long minSizeBytes) throws Exception {
        p = new PrintWriter(out);
        this.minSizeBytes = minSizeBytes;
        availableCharacters = "abcdefghijklmnopqrstuvwxyz";
        rng = new Random();
        generateBlock(JsonGrammarSymbols.OBJECT);
    }

    void generateBlock(JsonGrammarSymbols type) throws Exception {
        if (type == JsonGrammarSymbols.OBJECT) {
            print("{\n");
        } else if (type == JsonGrammarSymbols.ARRAY) {
            print("[\n");
        }

        while (generatedSize < minSizeBytes) {
            int shouldIncreaseDepth = (int) (Math.random() * 101);
            print("\t".repeat(depth));

            var toGenerate = options[(int) (Math.random() * options.length)];
            while (depth >= maxDepth && (toGenerate == JsonGrammarSymbols.OBJECT || toGenerate == JsonGrammarSymbols.ARRAY)) {
                toGenerate = options[(int) (Math.random() * options.length)];
            }

            if (type == JsonGrammarSymbols.OBJECT) {
                print(String.format("\"%s\" : ", randomString()));
            }
            if (toGenerate == JsonGrammarSymbols.STRING) {
                print(String.format("\"%s\"", randomString()));
            } else if(toGenerate == JsonGrammarSymbols.NUMBER) {
                print(String.format("%s", (int)(Math.random() * 100000)));
            } else if (toGenerate == JsonGrammarSymbols.OBJECT || toGenerate == JsonGrammarSymbols.ARRAY) {
                depth += 1;
                generateBlock(toGenerate);
            }

            if (generatedSize < minSizeBytes) {
                if (shouldIncreaseDepth < 50 && depth > 1) {
                    depth -= 1;
                    if (type == JsonGrammarSymbols.OBJECT) {
                        print(String.format("\n%s}", "\t".repeat(depth)));
                    } else if (type == JsonGrammarSymbols.ARRAY) {
                        print(String.format("\n%s]", "\t".repeat(depth)));
                    }
                    return;
                }
                print(",\n");
            }
        }

        if (depth > 0) {
            depth -= 1;
            if (type == JsonGrammarSymbols.OBJECT) {
                print(String.format("\n%s}", "\t".repeat(depth)));
            } else if (type == JsonGrammarSymbols.ARRAY) {
                print(String.format("\n%s]", "\t".repeat(depth)));
            }
        }
    }

    String randomString() {
        int length = (int) (Math.random() * 50);
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = availableCharacters.charAt(rng.nextInt(availableCharacters.length()));
        }
        return new String(text);
    }

    void print(String s) {
        p.printf(s);
        generatedSize += s.getBytes().length;
    }
}
