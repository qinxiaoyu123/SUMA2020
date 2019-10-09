package com.tju.gowl.dictionary;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.tju.gowl.bean.RdfDataBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Dictionary {
    private static Integer indexEncode = 2;
    private static Integer indexEncodeList = 1;
    private static final Map<String, Integer> Encode=new ConcurrentHashMap<>();

//    private static final List<List<String>> EncodeArry = new ArrayList<>();
//    //TODO 换成array
//    private static final Map<Integer, String> Decode=new ConcurrentHashMap<>();
//    public static Map<String, Integer> getEncode(){ return Encode; }
//    public static Map<Integer, String> getDecode(){ return Decode; }

    private static final List<String> EncodeList=new ArrayList<>();

    public static List<String> getEncodeList(){ return EncodeList; }

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

        Encode.put("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>",null);
//        Decode.put(0,"<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>");
        Encode.put("owl:Thing",null);
//        Decode.put(1,"owl:Thing");

        EncodeList.add("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>");
        EncodeList.add("owl:Thing");

    }
//    public static int encodeRdf(String ss) {
//        int ssIndex;
//        if(!Encode.containsKey(ss)){
//            Encode.put(ss, indexEncode);
//            Decode.put(indexEncode, ss);
//            ssIndex = indexEncode;
//            indexEncode ++;
//        }
//        else{
//            ssIndex = Encode.get(ss);
//        }
//        return ssIndex;
//    }

    public static int encodeRdfList(String ss) {
        int index = getEncodeList().indexOf(ss);
        if(index == -1){
            EncodeList.add(ss);
            indexEncodeList++;
            return indexEncodeList;
        }
        else{
            return index;
        }

    }


//    public static int encodeNsp(String ss) {
//        int ssIndex;
//        if(!EncodeNsp.containsKey(ss)){
//            EncodeNsp.put(ss, indexNsp);
//            DecodeNsp.put(indexNsp, ss);
//            ssIndex = indexNsp;
//            indexNsp ++;
//        }
//        else{
//            ssIndex = EncodeNsp.get(ss);
//        }
//        return ssIndex;
//    }
//
//    public static int encodeNpo(String ss) {
//        int ssIndex;
//        if(!EncodeNpo.containsKey(ss)){
//            EncodeNpo.put(ss, indexNpo);
//            DecodeNpo.put(indexNpo, ss);
//            ssIndex = indexNpo;
//            indexNpo ++;
//        }
//        else{
//            ssIndex = EncodeNpo.get(ss);
//        }
//        return ssIndex;
//    }

//    public static void decodeRdf(String subject, String predicate, RDFNode object) {
//    }
}
