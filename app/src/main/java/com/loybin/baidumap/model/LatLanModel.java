package com.loybin.baidumap.model;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/10/17 下午3:38
 * 描   述: 地址 经纬度 model
 */
public class LatLanModel {


    /**
     * address : 广东省深圳市南山区科苑北路靠近杭州银行(深圳科技支行)
     * lng : 113.9448722
     * lat : 113.9448722
     * deviceId : 192
     */

    private String address;
    private double lng;
    private double lat;
    private String deviceId;
    private String locationTime;

    public String getLocationTime() {
        return locationTime;
    }

    public void setLocationTime(String locationTime) {
        this.locationTime = locationTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
