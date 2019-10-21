package com.tju.gowl.run;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import static java.util.Objects.hash;

public class findAnyBug {
    @Test
    void know_string_append_is_right(){
        String ss = "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#takesCourse>";
        String rs = "<http://www.Department0.University0.edu/GraduateStudent0>";
        StringTokenizer st1 = new StringTokenizer(ss, "#");
        List<String> list2=new ArrayList<>();
        while(st1.hasMoreElements()) {
            list2.add(st1.nextToken());
        }
        String RoNew = rs.substring(0, rs.length()-1)+list2.get(1);
        System.out.println(RoNew);
    }

    @Test
    void know_string_append_is_right1(){
        String value = "<http://www.Department1.University0.edu>";
        byte[] aa = value.getBytes();
        while (true){

        }

//        StringBuffer ss = new StringBuffer();
//        StringTokenizer st = new StringTokenizer(valueNew, ".");
//         List<String> list = new ArrayList<>(5);
//         while (st.hasMoreElements()) {
//             list.add(st.nextToken());
//         }
//         int i = 1;
//
//         while(list.get(i)!=null){
//             ss.append(".").append(list.get(i));
//             i++;
//         }
//         System.out.println(valueNew);
    }

    @Test
    public void output_bug_line_in_file() throws IOException {
        String pathABox="data/new_uobm1.nt";
        Path fpath= Paths.get(pathABox);
        BufferedReader bfr= Files.newBufferedReader(fpath);
        String line;
        int index1 = 1;
        while((line=bfr.readLine())!=null) {
            if(index1>=(4)&&index1<=(4+10)){
                if(line.equals("")){
                    System.out.println("kong");
                }
                System.out.println(line);
            }
            index1 ++;
        }
        bfr.close();
    }

    @Test
    public void try_delete_file_XML_line() throws IOException {
        String pathABox="data/new_uobm1.nt";
        Path fpath= Paths.get(pathABox);
        BufferedReader bfr= Files.newBufferedReader(fpath);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/newThing_oubm1.nt"),"GBK"));
        String line;
        while((line=bfr.readLine())!=null) {
            if(line.contains("</rdf:RDF><?xml version=\"1.0\" encoding=\"UTF-8\"?>")){
                while((line=bfr.readLine())!=null){
                    if(line.contains("<rdf:Description")){
                        break;
                    }
                }
            }
            if(!line.equals("")){
                out.write(line);
                out.newLine();
            }


        }
        bfr.close();
        out.flush();
        out.close();
    }

    @Test
    public void add_quote_in_file_line() throws IOException {
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
}
