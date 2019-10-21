package com.tju.gowl.bean;

public class IndexBeanString {
    private int firstTriple;
    private int lastTriple;

    public IndexBeanString(int firstTriple, int lastTriple) {
        this.firstTriple = firstTriple;
        this.lastTriple = lastTriple;
    }

    public int getFirstTriple() {
        return firstTriple;
    }

    public void setFirstTriple(int firstTriple) {
        this.firstTriple = firstTriple;
    }

    public int getLastTriple() {
        return lastTriple;
    }

    public void setLastTriple(int lastTriple) {
        this.lastTriple = lastTriple;
    }
}
