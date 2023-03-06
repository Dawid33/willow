package com.dawidsobczak.tests;

import com.dawidsobczak.willow.JsonParser;
import org.junit.jupiter.api.Test;

public class JsonTest {
    @Test
    public void test() throws Exception {
        JsonParser p = new JsonParser("{ \"key\" : 100 }");
        p.parse();
    }
}
