package com.tju.gowl.materialization;

import com.tju.gowl.io.DictionaryInputNew;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

public class MaterializationImpl {

    public void materialAll() throws OWLOntologyCreationException {
        String pathTBox = "data/univ-bench-dl.owl";
        String pathABox = "data/uobm1.nt";
        CodeRepository.init();

        RegularBoxImpl.readTBox(pathTBox);

        long startTime = System.currentTimeMillis();
        DataBoxImpl.readABox(pathABox);
        long endTime = System.currentTimeMillis();
        System.out.println("reading time"+(endTime-startTime));
    }
}
