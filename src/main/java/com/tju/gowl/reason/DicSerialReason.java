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
    public static List<HashSet<Integer>> equiPool = new ArrayList<>();
    static Map<Integer, Integer> equiMapping = new ConcurrentHashMap<>();
    static Map<Integer, Integer> equiRepresentation = new ConcurrentHashMap<>();


    private static BufferedWriter out;

    static {
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("testManyHobbies.nt"),"GBK"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


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
                    boolean rsBool =  boolSameAs(Rs);
                    boolean rpBool =  boolSameAs(Rp);
                    boolean roBool =  boolSameAs(Ro);
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
                                        subPropertyReason(totalData, iteratorMap, stashMap, Isp, Iop, Rs, head, Ro);
                                        break;
                                    case 2022://ObjectPropertyDomain 2022
                                        basicReason(totalData, iteratorMap, stashMap, Isp, Iop, Rs, typeEncode, head);
                                        break;
                                    case 2023://ObjectPropertyRange 2023
                                        basicReason(totalData, iteratorMap, stashMap, Isp, Iop, Ro, typeEncode, head);
                                        break;
                                    case 2002://SubClassOf 2002
                                        basicReason(totalData, iteratorMap, stashMap, Isp, Iop, Rs, typeEncode, head);
                                        break;
                                    case 3005:
                                        objectSomeValuesFromReason(totalData, iteratorMap, stashMap, Isp, Iop, Rs, head);
                                        break;
                                    case 0:
                                        equivalentClass2Reason(Isp, Iop, Rs, head);
                                        break;
                                    case 1:
                                        equivalentRoleReason(Isp, Iop, Rs, Ro, head);
                                        break;
                                    case 2:
                                        equivalentClass3Reason(Isp, Iop, Rs, head);
                                        break;
                                    case 2019:
                                        transitiveReason(Isp, Iop, Rs, Rp, Ro);
                                        break;
                                    case 3006:
                                        objectAllValuesFromReason(Isp, Iop, Rs, head);
                                        break;
                                    case 3008:
                                        objectMinCardinalityReason(totalData, iteratorMap, stashMap, Isp, Iop, Rs, head);
                                        break;
                                    case -3006://ObjectAllValuesFrom
                                        objectAllValuesInverseFromReason(Isp, Iop, Rs, head);
                                        break;
                                    case 2017:
                                        symmetricObjectPropertyReason(Isp, Iop, Rs, Rp, Ro);
                                        break;
                                    case 2016:
                                        inverseFunctionalObjectPropertyReason(Isp, Iop, Rs, Rp, Ro);
                                        break;
                                    case 2015:
                                        functionalObjectPropertyReason(Isp, Iop, Rs, Rp, Ro);
                                        break;
                                    case 10:
                                        type10Reason(Isp, Iop, Rs, Rp, Ro, head);
                                        break;
                                    case 11:
                                        type11Reason(Isp, Iop, Rs, Rp, Ro, head);
                                        break;
                                    case 12:
                                        try {
                                            type12Reason(Isp, Iop, Rs, Rp, Ro, head);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case 14:
                                        type14Reason(Isp, Iop, Rs, Rp, Ro, head);
                                        break;
                                    case 15:
                                        type15Reason(Isp, Iop, Rs, Rp, Ro, head);
                                        break;
                                    case 16:
                                        type16Reason(Isp, Iop, Rs, Rp, Ro, head);
                                        break;
                                    case 17:
                                        type16Reason(Isp, Iop, Rs, Rp, Ro, head);
                                        break;
                                    case 18:
                                        type18Reason(Isp, Iop, Rs, Rp, Ro, head);
                                        break;
                                    case 19:
                                        type19Reason(Isp, Iop, Rs, Rp, Ro, head);
                                        break;
                                    case 20:
                                        type20Reason(Isp, Iop, Rs, Rp, Ro, head);
                                        break;
                                    case 21:
                                        type21Reason(Isp, Iop, Rs, Rp, Ro, head);
                                        break;
                                    case 22:
                                        type22Reason(Isp, Iop, Rs, Rp, Ro, head);
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
                addEquivIndividual(totalData);
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
                addEquivIndividual(totalData);
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
        out.flush();
        out.close();
    }

    private static boolean boolSameAs(int rs) {
        if(equiRepresentation.containsKey(rs)){
            if(equiRepresentation.get(rs)!= rs){
                return false;
            }
        }
        return true;
    }

    private static void type22Reason(Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs, int rp, int ro, List<Integer> head) {
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

            if(!DicRdfDataMap.checkDuplicate(rsTmp, typeEncode, class1, isp)) {
                if(DicRdfDataMap.checkDuplicate(rsTmp, typeEncode, class2, isp)){
                    if(checkAllValue(rsTmp,property,class3,isp)){
                        DicRdfDataMap.addNewRdfDataBean(isp, iop, rsTmp, typeEncode, class1);
                    }
                }
            }
        }while(indexNew != -1);
    }

    private static void type21Reason(Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs, int rp, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int class2 = head.get(1);
        int property = head.get(2);
        int class3 = head.get(3);
        if(DicRdfDataMap.checkDuplicate(rs, typeEncode, class1, isp)) return;
        if(!DicRdfDataMap.checkDuplicate(rs, typeEncode, class2, isp)) return;
        if(checkAllValue(rs,property,class3,isp)){
            DicRdfDataMap.addNewRdfDataBean(isp, iop, rs, typeEncode, class1);
        }
    }

    private static void type20Reason(Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs, int rp, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int class2 = head.get(1);
        int property = head.get(2);
        int class3 = head.get(3);
        if(DicRdfDataMap.checkDuplicate(rs, typeEncode, class1, isp)) return;
        if(checkAllValue(rs,property,class3,isp)){
            DicRdfDataMap.addNewRdfDataBean(isp, iop, rs, typeEncode, class1);
        }
    }

    private static void type19Reason(Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs, int rp, int ro, List<Integer> head) {
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
            if(!DicRdfDataMap.checkDuplicate(rsTmp, typeEncode, class1, isp)) {
                if(checkAllValue(rsTmp,property,class3,isp)){
                    DicRdfDataMap.addNewRdfDataBean(isp, iop, rsTmp, typeEncode, class1);
                }
            }
        }while(indexNew != -1);
    }

    private static void type18Reason(Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs, int rp, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int property = head.get(1);
        int class3 = head.get(2);
        if(DicRdfDataMap.checkDuplicate(rs, typeEncode, class1, isp)) return;
        if(checkAllValue(rs,property,class3,isp)){
            DicRdfDataMap.addNewRdfDataBean(isp, iop, rs, typeEncode, class1);
        }
    }


    private static void type16Reason(Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs, int rp, int ro, List<Integer> head) {
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

            if(!DicRdfDataMap.checkDuplicate(rsTmp, typeEncode, class1, isp)) {
                if(DicRdfDataMap.checkDuplicate(rsTmp, typeEncode, class2, isp)){
                    if(checkAllValue(rsTmp,property,class3,isp)){
                        if(checkAllValue(rsTmp,property,class4,isp)){
                            DicRdfDataMap.addNewRdfDataBean(isp, iop, rsTmp, typeEncode, class1);
                        }
                    }
                }
            }
        }while(indexNew != -1);
    }

    private static boolean checkAllValue(int rs, int property, int class3, Map<Integer, List<IndexBean>> isp) {
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
            if(!DicRdfDataMap.checkDuplicate(ro, typeEncode, class3, isp))
                return false;
        }while(indexNew != -1);
        return true;
    }

    private static void type15Reason(Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs, int rp, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int class2 = head.get(1);
        int property = head.get(2);
        int class3 = head.get(3);
        int class4= head.get(4);
        if(DicRdfDataMap.checkDuplicate(rs, typeEncode, class1, isp)) return;
        if(!DicRdfDataMap.checkDuplicate(rs, typeEncode, class2, isp)) return;
        int firstTripleIsp = IndexMap.getFirstIndexSpFromMap(rs, property);
        if(firstTripleIsp == -1) {
            DicRdfDataMap.addNewRdfDataBean(isp, iop, rs, typeEncode, class1);
            return;
        }
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIsp;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNsp();
            int ro1 = dicDataBeanIterator.getRo();
            if(!DicRdfDataMap.checkDuplicate(ro1, typeEncode, class3, isp))
                return;
            if(!DicRdfDataMap.checkDuplicate(ro1, typeEncode, class4, isp))
                return;
        }while(indexNew != -1);
        DicRdfDataMap.addNewRdfDataBean(isp, iop, rs, typeEncode, class1);
    }

    private static void type14Reason(Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs, int rp, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int class2 = head.get(1);
        int property = head.get(2);
        int class3 = head.get(3);
        int class4= head.get(4);
        if(DicRdfDataMap.checkDuplicate(rs, typeEncode, class1, isp)) return;
        int firstTripleIsp = IndexMap.getFirstIndexSpFromMap(rs, property);
        if(firstTripleIsp == -1) {
            DicRdfDataMap.addNewRdfDataBean(isp, iop, rs, typeEncode, class1);
            return;
        }
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIsp;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNsp();
            int ro1 = dicDataBeanIterator.getRo();
            if(!DicRdfDataMap.checkDuplicate(ro1, typeEncode, class3, isp))
                return;
            if(!DicRdfDataMap.checkDuplicate(ro1, typeEncode, class4, isp))
                return;
        }while(indexNew != -1);
        DicRdfDataMap.addNewRdfDataBean(isp, iop, rs, typeEncode, class1);
    }


    private static void type12Reason(Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs, int rp, int ro1, List<Integer> head) throws IOException {
        int class1 = head.get(0);
        int count =head.get(1);
        int property = head.get(2);
        int class2 = head.get(3);
        int count1 = 0;
        if(DicRdfDataMap.checkDuplicate(rs, typeEncode, class1, isp)) return;
        int firstTripleIsp = IndexMap.getFirstIndexSpFromMap(rs, rp);
        if(firstTripleIsp == -1) return;
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIsp;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNsp();
            int ro = dicDataBeanIterator.getRo();
            if(class2 == 1){//TODO owl:Thing 情况
//                out.write("test"+Dictionary.getDecode().get(class1));
//                out.newLine();
//                out.write("test"+Dictionary.getDecode().get(rs));
//                out.newLine();
//                out.write("test"+Dictionary.getDecode().get(rp));
//                out.newLine();
//                if(Dictionary.getDecode().containsKey(ro)){
//                    out.write("test"+Dictionary.getDecode().get(ro));
//                    out.newLine();
//                }
//                else{
//                    out.write("test"+ro);
//                    out.newLine();
//                }
                count1++;
            }
            else{
                if(DicRdfDataMap.checkDuplicate(ro, typeEncode, class2, isp)){
                    count1++;
                }
            }

        }while(indexNew != -1);
        if(count1 >= count){
            DicRdfDataMap.addNewRdfDataBean(isp, iop, rs, typeEncode, class1);
        }

    }

    private static void type11Reason(Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs1, int rp, int ro, List<Integer> head) {
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
            if(!DicRdfDataMap.checkDuplicate(rs, typeEncode, class1, isp)) {
                DicRdfDataMap.addNewRdfDataBean(isp, iop, rs, typeEncode, class1);
            }
        }while(indexNew != -1);
    }

    private static void type10Reason(Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs, int rp, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int property = head.get(1);
        int class2 = head.get(2);
        if(DicRdfDataMap.checkDuplicate(rs, 0, class1, isp)) return;
        if(class2 == 1){
            DicRdfDataMap.addNewRdfDataBean(isp, iop, rs, 0, class1);
            return;
        }
        if(DicRdfDataMap.checkDuplicate(ro, 0, class2, isp)){
            DicRdfDataMap.addNewRdfDataBean(isp, iop, rs, 0, class1);
        }

    }



    private static void addEquivIndividual(Map<Integer, DicRdfDataBean> totalData) {
        Iterator<HashSet<Integer>> iterPool = equiPool.iterator();
        Map<Integer, List<IndexBean>> isp = IndexMap.getIsp();
        Map<Integer, List<IndexBean>> iop = IndexMap.getIop();
        while(iterPool.hasNext()){
            HashSet<Integer> tmpPool = iterPool.next();
            Iterator<Integer> tmp1 = tmpPool.iterator();
            while(tmp1.hasNext()){
                int tmp = tmp1.next();
                addEquivRsTriple(totalData, isp, tmpPool, tmp);
                addEquivRoTriple(totalData, iop, tmpPool, tmp);
            }
        }
    }

    private static void addEquivRoTriple(Map<Integer, DicRdfDataBean> totalData, Map<Integer, List<IndexBean>> iop, HashSet<Integer> tmpPool, int tmp) {
        Map<Integer, List<IndexBean>> isp = IndexMap.getIsp();
        List<Integer> rsRpTriples = findAllTriplesFromRo(totalData, iop, tmp);
        Iterator<Integer> tmp2 = tmpPool.iterator();
        while(tmp2.hasNext()){
            int tmp22 = tmp2.next();
            if(tmp22 != tmp){
                Iterator<Integer> rsRpList = rsRpTriples.iterator();
                while(rsRpList.hasNext()){
                    int rs = rsRpList.next();
                    int rp = rsRpList.next();
                    if(!DicRdfDataMap.checkDuplicate(rs, rp, tmp22, isp)) {
                        DicRdfDataMap.addSourceRdfDataBean(totalData.size(), rs, rp, tmp22);

                    }
                }
            }

        }
    }

    private static List<Integer> findAllTriplesFromRo(Map<Integer, DicRdfDataBean> totalData, Map<Integer, List<IndexBean>> iop, int tmp) {
        List<Integer> rsRpTriples = new ArrayList<>();
        if(iop.containsKey(tmp)){
            List<IndexBean> indexBean = iop.get(tmp);
            Iterator<IndexBean> iterIndexBean = indexBean.iterator();
            while(iterIndexBean.hasNext()){
                //rs rp 第一条数据
                IndexBean tt = iterIndexBean.next();
                int firstTripleIop = tt.getIndex();
                int rp = tt.getResource();
                DicRdfDataBean dicDataBeanIterator;
                int indexNew = firstTripleIop;
                do{
                    dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
                    indexNew = dicDataBeanIterator.getNop();
                    int roTmp = dicDataBeanIterator.getRs();
                    rsRpTriples.add(roTmp);
                    rsRpTriples.add(rp);
                }while(indexNew != -1);
            }
        }
        return rsRpTriples;
    }

    private static void addEquivRsTriple(Map<Integer, DicRdfDataBean> totalData, Map<Integer, List<IndexBean>> isp, HashSet<Integer> tmpPool, int tmp) {
        List<Integer> rpRoTriples = findAllTriplesFromRs(totalData, isp, tmp);
        Iterator<Integer> tmp2 = tmpPool.iterator();
        while(tmp2.hasNext()){
            int tmp22 = tmp2.next();
            if(tmp22 != tmp){
                Iterator<Integer> rpRoList = rpRoTriples.iterator();
                while(rpRoList.hasNext()){
                    int rp = rpRoList.next();
                    int ro = rpRoList.next();
                    if(!DicRdfDataMap.checkDuplicate(tmp22, rp, ro, isp)) {
                        DicRdfDataMap.addSourceRdfDataBean(totalData.size(), tmp22, rp, ro);

                    }
                }
            }

        }
    }

    private static List<Integer> findAllTriplesFromRs(Map<Integer, DicRdfDataBean> totalData, Map<Integer, List<IndexBean>> isp, int tmp) {
        List<Integer> rpRoTriples = new ArrayList<>();
        if(isp.containsKey(tmp)){
            List<IndexBean> indexBean = isp.get(tmp);
            Iterator<IndexBean> iterIndexBean = indexBean.iterator();
            while(iterIndexBean.hasNext()){
                //rs rp 第一条数据
                IndexBean tt = iterIndexBean.next();
                int firstTripleIsp = tt.getIndex();
                int rp = tt.getResource();
                DicRdfDataBean dicDataBeanIterator;
                int indexNew = firstTripleIsp;
                do{
                    dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
                    indexNew = dicDataBeanIterator.getNsp();
                    int roTmp = dicDataBeanIterator.getRo();
                    rpRoTriples.add(rp);
                    rpRoTriples.add(roTmp);
                }while(indexNew != -1);
            }
        }
        return rpRoTriples;
    }

    private static void functionalObjectPropertyReason(Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs, int rp, int ro) {
        int rsEquiv = findEquivPoolIndex(rs);
//        System.out.println(Dictionary.getDecode().get(rs));
//        System.out.println(Dictionary.getDecode().get(rp));
//        System.out.println(Dictionary.getDecode().get(ro));
        if(rsEquiv == 0){//没有相等individual
            loopRsRpFindRo(rs, rp, ro);
        }
        else{
            HashSet<Integer> set = equiPool.get(rsEquiv - 1);
            Iterator<Integer> it = set.iterator();
            while (it.hasNext()) {
                Integer tmp = it.next();
                loopRsRpFindRo(tmp, rp, ro);
            }
        }
    }

    private static void loopRsRpFindRo(int rs, int rp, int ro) {
        int firstTripleIsp = IndexMap.getFirstIndexSpFromMap(rs, rp);
        if(firstTripleIsp == -1){ return; }

        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIsp;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNsp();
            int roTmp = dicDataBeanIterator.getRo();
            if(ro != roTmp) {
                comEquiPool(ro, roTmp);
            }
        }while(indexNew != -1);
    }

    private static void outEquiMapping() {
        List<String> encodeList = Dictionary.getEncodeList();
        Iterator<Map.Entry<Integer, Integer>> ii = equiMapping.entrySet().iterator();
        while( ii.hasNext()){
            Map.Entry<Integer, Integer> iii = ii.next();
            System.out.println(encodeList.get(iii.getKey())+" "+iii.getValue());

        }
    }

    private static void outEquiPool() {
        List<String> encodeList = Dictionary.getEncodeList();
        int count11 = 0;
        Iterator<HashSet<Integer>> ii = equiPool.iterator();
        while(ii.hasNext()){
            count11++;
            System.out.println(count11);
            HashSet<Integer> iii = ii.next();
            Iterator<Integer> iiii = iii.iterator();
            while(iiii.hasNext()){
                Integer iiiii = iiii.next();

                System.out.println(encodeList.get(iiiii));

            }

        }
    }

    private static void sameAsPropertyReason(Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs, int rp, int ro) {

    }

    private static void inverseFunctionalObjectPropertyReason(Map<Integer, List<IndexBean>> isp, Map<Integer, List<IndexBean>> iop, int rs, int rp, int ro) {
//        Map<String, Integer> inverseFunProMap = FunctionalPropertyMap.getInverseFunPropertyMap();
//        int rsEquiv = findEquivPoolIndex(rs);
        int roEquiv = findEquivPoolIndex(ro);
        if(roEquiv == 0){//没有相等individual
            loopRpRoFindRs(rs, rp, ro);
        }
        else{
            HashSet<Integer> set = equiPool.get(roEquiv - 1);
            Iterator<Integer> it = set.iterator();
            while (it.hasNext()) {
                Integer tmp = it.next();
                loopRpRoFindRs(rs, rp, tmp);
            }
        }

    }

    private static void loopRpRoFindRs(int rs, int rp, int ro) {
        int firstTripleIop = IndexMap.getFirstIndexOpFromMap(rp, ro);
        if(firstTripleIop == -1){ return; }

        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIop;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNop();
            int rsTmp = dicDataBeanIterator.getRs();
            if(rs != rsTmp) {
                comEquiPool(rs, rsTmp);
            }
        }while(indexNew != -1);
    }

    private static void comEquiPool(int rs, int rsTmp) {
        int rsEquiv = findEquivPoolIndex(rs);
        int rsTmpEquiv = findEquivPoolIndex(rsTmp);
        if(rsTmpEquiv ==0 && rsEquiv == 0){
            HashSet<Integer> newPool = new HashSet<>();
            newPool.add(rs);
            newPool.add(rsTmp);
            equiPool.add(newPool);
            //避免0冲突，存的是index of pool +1
            equiMapping.put(rs,equiPool.size());
            equiMapping.put(rsTmp,equiPool.size());
        }
        else if(rsTmpEquiv !=0 && rsEquiv != 0){
            if(rsTmpEquiv != rsEquiv){
                //池子合并
                int minn;
                int maxx;
                if(rsTmpEquiv < rsEquiv){
                    minn = rsTmpEquiv;
                    maxx = rsEquiv;
                }
                else{
                    maxx = rsTmpEquiv;
                    minn = rsEquiv;
                }
                //池子maxx装进池子minn
                HashSet<Integer> temp2Pool = equiPool.get(maxx - 1);
                HashSet<Integer> temp1Pool = equiPool.get(minn - 1);
                Iterator<Integer> it = temp2Pool.iterator();
                while (it.hasNext()) {
                    Integer tmp = it.next();
                    temp1Pool.add(tmp);
                    equiMapping.put(tmp,minn);
                }
                temp2Pool.clear();
            }
        }
        else if(rsTmpEquiv == 0){
            HashSet<Integer> tempPool = equiPool.get(rsEquiv - 1);
            tempPool.add(rsTmp);
            equiMapping.put(rsTmp,rsEquiv);
        }
        else if(rsEquiv == 0){
            HashSet<Integer> tempPool = equiPool.get(rsTmpEquiv - 1);
            tempPool.add(rs);
            equiMapping.put(rs,rsTmpEquiv);
        }
        //xinjia
        //TODO 该放哪里
        //rs 和 rsTmp是一个池子
        reFreshEquiRepre(rs);
//        reFreshEquiRepre(rsTmp);
    }

    private static void reFreshEquiRepre(int rs) {
        int poolIndex = equiMapping.get(rs)-1;
        HashSet<Integer> poolTmp = equiPool.get(poolIndex);
        int minNew = getMin(poolTmp);
        Iterator<Integer> samAsIter = poolTmp.iterator();
        while(samAsIter.hasNext()){
            int ii = samAsIter.next();
            if(equiRepresentation.containsKey(ii)){
                int tmp = equiRepresentation.get(ii);
                if(minNew != ii && minNew != tmp){
                    replaceWithMinIsp(ii, minNew);
                    replaceWithMinIop(ii, minNew);
                }
                else {
                    continue;
                }
            }
            else{
                if(minNew == ii){
                    equiRepresentation.put(ii, ii);
                }
                else{
                    equiRepresentation.put(ii, minNew);
                    replaceWithMinIsp(ii, minNew);
                    replaceWithMinIop(ii, minNew);
                }
            }
        }
    }

    private static void replaceWithMinIop(int ii, int minNew) {
        Map<Integer, List<IndexBean>> isp = IndexMap.getIsp();
        Map<Integer, List<IndexBean>> iop = IndexMap.getIop();
//        System.out.println(Dictionary.getDecode().get(ii));
//        System.out.println(Dictionary.getDecode().get(minNew));
        if(!iop.containsKey(ii)) return;
        List<IndexBean> indexBeanList = iop.get(ii);
        Iterator<IndexBean> indexBeanListIter = indexBeanList.iterator();
        while(indexBeanListIter.hasNext()){
            IndexBean beanTmp = indexBeanListIter.next();
            int rpTmp = beanTmp.getResource();
            int indexTmp = beanTmp.getIndex();
            int firstTripleIop = indexTmp;
            if(firstTripleIop == -1){ return; }
            DicRdfDataBean dicDataBeanIterator;
            int indexNew = firstTripleIop;
            do{
                dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
                indexNew = dicDataBeanIterator.getNop();
                int rs1 = dicDataBeanIterator.getRs();
                if(!DicRdfDataMap.checkDuplicate(rs1, rpTmp, minNew, isp)) {
                    //rs rp ro1
                    DicRdfDataMap.addNewRdfDataBean(isp, iop, rs1, rpTmp, minNew);
                }
            }while(indexNew != -1);
        }
    }

    private static void replaceWithMinIsp(int ii, int minNew) {
        Map<Integer, List<IndexBean>> isp = IndexMap.getIsp();
        Map<Integer, List<IndexBean>> iop = IndexMap.getIop();
        if(!isp.containsKey(ii)) return;
        List<IndexBean> indexBeanList = isp.get(ii);
        Iterator<IndexBean> indexBeanListIter = indexBeanList.iterator();
        while(indexBeanListIter.hasNext()){
            IndexBean beanTmp = indexBeanListIter.next();
            int rpTmp = beanTmp.getResource();
            int indexTmp = beanTmp.getIndex();
            int firstTripleIsp = indexTmp;
            if(firstTripleIsp == -1){ return; }
            DicRdfDataBean dicDataBeanIterator;
            int indexNew = firstTripleIsp;
            do{
                dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
                indexNew = dicDataBeanIterator.getNsp();
                int ro1 = dicDataBeanIterator.getRo();
                if(!DicRdfDataMap.checkDuplicate(minNew, rpTmp, ro1, isp)) {
                    //rs rp ro1
                    DicRdfDataMap.addNewRdfDataBean(isp, iop, minNew, rpTmp, ro1);
                }
            }while(indexNew != -1);
        }
    }



    private static int getMin(HashSet<Integer> poolTmp) {
        Iterator<Integer> i = poolTmp.iterator();
        int min = Integer.MAX_VALUE;
        int ii;
        while(i.hasNext()){
            ii = i.next();
            if(ii <= min){
                 min = ii;
            }
        }
        return min;
    }

    public static int findEquivPoolIndex(int rs) {
        if(equiMapping.containsKey(rs)){
            return equiMapping.get(rs);
        }
        else{
            equiMapping.put(rs,0);
            return 0;
        }
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
                if(class2 == 1){//TODO owl:Thing
                    exist++;
                }
                else{
                    int ro1 = dicDataBeanIterator.getRo();
                    if(DicRdfDataMap.checkDuplicate(ro1, typeEncode, class2, isp)) {
                        //rs rp ro1
                        exist++;
                    }
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
        if(flag1){
            if(class3 == 1){
                DicRdfDataMap.addNewRdfDataBean(isp, iop, rs, typeEncode, class1);
                return;
            }
            else{
                boolean flag2 = DicRdfDataMap.checkDuplicate(ro, typeEncode, class3, isp);
                if(flag2){
                    DicRdfDataMap.addNewRdfDataBean(isp, iop, rs, typeEncode, class1);
                }
            }
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
        if(class3 == 1){//owl:Thing
            DicRdfDataMap.addNewRdfDataBean(isp, iop, rs, typeEncode, class1);
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
            if(class2 == 1){//TODO owl:Thing 情况
                return;
            }
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
