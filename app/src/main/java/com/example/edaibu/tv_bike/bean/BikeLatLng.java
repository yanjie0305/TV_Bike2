package com.example.edaibu.tv_bike.bean;

/**
 * Created by ${gyj} on 2017/10/19.
 */

public class BikeLatLng {
    private String bikeCode;
    private String status;
    private  String lat;
    private  String lng;


    public BikeLatLng(String bikeCode,String status, String lat, String lng) {
        this.bikeCode = bikeCode;
        this.lat = lat;
        this.lng = lng;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBikeCode() {
        return bikeCode;
    }

    public void setBikeCode(String bikeCode) {
        this.bikeCode = bikeCode;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "BikeLatLng{" +
                "bikeCode='" + bikeCode + '\'' +
                ", status='" + status + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +

                '}';
    }
}
