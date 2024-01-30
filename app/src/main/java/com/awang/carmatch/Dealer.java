package com.awang.carmatch;

public class Dealer {
    private String id;
    private String name;
    private String latitude;
    private String longitude;

    public Dealer() {}

    public Dealer(String name, String latitude, String longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
