package com.tju.gowl.reason;

import com.tju.gowl.bean.DicRdfDataBean;
import com.tju.gowl.bean.DicRdfDataMap;
import com.tju.gowl.index.ThreeKeyMap;

import java.util.List;
import java.util.Map;

public class BasicReason {
    static void reason(Map<Integer, DicRdfDataBean> totalData, Map<Integer, DicRdfDataBean> iteratorMap, Map<Integer, DicRdfDataBean> stashMap, int rs, int rp, List<Integer> head) {
        int ro = head.get(0);
        int index = totalData.size()+iteratorMap.size()+stashMap.size();
        if(!ThreeKeyMap.checkDuplicate(rs,rp,ro,index)){
            DicRdfDataMap.addNewRdfDataBean(rs, rp, ro);
        }
    }
}
