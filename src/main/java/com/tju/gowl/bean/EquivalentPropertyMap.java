package com.tju.gowl.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EquivalentPropertyMap {
    private static final Map<Integer, Integer> EquivalentPropertyMap = new ConcurrentHashMap<>();
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

    @Override
    public String toString() {
        return "EquivalentPropertyMap{}";
    }

    public static void setEquivalentProperty(int first, int second) {
        EquivalentPropertyMap.put(first,second);
    }
}
