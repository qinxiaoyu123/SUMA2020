package com.tju.suma.rewrite;

import com.tju.suma.bean.DicOwlBean;
import com.tju.suma.bean.DicOwlMap;
import com.tju.suma.dictionary.Dictionary;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EquiClassRuleRewrite {

    public static void main(String[] args) throws IOException, OWLOntologyCreationException {
        rewrite();
    }

    public static void rewrite() {
        Iterator<Map.Entry<Integer, List<DicOwlBean>>> iter = DicOwlMap.EquiDicRuleMap.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry<Integer, List<DicOwlBean>> iterEntity = iter.next();
            int class1 = iterEntity.getKey();
            List<DicOwlBean> owlBeanList = iterEntity.getValue();
            if(owlBeanList.size()==1){
//                System.out.println(Dictionary.getDecode().get(class1));
                DicOwlBean owlBean = owlBeanList.get(0);
                int type = owlBean.getType();
                if(type == 3005){//ObjectSomeValuesFrom  Dean> (ObjectSomeValuesFrom(<#isHeadOf> <#College>)) )
                    List<Integer> ruleHead = owlBean.getRuleHead();
                    addEquiSomeValue(class1, ruleHead.get(0), ruleHead.get(1));
                }
                else if(type == 3008){//ObjectMinCardinality
                    List<Integer> ruleHead = owlBean.getRuleHead();
                    addEquiMin(class1, ruleHead.get(0), ruleHead.get(1), ruleHead.get(2));
//                    System.out.println(ruleHead.get(0));
//                    System.out.println(Dictionary.getDecode().get(ruleHead.get(1)));
//                    System.out.println(Dictionary.getDecode().get(ruleHead.get(2)));
                }
            }
            else if(owlBeanList.size() == 2){
                if(owlBeanList.get(0).getType() == 2002 && owlBeanList.get(1).getType() == 3005){
//                    System.out.println("2size："+Dictionary.getDecode().get(class1));
                    int class2 = owlBeanList.get(0).getRuleHead().get(0);
                    int property = owlBeanList.get(1).getRuleHead().get(0);
                    int class3 = owlBeanList.get(1).getRuleHead().get(1);
                    addEquiClassSomeValue(class1, class2, property, class3);

                }
                else if(owlBeanList.get(0).getType() == 2002 && owlBeanList.get(1).getType() == 3006){//NonScienceStudent
//                    System.out.println("20023006size："+Dictionary.getDecode().get(class1));
                    int class2 = owlBeanList.get(0).getRuleHead().get(0);
                    int property = owlBeanList.get(1).getRuleHead().get(0);
                    int class3 = owlBeanList.get(1).getRuleHead().get(1);
                    addEquiNonScienceStudent(class1,class2, property,class3);
                }
                else if(owlBeanList.get(0).getType() == 3006 && owlBeanList.get(1).getType() == 3005){//GraduateStudent
//                    System.out.println("30063005size："+Dictionary.getDecode().get(class1));

                    int property = owlBeanList.get(0).getRuleHead().get(0);
                    int class3 = owlBeanList.get(0).getRuleHead().get(1);
                    addEquiGraduateStudent(class1,property,class3);
//                    int class4 = owlBeanList.get(1).getRuleHead().get(1);
//                    System.out.println("30063005size："+Dictionary.getDecode().get(property));
//                    System.out.println("30063005size："+Dictionary.getDecode().get(class3));
//                    System.out.println("30063005size："+Dictionary.getDecode().get(class4));
                }
                else if(owlBeanList.get(0).getType() == 2002 && owlBeanList.get(1).getType() == 2002){
                    int class2 = owlBeanList.get(0).getRuleHead().get(0);
                    int class3 = owlBeanList.get(1).getRuleHead().get(0);
                    System.out.println(find(class2)+" "+find(class3));
                    addQuietDestination(class1, class2, class3);
                }
                else{
                    System.out.println("未处理等价类：");
                }
            }
            else if(owlBeanList.size() == 3){
                if(owlBeanList.get(0).getType() == 2002 && owlBeanList.get(1).getType() == 3005 && owlBeanList.get(2).getType() == 3008){
                    //ObjectMinCardinality(1 <http://www.owl-ontologies.com/travel.owl#hasAccommodation>  ObjectMinCardinality(2 <http://www.owl-ontologies.com/travel.owl#hasActivity>
                    System.out.println("xinadd1");
                    int class2 = owlBeanList.get(0).getRuleHead().get(0);
                    int property1 = owlBeanList.get(1).getRuleHead().get(0);
                    int class3 = owlBeanList.get(1).getRuleHead().get(1);
                    int count = owlBeanList.get(2).getRuleHead().get(0);
                    int property2 = owlBeanList.get(2).getRuleHead().get(1);
                    int class4 = owlBeanList.get(2).getRuleHead().get(2);
                    System.out.println(find(class2)+" "+find(property1)+" "+find(class3)+" "+find(property2)+" "+find(class4));
                    addFamilyDestination(class1, class2, property1, class3, count, property2, class4);
                }
                else if(owlBeanList.get(0).getType() == 2002 && owlBeanList.get(1).getType() == 3005 && owlBeanList.get(2).getType() == 3005){
                    System.out.println("xinadd2");
                    int class2 = owlBeanList.get(0).getRuleHead().get(0);
                    int property1 = owlBeanList.get(1).getRuleHead().get(0);
                    int class3 = owlBeanList.get(1).getRuleHead().get(1);

                    int property2 = owlBeanList.get(2).getRuleHead().get(0);
                    int class4 = owlBeanList.get(2).getRuleHead().get(1);
                    addBudgetHotelDestination(class1, class2, property1, class3, property2, class4);
//                    System.out.println(find(class2)+" "+find(property1)+" "+find(class3)+" "+find(property2)+" "+find(class4));
                }
                else if(owlBeanList.get(0).getType() == 2002 && owlBeanList.get(1).getType() == 4000 && owlBeanList.get(2).getType() == 4000){
                    System.out.println("xinadd");
                    int class2 = owlBeanList.get(0).getRuleHead().get(0);
                    int property = owlBeanList.get(1).getRuleHead().get(0);
                    int indual1 = owlBeanList.get(1).getRuleHead().get(1);
                    int indual2 = owlBeanList.get(2).getRuleHead().get(1);
                    System.out.println(find(class2)+" "+find(property)+" "+find(indual1)+" "+find(indual2));
                    addBudgetAccommodation(class1, class2, property, indual1, indual2);

                }
                else {
                    int class2 = owlBeanList.get(0).getRuleHead().get(0);
                    int property = owlBeanList.get(1).getRuleHead().get(0);
                    int class3 = owlBeanList.get(1).getRuleHead().get(1);
                    int class4= owlBeanList.get(2).getRuleHead().get(1);
                    addWomanCollege(class1, class2, property, class3, class4);
                    System.out.println(find(class2)+" "+find(property)+" "+find(class3)+" "+find(class4));
                }

//                    System.out.println(Dictionary.getDecode().get(class2));
//                    System.out.println(Dictionary.getDecode().get(property));
//                    System.out.println(Dictionary.getDecode().get(class3));
//                    System.out.println(Dictionary.getDecode().get(class4));

            }
            else if(owlBeanList.size() == 4){
                if(owlBeanList.get(0).getType() == 2002 && owlBeanList.get(1).getType() == 3005 && owlBeanList.get(2).getType() == 3005 && owlBeanList.get(3).getType() == 3005){
                    System.out.println("xinadd3");
                    int class2 = owlBeanList.get(0).getRuleHead().get(0);
                    int property1 = owlBeanList.get(1).getRuleHead().get(0);
                    int class3 = owlBeanList.get(1).getRuleHead().get(1);
                    int property2 = owlBeanList.get(2).getRuleHead().get(0);
                    int class4 = owlBeanList.get(2).getRuleHead().get(1);
                    int class5 = owlBeanList.get(3).getRuleHead().get(1);
                    addBackpackersDestination(class1, class2, property1, class3, property2, class4, class5);
//                    System.out.println(find(class2)+" "+find(property1)+" "+find(class3)+" "+find(property2)+" "+find(class4)+" "+find(class5));
                }
            }
            else{
//                System.out.println("0size："+Dictionary.getDecode().get(class1));
            }

//            Iterator<DicOwlBean> owlBean = owlBeanList.iterator();
//            while(owlBean.hasNext()){
//                DicOwlBean tmp = owlBean.next();
//                int type = tmp.getType();
//
//            }
        }
    }

    private static void addBudgetAccommodation(int class1, int class2, int property, int indual1, int indual2) {
        DicOwlBean dicOwlBean = new DicOwlBean();
        dicOwlBean.setType(38);//class2
        dicOwlBean.setRuleHead(class1, class2, property, indual1, indual2);
        StringBuffer ssbuff = new StringBuffer("*0");
        String key = ssbuff.append(class2).toString();
        DicOwlMap.addDicOwlMap(dicOwlBean, key);

        DicOwlBean dicOwlBean1 = new DicOwlBean();
        dicOwlBean1.setType(39);//class2
        dicOwlBean1.setRuleHead(class1, class2, property, indual1, indual2);
        StringBuffer ssbuff1 = new StringBuffer("*");
        String key1 = ssbuff1.append(property).append("*").toString();
        DicOwlMap.addDicOwlMap(dicOwlBean1, key1);
    }

    private static void addBackpackersDestination(int class1, int class2, int property1, int class3, int property2, int class4, int class5) {
        System.out.println(find(class2)+" "+find(property1)+" "+find(class3)+" "+find(property2)+" "+find(class4)+" "+find(class5));
        DicOwlBean dicOwlBean = new DicOwlBean();
        dicOwlBean.setType(32);//class2
        dicOwlBean.setRuleHead(class1, class2, property1, class3, property2, class4, class5);
        StringBuffer ssbuff = new StringBuffer("*0");
        String key = ssbuff.append(class2).toString();
        DicOwlMap.addDicOwlMap(dicOwlBean, key);

        DicOwlBean dicOwlBean1 = new DicOwlBean();
        dicOwlBean1.setType(33);//r
        dicOwlBean1.setRuleHead(class1, class2, property1, class3, property2, class4, class5);
        StringBuffer ssbuff1 = new StringBuffer("*");
        String key1 = ssbuff1.append(property1).append("*").toString();
        DicOwlMap.addDicOwlMap(dicOwlBean1, key1);

        DicOwlBean dicOwlBean2 = new DicOwlBean();
        dicOwlBean2.setType(34);//class3
        dicOwlBean2.setRuleHead(class1, class2, property1, class3, property2, class4, class5);
        StringBuffer ssbuff2 = new StringBuffer("*0");
        String key2 = ssbuff2.append(class3).toString();
        DicOwlMap.addDicOwlMap(dicOwlBean2, key2);

        DicOwlBean dicOwlBean3 = new DicOwlBean();
        dicOwlBean3.setType(35);//r
        dicOwlBean3.setRuleHead(class1, class2, property1, class3, property2, class4, class5);
        StringBuffer ssbuff3 = new StringBuffer("*");
        String key3 = ssbuff3.append(property2).append("*").toString();
        DicOwlMap.addDicOwlMap(dicOwlBean3, key3);

        DicOwlBean dicOwlBean4 = new DicOwlBean();
        dicOwlBean4.setType(36);//class3
        dicOwlBean4.setRuleHead(class1, class2, property1, class3, property2, class4, class5);
        StringBuffer ssbuff4 = new StringBuffer("*0");
        String key4 = ssbuff4.append(class4).toString();
        DicOwlMap.addDicOwlMap(dicOwlBean4, key4);

        DicOwlBean dicOwlBean5 = new DicOwlBean();
        dicOwlBean5.setType(37);//class3
        dicOwlBean5.setRuleHead(class1, class2, property1, class3, property2, class4, class5);
        StringBuffer ssbuff5 = new StringBuffer("*0");
        String key5 = ssbuff5.append(class5).toString();
        DicOwlMap.addDicOwlMap(dicOwlBean5, key5);
    }

    private static void addBudgetHotelDestination(int class1, int class2, int property1, int class3, int property2, int class4) {
        DicOwlBean dicOwlBean = new DicOwlBean();
        dicOwlBean.setType(28);//class2
        dicOwlBean.setRuleHead(class1, class2, property1, class3, property2, class4);
        StringBuffer ssbuff = new StringBuffer("*0");
        String key = ssbuff.append(class2).toString();
        DicOwlMap.addDicOwlMap(dicOwlBean, key);

        DicOwlBean dicOwlBean1 = new DicOwlBean();
        dicOwlBean1.setType(29);//r
        dicOwlBean1.setRuleHead(class1, class2, property1, class3, property2, class4);
        StringBuffer ssbuff1 = new StringBuffer("*");
        String key1 = ssbuff1.append(property1).append("*").toString();
        DicOwlMap.addDicOwlMap(dicOwlBean1, key1);

        DicOwlBean dicOwlBean2 = new DicOwlBean();
        dicOwlBean2.setType(30);//class3
        dicOwlBean2.setRuleHead(class1, class2, property1, class3, property2, class4);
        StringBuffer ssbuff2 = new StringBuffer("*0");
        String key2 = ssbuff2.append(class3).toString();
        DicOwlMap.addDicOwlMap(dicOwlBean2, key2);

        DicOwlBean dicOwlBean3 = new DicOwlBean();
        dicOwlBean3.setType(31);//class3
        dicOwlBean3.setRuleHead(class1, class2, property1, class3, property2, class4);
        StringBuffer ssbuff3 = new StringBuffer("*0");
        String key3 = ssbuff3.append(class4).toString();
        DicOwlMap.addDicOwlMap(dicOwlBean3, key3);
    }

    private static void addQuietDestination(int class1, int class2, int class3) {
        DicOwlBean dicOwlBean = new DicOwlBean();
        dicOwlBean.setType(26);//class2
        dicOwlBean.setRuleHead(class1, class2, class3);
        StringBuffer ssbuff = new StringBuffer("*0");
        String key = ssbuff.append(class2).toString();
        DicOwlMap.addDicOwlMap(dicOwlBean, key);

        DicOwlBean dicOwlBean1 = new DicOwlBean();
        dicOwlBean1.setType(27);//class3
        dicOwlBean1.setRuleHead(class1, class2, class3);
        StringBuffer ssbuff1 = new StringBuffer("*0");
        String key1 = ssbuff1.append(class3).toString();
        DicOwlMap.addDicOwlMap(dicOwlBean1, key1);
    }

    private static void addFamilyDestination(int class1, int class2, int property1, int class3, int count, int property2, int class4) {
        DicOwlBean dicOwlBean = new DicOwlBean();
        dicOwlBean.setType(23);//class2
        dicOwlBean.setRuleHead(class1, class2, property1, class3, count, property2, class4);
        StringBuffer ssbuff = new StringBuffer("*0");
        String key = ssbuff.append(class2).toString();
        DicOwlMap.addDicOwlMap(dicOwlBean, key);

        DicOwlBean dicOwlBean1 = new DicOwlBean();
        dicOwlBean1.setType(24);//r
        dicOwlBean1.setRuleHead(class1, class2, property1, class3, count, property2, class4);
        StringBuffer ssbuff1 = new StringBuffer("*");
        String key1 = ssbuff1.append(property1).append("*").toString();
        DicOwlMap.addDicOwlMap(dicOwlBean1, key1);

        DicOwlBean dicOwlBean2 = new DicOwlBean();
        dicOwlBean2.setType(25);//class3
        dicOwlBean2.setRuleHead(class1, class2, property1, class3, count, property2, class4);
        StringBuffer ssbuff2 = new StringBuffer("*");
        String key2 = ssbuff2.append(property2).append("*").toString();
        DicOwlMap.addDicOwlMap(dicOwlBean2, key2);
    }

    public static String find(int class2) {
        String ss = null;
        Map<String, Integer> encode = Dictionary.getEncode();
        Iterator<Map.Entry<String, Integer>> iter = encode.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry<String, Integer> tmp = iter.next();
            String ssTmp = tmp.getKey();
            int valueTmp = tmp.getValue();
            if(valueTmp == class2){
                return ssTmp;
            }
        }
        return ss;

    }



    private static void addEquiNonScienceStudent(int class1, int class2, int r, int class3) {
        DicOwlBean dicOwlBean = new DicOwlBean();
        dicOwlBean.setType(20);//class2
        dicOwlBean.setRuleHead(class1,class2,r,class3);
        StringBuffer ssbuff = new StringBuffer("*0");
        String key = ssbuff.append(class2).toString();
        DicOwlMap.addDicOwlMap(dicOwlBean, key);

        DicOwlBean dicOwlBean1 = new DicOwlBean();
        dicOwlBean1.setType(21);//r
        dicOwlBean1.setRuleHead(class1,class2,r,class3);
        StringBuffer ssbuff1 = new StringBuffer("*");
        String key1 = ssbuff1.append(r).append("*").toString();
        DicOwlMap.addDicOwlMap(dicOwlBean1, key1);
        if(class3 == 1){//owl:Thing
            System.out.println("class3==1: "+Dictionary.getDecode()[class1]);
            return;
        }
        DicOwlBean dicOwlBean2 = new DicOwlBean();
        dicOwlBean2.setType(22);//class3
        dicOwlBean2.setRuleHead(class1,class2,r,class3);
        StringBuffer ssbuff2 = new StringBuffer("*0");
        String key2 = ssbuff2.append(class3).toString();
        DicOwlMap.addDicOwlMap(dicOwlBean2, key2);
    }

    private static void addEquiGraduateStudent(int class1, int property, int class3) {
        DicOwlBean dicOwlBean = new DicOwlBean();
        dicOwlBean.setType(18);//class2
        dicOwlBean.setRuleHead(class1,property,class3);
        StringBuffer ssbuff = new StringBuffer("*");
        String key = ssbuff.append(property).append("*").toString();
        DicOwlMap.addDicOwlMap(dicOwlBean, key);

        DicOwlBean dicOwlBean2 = new DicOwlBean();
        dicOwlBean2.setType(19);//class3
        dicOwlBean2.setRuleHead(class1,property,class3);
        StringBuffer ssbuff2 = new StringBuffer("*0");
        String key2 = ssbuff2.append(class3).toString();
        DicOwlMap.addDicOwlMap(dicOwlBean2, key2);
    }

    private static void addEquiClassSomeValue(int class1, int class2, int r, int class3) {
        DicOwlBean dicOwlBean = new DicOwlBean();
        dicOwlBean.setType(0);//class2
        dicOwlBean.setRuleHead(class1,class2,r,class3);
        StringBuffer ssbuff = new StringBuffer("*0");
        String key = ssbuff.append(class2).toString();
        DicOwlMap.addDicOwlMap(dicOwlBean, key);

        DicOwlBean dicOwlBean1 = new DicOwlBean();
        dicOwlBean1.setType(1);//r
        dicOwlBean1.setRuleHead(class1,class2,r,class3);
        StringBuffer ssbuff1 = new StringBuffer("*");
        String key1 = ssbuff1.append(r).append("*").toString();
        DicOwlMap.addDicOwlMap(dicOwlBean1, key1);
        if(class3 == 1){//owl:Thing
//            System.out.println("class3==1: "+Dictionary.getDecode()[class1]);
            return;
        }
        DicOwlBean dicOwlBean2 = new DicOwlBean();
        dicOwlBean2.setType(2);//class3
        dicOwlBean2.setRuleHead(class1,class2,r,class3);
        StringBuffer ssbuff2 = new StringBuffer("*0");
        String key2 = ssbuff2.append(class3).toString();
        DicOwlMap.addDicOwlMap(dicOwlBean2, key2);

    }

    private static void addWomanCollege(int class1, int class2, int property, int class3, int class4) {
        DicOwlBean dicOwlBean = new DicOwlBean();
        dicOwlBean.setType(14);
        dicOwlBean.setRuleHead(class1, class2, property, class3, class4);
        StringBuffer ssbuff = new StringBuffer("*0");
        String key = ssbuff.append(class2).toString();
        DicOwlMap.addDicOwlMap(dicOwlBean, key);

        DicOwlBean dicOwlBean1 = new DicOwlBean();
        dicOwlBean1.setType(15);
        dicOwlBean1.setRuleHead(class1, class2, property, class3, class4);
        StringBuffer ssbuff1 = new StringBuffer("*");
        String key1 = ssbuff1.append(property).append("*").toString();
        DicOwlMap.addDicOwlMap(dicOwlBean1, key1);

        DicOwlBean dicOwlBean2 = new DicOwlBean();
        dicOwlBean2.setType(16);
        dicOwlBean2.setRuleHead(class1, class2, property, class3, class4);
        StringBuffer ssbuff2 = new StringBuffer("*0");
        String key2 = ssbuff2.append(class3).toString();
        DicOwlMap.addDicOwlMap(dicOwlBean2, key2);

        DicOwlBean dicOwlBean3 = new DicOwlBean();
        dicOwlBean3.setType(17);
        dicOwlBean3.setRuleHead(class1, class2, property, class3, class4);
        StringBuffer ssbuff3 = new StringBuffer("*0");
        String key3 = ssbuff3.append(class4).toString();
        DicOwlMap.addDicOwlMap(dicOwlBean3, key3);
    }

    //TODO Thing
    private static void addEquiMin(int class1, int count, int property, int class2) {
        DicOwlBean dicOwlBean = new DicOwlBean();
        dicOwlBean.setType(12);
        dicOwlBean.setRuleHead(class1, count, property, class2);
        StringBuffer ssbuff = new StringBuffer("*");
        String key = ssbuff.append(property).append("*").toString();
        DicOwlMap.addDicOwlMap(dicOwlBean, key);
//        System.out.println(key);
//        System.out.println(dicOwlBean);
        if (class2 == 1 ) return;
        DicOwlBean dicOwlBean1 = new DicOwlBean();
        dicOwlBean1.setType(13);//class2
        dicOwlBean1.setRuleHead(class1, count, property, class2);
        StringBuffer ssbuff1 = new StringBuffer("*0");
        String key1 = ssbuff1.append(class2).toString();
        DicOwlMap.addDicOwlMap(dicOwlBean1, key1);

    }

    private static void addEquiSomeValue(int class1, int property, int class2) {
        DicOwlBean dicOwlBean = new DicOwlBean();
        dicOwlBean.setType(10);//class2
        dicOwlBean.setRuleHead(class1, property, class2);
        StringBuffer ssbuff = new StringBuffer("*");
        String key = ssbuff.append(property).append("*").toString();
        DicOwlMap.addDicOwlMap(dicOwlBean, key);

        DicOwlBean dicOwlBean1 = new DicOwlBean();
        dicOwlBean1.setType(11);//class2
        dicOwlBean1.setRuleHead(class1, property, class2);
        StringBuffer ssbuff1 = new StringBuffer("*0");
        String key1 = ssbuff1.append(class2).toString();
        DicOwlMap.addDicOwlMap(dicOwlBean1, key1);
    }


}
