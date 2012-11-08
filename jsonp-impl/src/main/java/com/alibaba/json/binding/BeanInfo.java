package com.alibaba.json.binding;

import java.lang.reflect.Type;

public class BeanInfo {
    private final Type     type;
    private final Class<?> clazz;
    
    public BeanInfo(Type type, Class<?> clazz){
        super();
        this.type = type;
        this.clazz = clazz;
    }

    public Type getType() {
        return type;
    }

    public Class<?> getClazz() {
        return clazz;
    }

}
