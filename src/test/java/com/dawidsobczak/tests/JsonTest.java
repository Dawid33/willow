package com.dawidsobczak.tests;

import com.dawidsobczak.ds.JsonParser;
import com.dawidsobczak.ds.phase_one.*;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

public class JsonTest {
    @Test
    public void test() throws Exception {
        JsonParser p = new JsonParser("{ \"key\" : 100 }");
        p.parse();
    }
}
