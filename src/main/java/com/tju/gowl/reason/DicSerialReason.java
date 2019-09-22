package com.tju.gowl.reason;

import com.tju.gowl.bean.*;
import com.tju.gowl.io.Input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DicSerialReason {
    private static int anonymous = -2;
    private static int typeEncode = 0;
    private static int someValue = 1;
    private static boolean someValueFlag = false;
    public static void reason(){
        int loopCount = 1;
        //遍历数据
        Map<Integer, DicRdfDataBean> totalData = DicRdfDataMap.getDicDataMap();
        //存放每次新生成的数据,每次循环结束把新数据copy到iteratorMap进行第二轮迭代
        Map<Integer,DicRdfDataBean> stashMap = DicRdfDataMap.getDicStashMap();
        //迭代数据，第二轮以后迭代 iteratorMap，每次结束把数据copy到totalData进行存储
        Map<Integer,DicRdfDataBean> iteratorMap = DicRdfDataMap.getDicIteratorMap();
        //规则
        Map<String, List<DicOwlBean>> totalRule = DicOwlMap.getRuleMap();
        //索引
        Map<Integer, List<IndexBean>> Isp = IndexMap.getIsp();
        Map<Integer, List<IndexBean>> Iop = IndexMap.getIop();

        while(true){
            //preDataCount = totalData.size();
            System.out.println("loopCount"+" "+loopCount+" "+"datacount"+" "+(totalData.size()+iteratorMap.size()));
            someValueFlag = false;
            Iterator<Map.Entry<Integer, DicRdfDataBean>> entries;
            if(loopCount ==1) {
                entries = totalData.entrySet().iterator();
            }
            else{
                entries = iteratorMap.entrySet().iterator();
            }
            try {
                while (entries.hasNext()) {
                    Map.Entry<Integer, DicRdfDataBean> entry = entries.next();
                    DicRdfDataBean rdfData = entry.getValue();
                    //TODO
                    List<String> ruleKey = new ArrayList<>();

                    int Rs = rdfData.getRs();
                    int Rp = rdfData.getRp();
                    int Ro = rdfData.getRo();

                    convertDataToRuleKey(ruleKey, Rs, Rp, Ro);

                    ruleKey.forEach(str -> {
                        if (totalRule.containsKey(str)) {
                            List<DicOwlBean> OwlRule = totalRule.get(str);
                            for (DicOwlBean typeHead : OwlRule) {
                                int type = typeHead.getType();
                                List<Integer> head = typeHead.getRuleHead();
                         /*   ObjectPropertyDomain 2022
                            ObjectPropertyRange 2023
                            ObjectSomeValuesFrom 3005
                            SubClassOf 2002
                            SubObjectPropertyOf 2013
                            SymmetricObjectProperty 2017 */
                                if (type == 2013) {//SubObjectPropertyOf 2013
                                    subPropertyReason(totalData, iteratorMap, stashMap, Isp, Iop, Rs, head, Ro);
                                }
                                else if(type == 2022){//ObjectPropertyDomain 2022
                                    basicReason(totalData, iteratorMap, stashMap, Isp, Iop, Rs, typeEncode, head);
                                }
                                else if(type == 2023){//ObjectPropertyRange 2023
                                    basicReason(totalData, iteratorMap, stashMap, Isp, Iop, Ro, typeEncode, head);
                                }
                                else if(type == 2002){//SubClassOf 2002
                                    basicReason(totalData, iteratorMap, stashMap, Isp, Iop, Rs, typeEncode, head);
                                }
                                else if(type == 3005){//ObjectSomeValuesFrom 3005
                                    objectSomeValuesFromReason(totalData, iteratorMap, stashMap, Isp, Iop, Rs, head);
                                }
                                else if(type == 0){//class 2 0
                                    equivalentClass2Reason(Isp, Iop, Rs, head);
                                }
                                else if(type == 1){//r 1
                                    equivalentRoleReason(Isp, Iop, Rs, Ro, head);
                                }
                                else if(type == 2){//class3 2
                                    equivalentClass3Reason(Isp, Iop, Rs, head);
                                }
                                else if(type == 2019){//Transitive2019
                                    transitiveReason(Isp, Iop, Rs, Rp, Ro);
                                }
                                else if(type == 3006){//ObjectAllValuesFrom 3006
                                    objectAllValuesFromReason(Isp, Iop, Rs, head);
                                }
                                else if(type == 3008){//ObjectMinCardinality
                                    objectMinCardinalityReason(totalData, iteratorMap, stashMap, Isp, Iop, Rs, head);
                                }
                                else if(type == -3006){//ObjectMinCardinality
                                    objectAllValuesInverseFromReason(Isp, Iop, Rs, head);
                                }
                                else if(type == 2017){//SymmetricObjectProperty
                                    symmetricObjectPropertyReason(Isp, Iop, Rs, Rp, Ro);
                                }

                            }
                        }
                        else {
                            //无对应规则
                        }
                    });
                }
            } catch (Exception e) {
                e .printStackTrace();
                System.err.println("happen error");
            }
            if(stashMap.size()==0){
                //没有新数据产生
                System.out.println("没有新数据产生");
                totalData.putAll(iteratorMap);
                stashMap.clear();
                iteratorMap.clear();
                break;
            }
            if(someValue>=7){
                System.out.println("7轮循环结束");
                System.out.println("someValue"+someValue);
                totalData.putAll(stashMap);
                totalData.putAll(iteratorMap);
                stashMap.clear();
                iteratorMap.clear();
                break;
            }
            totalData.putAll(iteratorMap);
            iteratorMap.clear();
            iteratorMap.putAll(stashMap);
            stashMap.clear();
//            System.out.println(iteratorMap);
//            System.out.println(stashMap);
            loopCount++;
        }
        System.out.println("total data size"+totalData.size());


    }

    private static void symmetricObjectPropertyReason(Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs, int rp, int ro) {
        if(!DicRdfDataMap.checkDuplicate(ro, rp, rs, isp)) {
            DicRdfDataMap.addNewRdfDataBean(isp, iop, ro, rp, rs);
        }

    }

    private static void objectAllValuesInverseFromReason(Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs, List<Integer> head) {
        int rp = head.get(0);
        int class2 = head.get(1);
        int firstTripleIop = IndexMap.getFirstIndexOpFromMap(rp, rs);
        if(firstTripleIop == -1){ return; }
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIop;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNop();
            int rs1 = dicDataBeanIterator.getRs();
            if(!DicRdfDataMap.checkDuplicate(rs1, typeEncode, class2, isp)) {
                //rs rp ro1
                DicRdfDataMap.addNewRdfDataBean(isp, iop, rs1, typeEncode, class2);
            }
        }while(indexNew != -1);
    }

    private static void objectMinCardinalityReason(Map<Integer, DicRdfDataBean> totalData, Map<Integer, DicRdfDataBean> iteratorMap, Map<Integer, DicRdfDataBean> stashMap, Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs, List<Integer> head) {
        int cardinality = head.get(0);
        int rp = head.get(1);
        int class2 = head.get(2);
        int firstTripleIsp = IndexMap.getFirstIndexSpFromMap(rs, rp);
        if(firstTripleIsp == -1){
            //add cardinality
            int i = 0;
            while(i<cardinality){
                addSomeValueFrom(isp, iop, rs, rp, class2);
                i++;
            }
        }
        else {
            //add （cardinality - exist）
            // count exist
            int exist = 0;
            DicRdfDataBean dicDataBeanIterator;
            int indexNew = firstTripleIsp;
            do{
                dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
                indexNew = dicDataBeanIterator.getNsp();
                int ro1 = dicDataBeanIterator.getRo();
                if(DicRdfDataMap.checkDuplicate(ro1, typeEncode, class2, isp)) {
                    //rs rp ro1
                    exist++;
                }
            }while(indexNew != -1);
            int i = 0;
            if((cardinality-exist) <= 0){return ;}
            while(i<(cardinality-exist)){
                addSomeValueFrom(isp, iop, rs, rp, class2);
                i++;
            }
        }
    }

    private static void addSomeValueFrom(Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs, int rp, int class2, int firstTripleIsp) {
        int ro = anonymous;
        if (!someValueFlag && rs < 0) {
            someValue++;
            someValueFlag = true;
        }
        DicRdfDataMap.addNewRdfDataBean(isp, iop, rs, rp, ro, firstTripleIsp);
        anonymous--;
        DicRdfDataMap.addNewRdfDataBean(isp, iop, ro, typeEncode, class2);
    }
    private static void addSomeValueFrom(Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs, int rp, int class2) {
        int ro = anonymous;
        if (!someValueFlag && rs < 0) {
            someValue++;
            someValueFlag = true;
        }
        DicRdfDataMap.addNewRdfDataBean(isp, iop, rs, rp, ro);
        anonymous--;
        DicRdfDataMap.addNewRdfDataBean(isp, iop, ro, typeEncode, class2);
    }
    private static void objectAllValuesFromReason(Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs, List<Integer> head) {
        int rp = head.get(0);
        int class2 = head.get(1);
        int firstTripleIsp = IndexMap.getFirstIndexSpFromMap(rs, rp);
        if(firstTripleIsp == -1){ return; }
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIsp;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNsp();
            int ro1 = dicDataBeanIterator.getRo();
            if(!DicRdfDataMap.checkDuplicate(ro1, typeEncode, class2, isp)) {
                //rs rp ro1
                DicRdfDataMap.addNewRdfDataBean(isp, iop, ro1, typeEncode, class2);
            }
        }while(indexNew != -1);
    }

    private static void transitiveReason(Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs, int rp, int ro) {
        int firstTripleIsp = IndexMap.getFirstIndexSpFromMap(ro, rp);
        if (firstTripleIsp == -1) return;
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIsp;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNsp();
            int ro1 = dicDataBeanIterator.getRo();
            if(!DicRdfDataMap.checkDuplicate(rs, rp, ro1, isp)) {
                //rs rp ro1
                DicRdfDataMap.addNewRdfDataBean(isp, iop, rs, rp, ro1);
            }
        }while(indexNew != -1);

    }

    private static void equivalentClass3Reason(Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int class2 = head.get(1);
        int r = head.get(2);
        int class3 = head.get(3);
        int firstTripleIop = IndexMap.getFirstIndexOpFromMap(r, ro);
        if(firstTripleIop == -1){ return; }

        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIop;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNop();
            int rs = dicDataBeanIterator.getRs();
            if(!DicRdfDataMap.checkDuplicate(rs, typeEncode, class1, isp)) {
                if(DicRdfDataMap.checkDuplicate(rs, typeEncode, class2, isp)){
                    //rs type class1
                    DicRdfDataMap.addNewRdfDataBean(isp, iop, rs, typeEncode, class1);
                }
            }
        }while(indexNew != -1);

    }

    private static void equivalentRoleReason(Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int class2 = head.get(1);
        int r = head.get(2);
        int class3 = head.get(3);
        if(DicRdfDataMap.checkDuplicate(rs, typeEncode, class1, isp)){
            return;
        }
        boolean flag1 = DicRdfDataMap.checkDuplicate(rs, typeEncode, class2, isp);
        boolean flag2 = DicRdfDataMap.checkDuplicate(ro, typeEncode, class3, isp);
        if(flag1 && flag2){
            DicRdfDataMap.addNewRdfDataBean(isp, iop, rs, typeEncode, class1);
        }
    }

    private static void equivalentClass2Reason(Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs, List<Integer> head) {
        int class1 = head.get(0);
        int class2 = head.get(1);
        int r = head.get(2);
        int class3 = head.get(3);
        if(DicRdfDataMap.checkDuplicate(rs, typeEncode, class1, isp)){
            return;
        }
        int firstTripleIsp = IndexMap.getFirstIndexSpFromMap(rs, r);
        if(firstTripleIsp == -1){
            return;
        }
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIsp;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNsp();
            int ro = dicDataBeanIterator.getRo();
            if(DicRdfDataMap.checkDuplicate(ro, typeEncode, class3, isp)){
                //rs type class1
                DicRdfDataMap.addNewRdfDataBean(isp, iop, rs, typeEncode, class1);
                return;
            }
        }while(indexNew != -1);

    }

    private static void objectSomeValuesFromReason(Map<Integer, DicRdfDataBean> totalData, Map<Integer, DicRdfDataBean> iteratorMap, Map<Integer, DicRdfDataBean> stashMap, Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs, List<Integer> head) {
        int rp = head.get(0);
        int class2 = head.get(1);
        int index = totalData.size()+iteratorMap.size()+stashMap.size();
        int firstTripleIsp = IndexMap.getFirstIndexSpFromMap(rs, rp, index);
        if(firstTripleIsp != -1){
            DicRdfDataBean dicDataBeanIterator;
            int indexNew = firstTripleIsp;
            do{
                dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
                indexNew = dicDataBeanIterator.getNsp();
                int ro = dicDataBeanIterator.getRo();
                if(DicRdfDataMap.checkDuplicate(ro, typeEncode, class2, isp)){
                    return;
                }
            }while(indexNew != -1);
        }
        addSomeValueFrom(isp, iop, rs, rp, class2, firstTripleIsp);
    }

    private static void basicReason(Map<Integer, DicRdfDataBean> totalData, Map<Integer, DicRdfDataBean> iteratorMap, Map<Integer, DicRdfDataBean> stashMap, Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs, int rp, List<Integer> head) {
        int ro = head.get(0);
        int index = totalData.size()+iteratorMap.size()+stashMap.size();
        int firstTripleIsp = IndexMap.getFirstIndexSpFromMap(rs, rp, index);
        //System.out.println("firstTripleIsp"+index+firstTripleIsp);
        int firstTripleIop = IndexMap.getFirstIndexOpFromMap(rp, ro, index);
        DicRdfDataBean dicDataBean = new DicRdfDataBean();
        dicDataBean.setRs(rs);
        dicDataBean.setRp(rp);
        dicDataBean.setRo(ro);
        dicDataBean.setNp(-1);
        if(firstTripleIsp == -1 || firstTripleIop == -1){
            dicDataBean.setNsp(firstTripleIsp, index);
            dicDataBean.setNop(firstTripleIop, index);
            stashMap.put(index, dicDataBean);
        } else{
            if(DicRdfDataMap.checkDuplicate(firstTripleIsp, ro)){//true 已经存在，不插入
                return;
            }
            else{//插入
                //System.out.println("indexthis"+firstTripleIsp);
                dicDataBean.setNsp(firstTripleIsp, index);
                dicDataBean.setNop(firstTripleIop, index);
                stashMap.put(index, dicDataBean);
            }
        }

    }

    private static void subPropertyReason(Map<Integer, DicRdfDataBean> totalData, Map<Integer, DicRdfDataBean> iteratorMap, Map<Integer, DicRdfDataBean> stashMap, Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs, List<Integer> head, int ro) {
        int rp = head.get(0);
        //TODO
        int index = totalData.size()+iteratorMap.size()+stashMap.size();
        int firstTripleIsp = IndexMap.getFirstIndexSpFromMap(rs, rp, index);
        int firstTripleIop = IndexMap.getFirstIndexOpFromMap(rp, ro, index);
        DicRdfDataBean dicDataBean = new DicRdfDataBean();
        dicDataBean.setRs(rs);
        dicDataBean.setRp(rp);
        dicDataBean.setRo(ro);
        dicDataBean.setNp(-1);
        if(firstTripleIsp == -1 || firstTripleIop == -1){
            dicDataBean.setNsp(firstTripleIsp, index);
            dicDataBean.setNop(firstTripleIop, index);
            stashMap.put(index, dicDataBean);
        } else{
            if(DicRdfDataMap.checkDuplicate(firstTripleIsp, ro)){//true 已经存在，不插入
                return;
            }
            else{//插入
                dicDataBean.setNsp(firstTripleIsp, index);
                dicDataBean.setNop(firstTripleIop, index);
                stashMap.put(index, dicDataBean);
            }
        }
    }



    private static void convertDataToRuleKey(List<String> ruleKey, int rs, int rp, int ro) {
        StringBuffer ssbuff = new StringBuffer();
        String key1 = ssbuff.append("*").append(rp).append(ro).toString();
        ruleKey.add(key1);
        ssbuff.setLength(0);
        String key2 = ssbuff.append("*").append("*").append(ro).toString();
        ruleKey.add(key2);
        ssbuff.setLength(0);
        String key3 = ssbuff.append("*").append(rp).append("*").toString();
        ruleKey.add(key3);
        ssbuff.setLength(0);
        String key4 = ssbuff.append(rs).append("*").append(ro).toString();
        ruleKey.add(key4);
        ssbuff.setLength(0);
        String key5 = ssbuff.append(rs).append(rp).append("*").toString();
        ruleKey.add(key5);
        ssbuff.setLength(0);
        String key6 = ssbuff.append(rs).append("*").append("*").toString();
        ruleKey.add(key6);
        ssbuff.setLength(0);
    }
}
