package com.tju.gowl.materialization;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CodeRepository {
    private static int indexEncode = 2;
    private static final Map<String, Integer> EN_CODE = new ConcurrentHashMap<>();
    //TODO 换成array
    private static final Map<Integer, String> DE_CODE = new ConcurrentHashMap<>();

    public  Map<String, Integer> getEncode() {
        return EN_CODE;
    }

    public  Map<Integer, String> getDecode() {
        return DE_CODE;
    }

    public  void increaseIndexEncode(){
        indexEncode++;
    }

    public int getIndexEncode(){
        return indexEncode;
    }

    public static void init(){
        EN_CODE.put("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>",0);
        DE_CODE.put(0,"<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>");
        EN_CODE.put("owl:Thing",1);
        DE_CODE.put(1,"owl:Thing");
    }
}
