package com.tju.gowl.reason;

import com.tju.gowl.bean.DicRdfDataBean;
import com.tju.gowl.bean.DicRdfDataMap;
import com.tju.gowl.index.ThreeKeyMap;

import java.util.List;
import java.util.Map;

public class SubPropertyReason {
    static void reason(Map<Integer, DicRdfDataBean> totalData, Map<Integer, DicRdfDataBean> iteratorMap, Map<Integer, DicRdfDataBean> stashMap, int rs, List<Integer> head, int ro) {
        int rp = head.get(0);
        int index = totalData.size()+iteratorMap.size()+stashMap.size();
        //TODO
        if(!ThreeKeyMap.checkDuplicate(rs,rp,ro,index)){
            DicRdfDataMap.addNewRdfDataBean(rs, rp, ro);
        }

    }
}
