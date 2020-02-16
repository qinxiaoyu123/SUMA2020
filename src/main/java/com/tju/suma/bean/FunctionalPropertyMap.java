package com.tju.suma.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FunctionalPropertyMap {
    private static final Map<String, Integer> InverseFunPropertyMap = new ConcurrentHashMap<>();
    private static final Map<String, Integer> FunPropertyMap = new ConcurrentHashMap<>();

    public static Map<String, Integer> getInverseFunPropertyMap() {
        return InverseFunPropertyMap;
    }

    public static Map<String, Integer> getFunPropertyMap() {
        return FunPropertyMap;
    }
}
