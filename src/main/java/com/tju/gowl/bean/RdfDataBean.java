package com.tju.gowl.bean;

public class RdfDataBean {
    private String Rs;
    private String Rp;
    private String Ro;
    private int Nsp;
    private int Np;
    private int Npo;

    public String getRs() {
        return Rs;
    }

    public void setRs(String rs) {
        Rs = rs;
    }

    public String getRp() {
        return Rp;
    }

    public void setRp(String rp) {
        Rp = rp;
    }

    public String getRo() {
        return Ro;
    }

    public void setRo(String ro) {
        Ro = ro;
    }

    public int getNsp() {
        return Nsp;
    }

    public void setNsp(int nsp) {
        Nsp = nsp;
    }

    public int getNp() {
        return Np;
    }

    public void setNp(int np) {
        Np = np;
    }

    public int getNpo() {
        return Npo;
    }

    public void setNpo(int npo) {
        Npo = npo;
    }

    @Override
    public String toString() {
        return "RdfDataBean{" +
                "Rs='" + Rs + '\'' +
                ", Rp='" + Rp + '\'' +
                ", Ro='" + Ro + '\'' +
                ", Nsp=" + Nsp +
                ", Np=" + Np +
                ", Nop=" + Npo +
                '}';
    }
}
