package com.tju.gowl;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.sparql.engine.QueryIterator;
import com.tju.gowl.bean.DicOwlBean;
import com.tju.gowl.bean.OwlRuleMap;
import com.tju.gowl.bean.RdfDataBean;
import com.tju.gowl.bean.RdfDataMap;
import com.tju.gowl.dictionary.Dictionary;
import com.tju.gowl.io.Input;
import com.tju.gowl.io.Output;
import com.tju.gowl.reason.DicSerialReason;
import com.tju.gowl.reason.SerialReason;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JenaTest {
    static HashSet<String> resultsSet = new HashSet<>();
    public static void jenaQuery(String dataPath, String queryPath, String answerPath) throws IOException {
        //query
        Map<String, Integer> encodeMap = Dictionary.getEncode();
        Map<Integer, String> decodeMap = Dictionary.getDecode();
        query.readQuery(queryPath);
        Model model = ModelFactory.createMemModelMaker().createDefaultModel();
        //extended data
        model.read(dataPath);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/resultnew.nt"),"GBK"));
        long startTime1;
        long startTime2;
        List<String> queryList = query.getQueryList();
        Iterator<String> QueryIterator = queryList.iterator();
        int count = 0;
        while(QueryIterator.hasNext()){

            String queryString = QueryIterator.next();
            startTime1 = System.currentTimeMillis();
            Query query = QueryFactory.create(queryString);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            startTime2 = System.currentTimeMillis();

            ResultSet results = qe.execSelect();
            resultsSet.clear();
            //输出查询结果
            System.out.println("遍历结果集依次输出结果：");
            int resultsCount = 0;
            while(results.hasNext()){
                QuerySolution next = results.next();
                RDFNode resource = next.get("?x");
                if(next.get("?y")!=null){
                    System.out.println(next.get("?x").toString()+" "+next.get("?y").toString());
                }
                StringBuffer ss = new StringBuffer("<");
                String ss1 = ss.append(resource.toString().trim()).append(">").toString();
                if(encodeMap.containsKey(ss1)){
                    int tmp = encodeMap.get(ss1);
                    int tmpIndex = DicSerialReason.findEquivPoolIndex(tmp);
                    if(tmpIndex == 0){
//                    System.out.println(resource.toString());
                        if(!resultsSet.contains(resource.toString())){
                            resultsSet.add(resource.toString());
                            out.write(resource.toString());//写入文件
                            out.newLine();
                            resultsCount ++;
                        }

                    }
                    else{
                        Iterator<Integer> ii = DicSerialReason.equiPool.get(tmpIndex-1).iterator();
                        System.out.println(resource.toString());
                        while(ii.hasNext()){
                            Integer iii = ii.next();
                            String sss =  decodeMap.get(iii);
                            String sss1 = sss.substring(1,sss.length()-1);
                            if(!resultsSet.contains(sss1)){
                                resultsSet.add(sss1);
                                out.write(sss1);//写入文件
                                out.newLine();
                                resultsCount ++;
                            }
                        }
                    }
                }
                else{
                    if(!resultsSet.contains(resource.toString())){
                        resultsSet.add(resource.toString());
                        out.write(resource.toString());//写入文件
                        out.newLine();
                        resultsCount ++;
                    }

                }


            }

            // ResultSetFormatter.out(System.out, results, query);
            qe.close();
            count++;
            System.out.println("q"+" "+count);
            System.out.println("queryTime"+(startTime2-startTime1));
            System.out.println("resultsCount"+resultsCount);
            System.out.println("resultsCount"+resultsSet.size());

        }
        out.flush();
        out.close();
    }
    public static void jenaQuerySimple(String dataPath, String queryPath, String answerPath) throws IOException {
        //query
        Map<String, Integer> encodeMap = Dictionary.getEncode();
        Map<Integer, String> decodeMap = Dictionary.getDecode();
        query.readQuery(queryPath);
        Model model = ModelFactory.createMemModelMaker().createDefaultModel();
        //extended data
        model.read(dataPath);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/resultnew1.nt"),"GBK"));
        long startTime1;
        long startTime2;
        List<String> queryList = query.getQueryList();
        Iterator<String> QueryIterator = queryList.iterator();
        int count = 0;
        while(QueryIterator.hasNext()){
            resultsSet.clear();
            String queryString = QueryIterator.next();
            startTime1 = System.currentTimeMillis();
            Query query = QueryFactory.create(queryString);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            startTime2 = System.currentTimeMillis();

            ResultSet results = qe.execSelect();

            //输出查询结果
//            System.out.println("遍历结果集依次输出结果：");
            StringBuffer ss = new StringBuffer();
            int resultsCount = 0;
            while(results.hasNext()){
                QuerySolution next = results.next();
                RDFNode resource = next.get("?x");
                if(next.get("?y")!=null){
                    RDFNode resource1 = next.get("?y");
                    if(next.get("?z")!=null){
                        RDFNode resource2 = next.get("?z");
                        ss.setLength(0);
                        ss.append(resource.toString()).append(" ").append(resource1.toString()).append(" ").append(resource2.toString());
                        out.write(ss.toString());
                        resultsSet.add(ss.toString());
                        out.newLine();
                        resultsCount ++;
                    }
                    else{
                        ss.setLength(0);
                        ss.append(resource.toString()).append(" ").append(resource1.toString());
                        out.write(ss.toString());
                        resultsSet.add(ss.toString());
                        out.newLine();
                        resultsCount ++;
                    }

                }
                else{
                    ss.setLength(0);
                    ss.append(resource.toString());
                    out.write(ss.toString());
                    resultsSet.add(ss.toString());
                    out.newLine();
                    resultsCount ++;
                }

            }

            // ResultSetFormatter.out(System.out, results, query);
            qe.close();
            count++;
            System.out.println("q"+" "+count);
            System.out.println("queryTime"+(startTime2-startTime1));
            System.out.println("resultsCount"+resultsCount);
            System.out.println("resultsCount"+resultsSet.size());

        }
        out.flush();
        out.close();
    }

    public static void main(String[] args) throws Exception {
        String dataPath = "data/newThing_oubm1.nt";
//        String dataPath = "data/uobm1.nt";
//        String dataPath = "data/new_lubm10.nt";
//        String queryPath = "data/standard_and_gap.sparql";
        String queryPath = "data/test.sparql";
//        String answerPath = "data/answer.sparql";
//        String queryPath = "data/standard.sparql";
        String answerPath = null;
        jenaQuerySimple(dataPath, queryPath, answerPath);
    }


//        String queryString1 = "SELECT ?X WHERE { ?X <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://swat.cse.lehigh.edu/onto/univ-bench.owl#Chair> }";
//        String queryString2 = "SELECT ?X WHERE { ?X <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://swat.cse.lehigh.edu/onto/univ-bench.owl#Person> .  ?X <http://swat.cse.lehigh.edu/onto/univ-bench.owl#headOf> ?Y . ?Y <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://swat.cse.lehigh.edu/onto/univ-bench.owl#Department> .}";
//        String queryString3 = "SELECT ?X WHERE { ?X <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://swat.cse.lehigh.edu/onto/univ-bench.owl#GraduateStudent> .  ?X <http://swat.cse.lehigh.edu/onto/univ-bench.owl#advisor> ?Y . ?Y <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://swat.cse.lehigh.edu/onto/univ-bench.owl#Professor> .}";
//        String queryString4 = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
//                "PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#>\n" +
//                "SELECT ?X\n" +
//                "WHERE {\n" +
//                " <http://www.University0.edu> ub:hasAlumnus ?X .\n" +
//                " ?X rdf:type ub:Person\n" +
//                "}";







//        long startTime=System.currentTimeMillis();
//        //TODO 相对路径
//        String pathABox = "data/testequiv.nt";
//        String pathTBox = "data/univ-bench.owl";
//
//        //数据预处理
//        Input input=new Input();
//        input.readABox(pathABox);
//        //规则预处理
//        input.readTBox(pathTBox);
//        Output output=new Output();
//       // output.writeRuleFile("data/outRule.txt");
//        //单线程推理
//        SerialReason.reason();
//        Map<Integer, RdfDataBean> totalData = RdfDataMap.getDataMap();
//        System.out.println(totalData.size());
//        SerialReason.writeFile("data/jenatestoutdata.nt");
//       // SerialReason.writeFile("data/testequivout.txt");





        // 创建一个线程池对象，控制要创建几个线程对象。
//              ExecutorService pool = Executors.newFixedThreadPool(2);
//              // 可以执行Runnable对象或者Callable对象代表的线程
//              pool.submit(new MyRunnable());
//              pool.submit(new MyRunnable());
//             //结束线程池
//             pool.shutdown();

}
