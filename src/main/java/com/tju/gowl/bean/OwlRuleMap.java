package com.tju.gowl.bean;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class OwlRuleMap {
    private static final Map<String, List<OwlRuleBean>> RuleMap = new ConcurrentHashMap<>();
    private static final Map<String, String> InverseProperty = new ConcurrentHashMap<>();
    private static final Map<String, Integer> TransitiveProperty = new ConcurrentHashMap<>();
    public static Map<String, List<OwlRuleBean>> getRuleMap(){ return RuleMap; }
    public static Map<String, String> getInverseProperty(){ return InverseProperty; }
    public static Map<String, Integer> getTransitiveProperty(){ return TransitiveProperty; }
    // ip 是否取尖括号， 0取，1不取
    public static void setRuleMap(String line, int type, int ip){
        Pattern p = Pattern.compile("\\<(.*?)\\>");//正则表达式，取=和|之间的字符串，不包括=和|
        Matcher mm = p.matcher(line);
        List<String> list=new ArrayList<>();
        while(mm.find()) {
            list.add(mm.group(ip));
         //   System.out.println(mm.group(ip));//m.group(0)包括这两个字符
        }
        String key;
        //ObjectPropertyDomain 2022
        //ObjectPropertyRange 2023
        //ObjectSomeValuesFrom 0
        //SubClassOf 2002
        //SubObjectPropertyOf 2013
        //TODO InverseObjectProperties  TransitiveObjectProperty DataPropertyDomain AnnotationAssertion
        //TODO  EquivalentClasses 另一个方向
        if(type == 2023 || type == 2022 || type == 2013){
            if(list.size()==2){
                key = "*"+list.get(0)+"*";
                OwlRuleBean typeHead = new OwlRuleBean();
                typeHead.setType(type);
                typeHead.setRuleHead(list.get(1));
                writeRuleMap(key, typeHead);
            }
            else{
                System.out.println("未处理规则类型"+line);
                return ;
            }
        }
        else if (type == 0){//ObjectSomeValuesFrom
            key = "*"+"type"+list.get(0);
            if (someValuesFrom(line, list, key)) return ;
        }
        else if (type == 2002){//subclass
            key = "*"+"type"+list.get(0);
            if (subClassOf(line, type, list, key)) return;
        }
        // EquivalentClasses 2001
        else if (type == 2001){
            Pattern pEquivalentClasses = Pattern.compile("EquivalentClasses\\(<.*> ObjectIntersectionOf\\(<.*> ObjectSomeValuesFrom\\(<.*> <.*");//正则表达式，取=和|之间的字符串，不包括=和|
            Matcher mmEquivalentClasses = pEquivalentClasses.matcher(line);
            //检查是否匹配
            List<String> list1=new ArrayList<>();

            while(mmEquivalentClasses.find()) {
                list1.add(mmEquivalentClasses.group(0));
            }
            if(list1.size()>0){
                key = "*"+"type"+list.get(0);
                OwlRuleBean typeHead1 = new OwlRuleBean();
                typeHead1.setType(2002);
                typeHead1.setRuleHead(list.get(1));
                writeRuleMap(key, typeHead1);
                OwlRuleBean typeHead2 = new OwlRuleBean();
                typeHead2.setType(0);
                String ssHead = list.get(2)+" "+list.get(3);
                typeHead2.setRuleHead(ssHead);
                writeRuleMap(key, typeHead2);

                //另一个方向 C1 = C2 and #R.C3
                //type 20 用jena进行查询
                //String query = "SELECT ?x WHERE { ?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> "+list.get(1)+" . ?x "+list.get(2)+" ?y. ?y <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> "+list.get(3)+" .}";
                String query = list.get(0)+list.get(1)+list.get(2)+list.get(3);
                String key3 = "*"+"type"+list.get(1);
                String key4 = "*"+list.get(2)+"*";
                String key5 = "*"+"type"+list.get(3);
                System.out.println("query"+query);
                //
                OwlRuleBean typeHead3 = new OwlRuleBean();
                typeHead3.setType(21);//C2
                typeHead3.setRuleHead(query);
                writeRuleMap(key3, typeHead3);

                OwlRuleBean typeHead4 = new OwlRuleBean();
                typeHead4.setType(22);//R
                typeHead4.setRuleHead(query);
                writeRuleMap(key4, typeHead4);

                OwlRuleBean typeHead5 = new OwlRuleBean();
                typeHead5.setType(23);//C3
                typeHead5.setRuleHead(query);
                writeRuleMap(key5, typeHead5);

            }
            else{
                System.out.println("未处理规则类型"+line);
            }


        }
        else if(type == 2019){//TransitiveObjectProperty 2015
            if(list.size()==1){
                key = "*"+list.get(0)+"*";
                OwlRuleBean typeHead = new OwlRuleBean();
                typeHead.setType(type);
                typeHead.setRuleHead(list.get(0));
                writeRuleMap(key, typeHead);
            }
            else{
                System.out.println("未处理规则类型"+line);
                return ;
            }
        }
        else {
            System.out.println(line);
            return ;
        }


    }

    private static boolean subClassOf(String line, int type, List<String> list, String key) {
        if(line.contains("ObjectSomeValuesFrom")){
            //
            if (someValuesFrom(line, list, key)) return true;
        }
        else if(list.size()==2){//C1 subclass C2
            OwlRuleBean typeHead = new OwlRuleBean();
            typeHead.setType(type);
            typeHead.setRuleHead(list.get(1));
            writeRuleMap(key, typeHead);

        }
        else{
            System.out.println("未处理规则类型"+line);
            return true;
        }
        return false;
    }

    private static boolean someValuesFrom(String line, List<String> list, String key) {
        OwlRuleBean typeHead = new OwlRuleBean();
        typeHead.setType(0);
        String line3 = null;
        if(list.size()==3) {
            line3 = list.get(1) + " " + list.get(2);
        }
        else if(list.size()==2) {
            //TODO 是否需要加所有类属于Thing类这个公理
            line3 = list.get(1) + " " + "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#Thing>";
        }
         else {
            System.out.println("未处理规则类型"+line);
            return true;
        }
        typeHead.setRuleHead(line3);
        writeRuleMap(key, typeHead);
        return false;
    }

    private static void writeRuleMap(String key, OwlRuleBean typeHead) {
        if (RuleMap.containsKey(key)) {
            RuleMap.get(key).add(typeHead);
        } else {
            List<OwlRuleBean> listTypeHead = new ArrayList<>();
            listTypeHead.add(typeHead);
            RuleMap.put(key, listTypeHead);
        }
    }

    public static void main(String[] args) throws FileNotFoundException, OWLOntologyCreationException {
        String line = "EquivalentClasses(<http://swat.cse.lehigh.edu/onto/univ-bench.owl#Chair> ObjectIntersectionOf(<http://swat.cse.lehigh.edu/onto/univ-bench.owl#Person> ObjectSomeValuesFrom(<http://swat.cse.lehigh.edu/onto/univ-bench.owl#headOf> <http://swat.cse.lehigh.edu/onto/univ-bench.owl#Department>)) )";
//        StringTokenizer st = new StringTokenizer(line, " ");
//        List<String> list=new ArrayList<>();
//        while(st.hasMoreElements()) {
//            list.add(st.nextToken());
//        }
//        System.out.println(list);
//        if (line.contains("ObjectIntersectionOf")){
//            int index = line.indexOf("ObjectIntersectionOf");
//            String line1 = line.substring(index);
//            System.out.println(line1);
//        }





//        File testFile = new File("F:\\first paper\\parallel-gOWL\\data\\univ-bench.owl");
//        OWLOntologyManager m = OWLManager.createOWLOntologyManager();
//        OWLOntology univBench = m.loadOntologyFromOntologyDocument(testFile);
//        System.out.println("loaded ontology: " + univBench);
//        int index = 0;
//        for (OWLAxiom ax : univBench.getAxioms()) {
//            index ++;
//            setRuleMap(ax.toString(), ax.typeIndex(), 1);
//            //System.out.println(ax.toString()+ax.typeIndex()+ax.getAxiomType());
//        }
//        System.out.println("total aximos:"+index);
//        System.out.println(RuleMap);
    }
}

