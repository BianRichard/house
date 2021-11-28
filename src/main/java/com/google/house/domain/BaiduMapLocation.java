package com.google.house.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaiduMapLocation {

    //经度
    @JsonProperty("lon") //返回json的属性名
    private double longitude;
    //纬度
    @JsonProperty("lat")
    private double latitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
