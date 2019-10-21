package com.tju.gowl;

import com.tju.gowl.dictionary.Dictionary;
import com.tju.gowl.io.DictionaryInputNew;
import com.tju.gowl.io.DictionaryOutput;
import com.tju.gowl.reason.DicSerialReason;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.IOException;

import static com.tju.gowl.JenaTest.jenaQuerySimple;

public class DicTestRun {
    public static void main(String[] args) throws Exception {
        String pathTBox = "data/univ-bench-dl.owl";
        String pathABox = "data/uobm1.nt";
        long startTime1 = System.currentTimeMillis();
        preProcessData(pathTBox, pathABox);
        long startTime2 = System.currentTimeMillis();
        System.out.println("reading time"+(startTime2-startTime1));
//        System.out.println("hash冲突"+Dictionary.hashSet.size());

        materialization();
        System.out.println(Dictionary.getEncode().size());
        while(true){

        }
    }

    private static void preProcessData(String pathTBox, String pathABox) throws OWLOntologyCreationException, IOException {
        //type owl:Thing 编码
        new Dictionary();
//        规则预处理
//        DictionaryInput.readTBox(pathTBox);
        DictionaryInputNew.readTBox(pathTBox);
        //数据预处理
        preDealData(pathABox);

//        DictionaryOutput.outWriteDicOwlMap("data/outRule.txt");
    }

    public static void materialization() throws  IOException {

        //单线程推理
        long startTime3 = System.currentTimeMillis();
        DicSerialReason.reason();
        long startTime4=System.currentTimeMillis();
        System.out.println("reason time"+(startTime4-startTime3));
        System.out.println("size of dictionary"+Dictionary.getEncode().size());
        DictionaryOutput.outWriteSameAs("data/sameAs.nt");
        DictionaryOutput.encodeMap("data/encode.nt");
        DictionaryOutput.outWriteDicDataMap("data/new_uobm1.nt");
        //owl:Thing <owl:Thing> jena 解析
        RewriteThing.rewriteThing();
        jenaQuerySimple("data/newThing_oubm1.nt", "data/test.sparql", null);
    }

    private static void preDealData(String pathABox) {
        long startTime1 = System.currentTimeMillis();
        DictionaryInputNew.readABox(pathABox);
        long startTime2 = System.currentTimeMillis();
        System.out.println("reading time"+(startTime2-startTime1));
    }


}
