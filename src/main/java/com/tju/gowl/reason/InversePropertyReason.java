package com.tju.gowl.reason;

import com.tju.gowl.bean.DicRdfDataBean;
import com.tju.gowl.bean.DicRdfDataMap;
import com.tju.gowl.index.ThreeKeyMap;

import java.util.List;
import java.util.Map;

public class InversePropertyReason {
    public static void reason(int rs, int rp, int ro){

    }

    public static void reason(Map<Integer, DicRdfDataBean> totalData, Map<Integer, DicRdfDataBean> iteratorMap, Map<Integer, DicRdfDataBean> stashMap, int rs, List<Integer> head, int ro) {
        int rp = head.get(0);
        int index = totalData.size()+iteratorMap.size()+stashMap.size();
        //TODO
        if(!ThreeKeyMap.checkDuplicate(ro,rp,rs,index)){
            DicRdfDataMap.addNewRdfDataBean(ro, rp, rs);
        }
    }
}
