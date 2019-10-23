package com.tju.gowl.index;

import com.tju.gowl.bean.DicRdfDataBean;
import com.tju.gowl.bean.DicRdfDataMap;
import com.tju.gowl.bean.IndexBean;
import com.tju.gowl.bean.IndexMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OneKeyIndex {
    private static final Map<Integer, List<IndexBean>> Isp = new ConcurrentHashMap<>();
    private static final Map<Integer, List<IndexBean>> Iop = new ConcurrentHashMap<>();

    public static Map<Integer, List<IndexBean>> getIsp() {
        return Isp;
    }

    public static Map<Integer, List<IndexBean>> getIop() {
        return Iop;
    }

    public static int getFirstIndexSpFromMap(int rs, int rp, int index) {
        if (Isp.containsKey(rs)) {//包含 rs
            List<IndexBean> list = Isp.get(rs);
            Iterator it = list.iterator();
            boolean flag = false;
            while (it.hasNext()) {
                //System.out.println(it.next());
                IndexBean indexNode = (IndexBean) it.next();
                int resource = indexNode.getResource();
                if (rp == resource) {
                    flag = true;
                    return indexNode.getIndex();
                }
            }
            if (!flag) {
                IndexBean indexNode1 = new IndexBean();
                indexNode1.setResource(rp);
                indexNode1.setIndex(index);
                list.add(indexNode1);
                return -1;
            }

        } else {
            IndexBean indexNode = new IndexBean();
            indexNode.setResource(rp);
            indexNode.setIndex(index);
            List<IndexBean> list = new ArrayList<>();
            list.add(indexNode);
            Isp.put(rs, list);
            return -1;

        }
        return -1;
    }
    public static int getFirstIndexOpFromMap(int rp, int ro, int index) {
        if (Iop.containsKey(ro)) {//包含 rs
            List<IndexBean> list = Iop.get(ro);
            Iterator it = list.iterator();
            boolean flag = false;
            while (it.hasNext()) {
                //System.out.println(it.next());
                IndexBean indexNode = (IndexBean) it.next();
                int resource = indexNode.getResource();
                if (rp == resource) {
                    flag = true;
                    return indexNode.getIndex();
                }
            }
            if (!flag) {
                IndexBean indexNode1 = new IndexBean();
                indexNode1.setResource(rp);
                indexNode1.setIndex(index);
                list.add(indexNode1);
                return -1;
            }

        } else {
            IndexBean indexNode = new IndexBean();
            indexNode.setResource(rp);
            indexNode.setIndex(index);
            List<IndexBean> list = new ArrayList<>();
            list.add(indexNode);
            Iop.put(ro, list);
            return -1;

        }
        return -1;
    }


    public static int getFirstIndexSpFromMap(int rs, int rp) {
        if (Isp.containsKey(rs)) {//包含 rs
            List<IndexBean> list = Isp.get(rs);
            Iterator it = list.iterator();
            while (it.hasNext()) {
                //System.out.println(it.next());
                IndexBean indexNode = (IndexBean) it.next();
                int resource = indexNode.getResource();
                if (rp == resource) {
                    return indexNode.getIndex();
                }
            }

        }
        return -1;
    }

    public static int getFirstIndexOpFromMap(int rp, int ro) {
        if (Iop.containsKey(ro)) {//包含 rs
            List<IndexBean> list = Iop.get(ro);
            Iterator it = list.iterator();
            while (it.hasNext()) {
                //System.out.println(it.next());
                IndexBean indexNode = (IndexBean) it.next();
                int resource = indexNode.getResource();
                if (rp == resource) {
                    return indexNode.getIndex();
                }
            }

        }
        return -1;
    }

    public static List<Integer> findAllTriplesFromRs(int tmp) {
        List<Integer> rpRoTriples = new ArrayList<>();
        if(Isp.containsKey(tmp)){
            List<IndexBean> indexBean = Isp.get(tmp);
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
    public static List<Integer> findAllTriplesFromRo(int tmp) {
        List<Integer> rsRpTriples = new ArrayList<>();
        if(Iop.containsKey(tmp)){
            List<IndexBean> indexBean = Iop.get(tmp);
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

    public static void replaceWithMinIop(int ii, int minNew) {
        List<Integer> rsRpTriples = findAllTriplesFromRo(ii);
        Iterator<Integer> rsRpList = rsRpTriples.iterator();
        while(rsRpList.hasNext()){
            int rs = rsRpList.next();
            int rp = rsRpList.next();
            if(!checkDuplicate(rs, rp, minNew)) {
                DicRdfDataMap.addNewRdfDataBean(rs, rp, minNew);
            }
        }
    }
    public static void replaceWithMinIsp(int ii, int minNew) {
        List<Integer> rpRoTriples = findAllTriplesFromRs(ii);
        Iterator<Integer> rpRoList = rpRoTriples.iterator();
        while(rpRoList.hasNext()){
            int rp = rpRoList.next();
            int ro = rpRoList.next();
            if(!checkDuplicate(minNew, rp, ro)) {
                DicRdfDataMap.addNewRdfDataBean(minNew, rp, ro);

            }
        }
    }

    public static boolean checkDuplicate(int firstTripleIsp, int ro) {
        DicRdfDataBean dataBean;
        int indexNew = firstTripleIsp;
        if(indexNew == -1){
            return false;
        }
        do{
            dataBean = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dataBean.getNsp();
            int roIterator = dataBean.getRo();
            if(ro == roIterator) return true;
        }while(indexNew != -1);
        return false;
    }
    //Isp = 1, Isp ; Isp = 0; Ipo
    public static boolean checkDuplicate(int rs, int rp, int ro) {
        int firstIndex = getFirstIndexSpFromMap(rs,rp);
        if(firstIndex == -1){
            return false;
        }
        return checkDuplicate(firstIndex, ro);
    }
}
