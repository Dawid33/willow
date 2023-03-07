package com.dawidsobczak.willow;

import com.dawidsobczak.willow.phase_one.grammar.Grammar;
import com.dawidsobczak.willow.phase_one.grammar.GrammarBuilder;
import com.dawidsobczak.willow.phase_one.grammar.Rule;
import com.dawidsobczak.willow.phase_one.json.JsonGrammarSymbols;
import com.sun.tools.attach.AgentInitializationException;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

public class CodeGenerator {
    public static void GenerateJson(Writer out, int minSizeBytes) throws Exception {
        int depth = 0;
        final int maxDepth = 5;
        int generatedSize = 0;
        JsonGrammarSymbols[] options = new JsonGrammarSymbols[]{JsonGrammarSymbols.STRING, JsonGrammarSymbols.NUMBER};
        JsonGrammarSymbols[] increaseOptions = new JsonGrammarSymbols[]{JsonGrammarSymbols.OBJECT, JsonGrammarSymbols.ARRAY};

        PrintWriter p = new PrintWriter(out);

        p.println("{");

        while (generatedSize < minSizeBytes) {

//            int shouldIncreaseDepth = (int) (Math.random() * 101);
//            if (depth < maxDepth && shouldIncreaseDepth >= 50) {
//                var toGenerate = options[(int) (Math.random() * options.length)];
//                depth += 1;
//            } else if (shouldIncreaseDepth < 50) {
//                depth -= 1;
//
//            }

            p.printf("\t".repeat(depth + 1));

            var toGenerate = options[(int) (Math.random() * options.length)];
            if (toGenerate == JsonGrammarSymbols.STRING) {
                p.printf("\"string\" : \"test\"");
            } else if(toGenerate == JsonGrammarSymbols.NUMBER) {
                p.printf("\"number\" : %s", (int)(Math.random() * 100000));
            }

            generatedSize += 1;
            if (generatedSize < minSizeBytes) {
                p.println(",");
            }
        }

        for (int i = 0; i <= depth; i++) {
            p.println('}');
        }
    }
}
