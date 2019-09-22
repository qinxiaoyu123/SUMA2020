package com.tju.gowl;

import com.tju.gowl.bean.InversePropertyMap;
import com.tju.gowl.bean.RdfDataBean;
import com.tju.gowl.bean.RdfDataMap;
import com.tju.gowl.io.DictionaryInput;
import com.tju.gowl.io.DictionaryOutput;
import com.tju.gowl.io.Input;
import com.tju.gowl.reason.DicSerialReason;
import com.tju.gowl.reason.SerialReason;

import java.util.Map;
public class DicTestRun {
    public static void main(String[] args) throws Exception {
//        final String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
//        final String ub = "http://swat.cse.lehigh.edu/onto/univ-bench.owl#";

//        String pathTBox = "data/pellet.owl";
        String pathTBox = "data/uobm.owl";

        String pathABox = "data/uobm1.nt";
        //规则预处理
        DictionaryInput.readTBox(pathTBox);
//        InversePropertyMap.rewriteInverseRule();
        //数据预处理
        long startTime1 = System.currentTimeMillis();
        DictionaryInput.readABox(pathABox, "n","n",1);
        long startTime2 = System.currentTimeMillis();
        System.out.println("reason time"+(startTime2-startTime1));

        DictionaryOutput output=new DictionaryOutput();
        //output.writeRuleFile("data/outRule.txt");
        //单线程推理
        long startTime3 = System.currentTimeMillis();
        DicSerialReason.reason();
        //SerialReason.writeFile("data/out.txt");
        long startTime4=System.currentTimeMillis();
        System.out.println("reason time"+(startTime4-startTime3));
//        DictionaryOutput.encodeMap("data/encode.nt");
//        DictionaryOutput.outWriteDicDataMap("data/new.nt");
        //inverse write
        DictionaryOutput.outWriteDicDataMap("data/new_uobm1.nt",1);
    }
}
