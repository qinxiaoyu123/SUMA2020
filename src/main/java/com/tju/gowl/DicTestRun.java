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
        boolean isQueryByJena = true;
        String pathNewABox = "data/uobm1_new.nt";
        String queryPath = "data/test.sparql";

        materialization(pathTBox, pathABox, pathNewABox, isQueryByJena, queryPath);

    }

    public static void materialization(String pathTBox, String pathABox, String pathNewABox, boolean queryByJena, String queryPath) throws OWLOntologyCreationException, IOException {
        //type owl:Thing 编码
        new Dictionary();
//        规则预处理
//        DictionaryInput.readTBox(pathTBox);
        DictionaryInputNew.readTBox(pathTBox);
        //数据预处理
        preDealData(pathABox);

//        DictionaryOutput.outWriteDicOwlMap("data/outRule.txt");
        //单线程推理
        serialReason();
//        System.out.println("size of dictionary"+Dictionary.getEncode().size());
//        DictionaryOutput.outWriteSameAs("data/sameAs.nt");
//        DictionaryOutput.encodeMap("data/encode.nt");
        DictionaryOutput.outWriteDicDataMap(pathNewABox);
        //owl:Thing <owl:Thing> jena 解析
        queryByJena(pathNewABox, queryByJena, queryPath);
        while(true){

        }
    }

    private static void queryByJena(String pathNewABox, boolean queryByJena, String queryPath) throws IOException {
        if(queryByJena){
            String path = "jenaData.nt";
            RewriteThing.rewriteThing(pathNewABox, path);
            jenaQuerySimple(path, queryPath, null);
        }
    }

    private static void serialReason() throws IOException {
        long startTime3 = System.currentTimeMillis();
        DicSerialReason.reason();
        long startTime4=System.currentTimeMillis();
        System.out.println("Materialization time: "+(startTime4-startTime3));
    }

    private static void preDealData(String pathABox) {
        long startTime1 = System.currentTimeMillis();
        DictionaryInputNew.readABox(pathABox);
        long startTime2 = System.currentTimeMillis();
        System.out.println("Data preprocessing time: "+(startTime2-startTime1));
    }


}
