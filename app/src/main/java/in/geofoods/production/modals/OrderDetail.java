package in.geofoods.production.modals;

class OrderDetail {

    Double LI,GI,CI,MV,GC,GD,CC;

    public OrderDetail() {
    }

    public OrderDetail(Double LI, Double GI, Double CI, Double MV, Double GC, Double GD, Double CC) {
        this.LI = LI;
        this.GI = GI;
        this.CI = CI;
        this.MV = MV;
        this.GC = GC;
        this.GD = GD;
        this.CC = CC;
    }

    public Double getLI() {
        return LI;
    }

    public void setLI(Double LI) {
        this.LI = LI;
    }

    public Double getGI() {
        return GI;
    }

    public void setGI(Double GI) {
        this.GI = GI;
    }

    public Double getCI() {
        return CI;
    }

    public void setCI(Double CI) {
        this.CI = CI;
    }

    public Double getMV() {
        return MV;
    }

    public void setMV(Double MV) {
        this.MV = MV;
    }

    public Double getGC() {
        return GC;
    }

    public void setGC(Double GC) {
        this.GC = GC;
    }

    public Double getGD() {
        return GD;
    }

    public void setGD(Double GD) {
        this.GD = GD;
    }

    public Double getCC() {
        return CC;
    }

    public void setCC(Double CC) {
        this.CC = CC;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "LI=" + LI +
                ", GI=" + GI +
                ", CI=" + CI +
                ", MV=" + MV +
                ", GC=" + GC +
                ", GD=" + GD +
                ", CC=" + CC +
                '}';
    }
}
