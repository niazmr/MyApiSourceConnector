package com.kafka.MyApiConnector;

public class SourceInfo {
    private String id;
    private String dwd_station_id;
    private String observation_type;
    private float lat;
    private float lon;
    private float height;
    private String station_name;
    private String wmo_station_id;
    private String first_record;
    private String last_record;

    // Getters for each field
    public String getId() {
        return id;
    }

    public String getDwdStationId() {
        return dwd_station_id;
    }

    public String getObservationType() {
        return observation_type;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    public float getHeight() {
        return height;
    }

    public String getStationName() {
        return station_name;
    }

    public String getWmoStationId() {
        return wmo_station_id;
    }

    public String getFirstRecord() {
        return first_record;
    }

    public String getLastRecord() {
        return last_record;
    }



    public String dwd_station_id() {
        return dwd_station_id;
    }

    public void setDwd_station_id(String dwd_station_id) {
        this.dwd_station_id = dwd_station_id;
    }

    public String observation_type() {
        return observation_type;
    }

    public void setObservation_type(String observation_type) {
        this.observation_type = observation_type;
    }

    public float lat() {
        return lat;
    }

    public float lon() {
        return lon;
    }

    public float height() {
        return height;
    }

    public String station_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public String wmo_station_id() {
        return wmo_station_id;
    }

    public void setWmo_station_id(String wmo_station_id) {
        this.wmo_station_id = wmo_station_id;
    }

    public String first_record() {
        return first_record;
    }

    public void setFirst_record(String first_record) {
        this.first_record = first_record;
    }

    public String last_record() {
        return last_record;
    }

    public void setLast_record(String last_record) {
        this.last_record = last_record;
    }

    @Override
    public String toString() {
        return "SourceInfo{" +
                "id=" + id +
                ", dwd_station_id='" + dwd_station_id + '\'' +
                ", observation_type='" + observation_type + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", height=" + height +
                ", station_name='" + station_name + '\'' +
                ", wmo_station_id='" + wmo_station_id + '\'' +
                ", first_record='" + first_record + '\'' +
                ", last_record='" + last_record + '\'' +
                '}';
    }
}