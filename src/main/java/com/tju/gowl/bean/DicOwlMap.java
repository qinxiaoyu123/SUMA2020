package com.tju.gowl.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DicOwlMap {
    private static final Map<String, List<DicOwlBean>> DicRuleMap = new ConcurrentHashMap<>();
    public static final Map<Integer, List<DicOwlBean>> EquiDicRuleMap = new ConcurrentHashMap<>();

    public static Map<Integer, List<DicOwlBean>> getEquiDicRuleMap() {
        return EquiDicRuleMap;
    }

    //    private static final Map<String, String> InverseProperty = new ConcurrentHashMap<>();
//    private static final Map<String, Integer> TransitiveProperty = new ConcurrentHashMap<>();
    public static Map<String, List<DicOwlBean>> getRuleMap(){ return DicRuleMap; }
//    public static Map<String, String> getInverseProperty(){ return InverseProperty; }
//    public static Map<String, Integer> getTransitiveProperty(){ return TransitiveProperty; }

    public static void addEquiDicRuleMap(int class1, int type, int class2) {
        if (class2 == 0 ) return;
        if(type == 2002){//subClassOf
            if(EquiDicRuleMap.containsKey(class1)){
                DicOwlBean bean = new DicOwlBean();
                bean.setType(2002);
                bean.setRuleHead(class2);
                EquiDicRuleMap.get(class1).add(bean);
            }
            else{
                System.out.println("addEquiDicRuleMap_error");
            }
        }

    }

    public static void addEquiDicRuleMap(int class1, int type, int cardinality, int propertyInt, int class2Int) {
        if(type == 3008){//min
            if(DicOwlMap.EquiDicRuleMap.containsKey(class1)){
                DicOwlBean bean = new DicOwlBean();
                bean.setType(3008);
                bean.setRuleHead(cardinality, propertyInt, class2Int);
                DicOwlMap.EquiDicRuleMap.get(class1).add(bean);
            }
        }

    }

    public static void addEquiDicRuleMap(int class1, int type, int propertyInt, int fillterInt) {
        if(type == 3005){//someVauleFrom
            if(EquiDicRuleMap.containsKey(class1)){
                DicOwlBean bean = new DicOwlBean();
                bean.setType(3005);
                bean.setRuleHead(propertyInt, fillterInt);
                EquiDicRuleMap.get(class1).add(bean);
            }
            else{
                System.out.println("addEquiDicRuleMap_error");
            }
        }
        else if(type == 3006){//allVauleFrom
            if(EquiDicRuleMap.containsKey(class1)){
                DicOwlBean bean = new DicOwlBean();
                bean.setType(3006);
                bean.setRuleHead(propertyInt, fillterInt);
                EquiDicRuleMap.get(class1).add(bean);
            }
            else{
                System.out.println("addEquiDicRuleMap_error");
            }
        }
        else{
            if(EquiDicRuleMap.containsKey(class1)){
                DicOwlBean bean = new DicOwlBean();
                bean.setType(type);
                bean.setRuleHead(propertyInt, fillterInt);
                EquiDicRuleMap.get(class1).add(bean);
            }
            else{
                System.out.println("addEquiDicRuleMap_error");
            }
        }
    }
    public static void addDicOwlMap(int type, int pro) {
        if(type==2019 || type==2017|| type==2016|| type==2015) {
            //TransitiveObjectProperty 2019
            DicOwlBean dicOwlBean = new DicOwlBean();
            dicOwlBean.setType(type);
            dicOwlBean.setRuleHead(pro);
            StringBuffer ssbuff = new StringBuffer("*");
            String key = ssbuff.append(pro).append("*").toString();

            addDicOwlMap(dicOwlBean, key);
        }

    }
    public static void addDicOwlMap(int type, int class1, int class2) {


        DicOwlBean dicOwlBean = new DicOwlBean();
        dicOwlBean.setType(type);
        dicOwlBean.setRuleHead(class2);
        if (type == 2002){ //subclassof
            if(class2 == 0) return;
            StringBuffer ssbuff = new StringBuffer("*0");
            String key = ssbuff.append(class1).toString();
            addDicOwlMap(dicOwlBean, key);

        }
       else{//ObjectPropertyRange  ObjectPropertyDomain SubObjectPropertyOf
            StringBuffer ssbuff = new StringBuffer("*");
            String key = ssbuff.append(class1).append("*").toString();
            addDicOwlMap(dicOwlBean, key);
        }


    }
    public static void addDicOwlMap(int type, int class1, int r, int class3) {
//        if(type == 5){
//
////            DicOwlBean dicOwlBean = new DicOwlBean();
////            dicOwlBean.setType(6);
////            dicOwlBean.setRuleHead(class1,class3);
////            StringBuffer ssbuff = new StringBuffer("*");
////            String key = ssbuff.append(r).append("*").toString();
//
////            addDicOwlMap(dicOwlBean, key);
//            ssbuff.setLength(0);
//            DicOwlBean dicOwlBean1 = new DicOwlBean();
//            dicOwlBean1.setType(7);
//            dicOwlBean1.setRuleHead(class1,r);
//            ssbuff.append("*0");
//            String key1 = ssbuff.append(class3).toString();
//
//            addDicOwlMap(dicOwlBean1, key1);
//        }
//        else{
            //somevaluefromm
            DicOwlBean dicOwlBean = new DicOwlBean();
            dicOwlBean.setType(type);
            dicOwlBean.setRuleHead(r,class3);
            StringBuffer ssbuff = new StringBuffer("*0");
            String key = ssbuff.append(class1).toString();

            addDicOwlMap(dicOwlBean, key);
//        }

    }
    public static void addDicOwlMap(int type, int class1, int class2, int r, int class3) {
         if(type == 3008){//minCardinality  class1, cardinality, propertyInt, class2Int
            DicOwlBean dicOwlBean = new DicOwlBean();
            dicOwlBean.setType(3008);//class2
            dicOwlBean.setRuleHead(class2,r,class3);
            StringBuffer ssbuff = new StringBuffer("*0");
            String key = ssbuff.append(class1).toString();
            addDicOwlMap(dicOwlBean, key);
        }

    }

    public static void addDicOwlMap(DicOwlBean dicOwlBean, String key) {
        if (DicRuleMap.containsKey(key)) {
            List<DicOwlBean> lists = DicRuleMap.get(key);
            Iterator<DicOwlBean> iterator1 = lists.iterator();
            while(iterator1.hasNext()){
                DicOwlBean aa = iterator1.next();
                if(aa.equals(dicOwlBean)){
                    return;
                }
            }
            DicRuleMap.get(key).add(dicOwlBean);
        } else {
            List<DicOwlBean> listOwlBean = new ArrayList<>();
            listOwlBean.add(dicOwlBean);
            DicRuleMap.put(key, listOwlBean);
        }
    }


//    public static void addRuleMap(int class1, int type, int class2) {
//        if(type == 2002){
//            DicOwlBean dicOwlBean = new DicOwlBean();
//            dicOwlBean.setType(10);//equi
//            dicOwlBean.setRuleHead(class1);
//            StringBuffer ssbuff = new StringBuffer("*0");
//            String key = ssbuff.append(class2).toString();
//            addDicOwlMap(dicOwlBean, key);
//        }
//        else if(type == 3005||type == 3008){
//            DicOwlBean dicOwlBean = new DicOwlBean();
//            dicOwlBean.setType(10);//equi
//            dicOwlBean.setRuleHead(class1);
//            StringBuffer ssbuff = new StringBuffer("*");
//            String key = ssbuff.append(class2).append("*").toString();
//            addDicOwlMap(dicOwlBean, key);
//        }
//        else{
//            DicOwlBean dicOwlBean = new DicOwlBean();
//            dicOwlBean.setType(10);//equi
//            dicOwlBean.setRuleHead(class1);
//            StringBuffer ssbuff = new StringBuffer("*");
//            String key = ssbuff.append(class2).append("*").toString();
//            addDicOwlMap(dicOwlBean, key);
//        }
//    }
}
