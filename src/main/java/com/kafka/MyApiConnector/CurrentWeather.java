package com.kafka.MyApiConnector;

import com.google.gson.annotations.SerializedName;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

public class CurrentWeather {
    //  @SerializedName("source_id")
    private String source_id;
    // @SerializedName("timestamp")
    private String timestamp;
    @SerializedName("cloud_cover")
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

    public String getSourceId() {
        return source_id;
    }

    public void setSourceId(String source_id) {
        this.source_id = source_id;
    }

    public int getCloudCover() {
        return cloud_cover;
    }
    public void setCloudCover(int cloud_cover) {
        this.cloud_cover = cloud_cover;
    }

    public double getDewPoint() {
        return dew_point;
    }

    public void setDewPoint(double dew_point) {
        this.dew_point = dew_point;
    }

    public Double getSolar10() {
        return solar_10;
    }

    public void setSolar10(Double solar_10) {
        this.solar_10 = solar_10;
    }

    public Double getSolar30() {
        return solar_30;
    }

    public void setSolar30(Double solar_30) {
        this.solar_30 = solar_30;
    }

    public Double getSolar60() {
        return solar_60;
    }

    public void setSolar60(Double solar_60) {
        this.solar_60 = solar_60;
    }

    public double getPrecipitation10() {
        return precipitation_10;
    }

    public void setPrecipitation10(double precipitation_10) {
        this.precipitation_10 = precipitation_10;
    }

    public double getPrecipitation30() {
        return precipitation_30;
    }

    public void setPrecipitation30(double precipitation_30) {
        this.precipitation_30 = precipitation_30;
    }

    public double getPrecipitation60() {
        return precipitation_60;
    }

    public void setPrecipitation60(double precipitation_60) {
        this.precipitation_60 = precipitation_60;
    }

    public double getPressureMsl() {
        return pressure_msl;
    }

    public void setPressureMsl(double pressure_msl) {
        this.pressure_msl = pressure_msl;
    }

    public int getRelativeHumidity() {
        return relative_humidity;
    }

    public void setRelativeHumidity(int relative_humidity) {
        this.relative_humidity = relative_humidity;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getWindDirection10() {
        return wind_direction_10;
    }

    public void setWindDirection10(int wind_direction_10) {
        this.wind_direction_10 = wind_direction_10;
    }

    public int getWindDirection30() {
        return wind_direction_30;
    }

    public void setWindDirection30(int wind_direction_30) {
        this.wind_direction_30 = wind_direction_30;
    }

    public int getWindDirection60() {
        return wind_direction_60;
    }

    public void setWindDirection60(int wind_direction_60) {
        this.wind_direction_60 = wind_direction_60;
    }

    public double getWindSpeed10() {
        return wind_speed_10;
    }

    public void setWindSpeed10(double wind_speed_10) {
        this.wind_speed_10 = wind_speed_10;
    }

    public double getWindSpeed30() {
        return wind_speed_30;
    }

    public void setWindSpeed30(double wind_speed_30) {
        this.wind_speed_30 = wind_speed_30;
    }

    public double getWindSpeed60() {
        return wind_speed_60;
    }

    public void setWindSpeed60(double wind_speed_60) {
        this.wind_speed_60 = wind_speed_60;
    }

    public int getWindGustDirection10() {
        return wind_gust_direction_10;
    }

    public void setWindGustDirection10(int wind_gust_direction_10) {
        this.wind_gust_direction_10 = wind_gust_direction_10;
    }

    public int getWindGustDirection30() {
        return wind_gust_direction_30;
    }

    public void setWindGustDirection30(int wind_gust_direction_30) {
        this.wind_gust_direction_30 = wind_gust_direction_30;
    }

    public int getWindGustDirection60() {
        return wind_gust_direction_60;
    }

    public void setWindGustDirection60(int wind_gust_direction_60) {
        this.wind_gust_direction_60 = wind_gust_direction_60;
    }

    public double windGustSpeed10() {
        return wind_gust_speed_10;
    }

    public void setWindGustSpeed10(double wind_gust_speed_10) {
        this.wind_gust_speed_10 = wind_gust_speed_10;
    }

    public double getWindGustSpeed30() {
        return wind_gust_speed_30;
    }

    public void setWindGustSpeed30(double wind_gust_speed_30) {
        this.wind_gust_speed_30 = wind_gust_speed_30;
    }

    public double getWindGustSpeed60() {
        return wind_gust_speed_60;
    }

    public void setWindGustSpeed60(double wind_gust_speed_60) {
        this.wind_gust_speed_60 = wind_gust_speed_60;
    }

    public Double getSunshine30() {
        return sunshine_30;
    }

    public void setSunshine30(Double sunshine_30) {
        this.sunshine_30 = sunshine_30;
    }

    public Double getSunshine60() {
        return sunshine_60;
    }

    public void setSunshine60(Double sunshine_60) {
        this.sunshine_60 = sunshine_60;
    }


    public long getEpochTimestamp() {
        return this.epochTimestamp;
    }


    // Setter for timestamp

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp; // Save the raw timestamp for debugging
//        if(timestamp != null) {
//            throw new IllegalArgumentException("Timestamp cannot be null or empty");
//        }
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




    // Setter for epochTimestamp
    public void setEpochTimestamp(long epochTimestamp) {
        this.epochTimestamp = epochTimestamp;
    }

    @Override
    public String toString() {
        return "CurrentWeather{" +
                "source_id=" + source_id +
                ", timestamp='" + timestamp +
                ", cloud_cover=" + cloud_cover +
                ", condition='" + condition +
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
                ", icon='" + icon +
                //  ", lastUpdated='" + lastUpdated + '\'' +
                ", epochTimestamp=" + epochTimestamp +
                '}';
    }
}
