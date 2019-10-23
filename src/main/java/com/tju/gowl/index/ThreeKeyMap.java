package com.tju.gowl.index;

import com.tju.gowl.bean.DicRdfDataBean;
import com.tju.gowl.bean.DicRdfDataMap;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ThreeKeyMap {
    private static final Map<Integer, Map<Integer,Map <Integer,Integer>>> IndexThreeKey = new ConcurrentHashMap<>();
    private static final Map<Integer, Map<Integer,Map <Integer,Integer>>> IopThreeKey = new ConcurrentHashMap<>();

    public static Map<Integer, Map<Integer,Map <Integer,Integer>>> getIndexThreeKey() { return IndexThreeKey; }

    public static Map<Integer, Map<Integer,Map <Integer,Integer>>> getIop() {
        return IopThreeKey;
    }

    public static boolean checkDuplicate(int rs, int rp, int ro, int index) {
        if (IndexThreeKey.containsKey(rp)) {
            //rp rs ro index
            Map<Integer, Map<Integer, Integer>> IndexTwoKey = IndexThreeKey.get(rp);
            if(IndexTwoKey.containsKey(rs)){
                Map<Integer, Integer> IndexOneKey = IndexTwoKey.get(rs);
                if(IndexOneKey.containsKey(ro)){
                    return true;
                }
                else{
                    IndexOneKey.put(ro, index);
                    return false;
                }
            }
            else{
                IndexTwoKey.put(rs, new HashMap<Integer, Integer>() {
                    {put(ro, index);}
                });
                return false;

            }

        } else {
            IndexThreeKey.put(rp, new HashMap<Integer, Map <Integer,Integer>>(){{
                put(rs, new HashMap<Integer, Integer>(){{put(ro, index);}});
            }});
            return false;
        }
    }
    public static boolean checkDuplicate(int rs, int rp, int ro) {
        if (IndexThreeKey.containsKey(rp)) {
            //rp rs ro index
            Map<Integer, Map<Integer, Integer>> IndexTwoKey = IndexThreeKey.get(rp);
            if (IndexTwoKey.containsKey(rs)) {
                Map<Integer, Integer> IndexOneKey = IndexTwoKey.get(rs);
                if (IndexOneKey.containsKey(ro)) {
                    return true;
                }
            }
        }
        return false;
    }

}
