package com.tju.gowl.io;

import com.tju.gowl.bean.*;
import com.tju.gowl.dictionary.Dictionary;
import com.tju.gowl.reason.DicSerialReason;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Iterator;
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
    public static void outWriteSameAs(String path) throws IOException {
        List<String> encodeList = Dictionary.getEncodeList();

        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path),"GBK"));
        List<HashSet<Integer>> poolEqui = DicSerialReason.equiPool;
        Iterator<HashSet<Integer>> poolIter = poolEqui.iterator();
        int count = 0;
        while(poolIter.hasNext()){
            count++;
            out.write("EquiPool "+count);
            out.newLine();
            HashSet<Integer> poolTmp = poolIter.next();
            Iterator<Integer> poolTmpIter = poolTmp.iterator();
            while(poolTmpIter.hasNext()){
                Integer ii = poolTmpIter.next();
                out.write(encodeList.get(ii));//写入文件
                out.newLine();
            }
        }
        out.flush();
        out.close();
    }
    public static void outWriteEquiDicOwlMap(String pathTboxNew) throws IOException {

        List<String> encodeList = Dictionary.getEncodeList();

        Map<Integer, List<DicOwlBean>> totalRule = DicOwlMap.EquiDicRuleMap;
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathTboxNew),"GBK"));
        for(Map.Entry<Integer, List<DicOwlBean>> entry : totalRule.entrySet()){
            out.write(entry.getKey()+" ");
            out.write(encodeList.get(entry.getKey()));//写入文件
            out.newLine();
            Iterator<DicOwlBean> ii = entry.getValue().iterator();
            while(ii.hasNext()){
                DicOwlBean iii = ii.next();
                out.write(iii.getType()+" ");
                out.newLine();
                Iterator<Integer> iiii = iii.getRuleHead().iterator();
                while(iiii.hasNext()){
                    Integer iiiii = iiii.next();
                    out.write(iiiii+" ");
                    out.write(encodeList.get(iiiii));
                    out.newLine();
                }
            }
            out.newLine();
        }
        out.flush();
        out.close();
    }


    //逆属性进行写
    public static void outWriteDicDataMap(String pathAboxNew) throws IOException {
        int count = 0;
        Map<Integer, DicRdfDataBean> totalData = DicRdfDataMap.getDicDataMap();
        List<String> encodeList = Dictionary.getEncodeList();
        Map<Integer, Integer> inverseMapDecode = InversePropertyMap.getInverseMapDecode();
        Map<Integer, Integer> EquivalentMapDecode = EquivalentPropertyMap.getEquivalentPropertyMapDecode();
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
                Rs = encodeList.get(rs);
            }

            int rp = entry.getValue().getRp();
            String Rp;
            if(rp<0){
                Rp = "<"+String.valueOf(rp)+">";
            }
            else{
                Rp = encodeList.get(rp);
            }

            int ro = entry.getValue().getRo();
            String Ro;
            if(ro<0){
                Ro = "<"+String.valueOf(ro)+">";
            }
            else{
                Ro = encodeList.get(ro);
            }
//            out.write(entry.getKey()+" "+Rs+" "+Rp+" "+Ro+" ."+entry.getValue().getNsp()+" "+entry.getValue().getNp()+" "+entry.getValue().getNpo());
            if(inverseMapDecode.containsKey(rp)){
                String Rp1 = encodeList.get(inverseMapDecode.get(rp));
//                System.out.println(Ro+" "+Rp1+" "+Rs+" .");
                out.write(Ro+" "+Rp1+" "+Rs+" .");
                count++;
            }
            if(EquivalentMapDecode.containsKey(rp)){
                String Rp1 = encodeList.get(EquivalentMapDecode.get(rp));
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
//        Map<String, Integer> encode = Dictionary.getEncode();
        List<String> encodeList = Dictionary.getEncodeList();
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathEncode),"GBK"));
        int index = 0;
        for(String ss:encodeList){
            out.write(ss);
            out.write(" "+index);
            index++;
            out.newLine();
        }
        out.flush();
        out.close();
    }


}
