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
//        String pathTBox = "D:\\daima\\pellet-1.3\\pellet-1.3\\LUBM\\data\\gowl-univ-bench.owl";
//        String pathABox = "D:\\experiments\\服务器代码\\服务器代码\\无限模型\\lubm\\lubm1.nt";
//        String pathData = "D:\\experiments\\服务器代码\\服务器代码\\无限模型\\lubm\\lubm1_new.nt";
//        boolean isQueryByJena = true;
//        String pathDataThing = "D:\\experiments\\lubm1\\lubm1_newThing_gowl.nt";
//        String queryPath = "D:\\daima\\pellet-1.3\\pellet-1.3\\LUBM\\aaa\\lubm-infinite.sparql";
//        String answerPath = "D:\\experiments\\服务器代码\\服务器代码\\无限模型\\lubm\\query\\ans.nt";

        String pathTBox = "D:\\experiments\\服务器代码\\服务器代码\\无限模型\\100-lubm\\gowl-univ-bench-100.owl";
        String pathABox = "D:\\experiments\\服务器代码\\服务器代码\\无限模型\\100-lubm\\lubm1-100.nt";
        String pathData = "D:\\experiments\\服务器代码\\服务器代码\\无限模型\\lubm\\lubm1_new.nt";
        boolean isQueryByJena = true;
        String pathDataThing = "D:\\experiments\\lubm1\\lubm1_newThing_gowl.nt";
        String queryPath = "D:\\experiments\\服务器代码\\服务器代码\\无限模型\\100-lubm\\lubm1-100.sparql";
        String answerPath = "D:\\experiments\\服务器代码\\服务器代码\\无限模型\\lubm\\query\\ans.nt";
        int n = 8;
        materialization(pathTBox, pathABox, n);
        writeFile(pathData);
        if(isQueryByJena){
            queryByJena(pathData, pathDataThing, queryPath, answerPath);
        }
    }

    private static void queryUOBM() throws OWLOntologyCreationException, IOException {
        String pathTBox = "data/univ-bench-dl.owl";
//        String pathABox = "data/uobm1.nt";
        String pathABox = "data/uobm1.nt";
        String pathData = "data/new_uobm1.nt";
        boolean isQueryByJena = false;
        String pathDataThing = "data/newThing_oubm1.nt";
        String queryPath = "data/standard.sparql";
        String answerPath = "data/result_new.nt";
        int n = 7;
        materialization(pathTBox, pathABox, n);
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

    public static void materialization(String pathTBox, String pathABox, int n) throws OWLOntologyCreationException, IOException {
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
        reason(n);
        DictionaryInput.readDictionary("data/encode.txt");
//        System.out.println("size of dictionary"+Dictionary.getEncode().size());
//        DictionaryOutput.outWriteSameAs("data/sameAs.nt");
//        DictionaryOutput.encodeMap("data/encode.nt");

//        DictionaryOutput.outWriteEquiDicOwlMap("data/equiClassRule.nt");
        //owl:Thing <owl:Thing> jena 解析

    }

    private static void reason(int n) throws IOException {
        long startTime3 = System.currentTimeMillis();
        DicSerialReason.reason(n);
        long startTime4=System.currentTimeMillis();
        System.out.println("reason time: "+(startTime4-startTime3)+" ms");
        SameAsReason.addEquivIndividual();
    }

    private static void preDealData(String pathABox) {
        long startTime1 = System.currentTimeMillis();
        DictionaryInput.readABox(pathABox);
        long startTime2 = System.currentTimeMillis();
        System.out.println("reading time: "+(startTime2-startTime1) +"ms");
    }


}
