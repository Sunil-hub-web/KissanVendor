package in.co.kissanvendor.models;

import java.util.ArrayList;

public class ProductGetSet {

    String discount, subcategoryId, totalRating, avgRating, sold, _id, addedBy, title, price, type, description, soldBy, inStock, experience, categoryId, createdAt, updatedAt;
    ArrayList<ProductImageGetSet> productimagearray;
    ArrayList<Weight_ModelClass> weight;

    public ProductGetSet(String discount, String subcategoryId, String totalRating, String avgRating,
                         String sold, String _id, String addedBy, String title, String price, String type,
                         String description, String soldBy, String inStock, String experience,
                         String categoryId, String createdAt, String updatedAt,
                         ArrayList<ProductImageGetSet> productimagearray,ArrayList<Weight_ModelClass> weight) {
        this.discount = discount;
        this.subcategoryId = subcategoryId;
        this.totalRating = totalRating;
        this.avgRating = avgRating;
        this.sold = sold;
        this._id = _id;
        this.addedBy = addedBy;
        this.title = title;
        this.price = price;
        this.type = type;
        this.description = description;
        this.soldBy = soldBy;
        this.inStock = inStock;
        this.experience = experience;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.productimagearray = productimagearray;
        this.weight = weight;
    }


    public String getDiscount() {
        return discount;
    }

    public ProductGetSet setDiscount(String discount) {
        this.discount = discount;
        return this;
    }

    public String getSubcategoryId() {
        return subcategoryId;
    }

    public ProductGetSet setSubcategoryId(String subcategoryId) {
        this.subcategoryId = subcategoryId;
        return this;
    }

    public String getTotalRating() {
        return totalRating;
    }

    public ProductGetSet setTotalRating(String totalRating) {
        this.totalRating = totalRating;
        return this;
    }

    public String getAvgRating() {
        return avgRating;
    }

    public ProductGetSet setAvgRating(String avgRating) {
        this.avgRating = avgRating;
        return this;
    }

    public String getSold() {
        return sold;
    }

    public ProductGetSet setSold(String sold) {
        this.sold = sold;
        return this;
    }

    public String get_id() {
        return _id;
    }

    public ProductGetSet set_id(String _id) {
        this._id = _id;
        return this;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public ProductGetSet setAddedBy(String addedBy) {
        this.addedBy = addedBy;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ProductGetSet setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public ProductGetSet setPrice(String price) {
        this.price = price;
        return this;
    }

    public String getType() {
        return type;
    }

    public ProductGetSet setType(String type) {
        this.type = type;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductGetSet setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getSoldBy() {
        return soldBy;
    }

    public ProductGetSet setSoldBy(String soldBy) {
        this.soldBy = soldBy;
        return this;
    }

    public String getInStock() {
        return inStock;
    }

    public ProductGetSet setInStock(String inStock) {
        this.inStock = inStock;
        return this;
    }

    public String getExperience() {
        return experience;
    }

    public ProductGetSet setExperience(String experience) {
        this.experience = experience;
        return this;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public ProductGetSet setCategoryId(String categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public ProductGetSet setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public ProductGetSet setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public ArrayList<ProductImageGetSet> getProductimagearray() {
        return productimagearray;
    }

    public ProductGetSet setProductimagearray(ArrayList<ProductImageGetSet> productimagearray) {
        this.productimagearray = productimagearray;
        return this;
    }

    public ArrayList<Weight_ModelClass> getWeight() {
        return weight;
    }

    public void setWeight(ArrayList<Weight_ModelClass> weight) {
        this.weight = weight;
    }
}
