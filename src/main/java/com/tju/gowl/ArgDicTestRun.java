package com.tju.gowl;

import com.tju.gowl.io.DictionaryInputNew;
import com.tju.gowl.io.DictionaryOutput;
import com.tju.gowl.reason.DicSerialReason;

public class ArgDicTestRun {
    public static void main(String[] args) throws Exception {
//        final String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
//        final String ub = "http://swat.cse.lehigh.edu/onto/univ-bench.owl#";
        int index = 0;
        String pathTBox = null;
        String pathABox = null;
        String pathdata = null;
        if(args.length>index){
            pathTBox = args[index];
            index++;
        }
        if(args.length>index){
            pathABox = args[index];
            index++;
        }
        if(args.length>index){
            pathdata = args[index];
            index++;
        }
        //规则预处理
        DictionaryInputNew.readTBox(pathTBox);
//        InversePropertyMap.rewriteInverseRule();
        //数据预处理
        DictionaryInputNew.readABox(pathABox);

        //单线程推理
        long startTime = System.currentTimeMillis();
        DicSerialReason.reason();
        long startTime1=System.currentTimeMillis();
        System.out.println("reason time"+(startTime1-startTime));
        DictionaryOutput.outWriteDicDataMap(pathdata,1);
    }
}
