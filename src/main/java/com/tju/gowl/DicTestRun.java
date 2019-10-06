package com.tju.gowl;

import com.tju.gowl.bean.InversePropertyMap;
import com.tju.gowl.bean.RdfDataBean;
import com.tju.gowl.bean.RdfDataMap;
import com.tju.gowl.dictionary.Dictionary;
import com.tju.gowl.io.DictionaryInput;
import com.tju.gowl.io.DictionaryInputNew;
import com.tju.gowl.io.DictionaryOutput;
import com.tju.gowl.io.Input;
import com.tju.gowl.reason.DicSerialReason;
import com.tju.gowl.reason.EquiClassRuleRewrite;
import com.tju.gowl.reason.SerialReason;

import java.util.Map;

import static com.tju.gowl.JenaTest.jenaQuery;
import static com.tju.gowl.JenaTest.jenaQuerySimple;

public class DicTestRun {
    public static void main(String[] args) throws Exception {
//        final String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
//        final String ub = "http://swat.cse.lehigh.edu/onto/univ-bench.owl#";
        String rdf = "http://semantics.crl.ibm.com/univ-bench-dl.owl";
//        String pathTBox = "data/pellet.owl";
        String pathTBox = "data/univ-bench-dl.owl";

        String pathABox = "data/uobm1.nt";
        //type owl:Thing 编码
        Dictionary dd = new Dictionary();
        //规则预处理
//        DictionaryInput.readTBox(pathTBox);
        DictionaryInputNew.readTBox(pathTBox);
        EquiClassRuleRewrite.rewrite();
//        InversePropertyMap.rewriteInverseRule();
        //数据预处理
        long startTime1 = System.currentTimeMillis();
        DictionaryInput.readABox(pathABox, "n","n",1);
        long startTime2 = System.currentTimeMillis();
        System.out.println("reading time"+(startTime2-startTime1));

        DictionaryOutput output=new DictionaryOutput();
        output.outWriteDicOwlMap("data/outRule.txt");
        //单线程推理
        long startTime3 = System.currentTimeMillis();
        DicSerialReason reasonInstance = new DicSerialReason(rdf);
        DicSerialReason.reason();
        //SerialReason.writeFile("data/out.txt");
        long startTime4=System.currentTimeMillis();
        System.out.println("reason time"+(startTime4-startTime3));
        DictionaryOutput.outWriteSameAs("data/sameAs.nt");
        DictionaryOutput.encodeMap("data/encode.nt");
//        DictionaryOutput.outWriteDicDataMap("data/new.nt");
        //inverse write
        DictionaryOutput.outWriteDicDataMap("data/new_uobm1.nt",1);
        //owl:Thing <owl:Thing> jena 解析
        test.rewriteThing();
        jenaQuerySimple("data/newThing_oubm1.nt", "data/test.sparql", null);
    }
}
