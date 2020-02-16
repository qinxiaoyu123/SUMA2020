package com.tju.suma.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InversePropertyMap {
    static int inverseCount = 0;
//    private static final Map<Integer, InversePropertyBean> InversePropertyMap = new ConcurrentHashMap<>();
//    private static final Map<String, Integer> PropertyMap = new ConcurrentHashMap<>();
    public static final Map<Integer, Integer> InverseMap = new ConcurrentHashMap<>();
    public static final Map<Integer, Integer> InverseMapDecode = new ConcurrentHashMap<>();
    public static final List<Integer> InverseProperty = new ArrayList<>();


//    public static Map<Integer, InversePropertyBean> getInversePropertyMap(){ return InversePropertyMap; }
    public static Map<Integer, Integer> getInverseMap(){ return InverseMap; }
    public static Map<Integer, Integer> getInverseMapDecode(){ return InverseMapDecode; }


//    private static final Map<Integer, Integer> InversePropertyMap1 = new ConcurrentHashMap<>();
//    public static Map<Integer, Integer> getInverseMap1(){ return InversePropertyMap1; }

    @Override
    public String toString() {
        return "InversePropertyMap{}";
    }

//    public static void InversePropertyProcess(OWLOntology univBench) {
//        Iterator localIterator = univBench.getAxioms().iterator();
//        //第一遍循环找出所有逆属性
//        while(localIterator.hasNext()) {
//            OWLAxiom axiom = (OWLAxiom) localIterator.next();
//            if (axiom instanceof OWLInverseObjectPropertiesAxiom) {
//                System.out.println(axiom.toString());
//                String first = ((OWLInverseObjectPropertiesAxiom) axiom).getFirstProperty().toString();
//                String second = ((OWLInverseObjectPropertiesAxiom) axiom).getSecondProperty().toString();
//
//                InversePropertyBean inverse = new InversePropertyBean();
//                inverse.setInverseProperty(second);
//                inverse.setCount(0);
//                PropertyMap.put(first, inverseCount);
//                InversePropertyMap.put(inverseCount,inverse);
//                inverseCount++;
//
//                InversePropertyBean inverse1 = new InversePropertyBean();
//                inverse1.setInverseProperty(first);
//                inverse1.setCount(0);
//                PropertyMap.put(second, inverseCount);
//                InversePropertyMap.put(inverseCount,inverse1);
//                inverseCount++;
//
//            }
//
//        }
//        System.out.println(PropertyMap);
//        System.out.println(InversePropertyMap);
//        //第二遍循环统计所有逆属性相关公理
//        localIterator = univBench.getAxioms().iterator();
//        while(localIterator.hasNext()) {
//            OWLAxiom axiom = (OWLAxiom) localIterator.next();
//            if(axiom instanceof OWLPropertyDomain || axiom instanceof OWLPropertyRange||!(axiom instanceof OWLLogicalAxiom)){
//                continue;
//            }
//            String ss = axiom.toString();
//            Iterator<Map.Entry<String, Integer>> entries = PropertyMap.entrySet().iterator();
//            while(entries.hasNext()){
//                Map.Entry<String, Integer> entry = entries.next();
//                String ss1 = entry.getKey();
//                if(ss.contains(ss1)){
//
//                    System.out.println(ss);
//                    int index = entry.getValue();
//                    int count1 = InversePropertyMap.get(index).getCount();
//                    InversePropertyMap.get(index).setCount(++count1);
//                }
//            }
//        }
//        System.out.println(PropertyMap);
//        System.out.println(InversePropertyMap);
//        int encode1;
//        int encode2;
//        Iterator<Map.Entry<String, Integer>> entries = PropertyMap.entrySet().iterator();
//        while(entries.hasNext()){
//            Map.Entry<String, Integer> entry = entries.next();
//            InversePropertyBean node = InversePropertyMap.get(entry.getValue());
//            int count1 = node.getCount();
//            String propertyInverse = node.getInverseProperty();
//            int count2 = InversePropertyMap.get(PropertyMap.get(propertyInverse)).getCount();
//            if(count1 >= count2){
//                System.out.println(propertyInverse);
//                System.out.println(entry.getKey());
//                encode1 = Dictionary.encodeRdf(propertyInverse,"tbox");
//                encode2 = Dictionary.encodeRdf(entry.getKey(),"tbox");
//                InverseMap.put(encode2, encode1);
//            }
//        }
//        System.out.println(InverseMap);
//    }
//    public static void writeUOBMInverseMap(OWLOntology univBench) {
//        int encode1;
//        int encode2;
//        Map<String, String> InverseStringMap = new ConcurrentHashMap<>();
//        InverseStringMap.put("<http://semantics.crl.ibm.com/univ-bench-dl.owl#hasMember>","<http://semantics.crl.ibm.com/univ-bench-dl.owl#isMemberOf>");
//        InverseStringMap.put("<http://semantics.crl.ibm.com/univ-bench-dl.owl#hasStudent>","<http://semantics.crl.ibm.com/univ-bench-dl.owl#isStudentOf>");
//        InverseStringMap.put("<http://semantics.crl.ibm.com/univ-bench-dl.owl#hasAlumnus>","<http://semantics.crl.ibm.com/univ-bench-dl.owl#hasDegreeFrom>");
//        InverseStringMap.put("<http://semantics.crl.ibm.com/univ-bench-dl.owl#teacherOf>","<http://semantics.crl.ibm.com/univ-bench-dl.owl#isTaughtBy>");
//        Iterator<Map.Entry<String, String>> entries = InverseStringMap.entrySet().iterator();
//        while(entries.hasNext()){
//            Map.Entry<String, String> entry = entries.next();
//            encode1 = Dictionary.encodeRdf(entry.getKey(),"tbox");
//            encode2 = Dictionary.encodeRdf(entry.getValue(),"tbox");
//            InverseMap.put(encode1, encode2);
//            InverseMapDecode.put(encode2, encode1);
//        }
////        System.out.println(InverseMap);
//    }

//    public static void setInverseProperty(int first, int second) {
//        if(!InversePropertyMap.containsKey(first)){
//            //replace first with second
//            InversePropertyMap.put(first, second);
//            InversePropertyMap1.put(second, first);
//        }
//        else{
//            System.out.println("inverse error");
//        }
//    }
    public static int getInverseProperty(int first) {
        if(InverseMap.containsKey(first)){
            //replace first with second
            return InverseMap.get(first);
        }
        else{
            return -1;
        }
    }
//    public static void rewriteInverseRule(){
//        Iterator<Map.Entry<Integer, Integer>> entries = InverseMap.entrySet().iterator();
//        Map<String, List<DicOwlBean>> totalRule = DicOwlMap.getRuleMap();
////        int loop = 0;
//        StringBuffer ssbuff = new StringBuffer();
//        while (entries.hasNext()) {
////            loop++;
////            System.out.println("loop"+loop);
//            Map.Entry<Integer, Integer> entry = entries.next();
//            Integer data1 = entry.getKey();
//            Integer data2 = entry.getValue();
//            ssbuff.setLength(0);
//            String key1 = ssbuff.append("*").append(data1).append("*").toString();
//            ssbuff.setLength(0);
//            String key2 = ssbuff.append("*").append(data2).append("*").toString();
//
//
//            if (totalRule.containsKey(key1)) {
//                List<DicOwlBean> OwlRule = totalRule.get(key1);
////                System.out.println(key1+OwlRule);
//                for (DicOwlBean typeHead : OwlRule) {
//                    int type = typeHead.getType();
//                    System.out.println(type);
//                    List<Integer> head = typeHead.getRuleHead();
////                    if (type == 2023) {
////                        DicOwlMap.addDicOwlMap(2022, data2, head.get(0));
////                    } else if (type == 2022) {
////                        DicOwlMap.addDicOwlMap(2023, data2, head.get(0));
////                    }
//                }
//            }
//            if (totalRule.containsKey(key2)) {
//                List<DicOwlBean> OwlRule = totalRule.get(key2);
////                System.out.println(key2+OwlRule);
//            }
//        }
//
//    }

}
