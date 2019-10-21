package com.tju.gowl.bean;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class IndexMap {
//    private static final Map<Integer, List<IndexBean>> Isp = new ConcurrentHashMap<>();
    private static final Map<Integer, Map<Integer, IndexPair>>Isp = new ConcurrentHashMap<>();
    private static final Map<Integer, Map<Integer, IndexPair>>Iop = new ConcurrentHashMap<>();
//    private static final Map<Integer, List<IndexBean>> Iop = new ConcurrentHashMap<>();

    public static Map<Integer, Map<Integer, IndexPair>> getIsp() {
        return Isp;
    }

    public static Map<Integer, Map<Integer, IndexPair>> getIop() {
        return Iop;
    }

    public static int getFirstIndexSpFromMap(int rs, int rp, int index) {
        if (Isp.containsKey(rs)) {//包含 rs
            Map<Integer, IndexPair> Ip = Isp.get(rs);
            if(Ip.containsKey(rp)){
                return Ip.get(rp).getFirstTriple();
            }
            Ip.put(rp, new IndexPair(index, index));
            return -1;
        } else {
            Isp.put(rs, new HashMap<Integer, IndexPair>(){{
                put (rp, new IndexPair(index, index));
            }});
            return -1;
        }

    }
    public static int getLastIndexSpFromMap(int rs, int rp, int index) {
        if (Isp.containsKey(rs)) {//包含 rs
            Map<Integer, IndexPair> Ip = Isp.get(rs);
            if(Ip.containsKey(rp)){
                return Ip.get(rp).getLastTriple();
            }
            Ip.put(rp, new IndexPair(index, index));
            return -1;
        } else {
            Isp.put(rs, new HashMap<Integer, IndexPair>(){{
                put (rp, new IndexPair(index, index));
            }});
            return -1;
        }

    }

    public static int getFirstIndexOpFromMap(int rp, int ro, int index) {
        if (Iop.containsKey(ro)) {//包含 rs
            Map<Integer, IndexPair> Ip = Iop.get(ro);
            if(Ip.containsKey(rp)){
                return Ip.get(rp).getFirstTriple();
            }
            Ip.put(rp, new IndexPair(index, index));
            return -1;
        } else {
            Isp.put(ro, new HashMap<Integer, IndexPair>(){{
                put (rp, new IndexPair(index, index));
            }});
            return -1;
        }

    }

    public static int getLastIndexOpFromMap(int rp, int ro, int index) {
        if (Iop.containsKey(ro)) {//包含 rs
            Map<Integer, IndexPair> Ip = Iop.get(ro);
            if(Ip.containsKey(rp)){
                return Ip.get(rp).getLastTriple();
            }
            Ip.put(rp, new IndexPair(index, index));
            return -1;
        } else {
            Isp.put(ro, new HashMap<Integer, IndexPair>(){{
                put (rp, new IndexPair(index, index));
            }});
            return -1;
        }

    }


    public static int getFirstIndexSpFromMap(int rs, int rp) {
        if (Isp.containsKey(rs)) {//包含 rs
            Map<Integer, IndexPair> Ip = Isp.get(rs);
            if(Ip.containsKey(rp)){
                return Ip.get(rp).getFirstTriple();
            }
        }
        return -1;
    }
    public static int getLastIndexSpFromMap(int rs, int rp) {
        if (Isp.containsKey(rs)) {//包含 rs
            Map<Integer, IndexPair> Ip = Isp.get(rs);
            if(Ip.containsKey(rp)){
                return Ip.get(rp).getLastTriple();
            }
        }
        return -1;
    }


    public static int getFirstIndexOpFromMap(int rp, int ro) {
        if (Iop.containsKey(ro)) {//包含 rs
            Map<Integer, IndexPair> Ip = Iop.get(ro);
            if (Ip.containsKey(rp)) {
                return Ip.get(rp).getFirstTriple();
            }
        }
        return -1;
    }

    public static int getLastIndexOpFromMap(int rp, int ro) {
        if (Iop.containsKey(ro)) {//包含 rs
            Map<Integer, IndexPair> Ip = Iop.get(ro);
            if (Ip.containsKey(rp)) {
                return Ip.get(rp).getLastTriple();
            }
        }
        return -1;
    }
}

