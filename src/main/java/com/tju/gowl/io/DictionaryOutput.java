package com.tju.gowl.io;

import com.tju.gowl.bean.*;
import com.tju.gowl.dictionary.Dictionary;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

public class DictionaryOutput {
    public static void outWriteDicOwlMap(String pathTboxNew) throws IOException {
        Map<String, List<DicOwlBean>> totalRule = DicOwlMap.getRuleMap();
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathTboxNew),"GBK"));
        for(Map.Entry<String, List<DicOwlBean>> entry : totalRule.entrySet()){
            out.write(entry.getKey());//写入文件
            out.write(" "+entry.getValue().toString());
            out.newLine();
        }
        out.flush();
        out.close();
    }

    public static void outWriteDicDataMap(String pathAboxNew) throws IOException {
        Map<Integer, DicRdfDataBean> totalData = DicRdfDataMap.getDicDataMap();
        Map<Integer, String> decode = Dictionary.getDecode();
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathAboxNew),"GBK"));
        out.write("<unknown:namespace> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2002/07/owl#Ontology> .");
        out.newLine();
        out.write("<unknown:namespace> <http://www.w3.org/2002/07/owl#imports> <http://swat.cse.lehigh.edu/onto/univ-bench.owl> .");
        out.newLine();
        for(Map.Entry<Integer, DicRdfDataBean> entry : totalData.entrySet()){
//            out.write(entry.getKey());//写入文件
            int rs = entry.getValue().getRs();
            String Rs;
            if(rs<0){
                Rs = "<"+String.valueOf(rs)+">";
            }
            else{
                Rs = decode.get(rs);
            }

            int rp = entry.getValue().getRp();
            String Rp;
            if(rp<0){
                Rp = "<"+String.valueOf(rp)+">";
            }
            else{
                Rp = decode.get(rp);
            }

            int ro = entry.getValue().getRo();
            String Ro;
            if(ro<0){
                Ro = "<"+String.valueOf(ro)+">";
            }
            else{
                Ro = decode.get(ro);
            }
//            out.write(entry.getKey()+" "+Rs+" "+Rp+" "+Ro+" ."+entry.getValue().getNsp()+" "+entry.getValue().getNp()+" "+entry.getValue().getNpo());
            out.write(Rs+" "+Rp+" "+Ro+" .");
            out.newLine();
        }
        out.flush();
        out.close();
    }
    //逆属性进行写
    public static void outWriteDicDataMap(String pathAboxNew, int flag) throws IOException {
        int count = 0;
        Map<Integer, DicRdfDataBean> totalData = DicRdfDataMap.getDicDataMap();
        Map<Integer, String> decode = Dictionary.getDecode();
        Map<Integer, Integer> inverseMap = InversePropertyMap.getInverseMap1();
        Map<Integer, Integer> EquivalentMap = EquivalentPropertyMap.getEquivalentPropertyMap();
//        System.out.println(inverseMap);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathAboxNew),"GBK"));
        out.write("<unknown:namespace> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2002/07/owl#Ontology> .");
        out.newLine();
        out.write("<unknown:namespace> <http://www.w3.org/2002/07/owl#imports> <http://swat.cse.lehigh.edu/onto/univ-bench.owl> .");
        out.newLine();
        for(Map.Entry<Integer, DicRdfDataBean> entry : totalData.entrySet()){
//            out.write(entry.getKey());//写入文件
            int rs = entry.getValue().getRs();
            String Rs;
            if(rs<0){
                Rs = "<"+String.valueOf(rs)+">";
            }
            else{
                Rs = decode.get(rs);
            }

            int rp = entry.getValue().getRp();
            String Rp;
            if(rp<0){
                Rp = "<"+String.valueOf(rp)+">";
            }
            else{
                Rp = decode.get(rp);
            }

            int ro = entry.getValue().getRo();
            String Ro;
            if(ro<0){
                Ro = "<"+String.valueOf(ro)+">";
            }
            else{
                Ro = decode.get(ro);
            }
//            out.write(entry.getKey()+" "+Rs+" "+Rp+" "+Ro+" ."+entry.getValue().getNsp()+" "+entry.getValue().getNp()+" "+entry.getValue().getNpo());
            if(inverseMap.containsKey(rp)){
                String Rp1 = decode.get(inverseMap.get(rp));
//                System.out.println(Ro+" "+Rp1+" "+Rs+" .");
                out.write(Ro+" "+Rp1+" "+Rs+" .");
                count++;
            }
            if(EquivalentMap.containsKey(rp)){
                String Rp1 = decode.get(EquivalentMap.get(rp));
//                System.out.println(Ro+" "+Rp1+" "+Rs+" .");
                out.write(Rs+" "+Rp1+" "+Ro+" .");
                count++;
            }
            out.write(Rs+" "+Rp+" "+Ro+" .");
            count++;
            out.newLine();
        }
        out.flush();
        out.close();
        System.out.println("total data"+count);
    }

    public static void encodeMap(String pathEncode) throws IOException {
        Map<String, Integer> encode = Dictionary.getEncode();
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathEncode),"GBK"));
        for(Map.Entry<String, Integer> entry : encode.entrySet()){
            out.write(entry.getKey());//写入文件
            out.write(" "+entry.getValue().toString());
            out.newLine();
        }
        out.flush();
        out.close();
    }


}
