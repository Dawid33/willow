package com.dawidsobczak.ds;

import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        InputStream s = Main.class.getResourceAsStream("/test.ds");
//        JsonParser p = new JsonParser("{ \"key\" : 100 }");
//        p.parse();
    }
}