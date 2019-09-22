package com.tju.gowl;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.tju.gowl.bean.RdfDataBean;
import com.tju.gowl.bean.RdfDataMap;
import com.tju.gowl.io.Input;
import com.tju.gowl.io.Output;
import com.tju.gowl.reason.SerialReason;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestRun {
    public static void main(String[] args) throws Exception {

    //TODO 相对路径
//    String pathABox = "F:\\代码\\generate_data\\pagoda标准数据\\lubm\\data\\lubm100.nt";
    String pathABox = "data/1.nt";
    String pathTBox = "data/pellet.owl";

        //数据预处理
        Input input=new Input();
        input.readABox(pathABox);
        //规则预处理
        input.readTBox(pathTBox);
        Output output=new Output();
        //output.writeRuleFile("data/outRule.txt");
        //单线程推理
        long startTime=System.currentTimeMillis();
        SerialReason.reason();
        //SerialReason.writeFile("data/out.txt");
        long startTime1=System.currentTimeMillis();
        System.out.println("reason time"+(startTime1-startTime));
        Map<Integer, RdfDataBean> totalData = RdfDataMap.getDataMap();
        System.out.println(totalData.size());
     //   SerialReason.writeFile("data/outdata.nt");



        // 创建一个线程池对象，控制要创建几个线程对象。
//              ExecutorService pool = Executors.newFixedThreadPool(2);
//              // 可以执行Runnable对象或者Callable对象代表的线程
//              pool.submit(new MyRunnable());
//              pool.submit(new MyRunnable());
//             //结束线程池
//             pool.shutdown();
    }
}
