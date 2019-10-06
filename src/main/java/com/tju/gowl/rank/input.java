package com.tju.gowl.rank;

import com.tju.gowl.axiomProcessor.Processor;
import com.tju.gowl.bean.*;
import com.tju.gowl.dictionary.Dictionary;
import com.tju.gowl.io.DictionaryOutput;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class input {
    public static final unDirectedGraph.Graph graph = new unDirectedGraph.Graph();
    public static  void main(String[] args) throws OWLOntologyCreationException, IOException {
        readTBox("data/univ-bench-dl.owl");
        graph.depthFirstSearch();//ABCDE

        System.out.println();
        Iterator<Map.Entry<Integer, unDirectedGraph.Graph.Vertex>> entryIter = unDirectedGraph.Graph.vertexMap.entrySet().iterator();
        while(entryIter.hasNext()) {
            Map.Entry<Integer, unDirectedGraph.Graph.Vertex> entry = entryIter.next();
            int lab = entry.getKey();
            unDirectedGraph.Graph.Vertex vTmp = entry.getValue();
            System.out.println("lab "+lab+"  "+vTmp.poolIndex+" "+unDirectedGraph.Graph.poolWeightList.get(vTmp.poolIndex));
            System.out.println(Dictionary.getDecode().get(lab));
        }

    }

    public static void readTBox(String pathTBox) throws OWLOntologyCreationException, IOException {

        File testFile = new File(pathTBox);
        OWLOntologyManager m = OWLManager.createOWLOntologyManager();
        OWLOntology univBench = m.loadOntologyFromOntologyDocument(testFile);
        //TODO EquivalentProperty DisjointClasses
        EquivalentPropertyProcess(univBench);
//        SymmetricPropertyProcess(univBench);
        InversePropertyMap.writeUOBMInverseMap(univBench);

        Iterator localIterator1 = univBench.getAxioms().iterator();
        int index = 0;
        int type;
        while(localIterator1.hasNext()){
            index++;
            OWLAxiom axiom = (OWLAxiom)localIterator1.next();
//            System.out.println(axiom.toString());
            if(axiom instanceof OWLObjectPropertyRangeAxiom){
                ObjectPropertyRangeProcessor(axiom);
            }
            else if(axiom instanceof OWLObjectPropertyDomainAxiom){
                ObjectPropertyDomainProcessor(axiom);
            }
            else if(axiom instanceof OWLSubClassOfAxiom){
                OWLSubCLassProcessor(axiom);
            }
            else if(axiom instanceof OWLEquivalentClassesAxiom){
//                 System.out.println(axiom.toString());
                EquivalentClassProcessor((OWLEquivalentClassesAxiom) axiom, 0);
            }
            else if(axiom instanceof OWLSubObjectPropertyOfAxiom){
                OWLSubObjectPropertyProcessor(axiom);
            }
            else if(axiom instanceof OWLTransitiveObjectPropertyAxiom){
                String property = ((OWLTransitiveObjectPropertyAxiom) axiom).getProperty().toString();
                type = axiom.typeIndex();
                int pro = Dictionary.encodeRdf(property,"Tbox");
                DicOwlMap.addDicOwlMap(type, pro);
            }
            else if(axiom instanceof OWLSymmetricObjectPropertyAxiom){
                String property = ((OWLSymmetricObjectPropertyAxiom) axiom).getProperty().toString();
                type = axiom.typeIndex();
//                System.out.println("OWLSymmetricObjectPropertyAxiom"+type);
                int pro = Dictionary.encodeRdf(property,"Tbox");
                DicOwlMap.addDicOwlMap(type, pro);
            }
            //TODO 检查是否所有类型规则都add到ruleMap
            else if(axiom instanceof OWLInverseFunctionalObjectPropertyAxiom){
//                OWLInverseFunctionalObjectPropertyAxiom2016
                String property = ((OWLInverseFunctionalObjectPropertyAxiom) axiom).getProperty().toString();
                type = axiom.typeIndex();
//                System.out.println("OWLInverseFunctionalObjectPropertyAxiom"+type);
                int pro = Dictionary.encodeRdf(property,"Tbox");
                graph.addVertex(pro);
                DicOwlMap.addDicOwlMap(type, pro);
            }
            else if(axiom instanceof OWLFunctionalObjectPropertyAxiom){
//                OWLFunctionalObjectPropertyAxiom2015
                String property = ((OWLFunctionalObjectPropertyAxiom) axiom).getProperty().toString();
                type = axiom.typeIndex();
                int pro = Dictionary.encodeRdf(property,"Tbox");
                graph.addVertex(pro);
                DicOwlMap.addDicOwlMap(type, pro);
            }
            else if(axiom instanceof OWLClassAssertionAxiom){
//                OWLClassAssertionAxiom2005
                String class1 = ((OWLClassAssertionAxiom) axiom).getClassExpression().toString();
                String individual = ((OWLClassAssertionAxiom) axiom).getIndividual().toString();
//                type = axiom.typeIndex();
                int classInt = Dictionary.encodeRdf(class1,"Tbox");
                int individualInt = Dictionary.encodeRdf(individual,"Tbox");

//                DicOwlMap.addDicOwlMap(type, pro);
            }
//            else if(axiom instanceof OWLDeclarationAxiom){
////                OWLClassAssertionAxiom2005
//                if(axiom.toString().contains("Class")){
//                    String class1 = ((OWLDeclarationAxiom) axiom).getEntity().toString();
//                    int classInt = Dictionary.encodeRdf(class1,"Tbox");
//                    int classInt2 = Dictionary.encodeRdf("owl:Thing","Tbox");
//                    DicOwlMap.addDicOwlMap(2002, classInt, classInt2);
//                }
//
////                DicOwlMap.addDicOwlMap(type, pro);
//            }
            else if(!(axiom instanceof OWLLogicalAxiom)){
//                System.out.println("非逻辑公理"+axiom.toString());
            }
            else {
//                System.out.println("未处理公理"+axiom.toString());
            }

        }
        System.out.println("axioms count"+index);
        DictionaryOutput.outWriteDicOwlMap("data/new_rule");
//        InversePropertyMap.rewriteInverseRule();

    }



    private static void OWLSubObjectPropertyProcessor(OWLAxiom axiom) {
        int type;
        type = axiom.typeIndex();
        String SubProperty = ((OWLSubObjectPropertyOfAxiom) axiom).getSubProperty().toString();
        String SuperProperty = ((OWLSubObjectPropertyOfAxiom) axiom).getSuperProperty().toString();
        int sub = Dictionary.encodeRdf(SubProperty,"Tbox");
        int sup = Dictionary.encodeRdf(SuperProperty,"Tbox");
        Map<Integer, Integer> inverseMap = InversePropertyMap.getInverseMap();
        graph.addVertex(sub);
        graph.addVertex(sup);
        graph.addEdge(sub,sup);
        if(inverseMap.containsKey(sub)){
            if(inverseMap.containsKey(sup)){
                DicOwlMap.addDicOwlMap(type, inverseMap.get(sub), inverseMap.get(sup));
            }else{
                System.out.println("未处理逆角色");
            }
        }
        else if(inverseMap.containsKey(sup)){
            System.out.println("未处理逆角色");
        }
        else{
            DicOwlMap.addDicOwlMap(type, sub, sup);
        }
    }

    private static void OWLSubCLassProcessor(OWLAxiom axiom) {
        int type;
        type = axiom.typeIndex();
        String subclass = ((OWLSubClassOfAxiom) axiom).getSubClass().toString();
        int sub = Dictionary.encodeRdf(subclass,"Tbox");
        OWLClassExpression SuperClass = ((OWLSubClassOfAxiom) axiom).getSuperClass();
        if(SuperClass instanceof OWLObjectSomeValuesFrom){
            OWLObjectSomeValuesFromProcessor(SuperClass, sub);
        }
        else{
            String superClass = SuperClass.toString();
            int sup = Dictionary.encodeRdf(superClass,"Tbox");
            DicOwlMap.addDicOwlMap(type,sub,sup);
        }
    }

    private static void OWLObjectSomeValuesFromProcessor(OWLClassExpression ax, int class1) {
        String property = ((OWLObjectSomeValuesFrom) ax).getProperty().toString();
        String fillter = ((OWLObjectSomeValuesFrom) ax).getFiller().toString();
        int propertyInt = Dictionary.encodeRdf(property,"Tbox");
        int fillterInt = Dictionary.encodeRdf(fillter,"Tbox");
        DicOwlMap.addDicOwlMap(3005, class1, propertyInt, fillterInt);
        graph.addVertex(propertyInt);

    }
    private static void ObjectPropertyDomainProcessor(OWLAxiom axiom) {
        int type;
        type = axiom.typeIndex();
        String property = ((OWLObjectPropertyDomainAxiom) axiom).getProperty().toString();
        String domain = ((OWLObjectPropertyDomainAxiom) axiom).getDomain().toString();
        int pro = Dictionary.encodeRdf(property,"Tbox");
        int dom = Dictionary.encodeRdf(domain,"Tbox");
        Map<Integer, Integer> inverseMap = InversePropertyMap.getInverseMap();
        if(inverseMap.containsKey(pro)){
            DicOwlMap.addDicOwlMap(2023, inverseMap.get(pro), dom);
        }
        else{
            DicOwlMap.addDicOwlMap(type, pro, dom);
        }
    }

    private static void ObjectPropertyRangeProcessor(OWLAxiom axiom) {
        int type;
        type = axiom.typeIndex();
        String property = ((OWLObjectPropertyRangeAxiom) axiom).getProperty().toString();
        String range = ((OWLObjectPropertyRangeAxiom) axiom).getRange().toString();
        int pro = Dictionary.encodeRdf(property);
        int ran = Dictionary.encodeRdf(range);
        Map<Integer, Integer> inverseMap = InversePropertyMap.getInverseMap();

        if(inverseMap.containsKey(pro)){
            DicOwlMap.addDicOwlMap(2022, inverseMap.get(pro), ran);
        }
        else{
            DicOwlMap.addDicOwlMap(type, pro, ran);
        }
    }

    private static void EquivalentClassProcessor(OWLEquivalentClassesAxiom axiom, int i) {
        if(axiom.toString().contains("ObjectComplementOf")){
            axiom = (OWLEquivalentClassesAxiom) axiom.getNNF();
        }
        int class1 = 0;
        Iterator<OWLClassExpression> iterator = axiom.getOperandsAsList().iterator();
        while(iterator.hasNext()){
            OWLClassExpression ax = iterator.next();
            if(ax instanceof OWLClass){
//                System.out.println(ax);
                class1 = Dictionary.encodeRdf(ax.toString(),"Tbox");
            }
        }
        if(class1 == 0) {
            System.out.println("class1-error");
        }
        if(!DicOwlMap.EquiDicRuleMap.containsKey(class1)){
            DicOwlMap.EquiDicRuleMap.put(class1, new ArrayList<>());
        }else{
            System.out.println("class11-error");
        }
        iterator = axiom.getOperandsAsList().iterator();
        while(iterator.hasNext()){
            OWLClassExpression ax = iterator.next();

            if(ax instanceof OWLObjectIntersectionOf){
                OWLObjectIntersectionOfProcessor(ax, class1);
            }
            else if(ax instanceof OWLObjectMinCardinality){
                OWLObjectMinCardinalityProcessor(ax, class1);
            }
            else if(ax instanceof OWLObjectUnionOf){
                OWLObjectUnionOfProcessor(ax, class1);
            }

        }
    }

    private static void OWLObjectUnionOfProcessor(OWLClassExpression ax, int class1) {
        Pattern p = Pattern.compile("\\<(.*?)\\>");//正则表达式，取=和|之间的字符串，不包括=和|
        Matcher mm = p.matcher(ax.toString());
        List<String> list = new ArrayList<>();
        while(mm.find()) {
            list.add(mm.group(0));
            //   System.out.println(mm.group(ip));//m.group(0)包括这两个字符
        }
        int class2 = Dictionary.encodeRdf(list.get(0),"Tbox");
        int class3 = Dictionary.encodeRdf(list.get(1),"Tbox");
        OWLSubCLassProcessor(class2, class1);
        OWLSubCLassProcessor(class3, class1);
    }

    private static void OWLObjectMinCardinalityProcessor(OWLClassExpression ax, int class1) {
        int cardinality = ((OWLObjectMinCardinality)ax).getCardinality();
        String property = ((OWLObjectMinCardinality) ax).getProperty().toString();
        int propertyInt = Dictionary.encodeRdf(property,"Tbox");
        String class2 = ((OWLObjectMinCardinality) ax).getFiller().toString();
        System.out.println("class2"+class2);
        int class2Int = Dictionary.encodeRdf(class2,"Tbox");
        System.out.println("class2Int"+class2Int);
        //>=1 == ObjectSomeValuesFrom
        if(cardinality == 1){
            OWLObjectSomeValuesFromProcessor(class1, propertyInt, class2Int);
        }
        else if(cardinality >1){
//            3008
//            System.out.println("MinCardinality"+ax.typeIndex());
            //min 3008
            //TODO class2Int == 0 OWL：THing
            DicOwlMap.addEquiDicRuleMap(class1,3008, cardinality, propertyInt, class2Int);
//            DicOwlMap.addRuleMap(class1,3008, propertyInt);
            DicOwlMap.addDicOwlMap(ax.typeIndex(), class1, cardinality, propertyInt, class2Int);
            graph.addVertex(propertyInt);


        }


    }

    private static void OWLObjectSomeValuesFromProcessor(int class1, int propertyInt, int class2Int) {
        DicOwlMap.addEquiDicRuleMap(class1,3005, propertyInt, class2Int);
        graph.addVertex(propertyInt);
//        DicOwlMap.addRuleMap(class1,3005, propertyInt);
        DicOwlMap.addDicOwlMap(3005, class1, propertyInt, class2Int);
//        DicOwlMap.addDicOwlMap(2001, class1, class3, propertyInt, class2Int);
    }

    private static void OWLObjectSomeValuesFromProcessor(OWLClassExpression ax, int class1, int class2) {
        String property = ((OWLObjectSomeValuesFrom) ax).getProperty().toString();
        String fillter = ((OWLObjectSomeValuesFrom) ax).getFiller().toString();
        int propertyInt = Dictionary.encodeRdf(property,"Tbox");
        int fillterInt = Dictionary.encodeRdf(fillter,"Tbox");
        DicOwlMap.addDicOwlMap(3005, class1, propertyInt, fillterInt);
        graph.addVertex(propertyInt);
        DicOwlMap.addEquiDicRuleMap(class1,3005, propertyInt, fillterInt);
//        DicOwlMap.addRuleMap(class1,3005, propertyInt);
//        DicOwlMap.addDicOwlMap(2001, class1, class2, propertyInt, fillterInt););

    }

    private static void OWLObjectIntersectionOfProcessor(OWLClassExpression axiom, int class1) {
        Iterator<OWLClassExpression> iterator = ((OWLObjectIntersectionOf)axiom).getOperandsAsList().iterator();
        int class2 = 0;
        while(iterator.hasNext()) {
            OWLClassExpression ax = iterator.next();
            if (ax instanceof OWLClass) {
//                System.out.println(ax);
                class2 = Dictionary.encodeRdf(ax.toString(), "Tbox");
                OWLSubCLassProcessor(class1,class2);
            }
        }
        if(class2 != 0){
            DicOwlMap.addEquiSubClassOfRuleMap(class1, Processor.SubClassOf,class2);
        }

//        DicOwlMap.addRuleMap(class1,2002,class2);
        iterator = ((OWLObjectIntersectionOf)axiom).getOperandsAsList().iterator();
        while(iterator.hasNext()) {
            OWLClassExpression ax = iterator.next();
            if (ax instanceof OWLObjectAllValuesFrom) {
                OWLObjectAllValuesFromProcessor(ax, class1);
            } else if (ax instanceof OWLObjectMinCardinality) {
                OWLObjectMinCardinalityProcessor(ax, class1);
            } else if (ax instanceof OWLObjectUnionOf) {
                OWLObjectUnionOfProcessor(ax, class1);
            } else if (ax instanceof OWLObjectSomeValuesFrom){
                OWLObjectSomeValuesFromProcessor(ax, class1, class2);
            }
        }

    }



    private static void OWLSubCLassProcessor(int class1, int class2) {
        DicOwlMap.addDicOwlMap(2002, class1, class2);

    }

    private static void OWLObjectAllValuesFromProcessor(OWLClassExpression ax, int class1) {
        String property = ((OWLObjectAllValuesFrom)ax).getProperty().toString();
        int propertyInt = Dictionary.encodeRdf(property,"Tbox");
        OWLClassExpression fillter = ((OWLObjectAllValuesFrom) ax).getFiller();
        int classAllValues = 0;
//        System.out.println(((OWLObjectAllValuesFrom) ax).getFiller().toString());
        if(fillter instanceof OWLObjectComplementOf){
            String cc = ((OWLObjectComplementOf) fillter).getOperand().toString();
            int ccInt = Dictionary.encodeRdf(cc,"Tbox");
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
                classAllValues = Dictionary.encodeRdf(ss.toString(),"Tbox");
            }
            else{
                classAllValues = ccDisjoint;
            }
        }
        else if(fillter instanceof OWLClass){
            classAllValues = Dictionary.encodeRdf(fillter.toString(),"Tbox");
        }
        int type = ax.typeIndex();
//        System.out.println(type);
        Map<Integer, Integer> inverseMap = InversePropertyMap.getInverseMap();
        if(inverseMap.containsKey(propertyInt)){
            DicOwlMap.addDicOwlMap(-type, class1, inverseMap.get(propertyInt), classAllValues);
            graph.addVertex(propertyInt);

            System.out.println("class1"+class1);
            DicOwlMap.addEquiDicRuleMap(class1,-type, inverseMap.get(propertyInt), classAllValues);
//            DicOwlMap.addRuleMap(class1,-type, propertyInt);
        }
        else{
            DicOwlMap.addDicOwlMap(type, class1, propertyInt, classAllValues);
            graph.addVertex(propertyInt);

            DicOwlMap.addEquiDicRuleMap(class1,type, propertyInt, classAllValues);
//            DicOwlMap.addRuleMap(class1,type, propertyInt);

        }

    }



    private static void EquivalentPropertyProcess(OWLOntology univBench) {
        Iterator inverseIterator = univBench.getAxioms().iterator();
        //遍历找到inverse
        while(inverseIterator.hasNext()){
            OWLAxiom axiom = (OWLAxiom)inverseIterator.next();
            if(axiom instanceof OWLEquivalentObjectPropertiesAxiom){
                String ax = axiom.toString();
                Pattern p = Pattern.compile("\\<(.*?)\\>");//正则表达式，取=和|之间的字符串，不包括=和|
                Matcher mm = p.matcher(ax);
                List<String> list=new ArrayList<>();
                while(mm.find()) {
                    list.add(mm.group(0));
                    //   System.out.println(mm.group(ip));//m.group(0)包括这两个字符
                }
                String firstProperty = list.get(0);
                String secondProperty = list.get(1);
                int first = 0;
                int second = 0;
                //replace 1 with 2
                if(firstProperty.contains("love")){
                    first = Dictionary.encodeRdf(firstProperty,"Tbox");
                    second = Dictionary.encodeRdf(secondProperty,"Tbox");
                }
                else{
                    first = Dictionary.encodeRdf(secondProperty,"Tbox");
                    second = Dictionary.encodeRdf(firstProperty,"Tbox");
                }
                graph.addVertex(first);
                graph.addVertex(second);
                EquivalentPropertyMap.setEquivalentProperty(first, second);
                EquivalentPropertyMap.setEquivalentPropertyDecode(second, first);
            }
            if(axiom instanceof OWLDisjointClassesAxiom){
                String ax = axiom.toString();
                Pattern p = Pattern.compile("\\<(.*?)\\>");//正则表达式，取=和|之间的字符串，不包括=和|
                Matcher mm = p.matcher(ax);
                List<String> list=new ArrayList<>();
                while(mm.find()) {
                    list.add(mm.group(0));
                    //   System.out.println(mm.group(ip));//m.group(0)包括这两个字符
                }
                String firstProperty = list.get(0);
                String secondProperty = list.get(1);
                int first = Dictionary.encodeRdf(firstProperty,"Tbox");
                int second = Dictionary.encodeRdf(secondProperty,"Tbox");
                DisjointClassesMap.setDisjointClassesMap(first,second);
                DisjointClassesMap.setDisjointClassesMap(second,first);

            }
        }
    }
}
