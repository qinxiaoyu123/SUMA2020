package com.tju.gowl.reason;

import com.tju.gowl.bean.DicRdfDataBean;
import com.tju.gowl.bean.DicRdfDataMap;
import com.tju.gowl.index.ThreeKeyMap;
import com.tju.gowl.index.TwoKeyMap;

import java.util.List;
import java.util.Map;

import static com.tju.gowl.reason.DicSerialReason.typeEncode;

public class ObjectHasValueReason {

    public static void reason(Map<Integer, DicRdfDataBean> totalData, Map<Integer, DicRdfDataBean> iteratorMap, Map<Integer, DicRdfDataBean> stashMap, int rs, List<Integer> head) {
        int rp = head.get(0);
        int class2 = head.get(1);
        int index = totalData.size()+iteratorMap.size()+stashMap.size();
        if(!ThreeKeyMap.checkDuplicate(rs, rp, class2)) {
            DicRdfDataMap.addNewRdfDataBean(rs, rp, class2);
        }
    }
}
