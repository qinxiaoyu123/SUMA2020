package com.tju.suma.materialization;

import com.tju.suma.axiomProcessor.Processor;
import com.tju.suma.bean.EquivalentPropertyMap;
import com.tju.suma.bean.InversePropertyMap;
import com.tju.suma.rank.unDirectedGraph;
import com.tju.suma.rewrite.EquiClassRuleRewrite;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class RegularBox {
    public static void readTBox(String pathTBox) throws OWLOntologyCreationException {
        File testFile = new File(pathTBox);
        OWLOntologyManager m = OWLManager.createOWLOntologyManager();
        OWLOntology univBench = m.loadOntologyFromOntologyDocument(testFile);
        //添加属性图节点，边，权重更新, 不添加公理
        int ip = 0;
        //round1 初始化属性图（公理处理，添加属性图节点，边，权重更新）
        axiomProcessor(univBench, ip);
        //round2 图节点排序（属性重要度排序）
        //TODO graph声明位置变更
        Processor.graph.depthFirstSearch();
        //根据排序确定等价属性，逆属性替换表
        setEquivalentPropertyMap();
        setInversePropertyMap();
        //round3 根据属性重要度，添加公理，等价属性，逆属性替换
        ip = 1;
        axiomProcessor(univBench, ip);
        EquiClassRuleRewrite.rewrite();
    }

    private static void axiomProcessor(OWLOntology univBench, int ip) {
        int type;
        List<OWLAxiom> streamOwls = univBench.axioms().collect(Collectors.toList());
        for (OWLAxiom axiom : streamOwls) {
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
                    break;
            }
        }
        System.out.println("axioms count" + streamOwls.size());
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
}
