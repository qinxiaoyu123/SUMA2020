package com.tju.gowl.axiomProcessor;

import com.tju.gowl.bean.DicOwlMap;
import com.tju.gowl.bean.DisjointClassesMap;
import com.tju.gowl.bean.EquivalentPropertyMap;
import com.tju.gowl.bean.InversePropertyMap;
import com.tju.gowl.dictionary.Dictionary;
import com.tju.gowl.io.DictionaryInput;
import com.tju.gowl.io.DictionaryInputNew;
import com.tju.gowl.rank.unDirectedGraph;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;

public class Processor {
    public static List<Integer> classAssertion = new ArrayList<>();
    public static final int ObjectPropertyRange = 2023;
    public static final int ObjectPropertyDomain = 2022;
    public static final int SubClassOf = 2002;
    public static final int SubObjectPropertyOf = 2013;
    public static final int TransitiveProperty = 2019;
    public static final int SymmetricObjectProperty = 2017;
    public static final int InverseFunctionalObjectProperty = 2016;
    public static final int FunctionalObjectProperty = 2015;
    public static final int ObjectSomeValuesFrom = 3005;
    public static final int ObjectAllValuesFrom = 3006;
    public static final int ObjectMinCardinality = 3008;
    public static final int OWLClassAssertion = 2005;
    public static final int EquivalentClass = 2001;
    public static final int InverseProperty = 2014;
    public static final int EquivalentProperty = 2012;
    public static final int OWLDisjointClassesAxiom = 2003;
    public static final unDirectedGraph.Graph graph = new unDirectedGraph.Graph();
    public static final Map<Integer,Integer> typeInverse = new ConcurrentHashMap<Integer, Integer>(){{
        put(ObjectPropertyRange,ObjectPropertyDomain);
        put(ObjectPropertyDomain,ObjectPropertyRange);
        put(SubObjectPropertyOf,SubObjectPropertyOf);
        put(TransitiveProperty,TransitiveProperty);
        put(SymmetricObjectProperty,SymmetricObjectProperty);
        put(InverseFunctionalObjectProperty,FunctionalObjectProperty);
        put(FunctionalObjectProperty,InverseFunctionalObjectProperty);
        put(ObjectSomeValuesFrom,-ObjectSomeValuesFrom);
        put(ObjectAllValuesFrom,-ObjectAllValuesFrom);
        put(ObjectMinCardinality,-ObjectMinCardinality);
    }} ;
    public static void OWLClassAssertionProcessor(OWLClassAssertionAxiom axiom, int ip) {
        //                OWLClassAssertionAxiom2005
        if(ip == 0){
            return;
        }
//        System.out.println("OWLClassAssertionProcessor "+ip);
        String class1 = axiom.getClassExpression().toString();
        String individual = axiom.getIndividual().toString();
        int classInt = Dictionary.encodeRdf(class1);
        int individualInt = Dictionary.encodeRdf(individual);
        classAssertion.add(individualInt);
        classAssertion.add(classInt);
    }

    public static void FunctionalPropertyProcessor(OWLAxiom axiom, int ip) {
        //OWLFunctionalObjectPropertyAxiom2015
        int type = axiom.typeIndex();
        String property = ((OWLFunctionalObjectPropertyAxiom) axiom).getProperty().toString();
        int pro = Dictionary.encodeRdf(property);
        if(ip == 0){
            graph.addVertex(pro);
        }
        else{
            DicOwlMap.addDicOwlFunctionalPropertyMap(type, pro);
        }
    }

    public static void InverseFunctionalProcessor(OWLAxiom axiom, int ip) {
//        OWLInverseFunctionalObjectPropertyAxiom2016
        int type = axiom.typeIndex();
        String property = ((OWLInverseFunctionalObjectPropertyAxiom) axiom).getProperty().toString();
        int pro = Dictionary.encodeRdf(property);
        if(ip == 0){
            graph.addVertex(pro);
        }
        else{
            DicOwlMap.addDicOwlInverseFunctionalMap(type, pro);
        }
    }

    public static void OWLSymmetricObjectPropertyProcessor(OWLAxiom axiom, int ip) {
        int type = axiom.typeIndex();
        String property = ((OWLSymmetricObjectPropertyAxiom) axiom).getProperty().toString();
        int pro = Dictionary.encodeRdf(property);
        if(ip == 0){
            graph.addVertex(pro);
        }
        else{
            DicOwlMap.addDicOwlSymmetricPropertyMap(type, pro);
        }
    }

    public static void OWLTransitiveObjectPropertyProcessor(OWLAxiom axiom, int ip) {
        int type = axiom.typeIndex();
        String property = ((OWLTransitiveObjectPropertyAxiom) axiom).getProperty().toString();
        int pro = Dictionary.encodeRdf(property);
        if(ip == 0){
            graph.addVertex(pro);
        }
        else{
            DicOwlMap.addDicOwlTransitivePropertyMap(type, pro);
        }
    }


    public static void OWLSubObjectPropertyProcessor(OWLAxiom axiom, int ip) {
        int type = axiom.typeIndex();
        String SubProperty = ((OWLSubObjectPropertyOfAxiom) axiom).getSubProperty().toString();
        String SuperProperty = ((OWLSubObjectPropertyOfAxiom) axiom).getSuperProperty().toString();
        int sub = Dictionary.encodeRdf(SubProperty);
        int sup = Dictionary.encodeRdf(SuperProperty);
//        Map<Integer, Integer> inverseMap = InversePropertyMap.getInverseMap();
        if(ip == 0){
            graph.addVertex(sub);
            graph.addVertex(sup);
            graph.addEdge(sub, sup);
        }
        else{
            DicOwlMap.addDicOwlSubPropertyMap(type, sub, sup);
        }

//        if(inverseMap.containsKey(sub)){
//            if(inverseMap.containsKey(sup)){
//                DicOwlMap.addDicOwlMap(type, inverseMap.get(sub), inverseMap.get(sup));
//            }else{
//                System.out.println("未处理SubObjectProperty逆角色");
//            }
//        }
//        else if(inverseMap.containsKey(sup)){
//            System.out.println("未处理SubObjectProperty逆角色");
//        }
//        else{
//            DicOwlMap.addDicOwlMap(type, sub, sup);
//        }
    }

    public static void OWLSubCLassProcessor(OWLAxiom axiom, int ip) {
        int type = axiom.typeIndex();
        String subclass = ((OWLSubClassOfAxiom) axiom).getSubClass().toString();
        int sub = Dictionary.encodeRdf(subclass);
        OWLClassExpression SuperClass = ((OWLSubClassOfAxiom) axiom).getSuperClass();
        if(SuperClass instanceof OWLObjectSomeValuesFrom){
            OWLObjectSomeValuesFromProcessor(SuperClass, sub, ip);
        }
        else if(SuperClass instanceof OWLClass){
            if (ip == 0) {
                return;
            }
            String superClass = SuperClass.toString();
            int sup = Dictionary.encodeRdf(superClass);
            DicOwlMap.addDicOwlSubCLassMap(type,sub,sup);

        }
        else{
            System.out.println("未处理OWLSubCLass公理类型"+axiom.toString());
        }
    }

    public static void OWLObjectSomeValuesFromProcessor(OWLClassExpression ax, int class1, int ip) {
        String property = ((OWLObjectSomeValuesFrom) ax).getProperty().toString();
        int propertyInt = Dictionary.encodeRdf(property);
        if(ip == 0){
            graph.addVertex(propertyInt);
            return;
        }
        String fillter = ((OWLObjectSomeValuesFrom) ax).getFiller().toString();
        int fillterInt = Dictionary.encodeRdf(fillter);
        DicOwlMap.addDicOwlObjectSomeValuesMap(ObjectSomeValuesFrom, class1, propertyInt, fillterInt);

    }
    public static void ObjectPropertyDomainProcessor(OWLAxiom axiom, int ip) {
        if(ip == 0) {
            return;
        }
        int type = axiom.typeIndex();
        String property = ((OWLObjectPropertyDomainAxiom) axiom).getProperty().toString();
        String domain = ((OWLObjectPropertyDomainAxiom) axiom).getDomain().toString();
        int pro = Dictionary.encodeRdf(property);
        int dom = Dictionary.encodeRdf(domain);
        DicOwlMap.addDicOwlPropertyDomainMap(type, pro, dom);

    }

    public static void ObjectPropertyRangeProcessor(OWLAxiom axiom, int ip) {
        if(ip == 0) {
            return;
        }
        int type = axiom.typeIndex();
        String property = ((OWLObjectPropertyRangeAxiom) axiom).getProperty().toString();
        String range = ((OWLObjectPropertyRangeAxiom) axiom).getRange().toString();
        int pro = Dictionary.encodeRdf(property);
        int ran = Dictionary.encodeRdf(range);
        DicOwlMap.addDicOwlPropertyRangeMap(type, pro, ran);

    }

    public static void EquivalentClassProcessor(OWLEquivalentClassesAxiom axiom, int ip) {
        //NNF
        if(axiom.toString().contains("ObjectComplementOf")){
            axiom = (OWLEquivalentClassesAxiom) axiom.getNNF();
        }
        int class1 = 0;
        Iterator<OWLClassExpression> iterator = axiom.getOperandsAsList().iterator();
        while(iterator.hasNext()){
            OWLClassExpression ax = iterator.next();
            if(ax instanceof OWLClass){
                class1 = Dictionary.encodeRdf(ax.toString());
            }
        }
        if(ip == 1){
            if(!DicOwlMap.EquiDicRuleMap.containsKey(class1)){
                DicOwlMap.EquiDicRuleMap.put(class1, new ArrayList<>());
            }
        }
        iterator = axiom.getOperandsAsList().iterator();
        while(iterator.hasNext()){
            OWLClassExpression ax = iterator.next();

            if(ax instanceof OWLObjectIntersectionOf){
                EquivalentClassProcessor.OWLObjectIntersectionOfProcessor(ax, class1, ip);
            }
            else if(ax instanceof OWLObjectMinCardinality){
                EquivalentClassProcessor.OWLObjectMinCardinalityProcessor(ax, class1, ip);
            }
            else if(ax instanceof OWLObjectUnionOf){
                EquivalentClassProcessor.OWLObjectUnionOfProcessor(ax, class1, ip);
            }

        }
    }

    public static void OWLInversePropertyProcessor(OWLAxiom axiom, int ip) {
        if(ip == 1){
            return;
        }
        String ax = axiom.toString();
        Pattern p = compile("\\<(.*?)\\>");//正则表达式，取=和|之间的字符串，不包括=和|
        Matcher mm = p.matcher(ax);
        List<String> list=new ArrayList<>();
        while(mm.find()) {
            list.add(mm.group(0));
            //   System.out.println(mm.group(ip));//m.group(0)包括这两个字符
        }
        String firstProperty = list.get(0);
        String secondProperty = list.get(1);
        int pro = Dictionary.encodeRdf(firstProperty);
        int ran = Dictionary.encodeRdf(secondProperty);

        InversePropertyMap.InverseProperty.add(pro);
        InversePropertyMap.InverseProperty.add(ran);

        graph.addVertex(pro);
        graph.addVertex(ran);

    }

    public static void OWLEquivalentPropertyProcessor(OWLAxiom axiom, int ip) {
        if(ip == 1) {
            return;
        }
        String ax = axiom.toString();
        Pattern p = compile("\\<(.*?)\\>");//正则表达式，取=和|之间的字符串，不包括=和|
        Matcher mm = p.matcher(ax);
        List<String> list=new ArrayList<>();
        while(mm.find()) {
            list.add(mm.group(0));
            //   System.out.println(mm.group(ip));//m.group(0)包括这两个字符
        }
        String firstProperty = list.get(0);
        String secondProperty = list.get(1);
        int pro = Dictionary.encodeRdf(firstProperty);
        int ran = Dictionary.encodeRdf(secondProperty);

        EquivalentPropertyMap.EquivalentPropertyList.add(pro);
        EquivalentPropertyMap.EquivalentPropertyList.add(ran);

        graph.addVertex(pro);
        graph.addVertex(ran);
    }

    public static void OWLDisjointClassesProcessor(OWLAxiom axiom, int ip) {
        if(ip == 1){
            return;
        }
        String ax = axiom.toString();
        Pattern p = compile("\\<(.*?)\\>");//正则表达式，取=和|之间的字符串，不包括=和|
        Matcher mm = p.matcher(ax);
        List<String> list=new ArrayList<>();
        while(mm.find()) {
            list.add(mm.group(0));
            //   System.out.println(mm.group(ip));//m.group(0)包括这两个字符
        }
        String firstProperty = list.get(0);
        String secondProperty = list.get(1);
        int first = Dictionary.encodeRdf(firstProperty);
        int second = Dictionary.encodeRdf(secondProperty);
        DisjointClassesMap.setDisjointClassesMap(first,second);
        DisjointClassesMap.setDisjointClassesMap(second,first);
    }
}
