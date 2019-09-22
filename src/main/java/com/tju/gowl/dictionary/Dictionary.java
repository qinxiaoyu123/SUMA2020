package com.tju.gowl.dictionary;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.tju.gowl.bean.RdfDataBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Dictionary {
    private static Integer indexEncode = 1;
    private static final Map<String, Integer> Encode=new ConcurrentHashMap<>();
    //TODO 换成array
    private static final Map<Integer, String> Decode=new ConcurrentHashMap<>();
    public static Map<String, Integer> getEncode(){ return Encode; }
    public static Map<Integer, String> getDecode(){ return Decode; }

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
        Decode.put(0,"<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>");
    }
    public static int encodeRdf(String ss) {
        int ssIndex;
        if(!Encode.containsKey(ss)){
            Encode.put(ss, indexEncode);
            Decode.put(indexEncode, ss);
            ssIndex = indexEncode;
            indexEncode ++;
        }
        else{
            ssIndex = Encode.get(ss);
        }
        return ssIndex;
    }

    public static int encodeRdf(String ss, String tbox) {
        int ssIndex;
        if(!Encode.containsKey(ss)){
            Encode.put(ss, indexEncode);
            Decode.put(indexEncode, ss);
            ssIndex = indexEncode;
            indexEncode ++;
//            System.out.println("only appear at TBox"+ss);
        }
        else{
            ssIndex = Encode.get(ss);
        }
        return ssIndex;
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
