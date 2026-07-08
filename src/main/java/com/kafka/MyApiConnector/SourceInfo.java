package com.kafka.MyApiConnector;

import com.google.gson.annotations.SerializedName;

public class SourceInfo {
    private String id;
    @SerializedName("dwd_station_id")
    private String dwdStationId;
    @SerializedName("observation_type")
    private String observationType;
    private float lat;
    private float lon;
    private float height;
    @SerializedName("station_name")
    private String stationName;
    @SerializedName("wmo_station_id")
    private String wmoStationId;
    @SerializedName("first_record")
    private String firstRecord;
    @SerializedName("last_record")
    private String lastRecord;

    // Getters for each field
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDwdStationId() {
        return dwdStationId;
    }

    public String getObservationType() {
        return observationType;
    }

    public float getLat() {
        return lat;
    }
    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }
    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getHeight() {
        return height;
    }
    public void setHeight(float height) {
        this.height = height;
    }
    public String getStationName() {
        return stationName;
    }

    public String getWmoStationId() {
        return wmoStationId;
    }

    public String getFirstRecord() {
        return firstRecord;
    }

    public String getLastRecord() {
        return lastRecord;
    }
    public void setDwdStationId(String dwdStationId) {
        this.dwdStationId = dwdStationId;
    }

    public void setObservationType(String observationType) {
        this.observationType = observationType;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }


    public void setWmoStationId(String wmoStationId) {
        this.wmoStationId = wmoStationId;
    }

    public void setFirstRecord(String firstRecord) {
        this.firstRecord = firstRecord;
    }

    public void setLastRecord(String lastRecord) {
        this.lastRecord = lastRecord;
    }

    @Override
    public String toString() {
        return "SourceInfo {" +
                "id= " + id +
                ", dwdStationId='" + dwdStationId + '\'' +
                ", observationType='" + observationType + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", height=" + height +
                ", stationName='" + stationName + '\'' +
                ", wmoStationId='" + wmoStationId + '\'' +
                ", firstRecord='" + firstRecord + '\'' +
                ", lastRecord='" + lastRecord + '\'' +
                '}';
    }
}