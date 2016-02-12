package com.yifat.finalproject;

public class Place{

    private long id;
    private String name;
    private String address;
    private double distance;
    private String url;
    private double lat;
    private double lng;

    public Place() {
    }

    public Place(String name, String address, double distance, String url, double lat, double lng) {
        setName(name);
        setAddress(address);
        setDistance(distance);
        setUrl(url);
        setLat(lat);
        setLng(lng);

    }

    public Place(long id, String name, String address, double distance, String url, double lat, double lng) {
        this(name, address, distance, url, lat, lng);
        setId(id);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        if (id > 0) {
            this.id = id;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getDistance() {
        return distance; }

    public void setDistance(double distance) {
        if (distance > 0) {
            this.distance = distance;
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        if (lat > 0) {
            this.lat = lat;
        }
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        if (lng > 0) {
            this.lng = lng;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
