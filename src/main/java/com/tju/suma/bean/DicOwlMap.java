package com.tju.suma.bean;

import com.tju.suma.axiomProcessor.Processor;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    public static void addEquiSubClassOfRuleMap(int class1, int subClassOf, int class2) {
        DicOwlBean bean = new DicOwlBean();
        bean.setType(subClassOf);
        bean.setRuleHead(class2);
        EquiDicRuleMap.get(class1).add(bean);
    }

    public static void addDicOwlPropertyRangeMap(int type, int pro, int ran) {
        Pair<Integer, Integer> pair = rewriteProperty(type, pro);
        DicOwlBean dicOwlBean = new DicOwlBean();
        dicOwlBean.setType(pair.getValue());
        dicOwlBean.setRuleHead(ran);
        StringBuffer ssbuff = new StringBuffer("*");
        String key = ssbuff.append(pair.getKey()).append("*").toString();
        addDicOwlMap(dicOwlBean, key);

    }

    public static void addDicOwlInversePropertyMap(int type, int pro, int ran) {
        DicOwlBean dicOwlBean = new DicOwlBean();
        dicOwlBean.setType(type);
        dicOwlBean.setRuleHead(ran);
        StringBuffer ssbuff = new StringBuffer("*");
        String key = ssbuff.append(pro).append("*").toString();
        addDicOwlMap(dicOwlBean, key);
    }

    public static void addDicOwlEquivalentPropertyMap(int type, int pro, int ran) {
        DicOwlBean dicOwlBean = new DicOwlBean();
        dicOwlBean.setType(type);
        dicOwlBean.setRuleHead(ran);
        StringBuffer ssbuff = new StringBuffer("*");
        String key = ssbuff.append(pro).append("*").toString();
        addDicOwlMap(dicOwlBean, key);
    }

    public static void addDicOwlPropertyDomainMap(int type, int pro, int dom) {
        Pair<Integer, Integer> pair = rewriteProperty(type, pro);
        DicOwlBean dicOwlBean = new DicOwlBean();
        dicOwlBean.setType(pair.getValue());
        dicOwlBean.setRuleHead(dom);
        StringBuffer ssbuff = new StringBuffer("*");
        String key = ssbuff.append(pair.getKey()).append("*").toString();
        addDicOwlMap(dicOwlBean, key);
    }

    public static void addDicOwlObjectSomeValuesMap(int type, int class1, int propertyInt, int fillterInt) {
        DicOwlBean dicOwlBean = new DicOwlBean();
        Pair<Integer, Integer> pair = rewriteProperty(type, propertyInt);
        dicOwlBean.setType(pair.getValue());
        dicOwlBean.setRuleHead(pair.getKey(),fillterInt);
        StringBuffer ssbuff = new StringBuffer("*0");
        String key = ssbuff.append(class1).toString();
        addDicOwlMap(dicOwlBean, key);
    }
    public static void addDicOwlObjectHasValueMap(int type, int class1, int propertyInt, int fillterInt) {
        DicOwlBean dicOwlBean = new DicOwlBean();
        Pair<Integer, Integer> pair = rewriteProperty(type, propertyInt);
        dicOwlBean.setType(pair.getValue());
        dicOwlBean.setRuleHead(pair.getKey(),fillterInt);
        StringBuffer ssbuff = new StringBuffer("*0");
        String key = ssbuff.append(class1).toString();
        addDicOwlMap(dicOwlBean, key);
    }
    public static void addEquiDicSomeValuesMap(int class1, int type, int propertyInt, int class2Int) {
        DicOwlBean dicOwlBean = new DicOwlBean();
        Pair<Integer, Integer> pair = rewriteProperty(type, propertyInt);
        dicOwlBean.setType(pair.getValue());
        dicOwlBean.setRuleHead(pair.getKey(),class2Int);
        EquiDicRuleMap.get(class1).add(dicOwlBean);
    }



    private static Pair<Integer, Integer> rewriteProperty(int type, int propertyInt) {
        if(Processor.isRoleWriting == false) return new Pair<>(propertyInt, type);
        if(EquivalentPropertyMap.EquivalentPropertyMap.containsKey(propertyInt)){
            propertyInt = EquivalentPropertyMap.EquivalentPropertyMap.get(propertyInt);
        }
        if(InversePropertyMap.InverseMap.containsKey(propertyInt)){
            propertyInt = InversePropertyMap.InverseMap.get(propertyInt);
            type = Processor.typeInverse.get(type);
        }
        Pair<Integer, Integer> pair = new Pair<>(propertyInt, type);
        return pair;
    }

    public static void addDicOwlSubCLassMap(int type, int sub, int sup) {
        DicOwlBean dicOwlBean = new DicOwlBean();
        dicOwlBean.setType(type);
        dicOwlBean.setRuleHead(sup);
//        if(class2 == 0) return;
        StringBuffer ssbuff = new StringBuffer("*0");
        String key = ssbuff.append(sub).toString();
        addDicOwlMap(dicOwlBean, key);
    }

    public static void addDicOwlSubPropertyMap(int type, int sub, int sup) {
        if(InversePropertyMap.InverseMap.containsKey(sub)){
            if(InversePropertyMap.InverseMap.containsKey(sup)){
                DicOwlMap.addDicOwlMap(type, InversePropertyMap.InverseMap.get(sub), InversePropertyMap.InverseMap.get(sup));
            }else{
                System.out.println("未处理逆角色");
            }
        }
        else if(InversePropertyMap.InverseMap.containsKey(sup)){
            System.out.println("未处理逆角色");
        }
        else{
            DicOwlMap.addDicOwlMap(type, sub, sup);
        }
    }

    public static void addDicOwlSymmetricPropertyMap(int type, int pro) {
        Pair<Integer, Integer> pair = rewriteProperty(type, pro);
        DicOwlBean dicOwlBean = new DicOwlBean();
        dicOwlBean.setType(pair.getValue());
        dicOwlBean.setRuleHead(pair.getKey());
        StringBuffer ssbuff = new StringBuffer("*");
        String key = ssbuff.append(pro).append("*").toString();
        addDicOwlMap(dicOwlBean, key);
    }

    public static void addDicOwlTransitivePropertyMap(int type, int pro) {
        Pair<Integer, Integer> pair = rewriteProperty(type, pro);
        DicOwlBean dicOwlBean = new DicOwlBean();
        dicOwlBean.setType(pair.getValue());
        dicOwlBean.setRuleHead(pair.getKey());
        StringBuffer ssbuff = new StringBuffer("*");
        String key = ssbuff.append(pro).append("*").toString();
        addDicOwlMap(dicOwlBean, key);
    }

    public static void addDicOwlInverseFunctionalMap(int type, int pro) {
        Pair<Integer, Integer> pair = rewriteProperty(type, pro);
        DicOwlBean dicOwlBean = new DicOwlBean();
        dicOwlBean.setType(pair.getValue());
        dicOwlBean.setRuleHead(pair.getKey());
        StringBuffer ssbuff = new StringBuffer("*");
        String key = ssbuff.append(pro).append("*").toString();
        addDicOwlMap(dicOwlBean, key);
    }

    public static void addDicOwlFunctionalPropertyMap(int type, int pro) {
        Pair<Integer, Integer> pair = rewriteProperty(type, pro);
        DicOwlBean dicOwlBean = new DicOwlBean();
        dicOwlBean.setType(pair.getValue());
        dicOwlBean.setRuleHead(pair.getKey());
        StringBuffer ssbuff = new StringBuffer("*");
        String key = ssbuff.append(pro).append("*").toString();
        addDicOwlMap(dicOwlBean, key);
    }

    public static void addDicOwlObjectAllValuesMap(int type, int class1, int propertyInt, int classAllValues) {
        DicOwlBean dicOwlBean = new DicOwlBean();
        Pair<Integer, Integer> pair = rewriteProperty(type, propertyInt);
        dicOwlBean.setType(pair.getValue());
        dicOwlBean.setRuleHead(pair.getKey(),classAllValues);
        StringBuffer ssbuff = new StringBuffer("*0");
        String key = ssbuff.append(class1).toString();
        addDicOwlMap(dicOwlBean, key);
    }

    public static void addEquiDicAllVauleMap(int class1, int type, int propertyInt, int classAllValues) {
        Pair<Integer, Integer> pair = rewriteProperty(type, propertyInt);
        DicOwlBean bean = new DicOwlBean();
        bean.setType(pair.getValue());
        bean.setRuleHead(pair.getKey(), classAllValues);
        EquiDicRuleMap.get(class1).add(bean);
    }

    public static void addDicOwlMinCardinalityMap(int type, int class1, int cardinality, int propertyInt, int class2Int) {
        DicOwlBean dicOwlBean = new DicOwlBean();
        Pair<Integer, Integer> pair = rewriteProperty(type, propertyInt);
        dicOwlBean.setType(pair.getValue());//class2
        dicOwlBean.setRuleHead(cardinality,pair.getKey(),class2Int);
        StringBuffer ssbuff = new StringBuffer("*0");
        String key = ssbuff.append(class1).toString();
        addDicOwlMap(dicOwlBean, key);
    }

    public static void addEquiMinCardinalityMap(int class1, int type, int cardinality, int propertyInt, int class2Int) {
        DicOwlBean bean = new DicOwlBean();
        Pair<Integer, Integer> pair = rewriteProperty(type, propertyInt);
        bean.setType(pair.getValue());
        bean.setRuleHead(cardinality, pair.getKey(), class2Int);
        DicOwlMap.EquiDicRuleMap.get(class1).add(bean);
    }

    public static void addDicOwlRileAllValuesMap(int type, int class1, int propertyInt, int classAllValues) {
//        Pair<Integer, Integer> pair = rewriteProperty(type, propertyInt);
        DicOwlBean dicOwlBean = new DicOwlBean();
//        Pair<Integer, Integer> pair = rewriteProperty(type, propertyInt);
        dicOwlBean.setType(55);
        dicOwlBean.setRuleHead(class1,classAllValues);
        StringBuffer ssbuff = new StringBuffer("*");
        String key = ssbuff.append(propertyInt).append("*").toString();
        addDicOwlMap(dicOwlBean, key);
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
