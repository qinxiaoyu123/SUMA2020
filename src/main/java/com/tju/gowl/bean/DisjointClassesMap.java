package com.tju.gowl.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DisjointClassesMap {
    private static final Map<Integer, Integer> DisjointClassesMap = new ConcurrentHashMap<>();

    public static void setDisjointClassesMap(int first, int second) {
        DisjointClassesMap.put(first,second);
    }

    public static int getDisjointClassesMap(int first) {
        if(DisjointClassesMap.containsKey(first)){
            return DisjointClassesMap.get(first);
        }
        else{
            return -1;
        }
    }
}
