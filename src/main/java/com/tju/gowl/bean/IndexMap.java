package com.tju.gowl.bean;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class IndexMap {
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
}

