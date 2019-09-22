package com.tju.gowl.bean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DicOwlBean {
    private int type;
    private List<Integer> ruleHead;
    //int type


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DicOwlBean that = (DicOwlBean) o;
        return type == that.type &&
                Objects.equals(ruleHead, that.ruleHead);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, ruleHead);
    }

    @Override
    public String toString() {
        return "DicOwlBean{" +
                "type=" + type +
                ", ruleHead=" + ruleHead +
                '}';
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Integer> getRuleHead() {
        return ruleHead;
    }

    public void setRuleHead(int ruleHead) {
        this.ruleHead = Collections.singletonList(ruleHead);
    }
    public void setRuleHead(int ruleHead1, int ruleHead2) {
        List<Integer> listHead = Arrays.asList(ruleHead1, ruleHead2);
        this.ruleHead = listHead;
    }
    public void setRuleHead(int ruleHead1, int ruleHead2, int ruleHead3) {
        List<Integer> listHead = Arrays.asList(ruleHead1, ruleHead2, ruleHead3);
        this.ruleHead = listHead;
    }
    public void setRuleHead(int ruleHead1, int ruleHead2, int ruleHead3, int ruleHead4) {
        List<Integer> listHead = Arrays.asList(ruleHead1, ruleHead2, ruleHead3, ruleHead4);
        this.ruleHead = listHead;
    }



}
