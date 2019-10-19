package com.tju.gowl.materialization;

import com.tju.gowl.RewriteThing;
import com.tju.gowl.dictionary.Dictionary;
import com.tju.gowl.reason.DicSerialReason;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.IOException;

import static com.tju.gowl.JenaTest.jenaQuerySimple;

public class MaterializationImpl {

    public void materialAll() throws OWLOntologyCreationException, IOException {
        String pathTBox = "data/univ-bench-dl.owl";
        String pathABox = "data/uobm1.nt";
        CodeRepository.init();

        RegularBoxImpl.readTBox(pathTBox);

        long startTime = System.currentTimeMillis();
        DataBoxImpl.readABox(pathABox);
        long endTime = System.currentTimeMillis();
        System.out.println("reading time"+(endTime-startTime));

        OutputDictionary.outWriteDicOwlMap("data/outRule.txt");

        long startTimeReason = System.currentTimeMillis();
        DicSerialReason.reason();
        long endTimeReason=System.currentTimeMillis();
        System.out.println("reason time"+(endTimeReason-startTimeReason));
        System.out.println("size of dictionary"+ Dictionary.getEncode().size());

        OutputDictionary.outWriteSameAs("data/sameAs.nt");
        OutputDictionary.encodeMap("data/encode.nt");
        OutputDictionary.outWriteDicDataMap("data/new_uobm1.nt",1);

        //owl:Thing <owl:Thing> jena 解析
        RewriteThing.rewriteThing();
        jenaQuerySimple("data/newThing_oubm1.nt", "data/test.sparql", null);
    }
}
