package com.kafka.MyApiConnector;

import com.google.gson.annotations.SerializedName;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
//import java.util.logging.Logger;

public class CurrentWeather {
    //  @SerializedName("source_id")
    private String source_id;
    // @SerializedName("timestamp")
    private String timestamp;
    private int cloud_cover;
    private String condition;
    private double dew_point;
    private Double solar_10; // Matches "solar_10" (nullable)
    private Double solar_30; // Matches "solar_30" (nullable)
    private Double solar_60;
    private double precipitation_10; // Matches "precipitation_10"
    private double precipitation_30; // Matches "precipitation_30"
    private double precipitation_60; // Matches "precipitation_60"
    private double pressure_msl;
    private int relative_humidity; // Matches "relative_humidity"
    private int visibility; // Matches "visibility"
    private int wind_direction_10; // Matches "wind_direction_10"
    private int wind_direction_30; // Matches "wind_direction_30"
    private int wind_direction_60; // Matches "wind_direction_60"
    private double wind_speed_10; // Matches "wind_speed_10"
    private double wind_speed_30; // Matches "wind_speed_30"
    private double wind_speed_60; // Matches "wind_speed_60"
    private int wind_gust_direction_10; // Matches "wind_gust_direction_10"
    private int wind_gust_direction_30; // Matches "wind_gust_direction_30"
    private int wind_gust_direction_60; // Matches "wind_gust_direction_60"
    private double wind_gust_speed_10; // Matches "wind_gust_speed_10"
    private double wind_gust_speed_30; // Matches "wind_gust_speed_30"
    private double wind_gust_speed_60; // Matches "wind_gust_speed_60"
    private Double sunshine_30; // Matches "sunshine_30" (nullable)
    private Double sunshine_60; // Match
    private float temperature;
    private String icon;
    //  private String lastUpdated;
    private long epochTimestamp;

    private static final Logger log = LoggerFactory.getLogger(CurrentWeather.class.getName());

    // Getters
    public String getTimestamp() {
        return timestamp;
    }

    public String getCondition() {
        return condition;
    }

    public float getTemperature() {
        return temperature;
    }

    public String getIcon() {
        return icon;
    }

    public String source_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public int cloud_cover() {
        return cloud_cover;
    }

    public void setCloud_cover(int cloud_cover) {
        this.cloud_cover = cloud_cover;
    }

    public double dew_point() {
        return dew_point;
    }

    public void setDew_point(double dew_point) {
        this.dew_point = dew_point;
    }

    public Double solar_10() {
        return solar_10;
    }

    public void setSolar_10(Double solar_10) {
        this.solar_10 = solar_10;
    }

    public Double solar_30() {
        return solar_30;
    }

    public void setSolar_30(Double solar_30) {
        this.solar_30 = solar_30;
    }

    public Double solar_60() {
        return solar_60;
    }

    public void setSolar_60(Double solar_60) {
        this.solar_60 = solar_60;
    }

    public double precipitation_10() {
        return precipitation_10;
    }

    public void setPrecipitation_10(double precipitation_10) {
        this.precipitation_10 = precipitation_10;
    }

    public double precipitation_30() {
        return precipitation_30;
    }

    public void setPrecipitation_30(double precipitation_30) {
        this.precipitation_30 = precipitation_30;
    }

    public double precipitation_60() {
        return precipitation_60;
    }

    public void setPrecipitation_60(double precipitation_60) {
        this.precipitation_60 = precipitation_60;
    }

    public double pressure_msl() {
        return pressure_msl;
    }

    public void setPressure_msl(double pressure_msl) {
        this.pressure_msl = pressure_msl;
    }

    public int relative_humidity() {
        return relative_humidity;
    }

    public void setRelative_humidity(int relative_humidity) {
        this.relative_humidity = relative_humidity;
    }

    public int visibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int wind_direction_10() {
        return wind_direction_10;
    }

    public void setWind_direction_10(int wind_direction_10) {
        this.wind_direction_10 = wind_direction_10;
    }

    public int wind_direction_30() {
        return wind_direction_30;
    }

    public void setWind_direction_30(int wind_direction_30) {
        this.wind_direction_30 = wind_direction_30;
    }

    public int wind_direction_60() {
        return wind_direction_60;
    }

    public void setWind_direction_60(int wind_direction_60) {
        this.wind_direction_60 = wind_direction_60;
    }

    public double wind_speed_10() {
        return wind_speed_10;
    }

    public void setWind_speed_10(double wind_speed_10) {
        this.wind_speed_10 = wind_speed_10;
    }

    public double wind_speed_30() {
        return wind_speed_30;
    }

    public void setWind_speed_30(double wind_speed_30) {
        this.wind_speed_30 = wind_speed_30;
    }

    public double wind_speed_60() {
        return wind_speed_60;
    }

    public void setWind_speed_60(double wind_speed_60) {
        this.wind_speed_60 = wind_speed_60;
    }

    public int wind_gust_direction_10() {
        return wind_gust_direction_10;
    }

    public void setWind_gust_direction_10(int wind_gust_direction_10) {
        this.wind_gust_direction_10 = wind_gust_direction_10;
    }

    public int wind_gust_direction_30() {
        return wind_gust_direction_30;
    }

    public void setWind_gust_direction_30(int wind_gust_direction_30) {
        this.wind_gust_direction_30 = wind_gust_direction_30;
    }

    public int wind_gust_direction_60() {
        return wind_gust_direction_60;
    }

    public void setWind_gust_direction_60(int wind_gust_direction_60) {
        this.wind_gust_direction_60 = wind_gust_direction_60;
    }

    public double wind_gust_speed_10() {
        return wind_gust_speed_10;
    }

    public void setWind_gust_speed_10(double wind_gust_speed_10) {
        this.wind_gust_speed_10 = wind_gust_speed_10;
    }

    public double wind_gust_speed_30() {
        return wind_gust_speed_30;
    }

    public void setWind_gust_speed_30(double wind_gust_speed_30) {
        this.wind_gust_speed_30 = wind_gust_speed_30;
    }

    public double wind_gust_speed_60() {
        return wind_gust_speed_60;
    }

    public void setWind_gust_speed_60(double wind_gust_speed_60) {
        this.wind_gust_speed_60 = wind_gust_speed_60;
    }

    public Double sunshine_30() {
        return sunshine_30;
    }

    public void setSunshine_30(Double sunshine_30) {
        this.sunshine_30 = sunshine_30;
    }

    public Double sunshine_60() {
        return sunshine_60;
    }

    public void setSunshine_60(Double sunshine_60) {
        this.sunshine_60 = sunshine_60;
    }

    //    public String getLastUpdated() {
//        return lastUpdated;
//    }
    public long getEpochTimestamp() {
        return this.epochTimestamp;
    }


    // Setter for timestamp

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp; // Save the raw timestamp for debugging
        try {
            // Parse ISO 8601 format and convert to epoch seconds
            ZonedDateTime zdt = ZonedDateTime.parse(timestamp);
            this.epochTimestamp = zdt.toEpochSecond();
        } catch (DateTimeParseException e) {
            this.epochTimestamp = 0L; // Default to 0 if parsing fails
            System.err.println("Failed to parse timestamp: " + timestamp);
            e.printStackTrace();
        }
    }

    //    public void setTimestamp(String timestamp){ this.timestamp = timestamp; }
    // Setter for condition
    public void setCondition(String condition) {
        this.condition = condition;
    }

    // Setter for temperature
    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    // Setter for icon
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSource_id() {
        return source_id;
    }

    // Setter for lastUpdated
//    public void setLastUpdated(String lastUpdated) {
//        this.lastUpdated = lastUpdated;
//    }
//
//    // Setter for epochTimestamp
    public void setEpochTimestamp(long epochTimestamp) {
        this.epochTimestamp = epochTimestamp;
    }

    @Override
    public String toString() {
        return "CurrentWeather{" +
                "source_id=" + source_id +
                ", timestamp='" + timestamp + '\'' +
                ", cloud_cover=" + cloud_cover +
                ", condition='" + condition + '\'' +
                ", dew_point=" + dew_point +
                ", solar_10=" + solar_10 +
                ", solar_30=" + solar_30 +
                ", solar_60=" + solar_60 +
                ", precipitation_10=" + precipitation_10 +
                ", precipitation_30=" + precipitation_30 +
                ", precipitation_60=" + precipitation_60 +
                ", pressure_msl=" + pressure_msl +
                ", relative_humidity=" + relative_humidity +
                ", visibility=" + visibility +
                ", wind_direction_10=" + wind_direction_10 +
                ", wind_direction_30=" + wind_direction_30 +
                ", wind_direction_60=" + wind_direction_60 +
                ", wind_speed_10=" + wind_speed_10 +
                ", wind_speed_30=" + wind_speed_30 +
                ", wind_speed_60=" + wind_speed_60 +
                ", wind_gust_direction_10=" + wind_gust_direction_10 +
                ", wind_gust_direction_30=" + wind_gust_direction_30 +
                ", wind_gust_direction_60=" + wind_gust_direction_60 +
                ", wind_gust_speed_10=" + wind_gust_speed_10 +
                ", wind_gust_speed_30=" + wind_gust_speed_30 +
                ", wind_gust_speed_60=" + wind_gust_speed_60 +
                ", sunshine_30=" + sunshine_30 +
                ", sunshine_60=" + sunshine_60 +
                ", temperature=" + temperature +
                ", icon='" + icon + '\'' +
                //  ", lastUpdated='" + lastUpdated + '\'' +
                ", epochTimestamp=" + epochTimestamp +
                '}';
    }
}
