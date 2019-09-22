package com.tju.gowl.bean;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RdfDataMap {
    private static final Map<Integer,RdfDataBean> dataMap=new ConcurrentHashMap<>();

    //Isp Nsp
    private static final Map<String,Integer> dataIndex=new ConcurrentHashMap<>();
    //Ipo Npo
    private static final Map<String,Integer> dataIndexIpo=new ConcurrentHashMap<>();
    private static final Map<String, List<String>> ruleMap =new ConcurrentHashMap<>();
    private static final Map<Integer,RdfDataBean> stashMap=new ConcurrentHashMap<>();
    private static final Map<Integer,RdfDataBean> iteratorMap=new ConcurrentHashMap<>();


    private RdfDataMap(){}
    public static Map<Integer,RdfDataBean> getDataMap(){
        return dataMap;
    }

    public static Map<Integer,RdfDataBean> getstashMap(){ return stashMap; }
    public static Map<Integer,RdfDataBean> getiteratorMap(){ return iteratorMap; }

    public static Map<String,Integer> getDataIndex(){
        return dataIndex;
    }
    public static Map<String, List<String>> getRuleMap(){ return ruleMap; }

    public static Map<String, Integer> getDataIndexIpo() { return dataIndexIpo; }
}
