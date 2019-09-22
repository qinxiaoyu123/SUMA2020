package com.tju.gowl.reason;

import com.tju.gowl.bean.OwlRuleBean;
import com.tju.gowl.bean.OwlRuleMap;
import com.tju.gowl.bean.RdfDataBean;
import com.tju.gowl.bean.RdfDataMap;
import com.tju.gowl.io.Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SerialReason {
    final static Logger logger = LoggerFactory.getLogger(SerialReason.class);

    public static final String TYPE = "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>";
    //private static List<OwlRuleBean> OwlRule;
    private static int preDataCount = 0;

    public SerialReason(int preDataCount) {

    }
    public static int getpreDataCount(){ return preDataCount; }

    public static void reason() throws Exception {
        //TODO加停止循环条件
        int loopCount = 1;
        Map<Integer, RdfDataBean> totalData = RdfDataMap.getDataMap();
        //存放每次新生成的数据
        Map<Integer,RdfDataBean> stashMap = RdfDataMap.getstashMap();
        //迭代数据，第二轮以后迭代 iteratorMap
        Map<Integer,RdfDataBean> iteratorMap = RdfDataMap.getiteratorMap();
        //规则
        Map<String, List<OwlRuleBean>> totalRule = OwlRuleMap.getRuleMap();
        //sp链表
        Map<String, Integer> DataIndex = RdfDataMap.getDataIndex();
        //po链表
        Map<String, Integer> DataIndexIpo = RdfDataMap.getDataIndexIpo();

        while(true){

            preDataCount = totalData.size();
            System.out.println("loopCount"+loopCount+"preDataCount"+preDataCount);
            Iterator<Map.Entry<Integer, RdfDataBean>> entries;
            if(loopCount ==1) {
               entries = totalData.entrySet().iterator();
            }
            else{
               entries = iteratorMap.entrySet().iterator();
            }
            try {
                while (entries.hasNext()) {
                    Map.Entry<Integer, RdfDataBean> entry = entries.next();
                    RdfDataBean rdfData = entry.getValue();
                    //TODO
//                    System.out.println(rdfData+"rdfData");

                    List<String> ruleKey = new ArrayList<>();
                    //ruleKey.add(rdfData.getRs()+rdfData.getRp()+rdfData.getRo());
                    String Rs = rdfData.getRs();
                    String RpLong = rdfData.getRp();
                    String Rp;
                    if (rdfData.getRp().contains("type")) {
                        Rp = "type";
                    } else {
                        Rp = rdfData.getRp();
                    }
                    String Ro = rdfData.getRo();
                    //System.out.println("原始数据"+rdfData);
                    //都有的都没的没写
                    //子类
                    ruleKey.add("*" + Rp + Ro);
                    ruleKey.add("*" + "*" + Ro);
                    //子属性
                    ruleKey.add("*" + Rp + "*");
                    ruleKey.add(Rs + "*" + Ro);
                    ruleKey.add(Rs + Rp + "*");
                    ruleKey.add(Rs + "*" + "*");
                    ruleKey.forEach(str -> {
                        try {
                            if (totalRule.containsKey(str)) {
                                List<OwlRuleBean> OwlRule = totalRule.get(str);
                                //TODO
//                                System.out.println("rulekey"+str);
//                                System.out.println("rulehead"+OwlRule);
                                for (OwlRuleBean typeHead : OwlRule) {
                                    int type = typeHead.getType();
                                    String head = typeHead.getRuleHead();
                                    //TODO
//                                    System.out.println("type"+type);
//                                    System.out.println("head"+head);
                             /*   ObjectPropertyDomain 2022
                                ObjectPropertyRange 2023
                                ObjectSomeValuesFrom 0
                                SubClassOf 2002
                                SubObjectPropertyOf 2013*/
                                    if (type == 2013) {//SubObjectPropertyOf 2013
                                        subPropertyReason(totalData, iteratorMap, stashMap, DataIndex, DataIndexIpo, Rs, Ro, head);
                                    } else if (type == 2022 || type == 2002 || type == 2023) {

                                        //System.out.println("2022");

                                        subClassObjectPropertyDomainReason(totalData, iteratorMap, stashMap, DataIndex, DataIndexIpo, Rs, Ro, type, head);
                                    } else if (type == 0) {
                                        someValuePropertyReason(totalData, iteratorMap, stashMap, DataIndex, DataIndexIpo, Rs, Ro, head);
                                    } else if (type == 2019){//subOrganizationOf
                                        transitivePropertyReason(totalData, iteratorMap, stashMap, DataIndex, DataIndexIpo, Rs, Ro, head);
                                    }
                                    else if (type == 21){//C2   C1 = C2 and #R.C3
                                        C2ClassReason(totalData, iteratorMap, stashMap, DataIndex, DataIndexIpo, Rs, Ro, head);
                                    }
                                    else if (type == 22){//R
                                        RPropertyReason(totalData, iteratorMap, stashMap, DataIndex, DataIndexIpo, Rs, Ro, head);
                                    }
                                    else if (type == 23){//C3
                                        C3ClassReason(totalData, iteratorMap, stashMap, DataIndex, DataIndexIpo, Rs, Ro, head);
                                    }
                                }
                            }
                            else {
                                //无对应规则
                            }
                        }catch (IOException e){
                            System.err.println("myException "+ e);
                        }

                    });


                    //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                }
                //System.out.println(totalData.size());
                //System.out.println(totalData);

            } catch (Exception e) {
                e .printStackTrace();
                System.err.println("happen error");
            }
            if(stashMap.size()==0||loopCount>=7){
                //没有新数据产生
                System.out.println("没有新数据产生");
                break;
            }
            iteratorMap.clear();
            addRdfdata(totalData,stashMap);
            addRdfdata(iteratorMap,stashMap);
            stashMap.clear();

            loopCount++;
        }







    }

    private static void addRdfdata(Map<Integer, RdfDataBean> totalData, Map<Integer, RdfDataBean> stashMap) {
        totalData.putAll(stashMap);
    }

    private static void C3ClassReason(Map<Integer, RdfDataBean> totalData,  Map<Integer, RdfDataBean> iteratorMap, Map<Integer, RdfDataBean> stashMap, Map<String, Integer> dataIndex, Map<String, Integer> dataIndexIpo, String rs, String ro, String head) {
        Pattern p = Pattern.compile("\\<(.*?)\\>");//正则表达式，取=和|之间的字符串，不包括=和|
        //head : list.get(0)+list.get(1)+list.get(2)+list.get(3);
//        Map<Integer,RdfDataBean> stashMap = RdfDataMap.getstashMap();
        Matcher mm = p.matcher(head);
        List<String> list=new ArrayList<>();
        while(mm.find()) {
            list.add(mm.group(0));
        }
        String c1 = list.get(0);
        String c2 = list.get(1);
        String r = list.get(2);
        String c3 = list.get(3);
        if(list.size()==4) {
            String Npo = r + rs;
            boolean flagC2 = false;
            if (dataIndexIpo.containsKey(Npo)) {
                int totalIndex = dataIndexIpo.get(Npo);
                //在大表中的位置
                RdfDataBean dataBean ;
//                System.out.println("totalIndex"+totalIndex);
//                System.out.println("preDataCount"+preDataCount);
//                System.out.println("totalData"+totalData.size());
//                System.out.println("stashMap"+stashMap.size());
//                System.out.println("stashMap"+stashMap);
                if(totalIndex>=preDataCount){
                    dataBean = stashMap.get(totalIndex);
                }
                else{
                    dataBean = totalData.get(totalIndex);
                }
                int flag_lastone = 0;
                while (true) {
                    if(dataBean.getNpo() == -1){
                        flag_lastone = 1;
                    }
                    // c type C2   c R *   * type C3

                    String cc = dataBean.getRs();
                    if(dataIndex.containsKey(cc + TYPE)) {
                        //已经存在
                        RdfDataBean dataBeanC1 = findRdfData(totalData, stashMap, dataIndex, cc, c1, TYPE);
                        if(dataBeanC1 != null){
                            if (findRdfData(totalData, stashMap, dataIndex, cc, c2, TYPE) == null) {
                                //找到* type C2
                                //Map<Integer,RdfDataBean> stashMap = RdfDataMap.getstashMap();
                                //判断是否已经存在 cc type C1
                                //RdfDataBean dataBeanC1 = new RdfDataBean();
                                //dataBeanC1 = findRdfData(totalData, stashMap, dataIndex, cc, c1, TYPE);
                                //if (dataBeanC1 == null) return;//已经存在cc type C1
                                //else {//不存在cc type c1 插入
                                RdfDataBean rdfDataBean = new RdfDataBean();
                                int index = totalData.size() + stashMap.size();
                                dataBeanC1.setNsp(index);
                                Input.setIpoNpo1(totalData, stashMap, dataIndexIpo, index, rs, TYPE, c1, rdfDataBean);

                                rdfDataBean.setRs(cc);
                                rdfDataBean.setRp(TYPE);
                                rdfDataBean.setRo(c1);

                                rdfDataBean.setNsp(-1);
                                rdfDataBean.setNp(-1);
//                                totalData.put(index,rdfDataBean);
                                stashMap.put(index, rdfDataBean);
//                                System.out.println("新添加" + rdfDataBean);
                                //}

                                return;
                            }
                        }

                    }
                    if( flag_lastone > 0){
                        break;
                    }

                    totalIndex = dataBean.getNpo();
                    if(totalIndex>=preDataCount){
                        dataBean = stashMap.get(totalIndex);
                    }
                    else{
                        dataBean = totalData.get(totalIndex);
                    }
                }
            }
            else{
                return;
            }
        }
    }

    private static void RPropertyReason(Map<Integer, RdfDataBean> totalData,  Map<Integer, RdfDataBean> iteratorMap, Map<Integer, RdfDataBean> stashMap, Map<String, Integer> dataIndex, Map<String, Integer> dataIndexIpo, String rs, String ro, String head) {
        Pattern p = Pattern.compile("\\<(.*?)\\>");//正则表达式，取=和|之间的字符串，不包括=和|
        //head : list.get(0)+list.get(1)+list.get(2)+list.get(3);
        Matcher mm = p.matcher(head);
        List<String> list=new ArrayList<>();
        while(mm.find()) {
            list.add(mm.group(0));
        }
        String c1 = list.get(0);
        String c2 = list.get(1);
        String r = list.get(2);
        String c3 = list.get(3);
//        Map<Integer,RdfDataBean> stashMap = RdfDataMap.getstashMap();
        if(list.size()==4) {
            RdfDataBean dataBeanC1 = new RdfDataBean();
            if (dataIndex.containsKey(rs + TYPE)) {
                dataBeanC1 = findRdfData(totalData, stashMap, dataIndex, rs, c1, TYPE);
                if (dataBeanC1 == null) return;//已经存在，无需判断
            } else {
                //不包含，
            }
            String Nsp1 = rs + TYPE;
            boolean flagC2 = false;
            if (dataIndex.containsKey(Nsp1)) {
                if (findRdfData(totalData, stashMap,dataIndex, rs, c2, TYPE) == null) {
                    flagC2 = true;
                }
            }
            String Nsp2 = ro + TYPE;
            boolean flagC3 = false;
            if (dataIndex.containsKey(Nsp2)) {
                if (findRdfData(totalData, stashMap, dataIndex, ro, c3, TYPE) == null) {
                    flagC3 = true;
                }
            }
            if (flagC2 && flagC3) {
                //

                int dataIndexNew = totalData.size()+stashMap.size();;
                RdfDataBean rdfDataBeanNew = new RdfDataBean();
                if (!dataIndex.containsKey(rs + TYPE)) {
                    //不包含
                    dataIndex.put(rs + TYPE, dataIndexNew);
                } else {
                    dataBeanC1.setNsp(dataIndexNew);

                }

 //               Input.setIpoNpo(totalData, dataIndexIpo, dataIndexNew,  lists, rdfDataBeanNew);
                Input.setIpoNpo1(totalData, stashMap, dataIndexIpo, dataIndexNew, rs, TYPE, c1, rdfDataBeanNew);

                rdfDataBeanNew.setRs(rs);
                rdfDataBeanNew.setRp(TYPE);
                rdfDataBeanNew.setRo(c1);
                rdfDataBeanNew.setNsp(-1);
                rdfDataBeanNew.setNp(-1);

//                totalData.put(dataIndexNew, rdfDataBeanNew);
                stashMap.put(dataIndexNew, rdfDataBeanNew);
//                System.out.println("新添加"+rdfDataBeanNew);
                return;
            }
        }


    }

    private static void C2ClassReason(Map<Integer, RdfDataBean> totalData,  Map<Integer, RdfDataBean> iteratorMap, Map<Integer, RdfDataBean> stashMap, Map<String, Integer> dataIndex, Map<String, Integer> dataIndexIpo, String rs, String ro, String head){
        Pattern p = Pattern.compile("\\<(.*?)\\>");//正则表达式，取=和|之间的字符串，不包括=和|
        //head : list.get(0)+list.get(1)+list.get(2)+list.get(3);
        Matcher mm = p.matcher(head);
        List<String> list=new ArrayList<>();
        while(mm.find()) {
            list.add(mm.group(0));
        }
        String c1 = list.get(0);
        String c2 = list.get(1);
        String r = list.get(2);
        String c3 = list.get(3);
        //Map stashMap = RdfDataMap.getstashMap();
        if(list.size()==4) {
            RdfDataBean dataBeanC1 = new RdfDataBean();
            if(dataIndex.containsKey(rs+TYPE)){
                dataBeanC1 = findRdfData(totalData, stashMap, dataIndex, rs, c1, TYPE);
                if(dataBeanC1 == null) return;//已经存在，无需判断
            }
            else{
                //不包含，
            }
            String Nsp1 = rs + r;

           // Map<Integer,RdfDataBean> stashMap = RdfDataMap.getstashMap();
            if (dataIndex.containsKey(Nsp1)) {

                int totalIndex = dataIndex.get(Nsp1);
                //在大表中的位置
                RdfDataBean dataBean;
                if(totalIndex>=preDataCount){
                    dataBean = stashMap.get(totalIndex);
                }
                else{
                    dataBean = totalData.get(totalIndex);
                }
                //RdfDataBean dataBean = totalData.get(totalIndex);
                int flag_lastone = 0;
                while (true) {
                    if(dataBean.getNsp() == -1){
                        flag_lastone = 1;
                    }
                    // c type C2   c R *   * type C3

                    String cc = dataBean.getRo();
                    if(dataIndex.containsKey(cc+TYPE)){
                        if(findRdfData(totalData, stashMap , dataIndex, cc, c3, TYPE)==null){
                            //找到* type C3
                            //Map<Integer,RdfDataBean> stashMap = RdfDataMap.getstashMap();
                            int dataIndexNew = totalData.size()+stashMap.size();
                            RdfDataBean rdfDataBeanNew = new RdfDataBean();
                            if(!dataIndex.containsKey(rs+TYPE)){
                                //不包含
                                dataIndex.put(rs+TYPE,dataIndexNew);
                            }
                            else{
                                dataBeanC1.setNsp(dataIndexNew);

                            }

                            Input.setIpoNpo1(totalData, stashMap, dataIndexIpo, dataIndexNew, rs, TYPE, c1, rdfDataBeanNew);
                            rdfDataBeanNew.setRs(rs);
                            rdfDataBeanNew.setRp(TYPE);
                            rdfDataBeanNew.setRo(c1);
                            rdfDataBeanNew.setNsp(-1);
                            rdfDataBeanNew.setNp(-1);

//                            totalData.put(dataIndexNew, rdfDataBeanNew);
                            stashMap.put(dataIndexNew, rdfDataBeanNew);
//                            System.out.println("新添加"+rdfDataBeanNew);
                            return;
                        }
                    }

                    if( flag_lastone > 0){
                        break;
                    }

                    totalIndex = dataBean.getNsp();
                    if(totalIndex>=preDataCount){
                        dataBean = stashMap.get(totalIndex);
                    }
                    else{
                        dataBean = totalData.get(totalIndex);
                    }
                }
            } else {
                //C1 = C2 and #R.C3
                // c type C2 but not c R *

            }

        }
        else{
//            System.out.println("error query"+head);
        }
    }

    private static void transitivePropertyReason(Map<Integer, RdfDataBean> totalData,  Map<Integer, RdfDataBean> iteratorMap, Map<Integer, RdfDataBean> stashMap, Map<String, Integer> dataIndex, Map<String, Integer> dataIndexIpo, String rs, String ro, String head) {
        String Nsp = ro + head ;
        if (dataIndex.containsKey(Nsp)) {
            int totalIndex = dataIndex.get(Nsp);
            //在大表中的位置
            RdfDataBean dataBean;
            if(totalIndex>=preDataCount){
                dataBean = stashMap.get(totalIndex);
            }
            else{
                dataBean = totalData.get(totalIndex);
            }
            int flag_lastone = 0;
            while (true) {

                if(dataBean.getNsp() == -1){
                    flag_lastone = 1;
                }
                //spo op* 写入sp*
                //Map<Integer,RdfDataBean> stashMap = RdfDataMap.getstashMap();
                RdfDataBean rdfDataBeanNew = new RdfDataBean();
                int dataIndexNew = totalData.size()+stashMap.size();
                if(!dataIndex.containsKey(rs+head)){
//                    System.out.println("不可能");
                }
                else{
                    //index_ip 找到第一条sp数据的地址
                    String roNew = dataBean.getRo();
                    if(findRdfData(totalData, stashMap, dataIndex, rs, roNew, head)==null){
                        //已经存在，不写入
                    }
                    else{
                        findRdfData(totalData, stashMap, dataIndex, rs, roNew, head).setNsp(dataIndexNew);
                        Input.setIpoNpo1(totalData, stashMap, dataIndexIpo, dataIndexNew, rs, head, roNew, rdfDataBeanNew);
                        rdfDataBeanNew.setRs(rs);
                        rdfDataBeanNew.setRp(head);
                        rdfDataBeanNew.setRo(roNew);
                        rdfDataBeanNew.setNsp(-1);
                        rdfDataBeanNew.setNp(-1);
                       // rdfDataBeanNew.setNop(-1);
//                        totalData.put(dataIndexNew, rdfDataBeanNew);
                        stashMap.put(dataIndexNew, rdfDataBeanNew);
//                        System.out.println("新添加"+rdfDataBeanNew);
                    }

                }
                if( flag_lastone > 0){
                    break;
                }
                totalIndex = dataBean.getNsp();
                if(totalIndex>=preDataCount){
                    dataBean = stashMap.get(totalIndex);
                }
                else{
                    dataBean = totalData.get(totalIndex);
                }
            }
        } else {
            //spo op不存在

        }
    }

    private static RdfDataBean findRdfData(Map<Integer, RdfDataBean> totalData, Map<Integer, RdfDataBean> stashMap, Map<String, Integer> dataIndex, String rs, String ro, String rp) {

        int totalIndex = dataIndex.get(rs+rp);
        RdfDataBean dataBean;
        if(totalIndex>=preDataCount){
            dataBean = stashMap.get(totalIndex);
        }
        else{
            dataBean = totalData.get(totalIndex);
        }
        //RdfDataBean dataBean = totalData.get(totalIndex);
        int flag_lastone = 0;
        while (true) {
            if(dataBean.getNsp() == -1){
                flag_lastone = 1;
            }
            if (dataBean.getRo().equals(ro)) {
                //已经存在不插入
                return null;
            }
            if( flag_lastone > 0){
                break;
            }
            totalIndex = dataBean.getNsp();
            if(totalIndex>=preDataCount){
                dataBean = stashMap.get(totalIndex);
            }
            else{
                dataBean = totalData.get(totalIndex);
            }

        }
        return dataBean;



    }

    private static void someValuePropertyReason(Map<Integer, RdfDataBean> totalData,  Map<Integer, RdfDataBean> iteratorMap, Map<Integer, RdfDataBean> stashMap, Map<String, Integer> dataIndex, Map<String, Integer> dataIndexIpo, String rs, String ro, String head) {
        StringTokenizer st = new StringTokenizer(head, " ");
        List<String> list1=new ArrayList<>();//R C2
        while(st.hasMoreElements()) {
            list1.add(st.nextToken());
        }
        String Nsp = rs + list1.get(0);// rs R
        if (dataIndex.containsKey(Nsp)) {
            int totalIndex = dataIndex.get(Nsp);
            RdfDataBean dataBean;
            if(totalIndex>=preDataCount){
                dataBean = stashMap.get(totalIndex);
            }
            else{
                dataBean = totalData.get(totalIndex);
            }

            int flag = 1;
            int flag_lastone = 0;
            while (true) {

                if(dataBean.getNsp() == -1){
                    flag_lastone = 1;
                }
                ro = dataBean.getRo();
                if(dataIndex.containsKey(ro+TYPE)){
                    if (findRdfData(totalData, stashMap, dataIndex, ro, list1.get(1), TYPE)==null) {
                        //如果返回真，就是找到了，不用插入
                        flag = -1;
                        return;
                        //break;
                    }
                }

                if( flag_lastone > 0){
                    break;
                }
                totalIndex = dataBean.getNsp();
                if(totalIndex>=preDataCount){
                    dataBean = stashMap.get(totalIndex);
                }
                else{
                    dataBean = totalData.get(totalIndex);
                }

            }
            //没找到，插入新数据
            if (flag == 1) {
                //TODO 多线程获取totalData.size()会产生冲突？
                //Map<Integer,RdfDataBean> stashMap = RdfDataMap.getstashMap();
                StringTokenizer st1 = new StringTokenizer(list1.get(0), "#");
                List<String> list2=new ArrayList<>();
                while(st1.hasMoreElements()) {
                    list2.add(st1.nextToken());
                }
                String RoNew = rs.substring(0, rs.length()-1)+list2.get(1);
                //String RoNew = rs.substring(0,-1)+list2.get(1);
                int dataIndexNew = totalData.size()+stashMap.size();
                dataBean.setNsp(dataIndexNew);
                RdfDataBean rdfDataBeanNew1 = new RdfDataBean();
                rdfDataBeanNew1.setRs(rs);
                rdfDataBeanNew1.setRp(list1.get(0));
                rdfDataBeanNew1.setRo(RoNew);
                rdfDataBeanNew1.setNsp(-1);
                rdfDataBeanNew1.setNp(-1);
                rdfDataBeanNew1.setNpo(-1);
                //dataIndexIpo.put(list1.get(0)+rs+list1.get(0)+"<nnnnnnnnnnn>",dataIndexNew);
//                totalData.put(dataIndexNew, rdfDataBeanNew1);
                stashMap.put(dataIndexNew, rdfDataBeanNew1);
//                System.out.println("新添加"+rdfDataBeanNew1);

                int dataIndexNew1 = dataIndexNew + 1;
                dataIndex.put(RoNew+TYPE,dataIndexNew1);
                RdfDataBean rdfDataBeanNew2 = new RdfDataBean();
                rdfDataBeanNew2.setRs(RoNew);
                rdfDataBeanNew2.setRp(TYPE);
                rdfDataBeanNew2.setRo(list1.get(1));
                rdfDataBeanNew2.setNsp(-1);
                rdfDataBeanNew2.setNp(-1);
                rdfDataBeanNew2.setNpo(-1);
//                totalData.put(dataIndexNew1, rdfDataBeanNew2);
                stashMap.put(dataIndexNew1, rdfDataBeanNew2);
//                System.out.println("新添加"+rdfDataBeanNew2);

            }
        }
        else{
            //写入，并更新DataIndex
            //Map<Integer,RdfDataBean> stashMap = RdfDataMap.getstashMap();
            int dataIndexNew = totalData.size()+stashMap.size();
            dataIndex.put(Nsp,dataIndexNew);
            RdfDataBean rdfDataBeanNew1 = new RdfDataBean();

            StringTokenizer st1 = new StringTokenizer(list1.get(0), "#");
            List<String> list2=new ArrayList<>();
            while(st1.hasMoreElements()) {
                list2.add(st1.nextToken());
            }
            String RoNew = rs.substring(0, rs.length()-1)+list2.get(1);

            rdfDataBeanNew1.setRs(rs);
            rdfDataBeanNew1.setRp(list1.get(0));
            rdfDataBeanNew1.setRo(RoNew);
            rdfDataBeanNew1.setNsp(-1);
            rdfDataBeanNew1.setNp(-1);
            rdfDataBeanNew1.setNpo(-1);
//            totalData.put(dataIndexNew, rdfDataBeanNew1);
            stashMap.put(dataIndexNew, rdfDataBeanNew1);
//            System.out.println(dataIndexNew+"新添加"+rdfDataBeanNew1);

            int dataIndexNew1 = dataIndexNew + 1;
            dataIndex.put(RoNew+TYPE,dataIndexNew1);
            RdfDataBean rdfDataBeanNew2 = new RdfDataBean();
            rdfDataBeanNew2.setRs(RoNew);
            rdfDataBeanNew2.setRp(TYPE);
            rdfDataBeanNew2.setRo(list1.get(1));
            rdfDataBeanNew2.setNsp(-1);
            rdfDataBeanNew2.setNp(-1);
            rdfDataBeanNew2.setNpo(-1);
//            totalData.put(dataIndexNew1, rdfDataBeanNew2);
            stashMap.put(dataIndexNew1, rdfDataBeanNew2);
//            System.out.println(dataIndexNew1+"新添加"+rdfDataBeanNew2);
            // System.out.println("新添加"+rdfDataBeanNew);
        }
    }


    private static void subPropertyReason(Map<Integer, RdfDataBean> totalData,  Map<Integer, RdfDataBean> iteratorMap, Map<Integer, RdfDataBean> stashMap, Map<String, Integer> dataIndex, Map<String, Integer> dataIndexIpo, String rs, String ro, String head) {
        String Nsp = rs + head;
        if (dataIndex.containsKey(Nsp)) {
            int totalIndex = dataIndex.get(Nsp);
            //在大表中的位置
            RdfDataBean dataBean;
            if(totalIndex>=preDataCount){
                dataBean = stashMap.get(totalIndex);
            }
            else{
                dataBean = totalData.get(totalIndex);
            }
            //RdfDataBean dataBean = totalData.get(totalIndex);
            int flag = 1;
            int flag_lastone = 0;
            while (true) {

                if(dataBean.getNsp() == -1){
                    flag_lastone = 1;
                }
                if (dataBean.getRo().equals(ro)) {
//                                             System.out.println("要插入类"+head);
//                                             System.out.println("已存在，不插入 " + dataBean);
//                      System.out.println("已存在，不插入 " + ruleBean.getRo());
                    flag = -1;
                    break;
                }
                if( flag_lastone > 0){
                    break;
                }
                totalIndex = dataBean.getNsp();
                if(totalIndex>=preDataCount){
                    dataBean = stashMap.get(totalIndex);
                }
                else{
                    dataBean = totalData.get(totalIndex);
                }

            }
            //没找到，插入新数据
            if (flag == 1) {
                //TODO 多线程获取totalData.size()会产生冲突？
                //Map<Integer,RdfDataBean> stashMap = RdfDataMap.getstashMap();
                int dataIndexNew = totalData.size()+stashMap.size();
                dataBean.setNsp(dataIndexNew);
                RdfDataBean rdfDataBeanNew = new RdfDataBean();
                Input.setIpoNpo1(totalData, stashMap, dataIndexIpo, dataIndexNew, rs, head, ro, rdfDataBeanNew);

                rdfDataBeanNew.setRs(rs);
                rdfDataBeanNew.setRp(head);
                rdfDataBeanNew.setRo(ro);
                rdfDataBeanNew.setNsp(-1);
                rdfDataBeanNew.setNp(-1);

//                totalData.put(dataIndexNew, rdfDataBeanNew);
                stashMap.put(dataIndexNew, rdfDataBeanNew);
//                System.out.println("新添加"+rdfDataBeanNew);
                //System.out.println("新添加"+rdfDataBeanNew);

            }
        } else {
            //写入，并更新DataIndex
            int dataIndexNew = totalData.size()+stashMap.size();
//            System.out.println("totalData.size()"+totalData.size());
//            System.out.println("stashMap.size()"+stashMap.size());
            dataIndex.put(Nsp,dataIndexNew);
            RdfDataBean rdfDataBeanNew = new RdfDataBean();
            Input.setIpoNpo1(totalData, stashMap, dataIndexIpo, dataIndexNew, rs, head, ro, rdfDataBeanNew);
            rdfDataBeanNew.setRs(rs);
            rdfDataBeanNew.setRp(head);
            rdfDataBeanNew.setRo(ro);
            rdfDataBeanNew.setNsp(-1);
            rdfDataBeanNew.setNp(-1);
//            totalData.put(dataIndexNew, rdfDataBeanNew);
//            System.out.println("totalData"+totalData);
            stashMap.put(dataIndexNew, rdfDataBeanNew);
//            System.out.println("新添加"+rdfDataBeanNew);

        }
    }

    private static void subClassObjectPropertyDomainReason(Map<Integer, RdfDataBean> totalData, Map<Integer, RdfDataBean> iteratorMap, Map<Integer, RdfDataBean> stashMap, Map<String, Integer> dataIndex, Map<String, Integer> dataIndexIpo, String rs, String ro, int type, String head) throws  IOException {
        if (type == 2022||type == 2002||type == 2023) {//ObjectPropertyDomain 2022   SubClassOf 2002 ObjectPropertyRange 2023
            String RsNew;
            if(type == 2023){
                RsNew = ro ;
            }
            else{
                RsNew = rs ;
            }
            String Nsp = RsNew + TYPE;

            if (dataIndex.containsKey(Nsp)) {
                //System.out.println(11111);
                int totalIndex = dataIndex.get(Nsp);
                //在大表中的位置
                RdfDataBean dataBean = new RdfDataBean();
                if(totalIndex>=preDataCount){
                    dataBean = stashMap.get(totalIndex);
                }
                else{
                    dataBean = totalData.get(totalIndex);
                }
                //System.out.println(66666);
                int flag = 1;
                int flag_lastone = 0;
                while (true) {
                    //System.out.println(777777);
                    if(dataBean.getNsp() == -1){
                        flag_lastone = 1;
                    }
                    if (dataBean.getRo().equals(head)) {
//                        System.out.println("要插入类"+head);
//                        System.out.println("已存在，不插入 " + dataBean);
//                      System.out.println("已存在，不插入 " + ruleBean.getRo());
                        flag = -1;
                        break;
                    }
                    if( flag_lastone > 0){
                        break;
                    }
                    if(dataBean.getNsp()>=preDataCount){
                        dataBean = stashMap.get(dataBean.getNsp());
                    }
                    else{
                        dataBean = totalData.get(dataBean.getNsp());
                    }

                }
                //没找到，插入新数据
                if (flag == 1) {
                    //TODO 多线程获取totalData.size()会产生冲突？
                    //System.out.println(888888);
                    int dataIndexNew = totalData.size()+stashMap.size();
                    dataBean.setNsp(dataIndexNew);
                    RdfDataBean rdfDataBeanNew = new RdfDataBean();
                    rdfDataBeanNew.setRs(RsNew);
                    rdfDataBeanNew.setRp(TYPE);
                    rdfDataBeanNew.setRo(head);
                    rdfDataBeanNew.setNsp(-1);
                    rdfDataBeanNew.setNp(-1);

                    Input.setIpoNpo1(totalData, stashMap, dataIndexIpo, dataIndexNew, RsNew, TYPE, head, rdfDataBeanNew);
//                    totalData.put(dataIndexNew, rdfDataBeanNew);
                    stashMap.put(dataIndexNew, rdfDataBeanNew);
//                    System.out.println("新添加"+rdfDataBeanNew);

                }
            } else {
                //写入，并更新DataIndex
                int dataIndexNew = totalData.size()+stashMap.size();
                dataIndex.put(Nsp,dataIndexNew);
                RdfDataBean rdfDataBeanNew = new RdfDataBean();
                rdfDataBeanNew.setRs(RsNew);
                rdfDataBeanNew.setRp(TYPE);
                rdfDataBeanNew.setRo(head);
                rdfDataBeanNew.setNsp(-1);
                rdfDataBeanNew.setNp(-1);
                Input.setIpoNpo1(totalData, stashMap, dataIndexIpo, dataIndexNew, RsNew, TYPE, head, rdfDataBeanNew);
//                totalData.put(dataIndexNew, rdfDataBeanNew);
                stashMap.put(dataIndexNew, rdfDataBeanNew);
//                System.out.println("新添加"+rdfDataBeanNew);
            }

        }
    }

    public static void writeFile(String path) throws IOException {
        //临时存储要写入数据，数据到1000条再写入，减少io代价，同时不占用太大内存
        Map<Integer,RdfDataBean> totalRdfData = RdfDataMap.getDataMap();
        //TODO 不大于1000就不写，最后不足1000需要处理
        if(totalRdfData.size()>=0){
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path),"GBK"));
            out.write("<unknown:namespace> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2002/07/owl#Ontology> .");
            out.newLine();
            out.write("<unknown:namespace> <http://www.w3.org/2002/07/owl#imports> <http://swat.cse.lehigh.edu/onto/univ-bench.owl> .");
            out.newLine();
            for(Map.Entry<Integer, RdfDataBean> entry : totalRdfData.entrySet()){
                //out.write(entry.getKey().toString());//写入文件
                out.write(entry.getValue().getRs()+" "+entry.getValue().getRp()+" "+entry.getValue().getRo()+"  .");
                out.newLine();
            }
            out.flush();
            out.close();
           // totalRdfData.clear();
        }

    }

}
