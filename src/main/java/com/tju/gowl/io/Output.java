package com.tju.gowl.io;

import com.tju.gowl.bean.*;

import java.io.*;
import java.util.List;
import java.util.Map;

public class Output {

    public void writeRuleFile(String pathTboxNew) throws IOException {
        Map<String, List<OwlRuleBean>> totalRule = OwlRuleMap.getRuleMap();
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathTboxNew),"GBK"));
        for(Map.Entry<String, List<OwlRuleBean>> entry : totalRule.entrySet()){
            out.write(entry.getKey());//写入文件
            out.write(" "+entry.getValue().toString());
            out.newLine();
        }
        out.flush();
        out.close();
    }

    public static void writeFile(String path) throws IOException {
        //临时存储要写入数据，数据到1000条再写入，减少io代价，同时不占用太大内存
        Map<Integer, DicRdfDataBean> dicTotalRdfData = DicRdfDataMap.getDicDataMap();
        //TODO 不大于1000就不写，最后不足1000需要处理
        if(dicTotalRdfData.size()>=0){
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path),"GBK"));
//            out.write("<unknown:namespace> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2002/07/owl#Ontology> .");
//            out.newLine();
////            out.write("<unknown:namespace> <http://www.w3.org/2002/07/owl#imports> <http://swat.cse.lehigh.edu/onto/univ-bench.owl> .");
////            out.newLine();
            for(Map.Entry<Integer, DicRdfDataBean> entry : dicTotalRdfData.entrySet()){
                //out.write(entry.getKey().toString());//写入文件
                out.write(entry.getValue().getRs()+" "+entry.getValue().getRp()+" "+entry.getValue().getRo());
                out.newLine();
            }
            out.flush();
            out.close();
            // totalRdfData.clear();
        }

    }
}
