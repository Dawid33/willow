package com.dawidsobczak.willow;

import java.io.*;

public class RegenerateTestJson {
    public static void main(String[] args) throws Exception {
        genFile("1GB.json", 1000000000L);
        genFile("500MB.json", 500000000L);
        genFile("250MB.json", 250000000L);
        genFile("100MB.json", 100000000L);
        genFile("50MB.json", 50000000L);
        genFile("10MB.json", 10000000L);
        genFile("1MB.json", 1000000L);
        genFile("500KB.json", 500000L);
        genFile("100KB.json", 100000L);
        genFile("10KB.json", 10000L);
        genFile("1KB.json", 1000L);
    }

    static void genFile(String name, long size) throws Exception {
        CodeGenerator g = new CodeGenerator();
        var fn = new FileOutputStream(name);
        fn.close();
        var f = new FileWriter(name);
        g.GenerateJson(f, size);
        f.close();
    }
}
