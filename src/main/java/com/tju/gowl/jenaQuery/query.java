package com.tju.gowl.jenaQuery;

import com.tju.gowl.bean.DicOwlBean;
import com.tju.gowl.bean.DicRdfDataBean;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class query {
    private static final List<String> queryList = new ArrayList<>();
    public static List<String> getQueryList(){ return queryList; }
    static int queryCount = 0;

    public static void readQuery(String pathQuery) throws IOException {
        Path fpath= Paths.get(pathQuery);
        try {
            BufferedReader bfr = Files.newBufferedReader(fpath);
            String line;
            StringBuffer query = new StringBuffer();
            while ((line = bfr.readLine()) != null) {
                if(!line.contains("^")){
                    query.append(line).append("\n");
                    if(line.contains("}")){
                        queryList.add(query.toString());
                        queryCount ++;
                        query.setLength(0);
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        readQuery("data/standard_and_gap.sparql");
        System.out.println(queryList);
        System.out.println(queryCount);
    }
}
