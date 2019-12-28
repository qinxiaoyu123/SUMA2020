package com.tju.gowl.reason;

import com.tju.gowl.bean.*;
import com.tju.gowl.dictionary.Dictionary;
import com.tju.gowl.index.ThreeKeyMap;
import com.tju.gowl.index.TwoKeyMap;
import com.tju.gowl.index.TwoKeyMap;
import com.tju.gowl.index.TwoKeyMap;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DicSerialReason {
    public static int anonymous = -2;
    public static final int typeEncode = 0;
    private static StringBuffer sameAsString = new StringBuffer("sameAs");
    private static int sameAsInt = 0;
    public static int someValue = 1;
    public static boolean someValueFlag = false;



    public static void reason(int n) throws IOException {
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

        long startTime1 = System.currentTimeMillis();

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
                                switchReasonType(totalData, stashMap, iteratorMap, Rs, Rp, Ro, type, head);

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
                System.out.println("No new data was generated!");
                totalData.putAll(iteratorMap);
                stashMap.clear();
                iteratorMap.clear();
//                System.out.println("total data size"+totalData.size());
                break;
            }
            if(someValue>=20){
                System.out.println(20+"-step universal model is finished");
                System.out.println("someValue"+someValue);
                totalData.putAll(stashMap);
                totalData.putAll(iteratorMap);
                stashMap.clear();
                iteratorMap.clear();
//                System.out.println("total data size"+totalData.size());
                break;
            }
            if(loopCount>=n){
                System.out.println(n+"-step universal model is finished");
//                System.out.println("someValue"+someValue);
                totalData.putAll(stashMap);
                totalData.putAll(iteratorMap);
                stashMap.clear();
                iteratorMap.clear();
//                System.out.println("total data size: "+totalData.size());
                break;
            }
            totalData.putAll(iteratorMap);
            iteratorMap.clear();
            iteratorMap.putAll(stashMap);
            stashMap.clear();
            loopCount++;

        }

//        outEquiPool();
//        outEquiMapping();

    }

    private static void switchReasonType(Map<Integer, DicRdfDataBean> totalData, Map<Integer, DicRdfDataBean> stashMap, Map<Integer, DicRdfDataBean> iteratorMap, int rs, int rp, int ro, int type, List<Integer> head) {
        switch (type){
            case 2013://SubObjectPropertyOf 2013
                SubPropertyReason.reason(totalData, iteratorMap, stashMap, rs, head, ro);
                break;
            case 2022://ObjectPropertyDomain 2022
                BasicReason.reason(totalData, iteratorMap, stashMap, rs, typeEncode, head);
                break;
            case 2023://ObjectPropertyRange 2023
                BasicReason.reason(totalData, iteratorMap, stashMap, ro, typeEncode, head);
                break;
            case 2002://SubClassOf 2002
                BasicReason.reason(totalData, iteratorMap, stashMap, rs, typeEncode, head);
                break;
            case 3005:
                ObjectSomeValuesFromReason.reason(totalData, iteratorMap, stashMap, rs, head);
                break;
            case 0:
                QueryReason.equivalentClass2Reason(rs, head);
                break;
            case 1:
                QueryReason.equivalentRoleReason(rs, ro, head);
                break;
            case 2:
                QueryReason.equivalentClass3Reason(rs, head);
                break;
            case 2019:
                TransitiveReason.reason(rs, rp, ro);
                break;
            case 3006:
                ObjectAllValuesFromReason.reason(rs, head);
                break;
            case 3007:
                ObjectHasValueReason.reason(totalData, iteratorMap, stashMap, rs, head);
                break;
            case 3008:
                ObjectMinCardinalityReason.reason(rs, head);
                break;
            case -3006://ObjectAllValuesFrom
                ObjectAllValuesFromReason.inverseReason(rs, head);
                break;
            case 2017:
                SymmetricObjectPropertyReason.reason(rs, rp, ro);
                break;
            case 2016:
                FunctionalObjectPropertyReason.inverseReason(rs, rp, ro);
                break;
            case 2015:
                FunctionalObjectPropertyReason.reason(rs, rp, ro);
                break;
            case 10:
                QueryReason.type10Reason(rs, rp, ro, head);
                break;
            case 11:
                QueryReason.type11Reason(rs, rp, ro, head);
                break;
            case 12:
                try {
                    QueryReason.type12Reason(rs, rp, ro, head);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 14:
                QueryReason.type14Reason(rs, rp, ro, head);
                break;
            case 15:
                QueryReason.type15Reason(rs, rp, ro, head);
                break;
            case 16:
                QueryReason.type16Reason(rs, rp, ro, head);
                break;
            case 17:
                QueryReason.type16Reason(rs, rp, ro, head);
                break;
            case 18:
                QueryReason.type18Reason(rs, rp, ro, head);
                break;
            case 19:
                QueryReason.type19Reason(rs, rp, ro, head);
                break;
            case 20:
                QueryReason.type20Reason(rs, rp, ro, head);
                break;
            case 21:
                QueryReason.type21Reason(rs, rp, ro, head);
                break;
            case 22:
                QueryReason.type22Reason(rs, rp, ro, head);
                break;
            case 23:
                QueryReason.type23Reason(rs, rp, ro, head);
                break;
            case 24:
                QueryReason.type24Reason(rs, rp, ro, head);
                break;
            case 25:
                QueryReason.type25Reason(rs, rp, ro, head);
                break;
            case 26:
                QueryReason.type26Reason(rs, rp, ro, head);
                break;
            case 27:
                QueryReason.type27Reason(rs, rp, ro, head);
                break;
            case 28:
                QueryReason.type28Reason(rs, rp, ro, head);
                break;
            case 29:
                QueryReason.type29Reason(rs, rp, ro, head);
                break;
            case 30:
                QueryReason.type30Reason(rs, rp, ro, head);
                break;
            case 31:
                QueryReason.type31Reason(rs, rp, ro, head);
                break;
            case 32:
                QueryReason.type32Reason(rs, rp, ro, head);
                break;
            case 33:
                QueryReason.type33Reason(rs, rp, ro, head);
                break;
            case 34:
                QueryReason.type34Reason(rs, rp, ro, head);
                break;
            case 35:
                QueryReason.type35Reason(rs, rp, ro, head);
                break;
            case 36:
                QueryReason.type36Reason(rs, rp, ro, head);
                break;
            case 37:
                QueryReason.type37Reason(rs, rp, ro, head);
                break;
            case 38:
                QueryReason.type38Reason(rs, rp, ro, head);
                break;
            case 39:
                QueryReason.type39Reason(rs, rp, ro, head);
                break;
        }
    }


    private static void convertDataToRuleKey(List<String> ruleKey, int rs, int rp, int ro) {
        StringBuffer ssbuff = new StringBuffer();
        String key1 = ssbuff.append("*").append(rp).append(ro).toString();
        ruleKey.add(key1);
        ssbuff.setLength(0);
        String key2 = ssbuff.append("*").append(rp).append("*").toString();
        ruleKey.add(key2);
    }
}
