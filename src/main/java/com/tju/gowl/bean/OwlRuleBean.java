package com.tju.gowl.bean;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OwlRuleBean {
    private int type;
    private String ruleHead;
    //int type


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRuleHead() {
        return ruleHead;
    }

    public void setRuleHead(String ruleHead) {
        this.ruleHead = ruleHead;
    }

    @Override
    public String toString() {
        return "OwlRuleBean{" +
                "type=" + type +
                ", ruleHead='" + ruleHead + '\'' +
                '}';
    }
}
