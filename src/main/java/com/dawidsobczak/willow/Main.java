package com.dawidsobczak.willow;

import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        StringWriter w = new StringWriter();
//        CodeGenerator.GenerateJson(w, 100);
//        System.out.println(w.toString());
//        JsonParser p = new JsonParser(w.toString());
        JsonParser p = new JsonParser("{ \"hello\" : 100 }");
        p.parse();
    }
}