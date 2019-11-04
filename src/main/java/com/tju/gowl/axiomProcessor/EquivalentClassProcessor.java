package com.tju.gowl.axiomProcessor;

import com.tju.gowl.bean.DicOwlMap;
import com.tju.gowl.bean.DisjointClassesMap;
import com.tju.gowl.dictionary.Dictionary;
import org.semanticweb.owlapi.model.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EquivalentClassProcessor {

    public static void OWLObjectIntersectionOfProcessor(OWLClassExpression axiom, int class1, int ip) {
        Iterator<OWLClassExpression> iterator = ((OWLObjectIntersectionOf)axiom).getOperandsAsList().iterator();
        int class2 = 0;
        while(iterator.hasNext()) {
            OWLClassExpression ax = iterator.next();
            if (ax instanceof OWLClass) {
                if(ip ==1){
                    class2 = Dictionary.encodeRdf(ax.toString());
                    DicOwlMap.addDicOwlSubCLassMap(Processor.SubClassOf, class1, class2);
                    DicOwlMap.addEquiSubClassOfRuleMap(class1,Processor.SubClassOf,class2);
                }
            }
            else if(ax instanceof OWLObjectAllValuesFrom){
                EquivalentClassProcessor.OWLObjectAllValuesFromProcessor(ax, class1, ip);
            }else if (ax instanceof OWLObjectMinCardinality) {
                EquivalentClassProcessor.OWLObjectMinCardinalityProcessor(ax, class1, ip);
            } else if (ax instanceof OWLObjectUnionOf) {
                EquivalentClassProcessor.OWLObjectUnionOfProcessor(ax, class1, ip);
            } else if (ax instanceof OWLObjectSomeValuesFrom){
                EquivalentClassProcessor.OWLObjectSomeValuesFromProcessor(ax, class1, ip);
            }
            else if (ax instanceof OWLObjectComplementOf){
                if(ip ==1){
                    class2 = getComplementClass ((OWLObjectComplementOf) ax);
                    DicOwlMap.addDicOwlSubCLassMap(Processor.SubClassOf, class1, class2);
                    DicOwlMap.addEquiSubClassOfRuleMap(class1,Processor.SubClassOf,class2);
                }
            }
            else{
                System.out.println("未处理等价交类型："+ax.toString());
            }
        }

    }
    public static void OWLObjectSomeValuesFromProcessor(OWLClassExpression ax, int class1, int ip) {
        String property = ((OWLObjectSomeValuesFrom) ax).getProperty().toString();
        int propertyInt = Dictionary.encodeRdf(property);
        if(ip == 0){
            Processor.graph.addVertex(propertyInt);
            return;
        }
        OWLClassExpression axTmp = ((OWLObjectSomeValuesFrom) ax).getFiller();
        String fillter = axTmp.toString();
        if(axTmp instanceof OWLClass){
            int fillterInt = Dictionary.encodeRdf(fillter);
            DicOwlMap.addDicOwlObjectSomeValuesMap(Processor.ObjectSomeValuesFrom, class1, propertyInt, fillterInt);
            DicOwlMap.addEquiDicSomeValuesMap(class1, Processor.ObjectSomeValuesFrom, propertyInt, fillterInt);
        }
        else{
            if(axTmp instanceof OWLObjectIntersectionOf){
                Iterator<OWLClassExpression> iterator = ((OWLObjectIntersectionOf) axTmp).getOperandsAsList().iterator();
                while(iterator.hasNext()){
                    OWLClassExpression classTmp = iterator.next();
                    String classString = classTmp.toString();
                    int classInt = Dictionary.encodeRdf(classString);
                    if(classTmp instanceof OWLClass){
                        DicOwlMap.addDicOwlObjectSomeValuesMap(Processor.ObjectSomeValuesFrom, class1, propertyInt, classInt);
                        DicOwlMap.addEquiDicSomeValuesMap(class1, Processor.ObjectSomeValuesFrom, propertyInt, classInt);
                    }
                    else{
                        System.out.println("未处理OWLObjectSomeValues 71"+ax.toString());
                    }
                }

            }
            else if(axTmp instanceof OWLObjectUnionOf){
                //TODO 近似
//                System.out.println("qxy "+axTmp.toString());
                Iterator<OWLClassExpression> iterator = ((OWLObjectUnionOf) axTmp).getOperandsAsList().iterator();
                while(iterator.hasNext()){
                    OWLClassExpression classTmp = iterator.next();
                    String classString = classTmp.toString();
                    int classInt = Dictionary.encodeRdf(classString);
                    if(classTmp instanceof OWLClass){
                        DicOwlMap.addEquiDicSomeValuesMap(class1, Processor.ObjectSomeValuesFrom, propertyInt, classInt);
                    }
                    else{
                        System.out.println("未处理OWLObjectSomeValues 90"+ax.toString());
                    }
                }

            }
            else if(axTmp instanceof OWLHasValueRestriction){
                String proString = ((OWLHasValueRestriction) axTmp).getProperty().toString();

                String fillterString = ((OWLHasValueRestriction) axTmp).getFiller().toString();
                int proInt = Dictionary.encodeRdf(proString);
                int fillterInt = Dictionary.encodeRdf(fillterString);

            }
            else if(axTmp instanceof OWLObjectOneOf){
                System.out.println("qxy "+axTmp.toString());
            }
            else{
                System.out.println("未处理OWLObjectSomeValues "+ax.toString());
            }

        }

    }
    public static void OWLObjectUnionOfProcessor(OWLClassExpression ax, int class1, int ip) {
        if(ip == 0) return;
        Pattern p = Pattern.compile("\\<(.*?)\\>");//正则表达式，取=和|之间的字符串，不包括=和|
        Matcher mm = p.matcher(ax.toString());
        List<String> list = new ArrayList<>();
        while(mm.find()) {
            list.add(mm.group(0));
            //   System.out.println(mm.group(ip));//m.group(0)包括这两个字符
        }
        int class2 = Dictionary.encodeRdf(list.get(0));
        int class3 = Dictionary.encodeRdf(list.get(1));

        DicOwlMap.addDicOwlSubCLassMap(Processor.SubClassOf, class2, class1);
        DicOwlMap.addDicOwlSubCLassMap(Processor.SubClassOf, class3, class1);

    }
    public static void OWLObjectMinCardinalityProcessor(OWLClassExpression ax, int class1, int ip) {
        String property = ((OWLObjectMinCardinality) ax).getProperty().toString();
        int propertyInt = Dictionary.encodeRdf(property);
        if(ip == 0){
            Processor.graph.addVertex(propertyInt);
            return;
        }
        int cardinality = ((OWLObjectMinCardinality)ax).getCardinality();
        if(((OWLObjectMinCardinality) ax).getFiller() instanceof OWLClass){
//            System.out.println("MinCardinality： "+ax.toString());
            String class2 = ((OWLObjectMinCardinality) ax).getFiller().toString();
            int class2Int = Dictionary.encodeRdf(class2);

            //>=1 == ObjectSomeValuesFrom
            if(cardinality == 1){
                DicOwlMap.addEquiDicSomeValuesMap(class1, Processor.ObjectSomeValuesFrom, propertyInt, class2Int);
                DicOwlMap.addDicOwlObjectSomeValuesMap(Processor.ObjectSomeValuesFrom, class1, propertyInt, class2Int);
            }
            else if(cardinality >1){
//            3008
                DicOwlMap.addEquiMinCardinalityMap(class1, Processor.ObjectMinCardinality, cardinality, propertyInt, class2Int);
                DicOwlMap.addDicOwlMinCardinalityMap(Processor.ObjectMinCardinality, class1, cardinality, propertyInt, class2Int);
            }
        }
        else{
            System.out.println("未处理MinCardinality： "+ax.toString());
        }

    }
    public static void OWLObjectAllValuesFromProcessor(OWLClassExpression ax, int class1, int ip) {
        String property = ((OWLObjectAllValuesFrom)ax).getProperty().toString();
        int propertyInt = Dictionary.encodeRdf(property);
        if(ip == 0){
            Processor.graph.addVertex(propertyInt);
            return;
        }
        OWLClassExpression fillter = ((OWLObjectAllValuesFrom) ax).getFiller();
        int classAllValues = 0;
        if(fillter instanceof OWLObjectComplementOf){
            classAllValues = getComplementClass((OWLObjectComplementOf) fillter);
        }
        else if(fillter instanceof OWLClass){
            classAllValues = Dictionary.encodeRdf(fillter.toString());
        }
        int type = ax.typeIndex();
        DicOwlMap.addDicOwlObjectAllValuesMap(type, class1, propertyInt, classAllValues);
        DicOwlMap.addEquiDicAllVauleMap(class1,type, propertyInt, classAllValues);

    }

    private static int getComplementClass(OWLObjectComplementOf fillter) {
        int classAllValues;
        String cc = fillter.getOperand().toString();
        int ccInt = Dictionary.encodeRdf(cc);
        int ccDisjoint = DisjointClassesMap.getDisjointClassesMap(ccInt);
        //TODO 这里把DisjointClassesMap看作补集，不严格，消除补
        if(ccDisjoint == -1){
            StringTokenizer st = new StringTokenizer(cc, "#");
            List<String> list=new ArrayList<>();
            while(st.hasMoreElements()) {
                list.add(st.nextToken());
            }
            StringBuffer ss = new StringBuffer(list.get(0));
            ss.append("#ComplementOf").append(list.get(1));
            classAllValues = Dictionary.encodeRdf(ss.toString());
            System.out.println("qinxioayu"+ss.toString());
        }
        else{
            classAllValues = ccDisjoint;
        }
        return classAllValues;
    }
}
