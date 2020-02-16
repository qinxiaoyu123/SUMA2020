package com.tju.suma.reason;

import com.tju.suma.bean.DicRdfDataBean;
import com.tju.suma.bean.DicRdfDataMap;
import com.tju.suma.index.ThreeKeyMap;
import com.tju.suma.index.TwoKeyMap;

public class TransitiveReason {
    static void reason(int rs, int rp, int ro) {
        int firstTripleIsp = TwoKeyMap.getFirstIndexSpFromMap(ro, rp);
        if (firstTripleIsp == -1) return;
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIsp;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNsp();
            int ro1 = dicDataBeanIterator.getRo();
            if(!ThreeKeyMap.checkDuplicate(rs, rp, ro1)) {
                //rs rp ro1
                DicRdfDataMap.addNewRdfDataBean(rs, rp, ro1);
            }
        }while(indexNew != -1);

    }
}
