package com.tju.gowl.reason;

import com.tju.gowl.bean.DicRdfDataBean;
import com.tju.gowl.bean.DicRdfDataMap;
import com.tju.gowl.bean.IndexBean;
import com.tju.gowl.bean.IndexMap;
import com.tju.gowl.dictionary.Dictionary;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SameAsReason {
    public static List<HashSet<Integer>> equiPool = new ArrayList<>();
    static Map<Integer, Integer> equiMapping = new ConcurrentHashMap<>();
    static Map<Integer, Integer> equiRepresentation = new ConcurrentHashMap<>();
    static boolean boolSameAs(int rs) {
        if(equiRepresentation.containsKey(rs)){
            if(equiRepresentation.get(rs)!= rs){
                return false;
            }
        }
        return true;
    }
    static void loopRsRpFindRo(int rs, int rp, int ro) {
        int firstTripleIsp = IndexMap.getFirstIndexSpFromMap(rs, rp);
        if(firstTripleIsp == -1){ return; }

        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIsp;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNsp();
            int roTmp = dicDataBeanIterator.getRo();
            if(ro != roTmp) {
                comEquiPool(ro, roTmp);
            }
        }while(indexNew != -1);
    }

    private static void outEquiMapping() {
        List<String> decodeMap = Dictionary.getDecode();
        Iterator<Map.Entry<Integer, Integer>> ii = equiMapping.entrySet().iterator();
        while( ii.hasNext()){
            Map.Entry<Integer, Integer> iii = ii.next();
            System.out.println(decodeMap.get(iii.getKey())+" "+iii.getValue());

        }
    }

    private static void outEquiPool() {
        List<String> decodeMap = Dictionary.getDecode();
        int count11 = 0;
        Iterator<HashSet<Integer>> ii = equiPool.iterator();
        while(ii.hasNext()){
            count11++;
            System.out.println(count11);
            HashSet<Integer> iii = ii.next();
            Iterator<Integer> iiii = iii.iterator();
            while(iiii.hasNext()){
                Integer iiiii = iiii.next();

                System.out.println(decodeMap.get(iiiii));

            }

        }
    }
    private static List<Integer> findAllTriplesFromRs(Map<Integer, DicRdfDataBean> totalData, Map<Integer, List<IndexBean>> isp, int tmp) {
        List<Integer> rpRoTriples = new ArrayList<>();
        if(isp.containsKey(tmp)){
            List<IndexBean> indexBean = isp.get(tmp);
            Iterator<IndexBean> iterIndexBean = indexBean.iterator();
            while(iterIndexBean.hasNext()){
                //rs rp 第一条数据
                IndexBean tt = iterIndexBean.next();
                int firstTripleIsp = tt.getIndex();
                int rp = tt.getResource();
                DicRdfDataBean dicDataBeanIterator;
                int indexNew = firstTripleIsp;
                do{
                    dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
                    indexNew = dicDataBeanIterator.getNsp();
                    int roTmp = dicDataBeanIterator.getRo();
                    rpRoTriples.add(rp);
                    rpRoTriples.add(roTmp);
                }while(indexNew != -1);
            }
        }
        return rpRoTriples;
    }

    private static List<Integer> findAllTriplesFromRo(Map<Integer, DicRdfDataBean> totalData, Map<Integer, List<IndexBean>> iop, int tmp) {
        List<Integer> rsRpTriples = new ArrayList<>();
        if(iop.containsKey(tmp)){
            List<IndexBean> indexBean = iop.get(tmp);
            Iterator<IndexBean> iterIndexBean = indexBean.iterator();
            while(iterIndexBean.hasNext()){
                //rs rp 第一条数据
                IndexBean tt = iterIndexBean.next();
                int firstTripleIop = tt.getIndex();
                int rp = tt.getResource();
                DicRdfDataBean dicDataBeanIterator;
                int indexNew = firstTripleIop;
                do{
                    dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
                    indexNew = dicDataBeanIterator.getNop();
                    int roTmp = dicDataBeanIterator.getRs();
                    rsRpTriples.add(roTmp);
                    rsRpTriples.add(rp);
                }while(indexNew != -1);
            }
        }
        return rsRpTriples;
    }
    private static void addEquivRsTriple(Map<Integer, DicRdfDataBean> totalData, Map<Integer, List<IndexBean>> isp, HashSet<Integer> tmpPool, int tmp) {
        List<Integer> rpRoTriples = findAllTriplesFromRs(totalData, isp, tmp);
        Iterator<Integer> tmp2 = tmpPool.iterator();
        while(tmp2.hasNext()){
            int tmp22 = tmp2.next();
            if(tmp22 != tmp){
                Iterator<Integer> rpRoList = rpRoTriples.iterator();
                while(rpRoList.hasNext()){
                    int rp = rpRoList.next();
                    int ro = rpRoList.next();
                    if(!DicRdfDataMap.checkDuplicate(tmp22, rp, ro)) {
                        DicRdfDataMap.addSourceRdfDataBean(totalData.size(), tmp22, rp, ro);

                    }
                }
            }

        }
    }
    static void addEquivIndividual(Map<Integer, DicRdfDataBean> totalData) {
        Iterator<HashSet<Integer>> iterPool = equiPool.iterator();
        Map<Integer, List<IndexBean>> isp = IndexMap.getIsp();
        Map<Integer, List<IndexBean>> iop = IndexMap.getIop();
        while(iterPool.hasNext()){
            HashSet<Integer> tmpPool = iterPool.next();
            Iterator<Integer> tmp1 = tmpPool.iterator();
            while(tmp1.hasNext()){
                int tmp = tmp1.next();
                addEquivRsTriple(totalData, isp, tmpPool, tmp);
                addEquivRoTriple(totalData, iop, tmpPool, tmp);
            }
        }
    }

    private static void addEquivRoTriple(Map<Integer, DicRdfDataBean> totalData, Map<Integer, List<IndexBean>> iop, HashSet<Integer> tmpPool, int tmp) {
        Map<Integer, List<IndexBean>> isp = IndexMap.getIsp();
        List<Integer> rsRpTriples = findAllTriplesFromRo(totalData, iop, tmp);
        Iterator<Integer> tmp2 = tmpPool.iterator();
        while(tmp2.hasNext()){
            int tmp22 = tmp2.next();
            if(tmp22 != tmp){
                Iterator<Integer> rsRpList = rsRpTriples.iterator();
                while(rsRpList.hasNext()){
                    int rs = rsRpList.next();
                    int rp = rsRpList.next();
                    if(!DicRdfDataMap.checkDuplicate(rs, rp, tmp22)) {
                        DicRdfDataMap.addSourceRdfDataBean(totalData.size(), rs, rp, tmp22);

                    }
                }
            }

        }
    }
    static void loopRpRoFindRs(int rs, int rp, int ro) {
        int firstTripleIop = IndexMap.getFirstIndexOpFromMap(rp, ro);
        if(firstTripleIop == -1){ return; }

        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIop;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNop();
            int rsTmp = dicDataBeanIterator.getRs();
            if(rs != rsTmp) {
                comEquiPool(rs, rsTmp);
            }
        }while(indexNew != -1);
    }

    private static void comEquiPool(int rs, int rsTmp) {
        int rsEquiv = findEquivPoolIndex(rs);
        int rsTmpEquiv = findEquivPoolIndex(rsTmp);
        if(rsTmpEquiv ==0 && rsEquiv == 0){
            HashSet<Integer> newPool = new HashSet<>();
            newPool.add(rs);
            newPool.add(rsTmp);
            equiPool.add(newPool);
            //避免0冲突，存的是index of pool +1
            equiMapping.put(rs,equiPool.size());
            equiMapping.put(rsTmp,equiPool.size());
        }
        else if(rsTmpEquiv !=0 && rsEquiv != 0){
            if(rsTmpEquiv != rsEquiv){
                //池子合并
                int minn;
                int maxx;
                if(rsTmpEquiv < rsEquiv){
                    minn = rsTmpEquiv;
                    maxx = rsEquiv;
                }
                else{
                    maxx = rsTmpEquiv;
                    minn = rsEquiv;
                }
                //池子maxx装进池子minn
                HashSet<Integer> temp2Pool = equiPool.get(maxx - 1);
                HashSet<Integer> temp1Pool = equiPool.get(minn - 1);
                Iterator<Integer> it = temp2Pool.iterator();
                while (it.hasNext()) {
                    Integer tmp = it.next();
                    temp1Pool.add(tmp);
                    equiMapping.put(tmp,minn);
                }
                temp2Pool.clear();
            }
        }
        else if(rsTmpEquiv == 0){
            HashSet<Integer> tempPool = equiPool.get(rsEquiv - 1);
            tempPool.add(rsTmp);
            equiMapping.put(rsTmp,rsEquiv);
        }
        else if(rsEquiv == 0){
            HashSet<Integer> tempPool = equiPool.get(rsTmpEquiv - 1);
            tempPool.add(rs);
            equiMapping.put(rs,rsTmpEquiv);
        }
        //xinjia
        //TODO 该放哪里
        //rs 和 rsTmp是一个池子
        reFreshEquiRepre(rs);
//        reFreshEquiRepre(rsTmp);
    }

    private static void reFreshEquiRepre(int rs) {
        int poolIndex = equiMapping.get(rs)-1;
        HashSet<Integer> poolTmp = equiPool.get(poolIndex);
        int minNew = getMin(poolTmp);
        Iterator<Integer> samAsIter = poolTmp.iterator();
        while(samAsIter.hasNext()){
            int ii = samAsIter.next();
            if(equiRepresentation.containsKey(ii)){
                int tmp = equiRepresentation.get(ii);
                if(minNew != ii && minNew != tmp){
                    replaceWithMinIsp(ii, minNew);
                    replaceWithMinIop(ii, minNew);
                }
                else {
                    continue;
                }
            }
            else{
                if(minNew == ii){
                    equiRepresentation.put(ii, ii);
                }
                else{
                    equiRepresentation.put(ii, minNew);
                    replaceWithMinIsp(ii, minNew);
                    replaceWithMinIop(ii, minNew);
                }
            }
        }
    }

    private static void replaceWithMinIop(int ii, int minNew) {
        Map<Integer, List<IndexBean>> isp = IndexMap.getIsp();
        Map<Integer, List<IndexBean>> iop = IndexMap.getIop();
//        System.out.println(Dictionary.getDecode().get(ii));
//        System.out.println(Dictionary.getDecode().get(minNew));
        if(!iop.containsKey(ii)) return;
        List<IndexBean> indexBeanList = iop.get(ii);
        Iterator<IndexBean> indexBeanListIter = indexBeanList.iterator();
        while(indexBeanListIter.hasNext()){
            IndexBean beanTmp = indexBeanListIter.next();
            int rpTmp = beanTmp.getResource();
            int indexTmp = beanTmp.getIndex();
            int firstTripleIop = indexTmp;
            if(firstTripleIop == -1){ return; }
            DicRdfDataBean dicDataBeanIterator;
            int indexNew = firstTripleIop;
            do{
                dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
                indexNew = dicDataBeanIterator.getNop();
                int rs1 = dicDataBeanIterator.getRs();
                if(!DicRdfDataMap.checkDuplicate(rs1, rpTmp, minNew)) {
                    //rs rp ro1
                    DicRdfDataMap.addNewRdfDataBean(rs1, rpTmp, minNew);
                }
            }while(indexNew != -1);
        }
    }

    private static void replaceWithMinIsp(int ii, int minNew) {
        Map<Integer, List<IndexBean>> isp = IndexMap.getIsp();
        Map<Integer, List<IndexBean>> iop = IndexMap.getIop();
        if(!isp.containsKey(ii)) return;
        List<IndexBean> indexBeanList = isp.get(ii);
        Iterator<IndexBean> indexBeanListIter = indexBeanList.iterator();
        while(indexBeanListIter.hasNext()){
            IndexBean beanTmp = indexBeanListIter.next();
            int rpTmp = beanTmp.getResource();
            int indexTmp = beanTmp.getIndex();
            int firstTripleIsp = indexTmp;
            if(firstTripleIsp == -1){ return; }
            DicRdfDataBean dicDataBeanIterator;
            int indexNew = firstTripleIsp;
            do{
                dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
                indexNew = dicDataBeanIterator.getNsp();
                int ro1 = dicDataBeanIterator.getRo();
                if(!DicRdfDataMap.checkDuplicate(minNew, rpTmp, ro1)) {
                    //rs rp ro1
                    DicRdfDataMap.addNewRdfDataBean(minNew, rpTmp, ro1);
                }
            }while(indexNew != -1);
        }
    }



    private static int getMin(HashSet<Integer> poolTmp) {
        Iterator<Integer> i = poolTmp.iterator();
        int min = Integer.MAX_VALUE;
        int ii;
        while(i.hasNext()){
            ii = i.next();
            if(ii <= min){
                min = ii;
            }
        }
        return min;
    }

    public static int findEquivPoolIndex(int rs) {
        if(equiMapping.containsKey(rs)){
            return equiMapping.get(rs);
        }
        else{
            equiMapping.put(rs,0);
            return 0;
        }
    }
}
