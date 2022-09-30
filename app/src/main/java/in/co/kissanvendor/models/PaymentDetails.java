package in.co.kissanvendor.models;

public class PaymentDetails {

    String productid,productname,statues,cramount,type,estdate;

    public PaymentDetails(String productid, String productname, String statues, String cramount, String type, String estdate) {
        this.productid = productid;
        this.productname = productname;
        this.statues = statues;
        this.cramount = cramount;
        this.type = type;
        this.estdate = estdate;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getStatues() {
        return statues;
    }

    public void setStatues(String statues) {
        this.statues = statues;
    }

    public String getCramount() {
        return cramount;
    }

    public void setCramount(String cramount) {
        this.cramount = cramount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEstdate() {
        return estdate;
    }

    public void setEstdate(String estdate) {
        this.estdate = estdate;
    }
}
