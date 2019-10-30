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

public class ArgDicTestRun {
    public static void main(String[] args) throws Exception {
        int index = 0;
        String pathTBox = null;
        String pathABox = null;
        String pathData = null;
        boolean isQueryByJena = true;
        String pathDataThing = "newThing_oubm1.nt";
        String queryPath = null;
        String answerPath = "result_new.nt";
        if(args.length>index){
            pathTBox = args[index];
            index++;
        }
        if(args.length>index){
            pathABox = args[index];
            index++;
        }
        if(args.length>index){
            pathData = args[index];
            index++;
        }
        if(args.length>index){
            isQueryByJena = Boolean.parseBoolean(args[index]);
            index++;
        }
        if(args.length>index){
            queryPath = args[index];
            index++;
        }
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

//        DictionaryOutput.outWriteDicOwlMap("data/outRule.txt");
        DictionaryOutput.encodeMap("encode.txt");
        //单线程推理
        reason();
        DictionaryInput.readDictionary("encode.txt");
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
