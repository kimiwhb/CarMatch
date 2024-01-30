package com.awang.carmatch;

public class CarClass {

    private String carModel;
    private String carBrand;
    private String carCategory;
    private String carPrice;
    private String imageURL;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarCategory() {
        return carCategory;
    }

    public void setCarCategory(String carCategory) {
        this.carCategory = carCategory;
    }

    public String getCarPrice() {
        return carPrice;
    }

    public void setCarPrice(String carPrice) {
        this.carPrice = carPrice;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public CarClass(String carModel, String carBrand, String carCategory, String carPrice, String imageURL) {
        this.carModel = carModel;
        this.carBrand = carBrand;
        this.carCategory = carCategory;
        this.carPrice = carPrice;
        this.imageURL = imageURL;
    }

    public CarClass() {

    }
}
