package com.tju.suma.concurrent;

import java.io.*;
import java.util.List;

public class ReadBigFile {

    public static void main(String[] args) throws Exception {
        String fileName = "D:\\test.txt";
        long value = 0L;
        BufferedWriter bw  = new BufferedWriter(new FileWriter(fileName));
        for (int i = 0; i < 640; i++) {
            bw.write(String.valueOf(value));
            bw.newLine();
            value++;
        }
        bw.close();

        long startTime1 = System.currentTimeMillis();
        int i=0;
        BufferedReader br = new BufferedReader(new FileReader(fileName));
//        Set<String> set = new HashSet<>(200_000_000);
        String line;
        while ((line = br.readLine()) != null) {
//            set.add(line);
            System.out.print(line+" ");

        }
        br.close();
        System.out.println(String.format("BufferedReader read file cost time : %s", System.currentTimeMillis() - startTime1));

        File file = new File(fileName);
        List<IndexPair> indexPairs = IndexCalculate.getIndex(file, 64);
//        System.out.println(String.format("file index size : %s, result: %s", indexPairs.size(), indexPairs));
        long startTime2 = System.currentTimeMillis();
//        final AtomicInteger count = new AtomicInteger();
        ConcurrentReadFile concurrentReadFile = new ConcurrentReadFile(fileName, indexPairs, new FileHandle() {
            @Override
            public void handle(String value) {

                System.out.print(value+" ");
//                count.incrementAndGet();
//                if (!set.contains(value)) {
//                    System.out.println("ConcurrentReadFile read file exception:" + value);
//                }
            }
        });
        concurrentReadFile.readFile();
        concurrentReadFile.end();
        System.out.println(String.format("ConcurrentReadFile read file cost time : %s", System.currentTimeMillis() - startTime2));

    }

}
