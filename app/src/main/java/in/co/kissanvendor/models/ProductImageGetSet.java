package in.co.kissanvendor.models;

public class ProductImageGetSet {

    String imageurl;

    public ProductImageGetSet(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getImageurl() {
        return imageurl;
    }

    public ProductImageGetSet setImageurl(String imageurl) {
        this.imageurl = imageurl;
        return this;
    }
}
