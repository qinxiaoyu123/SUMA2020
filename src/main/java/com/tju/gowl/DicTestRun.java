package com.tju.gowl;

import com.tju.gowl.dictionary.Dictionary;
import com.tju.gowl.io.DictionaryInput;
import com.tju.gowl.io.DictionaryOutput;
import com.tju.gowl.jenaQuery.RewriteThing;
import com.tju.gowl.reason.DicSerialReason;
import com.tju.gowl.reason.SameAsReason;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.IOException;

import static com.tju.gowl.jenaQuery.JenaTest.jenaQuerySimple;

public class DicTestRun {
    public static void main(String[] args) throws Exception {
        queryUOBM();
//        queryLUBM();
    }

    private static void queryLUBM() throws OWLOntologyCreationException, IOException {
        String pathTBox = "D:\\experiments\\lubm1\\lubm-univ-bench.owl";
        String pathABox = "D:\\experiments\\lubm1\\lubm1.nt";
        String pathData = "D:\\experiments\\lubm1\\lubm1_new_gowl.nt";
        boolean isQueryByJena = true;
        String pathDataThing = "D:\\experiments\\lubm1\\lubm1_newThing_gowl.nt";
        String queryPath = "D:\\experiments\\lubm1\\standard_and_gap.sparql";
        String answerPath = "D:\\experiments\\lubm1\\ans.nt";
        materialization(pathTBox, pathABox);
        writeFile(pathData);
        if(isQueryByJena){
            queryByJena(pathData, pathDataThing, queryPath, answerPath);
        }
    }

    private static void queryUOBM() throws OWLOntologyCreationException, IOException {
        String pathTBox = "data/univ-bench-dl.owl";
        String pathABox = "data/uobm1.nt";
        String pathData = "data/new_uobm1.nt";
        boolean isQueryByJena = true;
        String pathDataThing = "data/newThing_oubm1.nt";
        String queryPath = "data/test.sparql";
        String answerPath = "data/result_new.nt";
        materialization(pathTBox, pathABox);
        writeFile(pathData);
        if(isQueryByJena){
            queryByJena(pathData, pathDataThing, queryPath, answerPath);
        }
    }

    private static void writeFile(String pathData) throws IOException {
        DictionaryOutput.outWriteDicDataMap(pathData);
    }

    private static void queryByJena(String pathABox, String pathABoxThing, String queryPath, String answerPath) throws IOException {
        RewriteThing.rewriteThing(pathABox, pathABoxThing);
        jenaQuerySimple(pathABoxThing, queryPath, answerPath);
    }

    public static void materialization(String pathTBox, String pathABox) throws OWLOntologyCreationException, IOException {
        //type owl:Thing 编码
        new Dictionary();
//        规则预处理
//        DictionaryInput.readTBox(pathTBox);
        DictionaryInput.readTBox(pathTBox);
        //数据预处理
        preDealData(pathABox);
        DictionaryOutput.encodeMap("data/encode.txt");
//        DictionaryOutput.outWriteDicOwlMap("data/outRule.txt");
        //单线程推理
        reason();
        DictionaryInput.readDictionary("data/encode.txt");
//        System.out.println("size of dictionary"+Dictionary.getEncode().size());
//        DictionaryOutput.outWriteSameAs("data/sameAs.nt");
//        DictionaryOutput.encodeMap("data/encode.nt");

//        DictionaryOutput.outWriteEquiDicOwlMap("data/equiClassRule.nt");
        //owl:Thing <owl:Thing> jena 解析

    }

    private static void reason() throws IOException {
        long startTime3 = System.currentTimeMillis();
        DicSerialReason.reason();
        long startTime4=System.currentTimeMillis();
        System.out.println("reason time"+(startTime4-startTime3));
        SameAsReason.addEquivIndividual();
    }

    private static void preDealData(String pathABox) {
        long startTime1 = System.currentTimeMillis();
        DictionaryInput.readABox(pathABox);
        long startTime2 = System.currentTimeMillis();
        System.out.println("reading time"+(startTime2-startTime1));
    }


}
