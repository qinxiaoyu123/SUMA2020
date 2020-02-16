package com.tju.suma.jenaQuery;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.*;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class JenaTest {
    static HashSet<String> resultsSet = new HashSet<>();

    public static void jenaQuerySimple(String dataPath, String queryPath) throws IOException {
         jenaQuerySimple(dataPath, queryPath, null);
    }

    public static void jenaQuerySimple(String dataPath, String queryPath, String answerPath) throws IOException {
        //query
        query.readQuery(queryPath);
        Model model = ModelFactory.createMemModelMaker().createDefaultModel();
        //extended data
        model.read(dataPath);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(answerPath), StandardCharsets.UTF_8));
        long startTime1;
        long startTime2;
        List<String> queryList = query.getQueryList();
        Iterator<String> QueryIterator = queryList.iterator();
        int count = 0;
        while (QueryIterator.hasNext()) {
            resultsSet.clear();
            String queryString = QueryIterator.next();
            startTime1 = System.currentTimeMillis();
            Query query = QueryFactory.create(queryString);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            startTime2 = System.currentTimeMillis();

            ResultSet results = qe.execSelect();

            //输出查询结果
            StringBuilder ss = new StringBuilder();
            int resultsCount = 0;
            while (results.hasNext()) {
                QuerySolution next = results.next();
                RDFNode resource = next.get("?x");
                if (next.get("?y") != null) {
                    RDFNode resource1 = next.get("?y");
                    if (next.get("?z") != null) {
                        RDFNode resource2 = next.get("?z");
                        ss.setLength(0);
                        ss.append(resource.toString()).append(" ").append(resource1.toString()).append(" ").append(resource2.toString());
                    } else {
                        ss.setLength(0);
                        ss.append(resource.toString()).append(" ").append(resource1.toString());
                    }

                } else {
                    ss.setLength(0);
                    ss.append(resource.toString());
                }
                out.write(ss.toString());
                if (!ss.toString().startsWith("-")) {
                    resultsSet.add(ss.toString());
                }
                out.newLine();
                resultsCount++;

            }
            qe.close();
            count++;
            System.out.println("q" + " " + count);
            System.out.println("queryTime" + (startTime2 - startTime1));
            System.out.println("resultsCount" + resultsCount);
            System.out.println("resultsCount" + resultsSet.size());

        }
        out.flush();
        out.close();
    }

    public static void main(String[] args) throws Exception {
        String dataPath = "data/newThing_oubm1.nt";
        String queryPath = "data/test.sparql";
        jenaQuerySimple(dataPath, queryPath);
    }

}
