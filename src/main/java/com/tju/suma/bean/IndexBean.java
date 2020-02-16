package com.tju.suma.bean;

public class IndexBean {
    private int resource;
    private int index;

    public int getResource() {
        return resource;
    }

    public void setResource(int rs) {
        resource = rs;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int ip) {
        index = ip;
    }

    @Override
    public String toString() {
        return "IndexBean{" +
                "resource=" + resource +
                ", index=" + index +
                '}';
    }
//    public int getNext() {
//        return next;
//    }
//
//    public void setNext(int nt) {
//        next = nt;
//    }


}
