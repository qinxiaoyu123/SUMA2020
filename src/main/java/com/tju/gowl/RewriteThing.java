package com.tju.gowl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class RewriteThing {
    public static void rewriteThing() throws IOException {
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
                } else {
                    ss.append(ss1);
                }
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
