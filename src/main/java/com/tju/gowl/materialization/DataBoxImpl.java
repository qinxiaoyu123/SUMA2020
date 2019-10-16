package com.tju.gowl.materialization;

import com.tju.gowl.bean.DicRdfDataMap;
import com.tju.gowl.bean.EquivalentPropertyMap;
import com.tju.gowl.bean.InversePropertyMap;
import com.tju.gowl.dictionary.Dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import static com.tju.gowl.axiomProcessor.Processor.classAssertion;

public class DataBoxImpl {
    public static void readABox(String pathABox) {

        Path fpath = Paths.get(pathABox);
        int index = 0;
        try {
            BufferedReader bfr = Files.newBufferedReader(fpath);
            String line;
            while ((line = bfr.readLine()) != null) {
                index = getIndex(index, line);
            }
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("初始数据数目" + index);
        addClassAssertion(index);
    }

    private static int getIndex(int index, String line) {
        //TODO 不识别标志
        if (!line.contains("\\") && !line.contains("unknown:namespace")) {
            StringTokenizer st = new StringTokenizer(line, " ");
            List<String> list = new ArrayList<>(3);
            while (st.hasMoreElements()) {
                list.add(st.nextToken());
            }

//                    List<String> list = Arrays.stream(line.split(" ")).collect(Collectors.toList());
//            DicRdfDataBean rdfDataBean = new DicRdfDataBean();
            String Rs = list.get(0);
            String Rp = list.get(1);
            String Ro = list.get(2);
            int rs = Dictionary.encodeRdf(Rs);
            int rp = Dictionary.encodeRdf(Rp);
            int ro = Dictionary.encodeRdf(Ro);

//                  逆角色，等价角色进行替换
            if (EquivalentPropertyMap.EquivalentPropertyMap.containsKey(rp)) {
                rp = EquivalentPropertyMap.EquivalentPropertyMap.get(rp);
            }
            if (InversePropertyMap.InverseMap.containsKey(rp)) {
                rp = InversePropertyMap.InverseMap.get(rp);
                int tmp = rs;
                rs = ro;
                ro = tmp;
            }
            DicRdfDataMap.addSourceRdfDataBean(index, rs, rp, ro);

            index++;
            if (index % 100000 == 0) {
                System.out.println("finish read " + index + " data");
            }

        } else {
//                    System.out.println(line);
        }
        return index;
    }

    private static void addClassAssertion(int index) {
        int tmpCount = index;
        Iterator<Integer> iter = classAssertion.iterator();
        while (iter.hasNext()) {
            int tmp1 = iter.next();
            int tmp2 = iter.next();
            DicRdfDataMap.addSourceRdfDataBean(tmpCount, tmp1, 0, tmp2);
            tmpCount++;
        }
        System.out.println("count after adding ClassAssertion: " + tmpCount);
    }
}
