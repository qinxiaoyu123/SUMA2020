package com.tju.gowl.io;

import com.tju.gowl.bean.OwlRuleBean;
import com.tju.gowl.bean.OwlRuleMap;
import com.tju.gowl.bean.RdfDataBean;
import com.tju.gowl.bean.RdfDataMap;
import com.tju.gowl.reason.SerialReason;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tju.gowl.bean.OwlRuleMap.setRuleMap;

public class Input {
    public static void readABox(String pathABox){
        Map<Integer, RdfDataBean> totalData= RdfDataMap.getDataMap();
        //Nsp链
        Map<String, Integer> DataIndex= RdfDataMap.getDataIndex();
        Map<String, Integer> DataIndexIpo= RdfDataMap.getDataIndexIpo();
        Path fpath= Paths.get(pathABox);
        int index=0;
        try {
            BufferedReader bfr= Files.newBufferedReader(fpath);
            String line;
            while((line=bfr.readLine())!=null){
                //TODO 不识别标志
                if(!line.contains("\\")&&!line.contains("unknown:namespace")){
                    StringTokenizer st = new StringTokenizer(line, " ");
                    List<String> list=new ArrayList<>();
                    while(st.hasMoreElements()) {
                        list.add(st.nextToken());
                    }
                    RdfDataBean rdfDataBean=new RdfDataBean();

                    setIspNsp(totalData, DataIndex, index, list, rdfDataBean);
                    setIpoNpo(totalData, DataIndexIpo, index, list, rdfDataBean);

                    rdfDataBean.setRs(list.get(0));
                    rdfDataBean.setRp(list.get(1));
                    rdfDataBean.setRo(list.get(2));

                    rdfDataBean.setNp(-1);
                    totalData.put(index,rdfDataBean);
                      //  ConcurrentHashMap<String, Integer> Isp=new ConcurrentHashMap<String, Integer>();
                    index ++;
                }
                else{
                    System.out.println(line);
                }

            }
            System.out.println("index"+index);
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//totalData, stashMap, dataIndexIpo, dataIndexNew, RsNew, rp, head，rdfDataBeanNew
    public static void setIpoNpo1(Map<Integer, RdfDataBean> totalData, Map<Integer, RdfDataBean> stashMapMap, Map<String, Integer> dataIndexIpo, int index, String rs, String rp, String ro, RdfDataBean rdfDataBean) {
        String ipo=rp+ro;
        if(!dataIndexIpo.containsKey(ipo)){
            dataIndexIpo.put(ipo,index);
            rdfDataBean.setNpo(-1);
        }
        else{
            //index_ip 找到第一条sp数据的地址
            int index_ip = dataIndexIpo.get(ipo);
            int count = SerialReason.getpreDataCount();
            int Npo_ip;
            if(index_ip >= count){
                Npo_ip = stashMapMap.get(index_ip).getNpo();
                stashMapMap.get(index_ip).setNpo(index);
            }
            else{
                Npo_ip = totalData.get(index_ip).getNpo();
                totalData.get(index_ip).setNpo(index);

            }
            rdfDataBean.setNpo(Npo_ip);
        }
    }
    public static void setIpoNpo(Map<Integer, RdfDataBean> totalData, Map<String, Integer> dataIndexIpo, int index, List<String> list, RdfDataBean rdfDataBean) {
        String ipo=list.get(1)+list.get(2);
        if(!dataIndexIpo.containsKey(ipo)){
            dataIndexIpo.put(ipo,index);
            rdfDataBean.setNpo(-1);
        }
        else{
            //index_ip 找到第一条sp数据的地址
            int index_ip = dataIndexIpo.get(ipo);
            int Npo_ip = totalData.get(index_ip).getNpo();
            rdfDataBean.setNpo(Npo_ip);
            totalData.get(index_ip).setNpo(index);
        }
    }

    public static void setIspNsp(Map<Integer, RdfDataBean> totalData, Map<String, Integer> dataIndex, int index, List<String> list, RdfDataBean rdfDataBean) {
        String isp=list.get(0)+list.get(1);
        if(!dataIndex.containsKey(isp)){
            dataIndex.put(isp,index);
            rdfDataBean.setNsp(-1);
        }
        else{
            //index_ip 找到第一条sp数据的地址
            int index_ip = dataIndex.get(isp);
            int Nsp_ip = totalData.get(index_ip).getNsp();
            rdfDataBean.setNsp(Nsp_ip);
            totalData.get(index_ip).setNsp(index);
        }
    }

    //TODO 是否可以使用dom树做文件读取处理？
    //TODO 规则存key，为了提高存取效率，是否可以使用encode？
    public void readTBox(String pathTBox) throws OWLOntologyCreationException {
        Map<String, List<OwlRuleBean>> totalRule = OwlRuleMap.getRuleMap();
        Map<String, String> InverseProperty = OwlRuleMap.getInverseProperty();
        Map<String, Integer> TransitiveProperty = OwlRuleMap.getTransitiveProperty();
        File testFile = new File(pathTBox);
        OWLOntologyManager m = OWLManager.createOWLOntologyManager();
        OWLOntology univBench = m.loadOntologyFromOntologyDocument(testFile);
        //System.out.println("loaded ontology: " + univBench);
        int index = 0;
        int countInverseProperty = 0;
        int countTransitiveProperty = 0;
        //对传递属性和逆属性进行筛选
        setInverseTransitiveProperty(InverseProperty, TransitiveProperty, univBench, countInverseProperty, countTransitiveProperty);
        for (OWLAxiom ax : univBench.getAxioms()) {
            index ++;
            //TODO 调用类方法也能改？
            setRuleMap(ax.toString(), ax.typeIndex(), 0);
            //System.out.println(ax.toString()+ax.typeIndex());
        }
        System.out.println("total aximos:"+index);
        //System.out.println(totalRule);

    }
    public static boolean removeAnnotations(OWLOntology inputOntology)
    {
        OWLOntologyManager manager = inputOntology.getOWLOntologyManager();
        boolean updated = false;
        for (OWLAxiom axiom : inputOntology.getAxioms()) {
            if (axiom.toString().contains("Annotation"))
            {
                manager.removeAxiom(inputOntology, axiom);
                updated = true;
            }
        }
        return updated;
    }
    private void setInverseTransitiveProperty(Map<String, String> inverseProperty, Map<String, Integer> transitiveProperty, OWLOntology univBench, int countInverseProperty, int countTransitiveProperty) {
        for (OWLAxiom ax : univBench.getAxioms()) {
            //TODO 调用类方法也能改？
            if(ax.typeIndex() == 2014){//InverseObjectProperties
                Pattern p = Pattern.compile("\\<(.*?)\\>");//正则表达式，取=和|之间的字符串，不包括=和|
                Matcher mm = p.matcher(ax.toString());
                List<String> list=new ArrayList<>();
                while(mm.find()) {
                    list.add(mm.group(0));
                    //   System.out.println(mm.group(ip));//m.group(0)包括这两个字符
                }
                //TODO 目前用第一个替换第二个 更优选择？
                countInverseProperty++;
                inverseProperty.put(list.get(1), list.get(0));
                countInverseProperty++;
                inverseProperty.put(list.get(0), list.get(1));
            }
            else if(ax.typeIndex() == 2019){//TransitiveObjectProperty
                Pattern p = Pattern.compile("\\<(.*?)\\>");//正则表达式，取=和|之间的字符串，不包括=和|
                Matcher mm = p.matcher(ax.toString());
                List<String> list=new ArrayList<>();
                while(mm.find()) {
                    list.add(mm.group(0));
                }
                if(list.size()==1){
                    countTransitiveProperty ++;
                    transitiveProperty.put(list.get(0),0);
                }
                else{
                    System.out.println("未处理TransitiveProperty"+ax.toString());
                }
            }
        }
        System.out.println("countInverseProperty");
        System.out.println(countInverseProperty);
        System.out.println("countTransitiveProperty");
        System.out.println(countTransitiveProperty);
    }

    public static void main(String[] args) {
        readABox("F:\\代码\\generate_data\\pagoda标准数据\\lubm\\data\\lubm1000.ttl");
//        String line="rdf:about = <\"http\">";
//        StringTokenizer st = new StringTokenizer(line, "\"");
//        while(st.hasMoreElements()){
//            System.out.println(st.nextToken());
//        }
    }
}
