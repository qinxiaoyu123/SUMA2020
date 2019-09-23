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
//    private static final Map<String, String> InverseProperty = new ConcurrentHashMap<>();
//    private static final Map<String, Integer> TransitiveProperty = new ConcurrentHashMap<>();
    public static Map<String, List<DicOwlBean>> getRuleMap(){ return DicRuleMap; }
//    public static Map<String, String> getInverseProperty(){ return InverseProperty; }
//    public static Map<String, Integer> getTransitiveProperty(){ return TransitiveProperty; }
    public static void addDicOwlMap(int type, int pro) {
        if(type==2019 || type==2017|| type==2016) {
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
        //somevaluefromm
        DicOwlBean dicOwlBean = new DicOwlBean();
        dicOwlBean.setType(type);
        dicOwlBean.setRuleHead(r,class3);
        StringBuffer ssbuff = new StringBuffer("*0");
        String key = ssbuff.append(class1).toString();

        addDicOwlMap(dicOwlBean, key);
    }
    public static void addDicOwlMap(int type, int class1, int class2, int r, int class3) {
        if(type == 2001){
            DicOwlBean dicOwlBean = new DicOwlBean();
            dicOwlBean.setType(0);//class2
            dicOwlBean.setRuleHead(class1,class2,r,class3);
            StringBuffer ssbuff = new StringBuffer("*0");
            String key = ssbuff.append(class2).toString();

            addDicOwlMap(dicOwlBean, key);

            DicOwlBean dicOwlBean1 = new DicOwlBean();
            dicOwlBean1.setType(1);//r
            dicOwlBean1.setRuleHead(class1,class2,r,class3);
            StringBuffer ssbuff1 = new StringBuffer("*");
            String key1 = ssbuff1.append(r).append("*").toString();

            addDicOwlMap(dicOwlBean1, key1);

            DicOwlBean dicOwlBean2 = new DicOwlBean();
            dicOwlBean2.setType(2);//class3
            dicOwlBean2.setRuleHead(class1,class2,r,class3);
            StringBuffer ssbuff2 = new StringBuffer("*0");
            String key2 = ssbuff2.append(class3).toString();

            addDicOwlMap(dicOwlBean2, key2);
        }
        else if(type == 3008){//minCardinality  class1, cardinality, propertyInt, class2Int
            DicOwlBean dicOwlBean = new DicOwlBean();
            dicOwlBean.setType(3008);//class2
            dicOwlBean.setRuleHead(class2,r,class3);
            StringBuffer ssbuff = new StringBuffer("*0");
            String key = ssbuff.append(class1).toString();
            addDicOwlMap(dicOwlBean, key);
        }

    }

    private static void addDicOwlMap(DicOwlBean dicOwlBean, String key) {
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


}
