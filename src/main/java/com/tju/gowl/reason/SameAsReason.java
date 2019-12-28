package com.tju.gowl.reason;

import com.tju.gowl.bean.DicRdfDataBean;
import com.tju.gowl.bean.DicRdfDataMap;
import com.tju.gowl.index.ThreeKeyMap;
import com.tju.gowl.index.TwoKeyMap;
import com.tju.gowl.dictionary.Dictionary;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SameAsReason {
    public static List<HashSet<Integer>> equiPool = new ArrayList<>();
    static Map<Integer, Integer> equiPoolIndex = new ConcurrentHashMap<>();
    static Map<Integer, Integer> equiRepresentation = new ConcurrentHashMap<>();
    static boolean boolSameAs(int rs) {
        if(equiRepresentation.containsKey(rs)){
            if(equiRepresentation.get(rs)!= rs){
//                System.out.println("equiRepresentation"+Dictionary.getDecode().get(equiRepresentation.get(rs))+" "+Dictionary.getDecode().get(rs));
                return false;
            }
        }
        return true;
    }
    static void loopRsRpFindRo(int rs, int rp, int ro) {
        int firstTripleIsp = TwoKeyMap.getFirstIndexSpFromMap(rs, rp);
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
        String[] decodeMap = Dictionary.getDecode();
        Iterator<Map.Entry<Integer, Integer>> ii = equiPoolIndex.entrySet().iterator();
        while( ii.hasNext()){
            Map.Entry<Integer, Integer> iii = ii.next();
            System.out.println(decodeMap[iii.getKey()]+" "+iii.getValue());

        }
    }

    private static void outEquiPool() {
        String[] decodeMap = Dictionary.getDecode();
        int count11 = 0;
        Iterator<HashSet<Integer>> ii = equiPool.iterator();
        while(ii.hasNext()){
            count11++;
            System.out.println(count11);
            HashSet<Integer> iii = ii.next();
            Iterator<Integer> iiii = iii.iterator();
            while(iiii.hasNext()){
                Integer iiiii = iiii.next();

                System.out.println(decodeMap[iiiii]);

            }

        }
    }



    private static void addEquivRsTriple(HashSet<Integer> tmpPool, int tmp) {
        List<Integer> rpRoTriples = TwoKeyMap.findAllTriplesFromRs(tmp);
        Iterator<Integer> tmp2 = tmpPool.iterator();
        while(tmp2.hasNext()){
            int tmp22 = tmp2.next();
            if(tmp22 != tmp){
                Iterator<Integer> rpRoList = rpRoTriples.iterator();
                while(rpRoList.hasNext()){
                    int rp = rpRoList.next();
                    int ro = rpRoList.next();
                    if(!ThreeKeyMap.checkDuplicate(tmp22, rp, ro)) {
                        DicRdfDataMap.addSourceRdfDataBean(tmp22, rp, ro);
//                        DicRdfDataMap.addSourceRdfDataBean(totalData.size(), tmp22, rp, ro);

                    }
                }
            }

        }
    }
    public static void addEquivIndividual() {
        Map<Integer, DicRdfDataBean> totalData = DicRdfDataMap.getDicDataMap();
        Iterator<HashSet<Integer>> iterPool = equiPool.iterator();
        while(iterPool.hasNext()){
            HashSet<Integer> tmpPool = iterPool.next();
            Iterator<Integer> tmp1 = tmpPool.iterator();
            while(tmp1.hasNext()){
                int tmp = tmp1.next();
                addEquivRsTriple(tmpPool, tmp);
                addEquivRoTriple(tmpPool, tmp);
            }
        }
        System.out.println("after equiv data size "+totalData.size());
    }

    private static void addEquivRoTriple(HashSet<Integer> tmpPool, int tmp) {
        List<Integer> rsRpTriples = TwoKeyMap.findAllTriplesFromRo(tmp);
        Iterator<Integer> tmp2 = tmpPool.iterator();
        while(tmp2.hasNext()){
            int tmp22 = tmp2.next();
            if(tmp22 != tmp){
                Iterator<Integer> rsRpList = rsRpTriples.iterator();
                while(rsRpList.hasNext()){
                    int rs = rsRpList.next();
                    int rp = rsRpList.next();
                    if(!ThreeKeyMap.checkDuplicate(rs, rp, tmp22)) {
                        DicRdfDataMap.addSourceRdfDataBean(rs, rp, tmp22);
//                        DicRdfDataMap.addSourceRdfDataBean(totalData.size(), rs, rp, tmp22);

                    }
                }
            }

        }
    }
    static void loopRpRoFindRs(int rs, int rp, int ro) {
        int firstTripleIop = TwoKeyMap.getFirstIndexOpFromMap(rp, ro);
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
            equiPoolIndex.put(rs,equiPool.size());
            equiPoolIndex.put(rsTmp,equiPool.size());
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
                    equiPoolIndex.put(tmp,minn);
                }
                temp2Pool.clear();
            }
        }
        else if(rsTmpEquiv == 0){
            HashSet<Integer> tempPool = equiPool.get(rsEquiv - 1);
            tempPool.add(rsTmp);
            equiPoolIndex.put(rsTmp,rsEquiv);
        }
        else if(rsEquiv == 0){
            HashSet<Integer> tempPool = equiPool.get(rsTmpEquiv - 1);
            tempPool.add(rs);
            equiPoolIndex.put(rs,rsTmpEquiv);
        }
        //xinjia
        //TODO 该放哪里
        //rs 和 rsTmp是一个池子
        reFreshEquiRepre(rs);
//        reFreshEquiRepre(rsTmp);
    }

    private static void reFreshEquiRepre(int rs) {
        int poolIndex = equiPoolIndex.get(rs)-1;
        HashSet<Integer> poolTmp = equiPool.get(poolIndex);
        int minNew = getMin(poolTmp);
        Iterator<Integer> samAsIter = poolTmp.iterator();
        while(samAsIter.hasNext()){
            int ii = samAsIter.next();
            if(equiRepresentation.containsKey(ii)){
                int tmp = equiRepresentation.get(ii);
                if(minNew != ii && minNew != tmp){
                    TwoKeyMap.replaceWithMinIsp(ii, minNew);
                    TwoKeyMap.replaceWithMinIop(ii, minNew);
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
                    TwoKeyMap.replaceWithMinIsp(ii, minNew);
                    TwoKeyMap.replaceWithMinIop(ii, minNew);
                }
            }
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
        if(equiPoolIndex.containsKey(rs)){
            return equiPoolIndex.get(rs);
        }
        else{
            equiPoolIndex.put(rs,0);
            return 0;
        }
    }
}
