package com.tju.gowl;

import com.hp.hpl.jena.sparql.pfunction.library.str;
import com.tju.gowl.bean.RdfDataBean;
import com.tju.gowl.io.Input;
import com.tju.gowl.io.Output;
import com.tju.gowl.reason.SerialReason;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class test {
    public static void main(String[] args) throws Exception {
////
//        String ss = "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#takesCourse>";
//        String rs = "<http://www.Department0.University0.edu/GraduateStudent0>";
//        StringTokenizer st1 = new StringTokenizer(ss, "#");
//        List<String> list2=new ArrayList<>();
//        while(st1.hasMoreElements()) {
//            list2.add(st1.nextToken());
//        }
//        String RoNew = rs.substring(0, rs.length()-1)+list2.get(1);
//        System.out.println(RoNew);


//        String pathABox="data/new_uobm1.nt";
//        Path fpath= Paths.get(pathABox);
//        BufferedReader bfr= Files.newBufferedReader(fpath);
//        String line;
//       int index1 = 1;
//        while((line=bfr.readLine())!=null) {
//            if(index1>=(260962)&&index1<=(260962+10)){
//                if(line.equals("")){
////                    System.out.println("kong");
//                }
//                System.out.println(line);
//            }
//            index1 ++;
//        }
//        bfr.close();



//        String pathABox="data/new_uobm1.nt";
//        Path fpath= Paths.get(pathABox);
//        BufferedReader bfr= Files.newBufferedReader(fpath);
//        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/newThing_oubm1.nt"),"GBK"));
//        String line;
//        while((line=bfr.readLine())!=null) {
//            if(line.contains("</rdf:RDF><?xml version=\"1.0\" encoding=\"UTF-8\"?>")){
//                while((line=bfr.readLine())!=null){
//                    if(line.contains("<rdf:Description")){
//                        break;
//                    }
//                }
//            }
//            if(!line.equals("")){
//                out.write(line);
//                out.newLine();
//            }
//
//
//        }
//        bfr.close();
//        out.flush();
//        out.close();

        String pathABox="data/new_uobm1.nt";
        Path fpath= Paths.get(pathABox);
        BufferedReader bfr= Files.newBufferedReader(fpath);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/newThing_oubm1.nt"),"GBK"));
        String line;
        boolean flag = false;
        StringBuffer ss = new StringBuffer();
        while((line=bfr.readLine())!=null) {
            flag = false;
            StringTokenizer st = new StringTokenizer(line, " ");
            List<String> list=new ArrayList<>();
            while(st.hasMoreElements()) {
                list.add(st.nextToken());
            }
            Iterator<String> iterorList = list.iterator();
            ss.setLength(0);
            while(iterorList.hasNext()) {
                if (flag) {
                    ss.append(" ");
                }
                String ss1 = iterorList.next();
                if (ss1.contains("owl:Thing")) {
                    ss.append("<").append(ss1).append(">");
                } else ss.append(ss1);
                flag = true;
            }
            out.write(ss.toString());
            out.newLine();
        }
        bfr.close();
        out.flush();
        out.close();
    }
//        //连接本地的 Redis 服务
//        Jedis jedis = new Jedis("localhost");
//        System.out.println("连接成功");
//        //查看服务是否运行
//        System.out.println("服务正在运行: "+jedis.ping());
//        RdfDataBean rdf = new RdfDataBean();
//        Map<String,String> map= new ConcurrentHashMap<>();
//        map.put("name","qin");
//        map.put("type","is");
//        map.put("place","teacher");
//        map.put("po","5");
//        map.put("sp","2");
//        map.put("p","3");
//        //cc.put(1,rdf);
//        jedis.hmset( "1",map);
//        Map<String, String> map1 = jedis;
//        System.out.println(map1);


}
