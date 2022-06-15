package in.co.kissanvendor.extras;

public class ImageResponse {
    private int code;
    private boolean err;
    private ImageMessage msg;

    @Override
    public String toString() {
        return "ImageResponse{" +
                "code=" + code +
                ", err=" + err +
                ", msg=" + msg +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isErr() {
        return err;
    }

    public void setErr(boolean err) {
        this.err = err;
    }

    public ImageMessage getMsg() {
        return msg;
    }

    public void setMsg(ImageMessage msg) {
        this.msg = msg;
    }
}
