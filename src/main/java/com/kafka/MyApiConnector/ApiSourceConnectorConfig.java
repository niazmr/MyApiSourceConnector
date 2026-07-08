package com.kafka.MyApiConnector;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;

public class ApiSourceConnectorConfig {
    public static final String STATION_ID = "dwd_station_id";
    public static final String SOURCE_ID = "source_id";
    public static final String WMO_ID = "wmo_station_id";
    public static final String POLLING_INTERVAL = "polling_interval";
    public static final String TOPIC = "kafka.topic";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String DISABLE_OFFSET_CHECK = "disable.offset.check";

    public static final ConfigDef configDef = new ConfigDef()
            .define(LATITUDE, ConfigDef.Type.STRING, null, Importance.LOW, "Latitude")
            .define(LONGITUDE, ConfigDef.Type.STRING, null, Importance.LOW, "Longitude")
            .define(STATION_ID, ConfigDef.Type.STRING, null, Importance.LOW, "DWD Station ID")
            .define(WMO_ID, ConfigDef.Type.STRING, null, Importance.LOW, "WMO Station ID")
            .define(SOURCE_ID, ConfigDef.Type.STRING, Importance.HIGH, "Source ID")
            .define(TOPIC, ConfigDef.Type.STRING, Importance.HIGH, "Kafka topic")
            .define(POLLING_INTERVAL, ConfigDef.Type.INT, 60, Importance.MEDIUM, "Polling interval in seconds")
            .define(DISABLE_OFFSET_CHECK, ConfigDef.Type.BOOLEAN, false, Importance.LOW, "Disable offset check for testing");
}