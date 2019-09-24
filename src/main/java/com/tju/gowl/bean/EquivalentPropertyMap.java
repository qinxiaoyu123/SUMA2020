package com.tju.gowl.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EquivalentPropertyMap {
    private static final Map<Integer, Integer> EquivalentPropertyMap = new ConcurrentHashMap<>();
    private static final Map<Integer, Integer> EquivalentPropertyMapDecode = new ConcurrentHashMap<>();

    public static Map<Integer, Integer> getEquivalentPropertyMapDecode() {
        return EquivalentPropertyMapDecode;
    }

    public static Map<Integer, Integer> getEquivalentPropertyMap(){ return EquivalentPropertyMap; }

    public static int getEquivalentProperty(int first) {
        if(EquivalentPropertyMap.containsKey(first)){
            //replace first with second
            return EquivalentPropertyMap.get(first);
        }
        else{
            return -1;
        }
    }

    public static void setEquivalentPropertyDecode(int second, int first) {
        EquivalentPropertyMapDecode.put(second,first);
    }

    @Override
    public String toString() {
        return "EquivalentPropertyMap{}";
    }

    public static void setEquivalentProperty(int first, int second) {
        EquivalentPropertyMap.put(first,second);
    }
}
