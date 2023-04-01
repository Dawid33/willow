package com.dawidsobczak.willow;

import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        StringWriter w = new StringWriter();
        var f = new FileInputStream("test.json");
        JsonParser p = new JsonParser(f);
        p.parse();
    }
}