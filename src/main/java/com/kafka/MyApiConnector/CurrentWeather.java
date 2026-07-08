package com.kafka.MyApiConnector;

import com.google.gson.annotations.SerializedName;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

public class CurrentWeather {
    @SerializedName("source_id")
    private String sourceId;
    @SerializedName("timestamp")
    private String timestamp;
    @SerializedName("cloud_cover")
    private int cloudCover;
    private String condition;
    @SerializedName("dew_point")
    private double dewPoint;
    @SerializedName("solar_10")
    private Double solar10;
    @SerializedName("solar_30")
    private Double solar30;
    @SerializedName("solar_60")
    private Double solar60;
    @SerializedName("precipitation_10")
    private double precipitation10;
    @SerializedName("precipitation_30")
    private double precipitation30;
    @SerializedName("precipitation_60")
    private double precipitation60;
    @SerializedName("pressure_msl")
    private double pressureMsl;
    @SerializedName("relative_humidity")
    private int relativeHumidity;
    private int visibility;
    @SerializedName("wind_direction_10")
    private int windDirection10;
    @SerializedName("wind_direction_30")
    private int windDirection30;
    @SerializedName("wind_direction_60")
    private int windDirection60;
    @SerializedName("wind_speed_10")
    private double windSpeed10;
    @SerializedName("wind_speed_30")
    private double windSpeed30;
    @SerializedName("wind_speed_60")
    private double windSpeed60;
    @SerializedName("wind_gust_direction_10")
    private int windGustDirection10;
    @SerializedName("wind_gust_direction_30")
    private int windGustDirection30;
    @SerializedName("wind_gust_direction_60")
    private int windGustDirection60;
    @SerializedName("wind_gust_speed_10")
    private double windGustSpeed10;
    @SerializedName("wind_gust_speed_30")
    private double windGustSpeed30;
    @SerializedName("wind_gust_speed_60")
    private double windGustSpeed60;
    @SerializedName("sunshine_30")
    private Double sunshine30;
    @SerializedName("sunshine_60")
    private Double sunshine60;
    private float temperature;
    private String icon;
    private long epochTimestamp;

    private static final Logger log = LoggerFactory.getLogger(CurrentWeather.class);

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
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public int getCloudCover() {
        return cloudCover;
    }
    public void setCloudCover(int cloudCover) {
        this.cloudCover = cloudCover;
    }

    public double getDewPoint() {
        return dewPoint;
    }

    public void setDewPoint(double dewPoint) {
        this.dewPoint = dewPoint;
    }

    public Double getSolar10() {
        return solar10;
    }

    public void setSolar10(Double solar10) {
        this.solar10 = solar10;
    }

    public Double getSolar30() {
        return solar30;
    }

    public void setSolar30(Double solar30) {
        this.solar30 = solar30;
    }

    public Double getSolar60() {
        return solar60;
    }

    public void setSolar60(Double solar60) {
        this.solar60 = solar60;
    }

    public double getPrecipitation10() {
        return precipitation10;
    }

    public void setPrecipitation10(double precipitation10) {
        this.precipitation10 = precipitation10;
    }

    public double getPrecipitation30() {
        return precipitation30;
    }

    public void setPrecipitation30(double precipitation30) {
        this.precipitation30 = precipitation30;
    }

    public double getPrecipitation60() {
        return precipitation60;
    }

    public void setPrecipitation60(double precipitation60) {
        this.precipitation60 = precipitation60;
    }

    public double getPressureMsl() {
        return pressureMsl;
    }

    public void setPressureMsl(double pressureMsl) {
        this.pressureMsl = pressureMsl;
    }

    public int getRelativeHumidity() {
        return relativeHumidity;
    }

    public void setRelativeHumidity(int relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getWindDirection10() {
        return windDirection10;
    }

    public void setWindDirection10(int windDirection10) {
        this.windDirection10 = windDirection10;
    }

    public int getWindDirection30() {
        return windDirection30;
    }

    public void setWindDirection30(int windDirection30) {
        this.windDirection30 = windDirection30;
    }

    public int getWindDirection60() {
        return windDirection60;
    }

    public void setWindDirection60(int windDirection60) {
        this.windDirection60 = windDirection60;
    }

    public double getWindSpeed10() {
        return windSpeed10;
    }

    public void setWindSpeed10(double windSpeed10) {
        this.windSpeed10 = windSpeed10;
    }

    public double getWindSpeed30() {
        return windSpeed30;
    }

    public void setWindSpeed30(double windSpeed30) {
        this.windSpeed30 = windSpeed30;
    }

    public double getWindSpeed60() {
        return windSpeed60;
    }

    public void setWindSpeed60(double windSpeed60) {
        this.windSpeed60 = windSpeed60;
    }

    public int getWindGustDirection10() {
        return windGustDirection10;
    }

    public void setWindGustDirection10(int windGustDirection10) {
        this.windGustDirection10 = windGustDirection10;
    }

    public int getWindGustDirection30() {
        return windGustDirection30;
    }

    public void setWindGustDirection30(int windGustDirection30) {
        this.windGustDirection30 = windGustDirection30;
    }

    public int getWindGustDirection60() {
        return windGustDirection60;
    }

    public void setWindGustDirection60(int windGustDirection60) {
        this.windGustDirection60 = windGustDirection60;
    }

    public double getWindGustSpeed10() {
        return windGustSpeed10;
    }

    public void setWindGustSpeed10(double windGustSpeed10) {
        this.windGustSpeed10 = windGustSpeed10;
    }

    public double getWindGustSpeed30() {
        return windGustSpeed30;
    }

    public void setWindGustSpeed30(double windGustSpeed30) {
        this.windGustSpeed30 = windGustSpeed30;
    }

    public double getWindGustSpeed60() {
        return windGustSpeed60;
    }

    public void setWindGustSpeed60(double windGustSpeed60) {
        this.windGustSpeed60 = windGustSpeed60;
    }

    public Double getSunshine30() {
        return sunshine30;
    }

    public void setSunshine30(Double sunshine30) {
        this.sunshine30 = sunshine30;
    }

    public Double getSunshine60() {
        return sunshine60;
    }

    public void setSunshine60(Double sunshine60) {
        this.sunshine60 = sunshine60;
    }


    public long getEpochTimestamp() {
        if (epochTimestamp != 0L) {
            log.debug("Returning cached epochTimestamp: {}", epochTimestamp);
            return epochTimestamp;
        }

        if (timestamp != null && !timestamp.isEmpty()) {
            log.info("Attempting to parse timestamp field: '{}'", timestamp);
            try {
                epochTimestamp = ZonedDateTime.parse(timestamp).toEpochSecond();
                log.info("Parsed epochTimestamp from '{}' = {}", timestamp, epochTimestamp);
            } catch (DateTimeParseException e) {
                log.error("Failed to parse timestamp '{}'", timestamp, e);
            }
        } else {
            log.warn("Timestamp is null or empty. JSON mapping may have failed.");
        }

        return epochTimestamp;
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
                "sourceId=" + sourceId +
                ", timestamp='" + timestamp +
                ", cloudCover=" + cloudCover +
                ", condition='" + condition +
                ", dewPoint=" + dewPoint +
                ", solar10=" + solar10 +
                ", solar30=" + solar30 +
                ", solar60=" + solar60 +
                ", precipitation10=" + precipitation10 +
                ", precipitation30=" + precipitation30 +
                ", precipitation60=" + precipitation60 +
                ", pressureMsl=" + pressureMsl +
                ", relativeHumidity=" + relativeHumidity +
                ", visibility=" + visibility +
                ", windDirection10=" + windDirection10 +
                ", windDirection30=" + windDirection30 +
                ", windDirection60=" + windDirection60 +
                ", windSpeed10=" + windSpeed10 +
                ", windSpeed30=" + windSpeed30 +
                ", windSpeed60=" + windSpeed60 +
                ", windGustDirection10=" + windGustDirection10 +
                ", windGustDirection30=" + windGustDirection30 +
                ", windGustDirection60=" + windGustDirection60 +
                ", windGustSpeed10=" + windGustSpeed10 +
                ", windGustSpeed30=" + windGustSpeed30 +
                ", windGustSpeed60=" + windGustSpeed60 +
                ", sunshine30=" + sunshine30 +
                ", sunshine60=" + sunshine60 +
                ", temperature=" + temperature +
                ", icon='" + icon +
                ", epochTimestamp=" + epochTimestamp +
                '}';
    }
}
