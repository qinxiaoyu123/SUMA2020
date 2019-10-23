package com.tju.gowl.reason;

import com.tju.gowl.bean.DicRdfDataMap;
import com.tju.gowl.index.ThreeKeyMap;

public class SymmetricObjectPropertyReason {
    static void reason(int rs, int rp, int ro) {
        if(!ThreeKeyMap.checkDuplicate(ro, rp, rs)) {
            DicRdfDataMap.addNewRdfDataBean(ro, rp, rs);
        }

    }
}
