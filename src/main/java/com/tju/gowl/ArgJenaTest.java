package com.tju.gowl;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class ArgJenaTest {
    public static void jenaQuery(String dataPath, String queryPath, String answerPath) throws IOException {
        //query

        query.readQuery(queryPath);
        Model model = ModelFactory.createMemModelMaker().createDefaultModel();
        //extended data
        model.read(dataPath);
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

            //输出查询结果
//            System.out.println("遍历结果集依次输出结果：");
            int resultsCount = 0;
            while(results.hasNext()){
                QuerySolution next = results.next();
//                RDFNode resource = next.get("?X");
//                System.out.println(resource.toString());
                resultsCount ++;
            }

            // ResultSetFormatter.out(System.out, results, query);
            qe.close();
            count++;
            System.out.println("q"+" "+count);
            System.out.println("queryTime"+(startTime2-startTime1));
            System.out.println("resultsCount"+resultsCount);

        }

    }


    public static void main(String[] args) throws Exception {
        int index = 0;
        String dataPath = null;
        String queryPath = null;
        String answerPath = null;
        if(args.length>index){
            dataPath = args[index];
            index++;
        }
        if(args.length>index){
            queryPath = args[index];
            index++;
        }
        if(args.length>index){
            answerPath = args[index];
            index++;
        }
        jenaQuery(dataPath, queryPath, answerPath);
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
