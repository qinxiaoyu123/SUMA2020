package com.tju.gowl.run;

import com.tju.gowl.jenaQuery.RewriteThing;
import com.tju.gowl.concurrent.ConcurrentReadFile;
import com.tju.gowl.concurrent.FileHandle;
import com.tju.gowl.concurrent.IndexCalculate;
import com.tju.gowl.concurrent.IndexPair;
import com.tju.gowl.dictionary.Dictionary;
import com.tju.gowl.io.DictionaryInput;
import com.tju.gowl.io.DictionaryOutput;
import com.tju.gowl.reason.DicSerialReason;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.tju.gowl.DicTestRun.materialization;
import static com.tju.gowl.jenaQuery.JenaTest.jenaQuerySimple;

class DicTestRunTest {
    @Test
    void materialization_LUBM() throws OWLOntologyCreationException, IOException {
        String pathTBox = "data/pellet.owl";
        String pathABox = "data/uobm1.nt";
        int n =0;
        materialization(pathTBox,pathABox, n);
    }

    @Test
    void materialization_UOBM() throws OWLOntologyCreationException, IOException {
        String pathTBox = "data/univ-bench-dl.owl";
        String pathABox = "data/uobm1.nt";
        int n = 0;
        materialization(pathTBox,pathABox, n);
    }

    @Test
    void materialization_UOBM_concurrent() throws Exception {
        new Dictionary();

        String pathTBox = "data/univ-bench-dl.owl";
        DictionaryInput.readTBox(pathTBox);

        String pathABox = "data/uobm1.nt";
        File file = new File(pathABox);
        List<IndexPair> indexPairs = IndexCalculate.getIndex(file, 64);
        long startTime2 = System.currentTimeMillis();
        ConcurrentReadFile concurrentReadFile = new ConcurrentReadFile(pathABox, indexPairs, new FileHandle() {
            @Override
            public void handle(String line) {
//                index = getIndex(index, line);
            }
        });
        concurrentReadFile.readFile();
        concurrentReadFile.end();
        System.out.println(String.format("ConcurrentReadFile read file cost time : %s", System.currentTimeMillis() - startTime2));


        DictionaryOutput.outWriteDicOwlMap("data/outRule.txt");
        //单线程推理
        long startTime3 = System.currentTimeMillis();
        int n = 0;
        DicSerialReason.reason(n);
        long startTime4=System.currentTimeMillis();
        System.out.println("reason time"+(startTime4-startTime3));

        DictionaryOutput.outWriteSameAs("data/sameAs.nt");
        DictionaryOutput.encodeMap("data/encode.nt");
        DictionaryOutput.outWriteDicDataMap("data/new_uobm1.nt");
        //owl:Thing <owl:Thing> jena 解析
        RewriteThing.rewriteThing("data/new_uobm1.nt", "data/newThing_oubm1.nt");
        jenaQuerySimple("data/newThing_oubm1.nt", "data/test.sparql", null);
    }


}