package in.co.kissanvendor.models;

import java.io.Serializable;

public class ProductImageGetSet implements Serializable {

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

    @Override
    public String toString() {
        return "ProductImageGetSet{" +
                "imageurl='" + imageurl + '\'' +
                '}';
    }
}
