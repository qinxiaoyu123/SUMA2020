package com.tju.gowl.owlProcessor;

import com.tju.gowl.bean.DicOwlMap;
import com.tju.gowl.dictionary.Dictionary;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;

public class OWLSubObjectPropertyProcessor {
    private static void Processor(OWLAxiom axiom) {
        int type;
        type = axiom.typeIndex();
        String SubProperty = ((OWLSubObjectPropertyOfAxiom) axiom).getSubProperty().toString();
        String SuperProperty = ((OWLSubObjectPropertyOfAxiom) axiom).getSuperProperty().toString();


    }
}
