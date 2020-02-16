package com.tju.suma.bean;

public class InversePropertyBean {
    int count;
    String inverseProperty;

    public String getInverseProperty() {
        return inverseProperty;
    }

    public void setInverseProperty(String inverseProperty) {
        this.inverseProperty = inverseProperty;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "InversePropertyBean{" +
                "count=" + count +
                ", inverseProperty='" + inverseProperty + '\'' +
                '}';
    }
}
