package com.tju.suma.bean;

public class DicRdfDataBean {
    private int Rs;
    private int Rp;
    private int Ro;
    private int Nsp;
    private int Np;
    private int Nop;

    public int getRs() {
        return Rs;
    }

    public void setRs(int rs) {
        Rs = rs;
    }

    public int getRp() {
        return Rp;
    }

    public void setRp(int rp) {
        Rp = rp;
    }

    public int getRo() {
        return Ro;
    }

    public void setRo(int ro) {
        Ro = ro;
    }

    public int getNsp() {
        return Nsp;
    }

    public void setNsp(int nsp) {
        Nsp = nsp;
    }

    public void setNsp(int nsp, int index) {
//        System.out.println("nsp"+nsp);
//        System.out.println("index"+index);
        if(nsp == -1){
            this.setNsp(-1);
//            System.out.println("aaaaaa");
        }
        else{
            DicRdfDataBean dataBean = DicRdfDataMap.getDataBean(nsp);
            this.setNsp(dataBean.getNsp());
//            System.out.println("qian"+index+dataBean);
            dataBean.setNsp(index);
//            System.out.println(dataBean);
        }
    }

    public void setNop(int nop, int index) {
        if(nop == -1){
            this.setNop(-1);
        }
        else{
            DicRdfDataBean dataBean = DicRdfDataMap.getDataBean(nop);
            this.setNop(dataBean.getNop());
            dataBean.setNop(index);
        }
    }

//    public int setNspFind(int nsp, Map<Integer, Integer> dicDataIndexIsp, Map<Integer, DicRdfDataBean> dicTotalData, int index) {
//        if (!dicDataIndexIsp.containsKey(nsp)){
//            dicDataIndexIsp.put(nsp,index);
//            return -1;
//        }
//        else{
//            //index_ip 找到第一条sp数据的地址
//            int index_ip = dicDataIndexIsp.get(nsp);
//            int Nsp_ip = dicTotalData.get(index_ip).getNsp();
//            dicTotalData.get(index_ip).setNsp(index);
//            return Nsp_ip;
//        }
//
//    }
//    public int setNpoFind(int npo, Map<Integer, Integer> dicDataIndexIpo, Map<Integer, DicRdfDataBean> dicTotalData, int index) {
//        if (!dicDataIndexIpo.containsKey(npo)){
//            dicDataIndexIpo.put(npo,index);
//            return -1;
//        }
//        else{
//            //index_ip 找到第一条sp数据的地址
//            int index_ip = dicDataIndexIpo.get(npo);
//            int Npo_ip = dicTotalData.get(index_ip).getNpo();
//            dicTotalData.get(index_ip).setNpo(index);
//            return Npo_ip;
//        }
//    }
    public int getNp() {
        return Np;
    }

    public void setNp(int np) {
        Np = np;
    }

    public int getNop() {
        return Nop;
    }

    public void setNop(int nop) {
        Nop = nop;
    }

    @Override
    public String toString() {
        return "RdfDataBean{" +
                "Rs='" + Rs + '\'' +
                ", Rp='" + Rp + '\'' +
                ", Ro='" + Ro + '\'' +
                ", Nsp=" + Nsp +
                ", Np=" + Np +
                ", Npo=" + Nop +
                '}';
    }


}
