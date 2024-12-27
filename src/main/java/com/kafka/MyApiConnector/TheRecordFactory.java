package com.kafka.MyApiConnector;

import java.util.Collections;
import java.util.Map;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.storage.OffsetStorageReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TheRecordFactory {

    private static final Logger log = LoggerFactory.getLogger(TheRecordFactory.class);
    private final String source_id;
    private final String topic;


    public TheRecordFactory(AbstractConfig config) {
        this.source_id = config.getString(ConnectorConfig.SOURCE_ID);
        this.topic = config.getString(ConnectorConfig.TOPIC);
    }


    // Schema for Weather Info
    private static final Schema WEATHER_SCHEMA = SchemaBuilder.struct().name("weather")
            .field("source_id", Schema.STRING_SCHEMA)
            .field("timestamp", Schema.STRING_SCHEMA)
           // .field("condition", Schema.STRING_SCHEMA)
            .field("condition", Schema.OPTIONAL_STRING_SCHEMA)
            .field("temperature", Schema.FLOAT32_SCHEMA)
          //  .field("icon", Schema.STRING_SCHEMA)
            .field("icon", Schema.OPTIONAL_STRING_SCHEMA)
            .build();

    // Schema for Source Info
    private static final Schema SOURCE_SCHEMA = SchemaBuilder.struct().name("sources")
            .field("id", Schema.STRING_SCHEMA)
            .field("dwd_station_id", Schema.STRING_SCHEMA)
            .field("observation_type", Schema.STRING_SCHEMA)
            .field("station_name", Schema.STRING_SCHEMA)
            .field("lat", Schema.FLOAT32_SCHEMA)
            .field("lon", Schema.FLOAT32_SCHEMA)
            .field("wmo_station_id", Schema.STRING_SCHEMA)
            .build();

    // Schema for API Response
    private static final Schema RESPONSE_SCHEMA = SchemaBuilder.struct().name("response")
            .field("Weather-Info", WEATHER_SCHEMA)
            .field("Source-Info", SOURCE_SCHEMA)
            .build();



    public Long getPersistedOffset(OffsetStorageReader offsetReader) {
        log.debug("Retrieving persisted offset for previously produced events.");

        if (offsetReader == null) {
            log.debug("No offset reader available, assuming no previous offsets.");
            return 0L;
        }

        Map<String, Object> sourcePartition = createSourcePartition();

        Map<String, Object> persistedOffsetInfo = offsetReader.offset(sourcePartition);

        if (persistedOffsetInfo == null) {
            log.debug("No persisted offset found for partition: {}", sourcePartition);
            return 0L;
        }

        Object offsetObj = persistedOffsetInfo.get("offset");
        Long offset = offsetObj instanceof Long ? (Long) offsetObj : 0L;

        log.info("Previous offset for {} is {}", sourcePartition, offset);
        return offset;
    }


    public SourceRecord createSourceRecord(Response data) {
        CurrentWeather currentWeather = data.getCurrent();
        if (currentWeather == null) {
            log.warn("No current weather data found in API response: {}", data);
            return null;
        }

        log.info("Creating SourceRecord for weather: {}", currentWeather);

        Struct weatherStruct = createWeatherStruct(currentWeather);
        SourceInfo sourceInfo = (data.getSources() != null && !data.getSources().isEmpty())
                ? data.getSources().get(0)
                : null;

        Struct sourceStruct = sourceInfo != null ? createSourceStruct(sourceInfo) : null;

        Struct responseStruct = new Struct(RESPONSE_SCHEMA)
                .put("Weather-Info", weatherStruct)
                .put("Source-Info", sourceStruct);

        log.info("Constructed Struct for SourceRecord: {}", responseStruct);

        return new SourceRecord(
                createSourcePartition(),
                createSourceOffset(currentWeather),
                topic, // supply the topic name in the connector configuration file
                RESPONSE_SCHEMA,
                responseStruct
        );
    }


    // Creates a partition map for offset management
    private Map<String, Object> createSourcePartition() {
        return Collections.singletonMap("source_id", source_id);
    }


    // Creates an offset map for the given CurrentWeather
    private Map<String, Object> createSourceOffset(CurrentWeather currentWeather) {
        return Collections.singletonMap("offset", currentWeather.getTimestamp());
    }


    private Struct createWeatherStruct(CurrentWeather currentWeather) {
        return new Struct(WEATHER_SCHEMA)
                .put("source_id", currentWeather.getSource_id())
                .put("timestamp", currentWeather.getTimestamp())
                .put("condition", currentWeather.getCondition() != null ? currentWeather.getCondition() : null)
                .put("temperature", currentWeather.getTemperature())
                .put("icon", currentWeather.getIcon() != null ? currentWeather.getIcon(): null);
    }



    private Struct createSourceStruct(SourceInfo sourceInfo) {
        return new Struct(SOURCE_SCHEMA)
                .put("id", sourceInfo.getId())
                .put("dwd_station_id", sourceInfo.getDwdStationId())
                .put("observation_type", sourceInfo.getObservationType())
                .put("station_name", sourceInfo.getStationName())
                .put("lat", sourceInfo.getLat())
                .put("lon", sourceInfo.getLon())
                .put("wmo_station_id", sourceInfo.getWmoStationId());
    }
}