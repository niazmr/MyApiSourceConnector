

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
            .field("condition", Schema.OPTIONAL_STRING_SCHEMA)
            .field("temperature", Schema.FLOAT32_SCHEMA)
            .field("icon", Schema.OPTIONAL_STRING_SCHEMA)
            .field("visibility", Schema.OPTIONAL_INT32_SCHEMA)
            .field("cloud_cover", Schema.INT32_SCHEMA) // Add this
            .field("dew_point", Schema.FLOAT64_SCHEMA) // Add this
            .field("solar_10", Schema.OPTIONAL_FLOAT64_SCHEMA) // Add this
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
            .field("epochTimestamp", Schema.INT64_SCHEMA) // Add this
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
            .field("height", Schema.FLOAT32_SCHEMA)
            .field("first_record", Schema.OPTIONAL_STRING_SCHEMA)
            .field("last_record", Schema.OPTIONAL_STRING_SCHEMA)
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
                .put("source_id", currentWeather.getSourceId())
                .put("timestamp", currentWeather.getTimestamp())
                .put("condition", currentWeather.getCondition() != null ? currentWeather.getCondition() : null)
                .put("temperature", currentWeather.getTemperature())
                .put("icon", currentWeather.getIcon() != null ? currentWeather.getIcon(): null)
                .put("cloud_cover", currentWeather.getCloudCover())
                .put("dew_point", currentWeather.getDewPoint())
                .put("solar_10", currentWeather.getSolar10())
                .put("solar_30", currentWeather.getSolar30())
                .put("solar_60", currentWeather.getSolar60())
                .put("precipitation_10", currentWeather.getPrecipitation10())
                .put("precipitation_30", currentWeather.getPrecipitation30())
                .put("precipitation_60", currentWeather.getPrecipitation60())
                .put("pressure_msl", currentWeather.getPressureMsl())
                .put("relative_humidity", currentWeather.getRelativeHumidity())
                .put("visibility", currentWeather.getVisibility())
                .put("wind_direction_10", currentWeather.getWindDirection10())
                .put("wind_direction_30", currentWeather.getWindDirection30())
                .put("wind_direction_60", currentWeather.getWindDirection60())
                .put("wind_speed_10", currentWeather.getWindSpeed10())
                .put("wind_speed_30", currentWeather.getWindSpeed30())
                .put("wind_speed_60", currentWeather.getWindSpeed60())
                .put("sunshine_30", currentWeather.getSunshine30())
                .put("sunshine_60", currentWeather.getSunshine60())
                .put("epochTimestamp", currentWeather.getEpochTimestamp());
    }



    private Struct createSourceStruct(SourceInfo sourceInfo) {
        return new Struct(SOURCE_SCHEMA)
                .put("id", sourceInfo.getId())
                .put("dwd_station_id", sourceInfo.getDwdStationId())
                .put("observation_type", sourceInfo.getObservationType())
                .put("station_name", sourceInfo.getStationName())
                .put("lat", sourceInfo.getLat())
                .put("lon", sourceInfo.getLon())
                .put("wmo_station_id", sourceInfo.getWmoStationId())
                .put("height", sourceInfo.getHeight())
                .put("first_record", sourceInfo.getFirstRecord())
                .put("last_record", sourceInfo.getLastRecord());
    }
}