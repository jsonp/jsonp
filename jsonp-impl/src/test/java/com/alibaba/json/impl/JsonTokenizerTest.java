package com.alibaba.json.impl;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.json.JsonToken;
import com.alibaba.json.JsonTokenizer;

public class JsonTokenizerTest extends TestCase {
    public void test_0 () throws Exception {
        JsonTokenizer tokenizer = new JsonTokenizer("{\"id\":12345}");
        
        Assert.assertEquals(JsonToken.LBRACE, tokenizer.token());
        Assert.assertEquals(12345, tokenizer.matchFieldInt("id"));
        Assert.assertEquals(JsonTokenizer.END, tokenizer.getMatchState());
        Assert.assertEquals(JsonToken.RBRACE, tokenizer.token());
    }
}
