package com.tju.gowl.io;

import com.tju.gowl.axiomProcessor.Processor;
import com.tju.gowl.bean.*;
import com.tju.gowl.dictionary.Dictionary;
import com.tju.gowl.rank.unDirectedGraph;
import com.tju.gowl.reason.DicSerialReason;
import com.tju.gowl.reason.EquiClassRuleRewrite;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.tju.gowl.axiomProcessor.Processor.classAssertion;


public class DictionaryInputNew {
//include 指定尖括号 0包含 1不包含
//    public static List<Integer> classAssertion = new ArrayList<>();

    public static void readABox(String pathABox) {

        Path fpath = Paths.get(pathABox);
        int index = 0;
        try {
            BufferedReader bfr = Files.newBufferedReader(fpath);
            String line;
            while ((line = bfr.readLine()) != null) {
                if (!line.contains("\\") && !line.contains("unknown:namespace")) {
                    List<String> list = Arrays.stream(line.split(" ")).collect(Collectors.toList());
                    String Rs = list.get(0);
                    String Rp = list.get(1);
                    String Ro = list.get(2);
                    int rs = Dictionary.encodeRdf(Rs);
                    int rp = Dictionary.encodeRdf(Rp);
                    int ro = Dictionary.encodeRdf(Ro);

//                  逆角色，等价角色进行替换
                    if (EquivalentPropertyMap.EquivalentPropertyMap.containsKey(rp)) {
                        rp = EquivalentPropertyMap.EquivalentPropertyMap.get(rp);
                    }

                    if (InversePropertyMap.InverseMap.containsKey(rp)) {
                        rp = InversePropertyMap.InverseMap.get(rp);
                        int tmp = rs;
                        rs = ro;
                        ro = tmp;
                    }
                    DicRdfDataMap.addSourceRdfDataBean(index, rs, rp, ro);

                    index++;
                    if (index % 100000 == 0) {
                        System.out.println("finish read " + index + " data");
                    }

                } else {
//                    System.out.println(line);
                }
            }
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("初始数据数目" + index);
        addClassAssertion(index);

    }

    private static void addClassAssertion(int index) {
        int tmpCount = index;
        Iterator<Integer> iter = classAssertion.iterator();
        while (iter.hasNext()) {
            int tmp1 = iter.next();
            int tmp2 = iter.next();
            DicRdfDataMap.addSourceRdfDataBean(tmpCount, tmp1, DicSerialReason.typeEncode, tmp2);
            tmpCount++;
        }
        System.out.println("count after adding ClassAssertion: " + tmpCount);
    }

    public static void readTBox(String pathTBox) throws OWLOntologyCreationException {
        File testFile = new File(pathTBox);
        OWLOntologyManager m = OWLManager.createOWLOntologyManager();
        OWLOntology univBench = m.loadOntologyFromOntologyDocument(testFile);
        int ip = 0;//添加属性图节点，边，权重更新, 不添加公理
        //round1 初始化  属性图（公理处理，添加属性图节点，边，权重更新）
        axiomProcessor(univBench, ip);
        //round2 图节点排序（属性重要度排序）
        //TODO graph声明位置变更
        Processor.graph.depthFirstSearch();
        //根据排序确定等价t属性，逆属性替换表
        setEquivalentPropertyMap();
        setInversePropertyMap();
        //round3 根据属性重要度，添加公理，等价属性，逆属性替换
        ip = 1;
        axiomProcessor(univBench, ip);
        EquiClassRuleRewrite.rewrite();
    }

    private static void setInversePropertyMap() {
        Iterator<Integer> inverseProIter = InversePropertyMap.InverseProperty.iterator();
        while (inverseProIter.hasNext()) {
            int inversePro1 = inverseProIter.next();
            int inversePro2 = inverseProIter.next();
            int weightPro1 = unDirectedGraph.Graph.getPropertyWeight(inversePro1);
            int weightPro2 = unDirectedGraph.Graph.getPropertyWeight(inversePro2);
            if (weightPro1 < weightPro2) {
                InversePropertyMap.InverseMap.put(inversePro1, inversePro2);
                InversePropertyMap.InverseMapDecode.put(inversePro2, inversePro1);
            } else if (weightPro1 > weightPro2) {
                InversePropertyMap.InverseMap.put(inversePro2, inversePro1);
                InversePropertyMap.InverseMapDecode.put(inversePro1, inversePro2);
            } else {
                System.out.println(inversePro1 + " " + inversePro2 + "(inverse) have the same weight!");
                if (inversePro1 < inversePro2) {
                    InversePropertyMap.InverseMap.put(inversePro1, inversePro2);
                    InversePropertyMap.InverseMapDecode.put(inversePro2, inversePro1);
                } else {
                    InversePropertyMap.InverseMap.put(inversePro2, inversePro1);
                    InversePropertyMap.InverseMapDecode.put(inversePro1, inversePro2);
                }
            }

        }
    }

    private static void setEquivalentPropertyMap() {
        Iterator<Integer> equiProIter = EquivalentPropertyMap.EquivalentPropertyList.iterator();
        while (equiProIter.hasNext()) {
            int equiPro1 = equiProIter.next();
            int equiPro2 = equiProIter.next();
            int weightPro1 = unDirectedGraph.Graph.getPropertyWeight(equiPro1);
            int weightPro2 = unDirectedGraph.Graph.getPropertyWeight(equiPro2);
            if (weightPro1 < weightPro2) {
                EquivalentPropertyMap.setEquivalentProperty(equiPro1, equiPro2);
                EquivalentPropertyMap.setEquivalentPropertyDecode(equiPro2, equiPro1);
            } else if (weightPro1 > weightPro2) {
                EquivalentPropertyMap.setEquivalentProperty(equiPro2, equiPro1);
                EquivalentPropertyMap.setEquivalentPropertyDecode(equiPro1, equiPro2);
            } else {
                System.out.println(weightPro1 + " " + weightPro2 + "(equi) have the same weight!");
                if (equiPro1 < equiPro2) {
                    EquivalentPropertyMap.setEquivalentProperty(equiPro1, equiPro2);
                    EquivalentPropertyMap.setEquivalentPropertyDecode(equiPro2, equiPro1);
                } else {
                    EquivalentPropertyMap.setEquivalentProperty(equiPro2, equiPro1);
                    EquivalentPropertyMap.setEquivalentPropertyDecode(equiPro1, equiPro2);
                }
            }

        }
    }

    private static void axiomProcessor(OWLOntology univBench, int ip) {
        int type;
        Iterator localIterator1 = univBench.getAxioms().iterator();
        int index = 0;
        while (localIterator1.hasNext()) {
            index++;
            OWLAxiom axiom = (OWLAxiom) localIterator1.next();
            type = axiom.typeIndex();
            switch (type) {
                case Processor.ObjectPropertyRange:
                    Processor.ObjectPropertyRangeProcessor(axiom, ip);
                    break;
                case Processor.ObjectPropertyDomain:
                    Processor.ObjectPropertyDomainProcessor(axiom, ip);
                    break;
                case Processor.SubClassOf:
                    Processor.OWLSubCLassProcessor(axiom, ip);
                    break;
                case Processor.SubObjectPropertyOf:
                    Processor.OWLSubObjectPropertyProcessor(axiom, ip);
                    break;
                case Processor.SymmetricObjectProperty:
                    Processor.OWLSymmetricObjectPropertyProcessor(axiom, ip);
                    break;
                case Processor.TransitiveProperty:
                    Processor.OWLTransitiveObjectPropertyProcessor(axiom, ip);
                    break;
                case Processor.InverseFunctionalObjectProperty:
                    Processor.InverseFunctionalProcessor(axiom, ip);
                    break;
                case Processor.FunctionalObjectProperty:
                    Processor.FunctionalPropertyProcessor(axiom, ip);
                    break;
                case Processor.EquivalentClass:
                    Processor.EquivalentClassProcessor((OWLEquivalentClassesAxiom) axiom, ip);
                    break;
                case Processor.OWLClassAssertion:
                    Processor.OWLClassAssertionProcessor((OWLClassAssertionAxiom) axiom, ip);
                    break;
                case Processor.InverseProperty:
                    Processor.OWLInversePropertyProcessor(axiom, ip);
                    break;
                case Processor.EquivalentProperty:
                    Processor.OWLEquivalentPropertyProcessor(axiom, ip);
                    break;
                case Processor.OWLDisjointClassesAxiom:
                    Processor.OWLDisjointClassesProcessor(axiom, ip);
                    break;
                default:
                    //OWLLogicalAxiom
                    break;
            }
        }
        System.out.println("axioms count" + index);
    }

    public static void main(String[] args) throws IOException, OWLOntologyCreationException {
        Dictionary dd = new Dictionary();
//      readABox("data/testequiv.nt", "n","n",1);
//        StringBuffer ssbuff = new StringBuffer("*0");
//        int i = 101;
//        String key = ssbuff.append(i).toString();
//
//        System.out.println(key);
        //Output.writeFile("data/dic100");

        DictionaryInputNew.readTBox("data/univ-bench-dl.owl");
        DictionaryOutput.outWriteEquiDicOwlMap("data/equiv.nt");
        DictionaryOutput.outWriteDicOwlMap("data/outRule1.txt");
        System.out.println(DicOwlMap.EquiDicRuleMap);
        System.out.println(DicOwlMap.EquiDicRuleMap.size());
        EquiClassRuleRewrite.rewrite();

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
