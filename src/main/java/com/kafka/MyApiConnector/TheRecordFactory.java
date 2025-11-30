package com.kafka.MyApiConnector;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.storage.OffsetStorageReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TheRecordFactory {

    private static final Logger log = LoggerFactory.getLogger(TheRecordFactory.class);
    private final String source_id;
    private final String topic;

    public TheRecordFactory(AbstractConfig config) {
        this.source_id = config.getString(ConnectorConfig.SOURCE_ID);
        this.topic = config.getString(ConnectorConfig.TOPIC);
    }

    // Weather Schema
    private static final Schema WEATHER_SCHEMA = SchemaBuilder.struct().name("weather")
            .field("source_id", Schema.STRING_SCHEMA)
            .field("timestamp", Schema.STRING_SCHEMA)
            .field("condition", Schema.OPTIONAL_STRING_SCHEMA)
            .field("temperature", Schema.FLOAT32_SCHEMA)
            .field("icon", Schema.OPTIONAL_STRING_SCHEMA)
            .field("visibility", Schema.OPTIONAL_INT32_SCHEMA)
            .field("cloud_cover", Schema.INT32_SCHEMA)
            .field("dew_point", Schema.FLOAT64_SCHEMA)
            .field("solar_10", Schema.OPTIONAL_FLOAT64_SCHEMA)
            .field("solar_30", Schema.OPTIONAL_FLOAT64_SCHEMA)
            .field("solar_60", Schema.OPTIONAL_FLOAT64_SCHEMA)
            .field("precipitation_10", Schema.FLOAT64_SCHEMA)
            .field("precipitation_30", Schema.FLOAT64_SCHEMA)
            .field("precipitation_60", Schema.FLOAT64_SCHEMA)
            .field("pressure_msl", Schema.FLOAT64_SCHEMA)
            .field("relative_humidity", Schema.INT32_SCHEMA)
            .field("wind_direction_10", Schema.INT32_SCHEMA)
            .field("wind_direction_30", Schema.INT32_SCHEMA)
            .field("wind_direction_60", Schema.INT32_SCHEMA)
            .field("wind_speed_10", Schema.FLOAT64_SCHEMA)
            .field("wind_speed_30", Schema.FLOAT64_SCHEMA)
            .field("wind_speed_60", Schema.FLOAT64_SCHEMA)
            .field("sunshine_30", Schema.OPTIONAL_FLOAT64_SCHEMA)
            .field("sunshine_60", Schema.OPTIONAL_FLOAT64_SCHEMA)
            .field("epochTimestamp", Schema.INT64_SCHEMA)
            .build();

    // Source Schema
    private static final Schema SOURCE_SCHEMA = SchemaBuilder.struct().name("sources")
            .field("id", Schema.STRING_SCHEMA)
            .field("dwd_station_id", Schema.STRING_SCHEMA)
            .field("observation_type", Schema.STRING_SCHEMA)
            .field("station_name", Schema.STRING_SCHEMA)
            .field("lat", Schema.FLOAT32_SCHEMA)
            .field("lon", Schema.FLOAT32_SCHEMA)
            .field("wmo_station_id", Schema.STRING_SCHEMA)
            .field("height", Schema.FLOAT32_SCHEMA)
            .field("first_record", Schema.OPTIONAL_STRING_SCHEMA)
            .field("last_record", Schema.OPTIONAL_STRING_SCHEMA)
            .build();

    // Response Schema
    private static final Schema RESPONSE_SCHEMA = SchemaBuilder.struct().name("response")
            .field("Weather-Info", WEATHER_SCHEMA)
            .field("Source-Info", SOURCE_SCHEMA)
            .build();

    public Long getPersistedOffset(OffsetStorageReader offsetReader) {
        if (offsetReader == null) {
            log.debug("No offset reader available, assuming no previous offsets.");
            return 0L;
        }

        Map<String, Object> sourcePartition = createSourcePartition();
        Map<String, Object> offsetInfo = offsetReader.offset(sourcePartition);

        if (offsetInfo == null) {
            log.info("No offset found for partition: {}", sourcePartition);
            return 0L;
        }

        Object offsetObj = offsetInfo.get("offset");
        Long offset;
        if (offsetObj instanceof Number) {
            offset = ((Number) offsetObj).longValue();
        } else if (offsetObj instanceof String) {
            try {
                offset = Long.parseLong((String) offsetObj);
            } catch (NumberFormatException e) {
                log.warn("Offset string could not be parsed: {}", offsetObj);
                offset = 0L;
            }
        } else {
            offset = 0L;
        }

        log.info("Restored offset for {} is {}", sourcePartition, offset);
        return offset;
    }

    public SourceRecord createSourceRecord(Response response) {
        CurrentWeather weather = response.getCurrentWeather();
        if (weather == null) {
            log.warn("No current weather data available in response.");
            return null;
        }
        log.info("Creating source record for {}", weather);

        Struct weatherStruct = createWeatherStruct(weather);

        SourceInfo sourceInfo = (response.getSources() != null && !response.getSources().isEmpty())
                ? response.getSources().get(0)
                : null;

        Struct sourceStruct = (sourceInfo != null) ? createSourceStruct(sourceInfo) : null;

        Struct payload = new Struct(RESPONSE_SCHEMA)
                .put("Weather-Info", weatherStruct)
                .put("Source-Info", sourceStruct);
        log.info("Constructed Struct for SourceRecord: {}", payload);


        SourceRecord record = new SourceRecord(

                createSourcePartition(),
                createSourceOffset(weather),
                topic,
                RESPONSE_SCHEMA,
                payload
        );

        log.info("[TheRecordFactory] OK. Created SourceRecord => topic={}, timestamp={}, station={}",
                topic, weather.getEpochTimestamp(), weather.getSourceId());

        return record;
    }

    private Map<String, Object> createSourcePartition() {
        return Collections.singletonMap("source_id", source_id);
    }

    private Map<String, Object> createSourceOffset(CurrentWeather weather) {
        return Collections.singletonMap("offset", weather.getEpochTimestamp());
    }

    private Struct createWeatherStruct(CurrentWeather w) {
        return new Struct(WEATHER_SCHEMA)
                .put("source_id", w.getSourceId())
                .put("timestamp", w.getTimestamp())
                .put("condition", w.getCondition())
                .put("temperature", w.getTemperature())
                .put("icon", w.getIcon())
                .put("visibility", w.getVisibility())
                .put("cloud_cover", w.getCloudCover())
                .put("dew_point", w.getDewPoint())
                .put("solar_10", w.getSolar10())
                .put("solar_30", w.getSolar30())
                .put("solar_60", w.getSolar60())
                .put("precipitation_10", w.getPrecipitation10())
                .put("precipitation_30", w.getPrecipitation30())
                .put("precipitation_60", w.getPrecipitation60())
                .put("pressure_msl", w.getPressureMsl())
                .put("relative_humidity", w.getRelativeHumidity())
                .put("wind_direction_10", w.getWindDirection10())
                .put("wind_direction_30", w.getWindDirection30())
                .put("wind_direction_60", w.getWindDirection60())
                .put("wind_speed_10", w.getWindSpeed10())
                .put("wind_speed_30", w.getWindSpeed30())
                .put("wind_speed_60", w.getWindSpeed60())
                .put("sunshine_30", w.getSunshine30())
                .put("sunshine_60", w.getSunshine60())
                .put("epochTimestamp", w.getEpochTimestamp());
    }

    private Struct createSourceStruct(SourceInfo s) {
        return new Struct(SOURCE_SCHEMA)
                .put("id", s.getId())
                .put("dwd_station_id", s.getDwdStationId())
                .put("observation_type", s.getObservationType())
                .put("station_name", s.getStationName())
                .put("lat", s.getLat())
                .put("lon", s.getLon())
                .put("wmo_station_id", s.getWmoStationId())
                .put("height", s.getHeight())
                .put("first_record", s.getFirstRecord())
                .put("last_record", s.getLastRecord());
    }
}