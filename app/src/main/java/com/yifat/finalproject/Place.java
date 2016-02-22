package com.yifat.finalproject;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Place implements Parcelable {

    private long id;
    private String name;
    private String address;
    private double distance;
    private String url;
    private double lat;
    private double lng;
    public Bitmap placeImage;

    public Place() {
    }

    public Place(Parcel input) {
        id = input.readLong();
        name = input.readString();
        address = input.readString();
        distance = input.readDouble();
        url = input.readString();
        lat = input.readDouble();
        lng = input.readDouble();
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
        return distance;
    }

    public void setDistance(double distance) {
        if (distance > 0) {
            this.distance = distance;
        }
    }

    public String getUrl() {

        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeDouble(distance);
        dest.writeString(url);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
    }

    public static final Parcelable.Creator<Place> CREATOR
            = new Parcelable.Creator<Place>() {
        public Place createFromParcel(Parcel in) {
            Log.d("Place", "create from parcel: Place");
            return new Place(in);
        }

        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

}
