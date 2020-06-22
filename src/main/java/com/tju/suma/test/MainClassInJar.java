package com.tju.suma.test;

import com.tju.suma.axiomProcessor.Processor;
import com.tju.suma.dictionary.Dictionary;
import com.tju.suma.io.DictionaryInput;
import com.tju.suma.io.DictionaryOutput;
import com.tju.suma.jenaQuery.RewriteThing;
import com.tju.suma.reason.DicSerialReason;
import com.tju.suma.reason.ObjectSomeValuesFromReason;
import com.tju.suma.reason.SameAsReason;
import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.IOException;

import static com.tju.suma.jenaQuery.JenaTest.jenaQuerySimple;

public class MainClassInJar {
    private static Logger log = Logger.getLogger(MainClassInJar.class);
    public static void main(String[] args) throws Exception {
        if(args.length<5){
            System.out.println("Please enter args as follows: pathTBox pathABox pathExtendedABox isQueryByJena queryPath");
            return;
        }
        String pathTBox = args[0];
        String pathABox = args[1];
        String pathExtendedABox = args[2];
        String pathDataThing = "newThing.nt";
        boolean isQueryByJena = Boolean.parseBoolean(args[3]);
        initIsRoleWriting(true);
        String queryPath = args[4];
        String answerPath = "result_new.nt";

        int n_step = 7;

        DictionaryInit();

        preProcessRules(pathTBox);
        preProcessData(pathABox);

        outPutDictionaryToFile();

        materialization(n_step);

        readDictionaryInMemory();

//        DictionaryOutput.sameAsMap("sameas.txt");


        writeFile(pathExtendedABox);

//        System.out.println("ObjectSomeValuesFromReason.objectSomeVaule "+ ObjectSomeValuesFromReason.objectSomeVaule);
//        System.out.println("ObjectSomeValuesFromReason.objectSomeReturn "+ ObjectSomeValuesFromReason.objectSomeReturn);

        if (isQueryByJena) {
            log.info("----------------------Start Query--------------------------");
            queryByJena(pathExtendedABox, pathDataThing, queryPath, answerPath);
        }
    }

    private static void DictionaryInit() {
        Dictionary.init();
    }

    private static void preProcessRules(String pathTBox) throws OWLOntologyCreationException {
        DictionaryInput.readTBox(pathTBox);
    }

    private static void preProcessData(String pathABox) throws IOException {
        long startTime1 = System.currentTimeMillis();
        DictionaryInput.readABox(pathABox);
        long startTime2 = System.currentTimeMillis();
        log.info("reading data time: " + (startTime2 - startTime1) + " ms");
    }

    private static void outPutDictionaryToFile() throws IOException {
        DictionaryOutput.encodeMap("encode.txt");
    }

    public static void materialization(int n_step) {
        long startTime3 = System.currentTimeMillis();
        DicSerialReason.reason(n_step);
        long startTime4 = System.currentTimeMillis();
        log.info("reason time: " + (startTime4 - startTime3) + " ms");
        SameAsReason.addEquivIndividual();
    }

    private static void readDictionaryInMemory() throws IOException {
        DictionaryInput.readDictionary("encode.txt");
    }

    public static void initIsRoleWriting(boolean isRoleWriting) {
        Processor.isRoleWriting = isRoleWriting;
    }

    private static void writeFile(String pathData) throws IOException {
        DictionaryOutput.outWriteDicDataMap(pathData);
    }

    private static void queryByJena(String pathABox, String pathABoxThing, String queryPath, String answerPath) throws IOException {
        RewriteThing.rewriteThing(pathABox, pathABoxThing);
        jenaQuerySimple(pathABoxThing, queryPath, answerPath);
    }

}
