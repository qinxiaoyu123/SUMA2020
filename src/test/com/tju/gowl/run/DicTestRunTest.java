package com.tju.gowl.run;

import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.IOException;

import static com.tju.gowl.DicTestRun.materialization;

class DicTestRunTest {
    @Test
    void materialization_LUBM() throws OWLOntologyCreationException, IOException {
        String pathTBox = "data/pellet.owl";
        String pathABox = "data/uobm1.nt";
        materialization(pathTBox,pathABox);
    }

    @Test
    void materialization_UOBM() throws OWLOntologyCreationException, IOException {
        String pathTBox = "data/univ-bench-dl.owl";
        String pathABox = "data/uobm1.nt";
        materialization(pathTBox,pathABox);
    }

}