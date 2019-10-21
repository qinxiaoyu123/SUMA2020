package com.tju.gowl.dictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Dictionary {
    private static Integer indexEncode = 2;
    private static final Map<String, Integer> Encode=new ConcurrentHashMap<>();
    //TODO 换成array
    private static final List<String> Decode=new ArrayList<>();
    public static Map<String, Integer> getEncode(){ return Encode; }
    public static List<String> getDecode(){ return Decode; }

//    private static Integer indexNsp = 0;
//    private static final Map<String, Integer> EncodeNsp=new ConcurrentHashMap<>();
//    //TODO 换成array
//    private static final Map<Integer, String> DecodeNsp=new ConcurrentHashMap<>();
//    public static Map<String, Integer> getEncodeNsp(){ return EncodeNsp; }
//    public static Map<Integer, String> getDecodeNsp(){ return DecodeNsp; }
//
//    private static Integer indexNpo = 0;
//    private static final Map<String, Integer> EncodeNpo=new ConcurrentHashMap<>();
//    //TODO 换成array
//    private static final Map<Integer, String> DecodeNpo=new ConcurrentHashMap<>();
//    public static Map<String, Integer> getEncodeNpo(){ return EncodeNpo; }
//    public static Map<Integer, String> getDecodeNpo(){ return DecodeNpo; }

    public Dictionary() {

        Encode.put("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>",0);
        Decode.add("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>");
        Encode.put("owl:Thing",1);
        Decode.add("owl:Thing");
    }
    public static int encodeRdf(String ss) {
        int ssIndex;
        if(!Encode.containsKey(ss)){
            Encode.put(ss, indexEncode);
            Decode.add(ss);
            ssIndex = indexEncode;
            indexEncode ++;
        }
        else{
            ssIndex = Encode.get(ss);
        }
        return ssIndex;
    }

}
