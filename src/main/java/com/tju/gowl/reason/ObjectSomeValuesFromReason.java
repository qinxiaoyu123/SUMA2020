package com.tju.gowl.reason;

import com.tju.gowl.bean.DicRdfDataBean;
import com.tju.gowl.bean.DicRdfDataMap;
import com.tju.gowl.index.ThreeKeyMap;
import com.tju.gowl.index.TwoKeyMap;

import java.util.List;
import java.util.Map;

import static com.tju.gowl.reason.DicSerialReason.*;

public class ObjectSomeValuesFromReason {
    static void reason(Map<Integer, DicRdfDataBean> totalData, Map<Integer, DicRdfDataBean> iteratorMap, Map<Integer, DicRdfDataBean> stashMap, int rs, List<Integer> head) {
        int rp = head.get(0);
        int class2 = head.get(1);
        int index = totalData.size()+iteratorMap.size()+stashMap.size();
        int firstTripleIsp = TwoKeyMap.getFirstIndexSpFromMap(rs, rp, index);
        if(firstTripleIsp != -1){
            if(class2 == 1){//TODO owl:Thing 情况
                return;
            }
            DicRdfDataBean dicDataBeanIterator;
            int indexNew = firstTripleIsp;
            do{
                dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
                indexNew = dicDataBeanIterator.getNsp();
                int ro = dicDataBeanIterator.getRo();
                if(ThreeKeyMap.checkDuplicate(ro, typeEncode, class2)){
                    return;
                }
            }while(indexNew != -1);
        }
        addSomeValueFrom(rs, rp, class2, firstTripleIsp);
    }
    private static void addSomeValueFrom(int rs, int rp, int class2, int firstTripleIsp) {
        int ro = anonymous;
        if (!someValueFlag && rs < 0) {
            someValue++;
            someValueFlag = true;
        }
        DicRdfDataMap.addNewRdfDataBean(rs, rp, ro, firstTripleIsp);
        anonymous--;
        DicRdfDataMap.addNewRdfDataBean(ro, typeEncode, class2);
    }
}
