package com.tju.suma.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DisjointClassesMap {
    public static final Map<Integer, Integer> disjointClassesMap = new ConcurrentHashMap<>();

    public static void setDisjointClassesMap(int first, int second) {
        disjointClassesMap.put(first,second);
    }

    public static int getDisjointClassesMap(int first) {
        if(disjointClassesMap.containsKey(first)){
            return disjointClassesMap.get(first);
        }
        else{
            return -1;
        }
    }
}
