package com.tju.gowl.reason;

import com.tju.gowl.bean.*;
import com.tju.gowl.dictionary.Dictionary;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DicSerialReason {
    private static int anonymous = -2;
    private static int typeEncode = 0;
    private static StringBuffer sameAsString = new StringBuffer("sameAs");
    private static int sameAsInt = 0;
    private static int someValue = 1;
    private static boolean someValueFlag = false;



    public static void reason() throws IOException {
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
                    boolean rsBool =  SameAsReason.boolSameAs(Rs);
                    boolean rpBool =  SameAsReason.boolSameAs(Rp);
                    boolean roBool =  SameAsReason.boolSameAs(Ro);
                    if(!(rsBool && rpBool && roBool)){
                        continue;
                    }
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
                                switch (type){
                                    case 2013://SubObjectPropertyOf 2013
                                        subPropertyReason(totalData, iteratorMap, stashMap, Rs, head, Ro);
                                        break;
                                    case 2022://ObjectPropertyDomain 2022
                                        basicReason(totalData, iteratorMap, stashMap, Rs, typeEncode, head);
                                        break;
                                    case 2023://ObjectPropertyRange 2023
                                        basicReason(totalData, iteratorMap, stashMap, Ro, typeEncode, head);
                                        break;
                                    case 2002://SubClassOf 2002
                                        basicReason(totalData, iteratorMap, stashMap, Rs, typeEncode, head);
                                        break;
                                    case 3005:
                                        objectSomeValuesFromReason(totalData, iteratorMap, stashMap, Rs, head);
                                        break;
                                    case 0:
                                        equivalentClass2Reason(Rs, head);
                                        break;
                                    case 1:
                                        equivalentRoleReason(Rs, Ro, head);
                                        break;
                                    case 2:
                                        equivalentClass3Reason(Rs, head);
                                        break;
                                    case 2019:
                                        transitiveReason(Rs, Rp, Ro);
                                        break;
                                    case 3006:
                                        objectAllValuesFromReason(Rs, head);
                                        break;
                                    case 3008:
                                        objectMinCardinalityReason(Rs, head);
                                        break;
                                    case -3006://ObjectAllValuesFrom
                                        objectAllValuesInverseFromReason(Rs, head);
                                        break;
                                    case 2017:
                                        symmetricObjectPropertyReason(Rs, Rp, Ro);
                                        break;
                                    case 2016:
                                        inverseFunctionalObjectPropertyReason(Rs, Rp, Ro);
                                        break;
                                    case 2015:
                                        functionalObjectPropertyReason(Rs, Rp, Ro);
                                        break;
                                    case 10:
                                        type10Reason(Rs, Rp, Ro, head);
                                        break;
                                    case 11:
                                        type11Reason(Rs, Rp, Ro, head);
                                        break;
                                    case 12:
                                        try {
                                            type12Reason(Rs, Rp, Ro, head);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case 14:
                                        type14Reason(Rs, Rp, Ro, head);
                                        break;
                                    case 15:
                                        type15Reason(Rs, Rp, Ro, head);
                                        break;
                                    case 16:
                                        type16Reason(Rs, Rp, Ro, head);
                                        break;
                                    case 17:
                                        type16Reason(Rs, Rp, Ro, head);
                                        break;
                                    case 18:
                                        type18Reason(Rs, Rp, Ro, head);
                                        break;
                                    case 19:
                                        type19Reason(Rs, Rp, Ro, head);
                                        break;
                                    case 20:
                                        type20Reason(Rs, Rp, Ro, head);
                                        break;
                                    case 21:
                                        type21Reason(Rs, Rp, Ro, head);
                                        break;
                                    case 22:
                                        type22Reason(Rs, Rp, Ro, head);
                                        break;

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
                System.out.println("total data size"+totalData.size());
                SameAsReason.addEquivIndividual(totalData);
                System.out.println("after equiv data size"+totalData.size());
                break;
            }
            if(someValue>=14){
                System.out.println("14轮循环结束");
                System.out.println("someValue"+someValue);
                totalData.putAll(stashMap);
                totalData.putAll(iteratorMap);
                stashMap.clear();
                iteratorMap.clear();
                System.out.println("total data size"+totalData.size());
                SameAsReason.addEquivIndividual(totalData);
                System.out.println("after equiv data size"+totalData.size());

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

//        outEquiPool();
//        outEquiMapping();

    }



    private static void type22Reason(int rs, int rp, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int class2 = head.get(1);
        int property = head.get(2);
        int class3 = head.get(3);
        int firstTripleIop = IndexMap.getFirstIndexOpFromMap(property, rs);
        if(firstTripleIop == -1){ return; }
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIop;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNop();
            int rsTmp = dicDataBeanIterator.getRs();

            if(!DicRdfDataMap.checkDuplicate(rsTmp, typeEncode, class1)) {
                if(DicRdfDataMap.checkDuplicate(rsTmp, typeEncode, class2)){
                    if(checkAllValue(rsTmp,property,class3)){
                        DicRdfDataMap.addNewRdfDataBean(rsTmp, typeEncode, class1);
                    }
                }
            }
        }while(indexNew != -1);
    }

    private static void type21Reason(int rs, int rp, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int class2 = head.get(1);
        int property = head.get(2);
        int class3 = head.get(3);
        if(DicRdfDataMap.checkDuplicate(rs, typeEncode, class1)) return;
        if(!DicRdfDataMap.checkDuplicate(rs, typeEncode, class2)) return;
        if(checkAllValue(rs,property,class3)){
            DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
        }
    }

    private static void type20Reason(int rs, int rp, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int class2 = head.get(1);
        int property = head.get(2);
        int class3 = head.get(3);
        if(DicRdfDataMap.checkDuplicate(rs, typeEncode, class1)) return;
        if(checkAllValue(rs,property,class3)){
            DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
        }
    }

    private static void type19Reason(int rs, int rp, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int property = head.get(1);
        int class3 = head.get(2);
        int firstTripleIop = IndexMap.getFirstIndexOpFromMap(property, rs);
        if(firstTripleIop == -1){ return; }
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIop;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNop();
            int rsTmp = dicDataBeanIterator.getRs();
            if(!DicRdfDataMap.checkDuplicate(rsTmp, typeEncode, class1)) {
                if(checkAllValue(rsTmp,property,class3)){
                    DicRdfDataMap.addNewRdfDataBean(rsTmp, typeEncode, class1);
                }
            }
        }while(indexNew != -1);
    }

    private static void type18Reason(int rs, int rp, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int property = head.get(1);
        int class3 = head.get(2);
        if(DicRdfDataMap.checkDuplicate(rs, typeEncode, class1)) return;
        if(checkAllValue(rs,property,class3)){
            DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
        }
    }


    private static void type16Reason(int rs, int rp, int ro, List<Integer> head) {
        //rs type class3
        int class1 = head.get(0);
        int class2 = head.get(1);
        int property = head.get(2);
        int class3 = head.get(3);
        int class4= head.get(4);
        int firstTripleIop = IndexMap.getFirstIndexOpFromMap(property, rs);
        if(firstTripleIop == -1){ return; }
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIop;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNop();
            int rsTmp = dicDataBeanIterator.getRs();

            if(!DicRdfDataMap.checkDuplicate(rsTmp, typeEncode, class1)) {
                if(DicRdfDataMap.checkDuplicate(rsTmp, typeEncode, class2)){
                    if(checkAllValue(rsTmp,property,class3)){
                        if(checkAllValue(rsTmp,property,class4)){
                            DicRdfDataMap.addNewRdfDataBean(rsTmp, typeEncode, class1);
                        }
                    }
                }
            }
        }while(indexNew != -1);
    }

    private static boolean checkAllValue(int rs, int property, int class3) {
        int firstTripleIsp = IndexMap.getFirstIndexSpFromMap(rs, property);
        if(firstTripleIsp == -1) {
            return true;
        }
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIsp;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNsp();
            int ro = dicDataBeanIterator.getRo();
            if(!DicRdfDataMap.checkDuplicate(ro, typeEncode, class3))
                return false;
        }while(indexNew != -1);
        return true;
    }

    private static void type15Reason(int rs, int rp, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int class2 = head.get(1);
        int property = head.get(2);
        int class3 = head.get(3);
        int class4= head.get(4);
        if(DicRdfDataMap.checkDuplicate(rs, typeEncode, class1)) return;
        if(!DicRdfDataMap.checkDuplicate(rs, typeEncode, class2)) return;
        int firstTripleIsp = IndexMap.getFirstIndexSpFromMap(rs, property);
        if(firstTripleIsp == -1) {
            DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
            return;
        }
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIsp;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNsp();
            int ro1 = dicDataBeanIterator.getRo();
            if(!DicRdfDataMap.checkDuplicate(ro1, typeEncode, class3))
                return;
            if(!DicRdfDataMap.checkDuplicate(ro1, typeEncode, class4))
                return;
        }while(indexNew != -1);
        DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
    }

    private static void type14Reason(int rs, int rp, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int class2 = head.get(1);
        int property = head.get(2);
        int class3 = head.get(3);
        int class4= head.get(4);
        if(DicRdfDataMap.checkDuplicate(rs, typeEncode, class1)) return;
        int firstTripleIsp = IndexMap.getFirstIndexSpFromMap(rs, property);
        if(firstTripleIsp == -1) {
            DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
            return;
        }
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIsp;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNsp();
            int ro1 = dicDataBeanIterator.getRo();
            if(!DicRdfDataMap.checkDuplicate(ro1, typeEncode, class3))
                return;
            if(!DicRdfDataMap.checkDuplicate(ro1, typeEncode, class4))
                return;
        }while(indexNew != -1);
        DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
    }


    private static void type12Reason(int rs, int rp, int ro1, List<Integer> head) throws IOException {
        int class1 = head.get(0);
        int count =head.get(1);
        int property = head.get(2);
        int class2 = head.get(3);
        int count1 = 0;
        if(DicRdfDataMap.checkDuplicate(rs, typeEncode, class1)) return;
        int firstTripleIsp = IndexMap.getFirstIndexSpFromMap(rs, rp);
        if(firstTripleIsp == -1) return;
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIsp;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNsp();
            int ro = dicDataBeanIterator.getRo();
            if(class2 == 1){//TODO owl:Thing 情况
                count1++;
            }
            else{
                if(DicRdfDataMap.checkDuplicate(ro, typeEncode, class2)){
                    count1++;
                }
            }

        }while(indexNew != -1);
        if(count1 >= count){
            DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
        }

    }

    private static void type11Reason(int rs1, int rp, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int property = head.get(1);
        int class2 = head.get(2);
        int firstTripleIop = IndexMap.getFirstIndexOpFromMap(property, ro);
        if(firstTripleIop == -1){ return; }
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIop;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNop();
            int rs = dicDataBeanIterator.getRs();
            if(!DicRdfDataMap.checkDuplicate(rs, typeEncode, class1)) {
                DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
            }
        }while(indexNew != -1);
    }

    private static void type10Reason(int rs, int rp, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int property = head.get(1);
        int class2 = head.get(2);
        if(DicRdfDataMap.checkDuplicate(rs, 0, class1)) return;
        if(class2 == 1){
            DicRdfDataMap.addNewRdfDataBean(rs, 0, class1);
            return;
        }
        if(DicRdfDataMap.checkDuplicate(ro, 0, class2)){
            DicRdfDataMap.addNewRdfDataBean(rs, 0, class1);
        }

    }











    private static void functionalObjectPropertyReason(int rs, int rp, int ro) {
        int rsEquiv = SameAsReason.findEquivPoolIndex(rs);
//        System.out.println(Dictionary.getDecode().get(rs));
//        System.out.println(Dictionary.getDecode().get(rp));
//        System.out.println(Dictionary.getDecode().get(ro));
        if(rsEquiv == 0){//没有相等individual
            SameAsReason.loopRsRpFindRo(rs, rp, ro);
        }
        else{
            HashSet<Integer> set = SameAsReason.equiPool.get(rsEquiv - 1);
            Iterator<Integer> it = set.iterator();
            while (it.hasNext()) {
                Integer tmp = it.next();
                SameAsReason.loopRsRpFindRo(tmp, rp, ro);
            }
        }
    }





    private static void inverseFunctionalObjectPropertyReason(int rs, int rp, int ro) {
//        Map<String, Integer> inverseFunProMap = FunctionalPropertyMap.getInverseFunPropertyMap();
//        int rsEquiv = findEquivPoolIndex(rs);
        int roEquiv = SameAsReason.findEquivPoolIndex(ro);
        if(roEquiv == 0){//没有相等individual
            SameAsReason.loopRpRoFindRs(rs, rp, ro);
        }
        else{
            HashSet<Integer> set = SameAsReason.equiPool.get(roEquiv - 1);
            Iterator<Integer> it = set.iterator();
            while (it.hasNext()) {
                Integer tmp = it.next();
                SameAsReason.loopRpRoFindRs(rs, rp, tmp);
            }
        }

    }



    private static void symmetricObjectPropertyReason(int rs, int rp, int ro) {
        if(!DicRdfDataMap.checkDuplicate(ro, rp, rs)) {
            DicRdfDataMap.addNewRdfDataBean(ro, rp, rs);
        }

    }

    private static void objectAllValuesInverseFromReason(int rs, List<Integer> head) {
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
            if(!DicRdfDataMap.checkDuplicate(rs1, typeEncode, class2)) {
                //rs rp ro1
                DicRdfDataMap.addNewRdfDataBean(rs1, typeEncode, class2);
            }
        }while(indexNew != -1);
    }

    private static void objectMinCardinalityReason(int rs, List<Integer> head) {
        int cardinality = head.get(0);
        int rp = head.get(1);
        int class2 = head.get(2);
        int firstTripleIsp = IndexMap.getFirstIndexSpFromMap(rs, rp);
        if(firstTripleIsp == -1){
            //add cardinality
            int i = 0;
            while(i<cardinality){
                addSomeValueFrom(rs, rp, class2);
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
                if(class2 == 1){//TODO owl:Thing
                    exist++;
                }
                else{
                    int ro1 = dicDataBeanIterator.getRo();
                    if(DicRdfDataMap.checkDuplicate(ro1, typeEncode, class2)) {
                        //rs rp ro1
                        exist++;
                    }
                }

            }while(indexNew != -1);
            int i = 0;
            if((cardinality-exist) <= 0){return ;}
            while(i<(cardinality-exist)){
                addSomeValueFrom(rs, rp, class2);
                i++;
            }
        }
    }

    private static void addSomeValueFrom(int rs, int rp, int class2, int firstTripleIsp) {
        int ro = anonymous;
        if (!someValueFlag && rs < 0) {
            someValue++;
            someValueFlag = true;
        }
        DicRdfDataMap.addNewRdfDataBean(rs, rp, ro, firstTripleIsp);
        anonymous--;
        DicRdfDataMap.addNewRdfDataBean(ro, typeEncode, class2);
    }
    private static void addSomeValueFrom(int rs, int rp, int class2) {
        int ro = anonymous;
        if (!someValueFlag && rs < 0) {
            someValue++;
            someValueFlag = true;
        }
        DicRdfDataMap.addNewRdfDataBean(rs, rp, ro);
        anonymous--;
        DicRdfDataMap.addNewRdfDataBean(ro, typeEncode, class2);
    }
    private static void objectAllValuesFromReason(int rs, List<Integer> head) {
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
            if(!DicRdfDataMap.checkDuplicate(ro1, typeEncode, class2)) {
                //rs rp ro1
                DicRdfDataMap.addNewRdfDataBean(ro1, typeEncode, class2);
            }
        }while(indexNew != -1);
    }

    private static void transitiveReason(int rs, int rp, int ro) {
        int firstTripleIsp = IndexMap.getFirstIndexSpFromMap(ro, rp);
        if (firstTripleIsp == -1) return;
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIsp;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNsp();
            int ro1 = dicDataBeanIterator.getRo();
            if(!DicRdfDataMap.checkDuplicate(rs, rp, ro1)) {
                //rs rp ro1
                DicRdfDataMap.addNewRdfDataBean(rs, rp, ro1);
            }
        }while(indexNew != -1);

    }

    private static void equivalentClass3Reason(int ro, List<Integer> head) {
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
            if(!DicRdfDataMap.checkDuplicate(rs, typeEncode, class1)) {
                if(DicRdfDataMap.checkDuplicate(rs, typeEncode, class2)){
                    //rs type class1
                    DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
                }
            }
        }while(indexNew != -1);

    }

    private static void equivalentRoleReason(int rs, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int class2 = head.get(1);
        int r = head.get(2);
        int class3 = head.get(3);
        if(DicRdfDataMap.checkDuplicate(rs, typeEncode, class1)){
            return;
        }
        boolean flag1 = DicRdfDataMap.checkDuplicate(rs, typeEncode, class2);
        if(flag1){
            if(class3 == 1){
                DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
                return;
            }
            else{
                boolean flag2 = DicRdfDataMap.checkDuplicate(ro, typeEncode, class3);
                if(flag2){
                    DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
                }
            }
        }

    }

    private static void equivalentClass2Reason(int rs, List<Integer> head) {
        int class1 = head.get(0);
        int class2 = head.get(1);
        int r = head.get(2);
        int class3 = head.get(3);
        if(DicRdfDataMap.checkDuplicate(rs, typeEncode, class1)){
            return;
        }
        int firstTripleIsp = IndexMap.getFirstIndexSpFromMap(rs, r);
        if(firstTripleIsp == -1){
            return;
        }
        if(class3 == 1){//owl:Thing
            DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
            return;
        }
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIsp;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNsp();
            int ro = dicDataBeanIterator.getRo();
            if(DicRdfDataMap.checkDuplicate(ro, typeEncode, class3)){
                //rs type class1
                DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
                return;
            }
        }while(indexNew != -1);

    }

    private static void objectSomeValuesFromReason(Map<Integer, DicRdfDataBean> totalData, Map<Integer, DicRdfDataBean> iteratorMap, Map<Integer, DicRdfDataBean> stashMap, int rs, List<Integer> head) {
        int rp = head.get(0);
        int class2 = head.get(1);
        int index = totalData.size()+iteratorMap.size()+stashMap.size();
        int firstTripleIsp = IndexMap.getFirstIndexSpFromMap(rs, rp, index);
        if(firstTripleIsp != -1){
            if(class2 == 1){//TODO owl:Thing 情况
                return;
            }
            DicRdfDataBean dicDataBeanIterator;
            int indexNew = firstTripleIsp;
            do{
                dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
                indexNew = dicDataBeanIterator.getNsp();
                int ro = dicDataBeanIterator.getRo();
                if(DicRdfDataMap.checkDuplicate(ro, typeEncode, class2)){
                    return;
                }
            }while(indexNew != -1);
        }
        addSomeValueFrom(rs, rp, class2, firstTripleIsp);
    }

    private static void basicReason(Map<Integer, DicRdfDataBean> totalData, Map<Integer, DicRdfDataBean> iteratorMap, Map<Integer, DicRdfDataBean> stashMap, int rs, int rp, List<Integer> head) {
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

    private static void subPropertyReason(Map<Integer, DicRdfDataBean> totalData, Map<Integer, DicRdfDataBean> iteratorMap, Map<Integer, DicRdfDataBean> stashMap, int rs, List<Integer> head, int ro) {
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
