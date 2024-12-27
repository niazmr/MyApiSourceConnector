package com.kafka.MyApiConnector;

import com.google.gson.annotations.SerializedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Response {
    @SerializedName("epochTimestamp")
    private long epochTimestamp; // Add this field
    private CurrentWeather weather;
    private List<SourceInfo> sources;
    private static Logger log = LoggerFactory.getLogger(Response.class);
    public List<SourceInfo> getSources(){
        return sources;
    }
//    public long getEpochTimestamp() {
//        return weather != null ? weather.getEpochTimestamp() : 0L;
//    }

    @Override
    public String toString() {
        return "Response{" +
                "weather=" + weather +
                ", sources=" + sources +
                '}';
    }
    // Getter for epochTimestamp
    public long getEpochTimestamp() {
    //    log.info("Value of getEpochTimestamp: " + epochTimestamp);
        if (this.weather != null) {
            return this.weather.getEpochTimestamp();
        }
        return 0L; // Return 0 if weather is null
    }

    // Setter for epochTimestamp (optional, depending on your JSON mapping)
//    public void setEpochTimestamp(long epochTimestamp) {
//        this.epochTimestamp = epochTimestamp;
//    }
public void setEpochTimestamp(String timestamp) {
    try {
        ZonedDateTime zdt = ZonedDateTime.parse(timestamp);
        this.epochTimestamp = zdt.toEpochSecond();
        log.info("Parsed epochTimestamp: {}", this.epochTimestamp);
    } catch (DateTimeParseException e) {
        log.error("Failed to parse epochTimestamp from: {}", timestamp, e);
        this.epochTimestamp = 0L; // Default to 0 if parsing fails
    }
}


    // Getter for current
    public CurrentWeather getCurrent() {
        return weather;
    }
    public long parseEpochTimestamp(String timestamp) {
        ZonedDateTime zdt = ZonedDateTime.parse(timestamp);
        return zdt.toEpochSecond();
    }
    // Setter for current (optional, depending on your JSON mapping)
    public void setCurrent(CurrentWeather weather) {
        this.weather = weather;
    }
    public void setSources(List<SourceInfo> sources) {
        this.sources = sources;
    }
}