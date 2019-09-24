package com.tju.gowl.io;

import com.tju.gowl.bean.*;
import com.tju.gowl.dictionary.Dictionary;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DictionaryInput {
//include 指定尖括号 0包含 1不包含


    public static void readABox(String pathABox, String rdf, String ub, int include){
        Dictionary dd = new Dictionary();
        Map<Integer, DicRdfDataBean> DicTotalData= DicRdfDataMap.getDicDataMap();
        Map<String, Integer> encode= Dictionary.getEncode();
        //Map<Integer, String> decode= Dictionary.getDecode();
        //Nsp链
        Map<Integer, List<IndexBean>> IndexIsp= IndexMap.getIsp();
        //Npo链
        Map<Integer, List<IndexBean>> IndexIop= IndexMap.getIop();

        Path fpath= Paths.get(pathABox);
        int index=0;
        try {
            BufferedReader bfr= Files.newBufferedReader(fpath);
            String line;
            while((line=bfr.readLine())!=null){
                //TODO 不识别标志
                if(!line.contains("\\")&&!line.contains("unknown:namespace")){
                    StringTokenizer st = new StringTokenizer(line, " ");
                    List<String> list=new ArrayList<>();
                    while(st.hasMoreElements()) {
                        list.add(st.nextToken());
                    }
                    DicRdfDataBean rdfDataBean = new DicRdfDataBean();
                    String Rs = list.get(0);
                    String Rp = list.get(1);
                    String Ro = list.get(2);
                    int rs = Dictionary.encodeRdf(Rs);
                    int rp = Dictionary.encodeRdf(Rp);
                    int ro = Dictionary.encodeRdf(Ro);


//                    逆角色进行替换
                    int inverse = InversePropertyMap.getInverseProperty(rp);
                    int equivalent = EquivalentPropertyMap.getEquivalentProperty(rp);
                    if(inverse == -1 && equivalent == -1){
                        rdfDataBean.setRs(rs);
                        rdfDataBean.setRp(rp);
                        rdfDataBean.setRo(ro);

                        int indexSp = IndexMap.getFirstIndexSpFromMap(rs, rp, index);
                        rdfDataBean.setNsp(indexSp, index);

                        int indexOp = IndexMap.getFirstIndexOpFromMap(rp, ro, index);
                        rdfDataBean.setNop(indexOp, index);

                        rdfDataBean.setNp(-1);
                    }
                    else if(inverse != -1){
                        rp = inverse;
                        rdfDataBean.setRs(ro);
                        rdfDataBean.setRp(rp);
                        rdfDataBean.setRo(rs);

                        int indexSp = IndexMap.getFirstIndexSpFromMap(ro, rp, index);
                        rdfDataBean.setNsp(indexSp, index);

                        int indexOp = IndexMap.getFirstIndexOpFromMap(rp, rs, index);
                        rdfDataBean.setNop(indexOp, index);

                        rdfDataBean.setNp(-1);
                    }
                    else if(equivalent != -1){
                        rp = equivalent;
                        rdfDataBean.setRs(rs);
                        rdfDataBean.setRp(rp);
                        rdfDataBean.setRo(ro);

                        int indexSp = IndexMap.getFirstIndexSpFromMap(rs, rp, index);
                        rdfDataBean.setNsp(indexSp, index);

                        int indexOp = IndexMap.getFirstIndexOpFromMap(rp, ro, index);
                        rdfDataBean.setNop(indexOp, index);

                        rdfDataBean.setNp(-1);
                    }
                    else{
                        System.out.println("未处理：DictionaryInput 100");
                    }

                    DicTotalData.put(index,rdfDataBean);
                    index++;
                    if(index % 100000 == 0){
                        System.out.println("finish read "+index+" data");
                    }

                }
                else{
//                    System.out.println(line);
                }

            }
//            System.out.println("index"+index);
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("初始数据数目"+index);
//        System.out.println("encode"+encode.size());
//        System.out.println(encode);
//
//        System.out.println(DicTotalData);
//        System.out.println(IndexIsp);
//        System.out.println(IndexIpo);
    }

    public static void readTBox(String pathTBox) throws OWLOntologyCreationException, IOException {
        Map<String, List<DicOwlBean>> totalRule = DicOwlMap.getRuleMap();
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
                DicOwlMap.addDicOwlMap(type, pro);
            }
            else if(axiom instanceof OWLFunctionalObjectPropertyAxiom){
//                OWLFunctionalObjectPropertyAxiom2015
                String property = ((OWLFunctionalObjectPropertyAxiom) axiom).getProperty().toString();
                type = axiom.typeIndex();
                int pro = Dictionary.encodeRdf(property,"Tbox");
                DicOwlMap.addDicOwlMap(type, pro);
            }
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
        int pro = Dictionary.encodeRdf(property,"Tbox");
        int ran = Dictionary.encodeRdf(range,"Tbox");
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
        int class2Int = Dictionary.encodeRdf(class2,"Tbox");
        //>=1 == ObjectSomeValuesFrom
        if(cardinality == 1){
           OWLObjectSomeValuesFromProcessor(class1, propertyInt, class2Int);
        }
        else if(cardinality >1){
//            3008
//            System.out.println("MinCardinality"+ax.typeIndex());
            DicOwlMap.addDicOwlMap(ax.typeIndex(), class1, cardinality, propertyInt, class2Int);

        }


    }

    private static void OWLObjectSomeValuesFromProcessor(int class1, int propertyInt, int class2Int) {
        DicOwlMap.addDicOwlMap(3005, class1, propertyInt, class2Int);
    }

    private static void OWLObjectSomeValuesFromProcessor(OWLClassExpression ax, int class1, int class2) {
        String property = ((OWLObjectSomeValuesFrom) ax).getProperty().toString();
        String fillter = ((OWLObjectSomeValuesFrom) ax).getFiller().toString();
        int propertyInt = Dictionary.encodeRdf(property,"Tbox");
        int fillterInt = Dictionary.encodeRdf(fillter,"Tbox");
        DicOwlMap.addDicOwlMap(3005, class1, propertyInt, fillterInt);
        DicOwlMap.addDicOwlMap(2001, class1, class2, propertyInt, fillterInt);
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
        System.out.println(type);
        Map<Integer, Integer> inverseMap = InversePropertyMap.getInverseMap();
        if(inverseMap.containsKey(propertyInt)){
            DicOwlMap.addDicOwlMap(-type, class1, inverseMap.get(propertyInt), classAllValues);
        }
        else{
            DicOwlMap.addDicOwlMap(type, class1, propertyInt, classAllValues);
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


    public static void main(String[] args) throws IOException, OWLOntologyCreationException {

//      readABox("data/testequiv.nt", "n","n",1);
//        StringBuffer ssbuff = new StringBuffer("*0");
//        int i = 101;
//        String key = ssbuff.append(i).toString();
//
//        System.out.println(key);
        //Output.writeFile("data/dic100");

        DictionaryInput.readTBox("data/univ-bench-dl.owl");
//        Map<Integer, Integer> ee = EquivalentPropertyMap.getEquivalentPropertyMap();
//        System.out.println(ee);
//        Map<Integer, Integer> aa = InversePropertyMap.getInverseMap();
//        System.out.println(aa);
//        InversePropertyMap.rewriteInverseRule();
//        DictionaryInput.readABox("data/small.nt","n","n",1);
//        DictionaryOutput.outWriteDicDataMap("data/inverse.nt");
//        DictionaryOutput.outWriteDicOwlMap("data/out-rule.txt");
//        DictionaryOutput.encodeMap("data/out-encode.txt");
        //Scanner cin = new Scanner(System.in);
//        StringBuilder s = new StringBuilder("SubClassOf(<http://swat.cse.lehigh.edu/onto/univ-bench.owl#Specification> <http://swat.cse.lehigh.edu/onto/univ-bench.owl#Publication>)2002");
//        String sr =s.toString();
//        StringTokenizer st = new StringTokenizer(sr,"SubClassOf<>");//去除字符串中的逗号，空格，斜线
//        while(st.hasMoreTokens()) {
//            System.out.print(st.nextToken());//输出结果为：Java ,is /,great.
//        }

    }
}
