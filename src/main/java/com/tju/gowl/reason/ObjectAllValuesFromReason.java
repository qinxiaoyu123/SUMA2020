package com.tju.gowl.reason;

import com.tju.gowl.bean.DicRdfDataBean;
import com.tju.gowl.bean.DicRdfDataMap;
import com.tju.gowl.index.ThreeKeyMap;
import com.tju.gowl.index.TwoKeyMap;

import java.util.List;

import static com.tju.gowl.reason.DicSerialReason.typeEncode;

public class ObjectAllValuesFromReason {
    static void reason(int rs, List<Integer> head) {
        int rp = head.get(0);
        int class2 = head.get(1);
        int firstTripleIsp = TwoKeyMap.getFirstIndexSpFromMap(rs, rp);
        if(firstTripleIsp == -1){ return; }
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIsp;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNsp();
            int ro1 = dicDataBeanIterator.getRo();
            if(!ThreeKeyMap.checkDuplicate(ro1, typeEncode, class2)) {
                //rs rp ro1
                DicRdfDataMap.addNewRdfDataBean(ro1, typeEncode, class2);
            }
        }while(indexNew != -1);
    }
    static void inverseReason(int rs, List<Integer> head) {
        int rp = head.get(0);
        int class2 = head.get(1);
        int firstTripleIop = TwoKeyMap.getFirstIndexOpFromMap(rp, rs);
        if(firstTripleIop == -1){ return; }
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIop;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNop();
            int rs1 = dicDataBeanIterator.getRs();
            if(!ThreeKeyMap.checkDuplicate(rs1, typeEncode, class2)) {
                //rs rp ro1
                DicRdfDataMap.addNewRdfDataBean(rs1, typeEncode, class2);
            }
        }while(indexNew != -1);
    }
}
