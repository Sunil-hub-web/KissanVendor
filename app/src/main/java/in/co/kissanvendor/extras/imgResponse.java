package in.co.kissanvendor.extras;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class imgResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("err")
    @Expose
    private Boolean err;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private Data data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getErr() {
        return err;
    }

    public void setErr(Boolean err) {
        this.err = err;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
