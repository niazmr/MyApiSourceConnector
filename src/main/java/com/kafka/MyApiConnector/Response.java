package com.kafka.MyApiConnector;

import com.google.gson.annotations.SerializedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

public class Response {

    private static final Logger log = LoggerFactory.getLogger(Response.class);

    @SerializedName("weather")
    private CurrentWeather currentWeather;

    @SerializedName("sources")
    private List<SourceInfo> sources;

    private long epochTimestamp;

    // === Getters ===

    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

    public List<SourceInfo> getSources() {
        return sources;
    }

    public long getEpochTimestamp() {
        if (epochTimestamp != 0L) {
            return epochTimestamp;
        }
        return currentWeather != null ? currentWeather.getEpochTimestamp() : 0L;
    }

    public String getCurrentStationId() {
        return currentWeather != null ? Objects.requireNonNullElse(currentWeather.getSourceId(), "") : "";
    }

    public String getStationIdSafe() {
        return getCurrentStationId().isEmpty() ? "unknown" : getCurrentStationId();
    }

    // === Setters ===

    public void setCurrentWeather(CurrentWeather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public void setSources(List<SourceInfo> sources) {
        this.sources = sources;
    }

    public void setEpochTimestamp(String timestamp) {
        try {
            ZonedDateTime zdt = ZonedDateTime.parse(timestamp);
            this.epochTimestamp = zdt.toEpochSecond();
            log.info("Parsed epochTimestamp: {}", this.epochTimestamp);
        } catch (DateTimeParseException e) {
            log.error("Failed to parse epochTimestamp from: {}", timestamp, e);
            this.epochTimestamp = 0L;
        }
    }

    public void setEpochTimestamp(long timestamp) {
        this.epochTimestamp = timestamp;
    }

    // === Utility Methods ===

    @Override
    public String toString() {
        return "Response{" +
                "currentWeather=" + currentWeather +
                ", sources=" + sources +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Response)) return false;
        Response other = (Response) obj;
        return this.getEpochTimestamp() == other.getEpochTimestamp()
                && this.getCurrentStationId().equals(other.getCurrentStationId());
    }

    @Override
    public int hashCode() {
        long ts = getEpochTimestamp();
        int tsHash = (int) (ts ^ (ts >>> 32));
        return 31 * tsHash + getCurrentStationId().hashCode();
    }
}