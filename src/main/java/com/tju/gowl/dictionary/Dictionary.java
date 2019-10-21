package com.tju.gowl.dictionary;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Dictionary {

    private static Integer indexEncode = 2;
    private static final Map<String, Integer> Encode = new ConcurrentHashMap<>();
    //TODO 换成array
    private static final List<String> Decode = new ArrayList<>();

    public static Map<String, Integer> getEncode() {
        return Encode;
    }

    public static List<String> getDecode() {
        return Decode;
    }

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

        Encode.put("#type", 0);
        Decode.add("#type");
        Encode.put("owl:Thing", 1);
        Decode.add("owl:Thing");
    }




    public static final Map<String, HashSet<String>> hashMap = new HashMap<>();
    public static final HashSet<String> hashSet = new HashSet<>();

//    public static int readInHash(String value) {
//        String lineNew = splitLine(value);
//        int hashInt = hash(lineNew);
////        int hashInt = hash(value);
//        if(hashInt < 0){
//            hashInt = -hashInt ;
//        }
////        System.out.println("hashInt "+hashInt);
//        int i;
//        i = hashInt % (length - 1);
//        while(hashArray[i] != null ){
//            if(hashArray[i].equals(value)){
//                return i;
//            }
//            else{
//                hashSet.add(value);
//                i = i+2;
//                if(i == length){
//                    i = 0;
//                }
//            }
//        }
//        hashArray[i] = value;
//        return i;
//    }

    private static String splitLine(String value) {
        StringBuffer ss = new StringBuffer();
        if(value.contains("#")){
            String valueNew = value.substring(1,value.length()-1);
            StringTokenizer st = new StringTokenizer(valueNew, "#");
            List<String> list = new ArrayList<>(2);
            while (st.hasMoreElements()) {
                list.add(st.nextToken());
            }
            return ss.append("#").append(list.get(1)).toString();
        }
        else if(value.contains("http://www.")){
            return ss.append("*").append(value.substring(12,value.length()-1)).toString();
        }
        else return value;
    }


    static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    public static int ELFHash(String strUri) {
        int hash = 0;
        long x = 0;
        for (int i = 0; i < strUri.length(); i++) {
            hash = (hash << 4) + strUri.charAt(i);
            if ((x = hash & 0xF0000000L) != 0) {
                hash ^= (x >> 24);
                hash &= ~x;
            }
        }
        return (hash & 0x7FFFFFFF);
    }

    public static int readInHash(String value) {
        String ss = splitLine(value);
        int ssIndex;
        if (!Encode.containsKey(ss)) {
            Encode.put(ss, indexEncode);
            Decode.add(ss);
            ssIndex = indexEncode;
            indexEncode++;
        } else {
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
