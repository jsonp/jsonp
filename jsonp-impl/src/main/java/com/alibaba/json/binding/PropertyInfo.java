package com.alibaba.json.binding;

import java.lang.reflect.Type;

public class PropertyInfo {

    private final Class<?> clazz;
    private final Type     type;
    private final String   name;

    public PropertyInfo(Class<?> clazz, Type type, String name){
        super();
        this.clazz = clazz;
        this.type = type;
        this.name = name;
    }

    
    public Class<?> getClazz() {
        return clazz;
    }

    
    public Type getType() {
        return type;
    }

    
    public String getName() {
        return name;
    }

    
}
