package com.alibaba.json;

public enum JsonToken {
    INT, //
    DOUBLE, //
    STRING, //
    TRUE, //
    FALSE, //
    NULL, //
    EOF, //

    LBRACE("{"), //
    RBRACE("}"), //
    LBRACKET("["), //
    RBRACKET("]"), //
    COMMA(","), //
    COLON(":"),

    ;

    public final String name;

    private JsonToken(){
        this(null);
    }

    private JsonToken(String name){
        this.name = name;
    }
}