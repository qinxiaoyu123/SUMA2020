package com.tju.gowl.reason;

import com.tju.gowl.bean.DicOwlBean;
import com.tju.gowl.bean.DicOwlMap;
import com.tju.gowl.dictionary.Dictionary;
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
                else{
                    System.out.println("未处理等价类");
                }
            }
            else if(owlBeanList.size() == 3){
                int class2 = owlBeanList.get(0).getRuleHead().get(0);
                int property = owlBeanList.get(1).getRuleHead().get(0);
                int class3 = owlBeanList.get(1).getRuleHead().get(1);
                int class4= owlBeanList.get(2).getRuleHead().get(1);
                addWomanCollege(class1, class2, property, class3, class4);
//                    System.out.println(Dictionary.getDecode().get(class2));
//                    System.out.println(Dictionary.getDecode().get(property));
//                    System.out.println(Dictionary.getDecode().get(class3));
//                    System.out.println(Dictionary.getDecode().get(class4));

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
//            System.out.println("class3==1: "+Dictionary.getDecode().get(class1));
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
//            System.out.println("class3==1: "+Dictionary.getDecode().get(class1));
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
