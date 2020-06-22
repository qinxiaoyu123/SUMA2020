package com.tju.suma.util;

import com.tju.suma.dictionary.Dictionary;
import com.tju.suma.reason.SameAsReason;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class test_bug {
    public static void output_bug_line_in_file() throws IOException {
        String pathABox="newThing_oubm1.nt";
        Path fpath= Paths.get(pathABox);
        BufferedReader bfr= Files.newBufferedReader(fpath);
        String line;
        int index1 = 1;
        while((line=bfr.readLine())!=null) {
            if(index1>=(29903268-2)&&index1<=(29903268+10)){
                if(line.equals("")){
                    System.out.println("kong");
                }
                System.out.println(line);
            }
            index1 ++;
        }
        bfr.close();
    }

    public static void rewrite_yago_ontology() throws IOException {
        String pathTBoxBefore = "D:\\experiments\\服务器代码\\yago数据\\yago-wd-schema.nt\\yago-wd-schema.nt";
        Path fpath= Paths.get(pathTBoxBefore);
        BufferedReader bfr= Files.newBufferedReader(fpath);

        String pathTBoxAfter ="D:\\experiments\\服务器代码\\yago数据\\yago-wd-schema.nt\\yago-wd-schema-rewrite.nt";
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathTBoxAfter), StandardCharsets.UTF_8));
        String line;
        while((line=bfr.readLine())!=null) {
                if(line.contains("http://schema.org/")){
                    String s1 = line.replaceAll("http://schema.org/", "http://schema.org#");
                    out.write(s1);
                    out.newLine();

                }
                else{
                    out.write(line);
                    out.newLine();
                }
        }
        out.flush();
        out.close();
        bfr.close();
    }

    public static void rewrite_yago_data() throws IOException {
        String pathABoxBefore = "D:\\experiments\\服务器代码\\yago数据\\yago-wd-facts.nt\\yago-wd-facts-rewrite.nt";
        Path fpath= Paths.get(pathABoxBefore);
        BufferedReader bfr= Files.newBufferedReader(fpath);

        String pathABoxAfter ="D:\\experiments\\服务器代码\\yago数据\\yago-wd-facts.nt\\yago-wd-facts-rewrite.nt";
//        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathABoxAfter), StandardCharsets.UTF_8));
        int index = 0;
        String line;
        while((line=bfr.readLine())!=null && index<10) {
            System.out.println(line);
            index++;
        }
    }

    public static void main(String[] args) throws Exception {
//        output_bug_line_in_file();
        rewrite_yago_data();
//        rewrite_yago_ontology();

    }
}
