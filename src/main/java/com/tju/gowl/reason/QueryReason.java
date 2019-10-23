package com.tju.gowl.reason;

import com.tju.gowl.bean.DicRdfDataBean;
import com.tju.gowl.bean.DicRdfDataMap;
import com.tju.gowl.index.ThreeKeyMap;
import com.tju.gowl.index.TwoKeyMap;

import java.io.IOException;
import java.util.List;
import static com.tju.gowl.reason.DicSerialReason.*;
public class QueryReason {
    static void type22Reason(int rs, int rp, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int class2 = head.get(1);
        int property = head.get(2);
        int class3 = head.get(3);
        int firstTripleIop = TwoKeyMap.getFirstIndexOpFromMap(property, rs);
        if(firstTripleIop == -1){ return; }
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIop;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNop();
            int rsTmp = dicDataBeanIterator.getRs();

            if(!ThreeKeyMap.checkDuplicate(rsTmp, typeEncode, class1)) {
                if(ThreeKeyMap.checkDuplicate(rsTmp, typeEncode, class2)){
                    if(checkAllValue(rsTmp,property,class3)){
                        DicRdfDataMap.addNewRdfDataBean(rsTmp, typeEncode, class1);
                    }
                }
            }
        }while(indexNew != -1);
    }

    static void type21Reason(int rs, int rp, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int class2 = head.get(1);
        int property = head.get(2);
        int class3 = head.get(3);
        if(ThreeKeyMap.checkDuplicate(rs, typeEncode, class1)) return;
        if(!ThreeKeyMap.checkDuplicate(rs, typeEncode, class2)) return;
        if(checkAllValue(rs,property,class3)){
            DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
        }
    }

    static void type20Reason(int rs, int rp, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int class2 = head.get(1);
        int property = head.get(2);
        int class3 = head.get(3);
        if(ThreeKeyMap.checkDuplicate(rs, typeEncode, class1)) return;
        if(checkAllValue(rs,property,class3)){
            DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
        }
    }

    static void type19Reason(int rs, int rp, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int property = head.get(1);
        int class3 = head.get(2);
        int firstTripleIop = TwoKeyMap.getFirstIndexOpFromMap(property, rs);
        if(firstTripleIop == -1){ return; }
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIop;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNop();
            int rsTmp = dicDataBeanIterator.getRs();
            if(!ThreeKeyMap.checkDuplicate(rsTmp, typeEncode, class1)) {
                if(checkAllValue(rsTmp,property,class3)){
                    DicRdfDataMap.addNewRdfDataBean(rsTmp, typeEncode, class1);
                }
            }
        }while(indexNew != -1);
    }

    static void type18Reason(int rs, int rp, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int property = head.get(1);
        int class3 = head.get(2);
        if(ThreeKeyMap.checkDuplicate(rs, typeEncode, class1)) return;
        if(checkAllValue(rs,property,class3)){
            DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
        }
    }


    static void type16Reason(int rs, int rp, int ro, List<Integer> head) {
        //rs type class3
        int class1 = head.get(0);
        int class2 = head.get(1);
        int property = head.get(2);
        int class3 = head.get(3);
        int class4= head.get(4);
        int firstTripleIop = TwoKeyMap.getFirstIndexOpFromMap(property, rs);
        if(firstTripleIop == -1){ return; }
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIop;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNop();
            int rsTmp = dicDataBeanIterator.getRs();

            if(!ThreeKeyMap.checkDuplicate(rsTmp, typeEncode, class1)) {
                if(ThreeKeyMap.checkDuplicate(rsTmp, typeEncode, class2)){
                    if(checkAllValue(rsTmp,property,class3)){
                        if(checkAllValue(rsTmp,property,class4)){
                            DicRdfDataMap.addNewRdfDataBean(rsTmp, typeEncode, class1);
                        }
                    }
                }
            }
        }while(indexNew != -1);
    }

    static boolean checkAllValue(int rs, int property, int class3) {
        int firstTripleIsp = TwoKeyMap.getFirstIndexSpFromMap(rs, property);
        if(firstTripleIsp == -1) {
            return true;
        }
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIsp;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNsp();
            int ro = dicDataBeanIterator.getRo();
            if(!ThreeKeyMap.checkDuplicate(ro, typeEncode, class3))
                return false;
        }while(indexNew != -1);
        return true;
    }

    static void type15Reason(int rs, int rp, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int class2 = head.get(1);
        int property = head.get(2);
        int class3 = head.get(3);
        int class4= head.get(4);
        if(ThreeKeyMap.checkDuplicate(rs, typeEncode, class1)) return;
        if(!ThreeKeyMap.checkDuplicate(rs, typeEncode, class2)) return;
        int firstTripleIsp = TwoKeyMap.getFirstIndexSpFromMap(rs, property);
        if(firstTripleIsp == -1) {
            DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
            return;
        }
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIsp;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNsp();
            int ro1 = dicDataBeanIterator.getRo();
            if(!ThreeKeyMap.checkDuplicate(ro1, typeEncode, class3))
                return;
            if(!ThreeKeyMap.checkDuplicate(ro1, typeEncode, class4))
                return;
        }while(indexNew != -1);
        DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
    }

    static void type14Reason(int rs, int rp, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int class2 = head.get(1);
        int property = head.get(2);
        int class3 = head.get(3);
        int class4= head.get(4);
        if(ThreeKeyMap.checkDuplicate(rs, typeEncode, class1)) return;
        int firstTripleIsp = TwoKeyMap.getFirstIndexSpFromMap(rs, property);
        if(firstTripleIsp == -1) {
            DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
            return;
        }
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIsp;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNsp();
            int ro1 = dicDataBeanIterator.getRo();
            if(!ThreeKeyMap.checkDuplicate(ro1, typeEncode, class3))
                return;
            if(!ThreeKeyMap.checkDuplicate(ro1, typeEncode, class4))
                return;
        }while(indexNew != -1);
        DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
    }


    static void type12Reason(int rs, int rp, int ro1, List<Integer> head) throws IOException {
        int class1 = head.get(0);
        int count =head.get(1);
        int property = head.get(2);
        int class2 = head.get(3);
        int count1 = 0;
        if(ThreeKeyMap.checkDuplicate(rs, typeEncode, class1)) return;
        int firstTripleIsp = TwoKeyMap.getFirstIndexSpFromMap(rs, rp);
        if(firstTripleIsp == -1) return;
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIsp;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNsp();
            int ro = dicDataBeanIterator.getRo();
            if(class2 == 1){//TODO owl:Thing 情况
                count1++;
            }
            else{
                if(ThreeKeyMap.checkDuplicate(ro, typeEncode, class2)){
                    count1++;
                }
            }

        }while(indexNew != -1);
        if(count1 >= count){
            DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
        }

    }

    static void type11Reason(int rs1, int rp, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int property = head.get(1);
        int class2 = head.get(2);
        int firstTripleIop = TwoKeyMap.getFirstIndexOpFromMap(property, ro);
        if(firstTripleIop == -1){ return; }
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIop;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNop();
            int rs = dicDataBeanIterator.getRs();
            if(!ThreeKeyMap.checkDuplicate(rs, typeEncode, class1)) {
                DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
            }
        }while(indexNew != -1);
    }

    static void type10Reason(int rs, int rp, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int property = head.get(1);
        int class2 = head.get(2);
        if(ThreeKeyMap.checkDuplicate(rs, typeEncode, class1)) return;
        if(class2 == 1){
            DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
            return;
        }
        if(ThreeKeyMap.checkDuplicate(ro, typeEncode, class2)){
            DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
        }

    }
    static void equivalentClass3Reason(int ro, List<Integer> head) {
        int class1 = head.get(0);
        int class2 = head.get(1);
        int r = head.get(2);
        int class3 = head.get(3);
        int firstTripleIop = TwoKeyMap.getFirstIndexOpFromMap(r, ro);
        if(firstTripleIop == -1){ return; }

        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIop;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNop();
            int rs = dicDataBeanIterator.getRs();
            if(!ThreeKeyMap.checkDuplicate(rs, typeEncode, class1)) {
                if(ThreeKeyMap.checkDuplicate(rs, typeEncode, class2)){
                    //rs type class1
                    DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
                }
            }
        }while(indexNew != -1);

    }

    static void equivalentRoleReason(int rs, int ro, List<Integer> head) {
        int class1 = head.get(0);
        int class2 = head.get(1);
        int r = head.get(2);
        int class3 = head.get(3);
        if(ThreeKeyMap.checkDuplicate(rs, typeEncode, class1)){
            return;
        }
        boolean flag1 = ThreeKeyMap.checkDuplicate(rs, typeEncode, class2);
        if(flag1){
            if(class3 == 1){
                DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
                return;
            }
            else{
                boolean flag2 = ThreeKeyMap.checkDuplicate(ro, typeEncode, class3);
                if(flag2){
                    DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
                }
            }
        }

    }

    static void equivalentClass2Reason(int rs, List<Integer> head) {
        int class1 = head.get(0);
        int class2 = head.get(1);
        int r = head.get(2);
        int class3 = head.get(3);
        if(ThreeKeyMap.checkDuplicate(rs, typeEncode, class1)){
            return;
        }
        int firstTripleIsp = TwoKeyMap.getFirstIndexSpFromMap(rs, r);
        if(firstTripleIsp == -1){
            return;
        }
        if(class3 == 1){//owl:Thing
            DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
            return;
        }
        DicRdfDataBean dicDataBeanIterator;
        int indexNew = firstTripleIsp;
        do{
            dicDataBeanIterator = DicRdfDataMap.getDataBean(indexNew);
            indexNew = dicDataBeanIterator.getNsp();
            int ro = dicDataBeanIterator.getRo();
            if(ThreeKeyMap.checkDuplicate(ro, typeEncode, class3)){
                //rs type class1
                DicRdfDataMap.addNewRdfDataBean(rs, typeEncode, class1);
                return;
            }
        }while(indexNew != -1);

    }
}
