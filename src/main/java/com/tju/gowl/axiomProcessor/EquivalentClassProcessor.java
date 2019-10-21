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
                    class2 = Dictionary.readInHash(ax.toString());
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
        }

    }
    public static void OWLObjectSomeValuesFromProcessor(OWLClassExpression ax, int class1, int ip) {
        String property = ((OWLObjectSomeValuesFrom) ax).getProperty().toString();
        int propertyInt = Dictionary.readInHash(property);
        if(ip == 0){
            Processor.graph.addVertex(propertyInt);
            return;
        }
        String fillter = ((OWLObjectSomeValuesFrom) ax).getFiller().toString();
        int fillterInt = Dictionary.readInHash(fillter);
        DicOwlMap.addDicOwlObjectSomeValuesMap(Processor.ObjectSomeValuesFrom, class1, propertyInt, fillterInt);
        DicOwlMap.addEquiDicSomeValuesMap(class1, Processor.ObjectSomeValuesFrom, propertyInt, fillterInt);

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
        int class2 = Dictionary.readInHash(list.get(0));
        int class3 = Dictionary.readInHash(list.get(1));

        DicOwlMap.addDicOwlSubCLassMap(Processor.SubClassOf, class2, class1);
        DicOwlMap.addDicOwlSubCLassMap(Processor.SubClassOf, class3, class1);

    }
    public static void OWLObjectMinCardinalityProcessor(OWLClassExpression ax, int class1, int ip) {
        String property = ((OWLObjectMinCardinality) ax).getProperty().toString();
        int propertyInt = Dictionary.readInHash(property);
        if(ip == 0){
            Processor.graph.addVertex(propertyInt);
            return;
        }
        int cardinality = ((OWLObjectMinCardinality)ax).getCardinality();
        String class2 = ((OWLObjectMinCardinality) ax).getFiller().toString();
        int class2Int = Dictionary.readInHash(class2);

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
    public static void OWLObjectAllValuesFromProcessor(OWLClassExpression ax, int class1, int ip) {
        String property = ((OWLObjectAllValuesFrom)ax).getProperty().toString();
        int propertyInt = Dictionary.readInHash(property);
        if(ip == 0){
            Processor.graph.addVertex(propertyInt);
            return;
        }
        OWLClassExpression fillter = ((OWLObjectAllValuesFrom) ax).getFiller();
        int classAllValues = 0;
        if(fillter instanceof OWLObjectComplementOf){
            String cc = ((OWLObjectComplementOf) fillter).getOperand().toString();
            int ccInt = Dictionary.readInHash(cc);
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
                classAllValues = Dictionary.readInHash(ss.toString());
            }
            else{
                classAllValues = ccDisjoint;
            }
        }
        else if(fillter instanceof OWLClass){
            classAllValues = Dictionary.readInHash(fillter.toString());
        }
        int type = ax.typeIndex();
        DicOwlMap.addDicOwlObjectAllValuesMap(type, class1, propertyInt, classAllValues);
        DicOwlMap.addEquiDicAllVauleMap(class1,type, propertyInt, classAllValues);

    }
}
