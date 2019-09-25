package com.tju.gowl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class compare {
    static HashSet<String> data_gOWL = new HashSet<>();

    static HashSet<String> data_pellet = new HashSet<>();

    public static void main(String[] args) throws Exception {
        String pathABox1="F:\\first paper\\experiments\\pellet-1.3\\uobm\\aaa\\resultnew.nt";
        String pathABox2="data/resultnew.nt";
        readPelletData(pathABox1);
        readgOWLData(pathABox2);
        System.out.println(data_gOWL.size());
        System.out.println(data_pellet.size());
        data_pellet.removeAll(data_gOWL);
        Iterator<String> iter = data_pellet.iterator();
        while (iter.hasNext()){
            System.out.println(iter.next());
        }
//        System.out.println(data_pellet);
        System.out.println(data_pellet.size());
    }

    private static void readgOWLData(String pathABox2) throws IOException {
        Path fpath= Paths.get(pathABox2);
        BufferedReader bfr= Files.newBufferedReader(fpath);

        String line;

        StringBuffer ss = new StringBuffer();
        while((line=bfr.readLine())!=null) {
            StringTokenizer st = new StringTokenizer(line, "//");
            List<String> list=new ArrayList<>();
            while(st.hasMoreElements()) {
                list.add(st.nextToken());
            }
            if(list.size()!=3){
                System.out.println("error");


            }
            else{
//                System.out.println(list.get(0));
                ss.append(list.get(1)).append("/").append(list.get(2));
                String ss1 = ss.toString().trim();
//                System.out.println(ss1);
                if(data_gOWL.contains(ss1)){
                    System.out.println("重复： "+ss1);
                }
                data_gOWL.add(ss1);
                ss.setLength(0);

            }
        }
    }

    private static void readPelletData(String pathABox) throws IOException {
        Path fpath= Paths.get(pathABox);
        BufferedReader bfr= Files.newBufferedReader(fpath);

        String line;

        StringBuffer ss = new StringBuffer();
        while((line=bfr.readLine())!=null) {
            StringTokenizer st = new StringTokenizer(line, ":");
            List<String> list=new ArrayList<>();
            while(st.hasMoreElements()) {
                list.add(st.nextToken());
            }
            if(list.size()!=2){
                System.out.println("error");
            }
            else{
//                System.out.println(list.get(0));
                ss.append(list.get(0)).append("/").append(list.get(1));
                String ss1 = ss.toString().trim();
//                System.out.println(ss1);
                data_pellet.add(ss1);
                ss.setLength(0);
            }
        }
    }
}
