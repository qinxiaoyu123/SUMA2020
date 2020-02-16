package com.tju.suma.reason;

import java.util.HashSet;
import java.util.Iterator;

public class FunctionalObjectPropertyReason {
    static void reason(int rs, int rp, int ro) {
        int rsEquiv = SameAsReason.findEquivPoolIndex(rs);
//        System.out.println(Dictionary.getDecode().get(rs));
//        System.out.println(Dictionary.getDecode().get(rp));
//        System.out.println(Dictionary.getDecode().get(ro));
        if(rsEquiv == 0){//没有相等individual
            SameAsReason.loopRsRpFindRo(rs, rp, ro);
        }
        else{
            HashSet<Integer> set = SameAsReason.equiPool.get(rsEquiv - 1);
            Iterator<Integer> it = set.iterator();
            while (it.hasNext()) {
                Integer tmp = it.next();
                SameAsReason.loopRsRpFindRo(tmp, rp, ro);
            }
        }
    }
    static void inverseReason(int rs, int rp, int ro) {
//        Map<String, Integer> inverseFunProMap = FunctionalPropertyMap.getInverseFunPropertyMap();
//        int rsEquiv = findEquivPoolIndex(rs);
        int roEquiv = SameAsReason.findEquivPoolIndex(ro);
        if(roEquiv == 0){//没有相等individual
            SameAsReason.loopRpRoFindRs(rs, rp, ro);
        }
        else{
            HashSet<Integer> set = SameAsReason.equiPool.get(roEquiv - 1);
            Iterator<Integer> it = set.iterator();
            while (it.hasNext()) {
                Integer tmp = it.next();
                SameAsReason.loopRpRoFindRs(rs, rp, tmp);
            }
        }

    }
}
