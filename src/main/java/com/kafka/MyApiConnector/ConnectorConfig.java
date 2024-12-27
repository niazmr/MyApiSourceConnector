package com.kafka.MyApiConnector;


import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;

public class ConnectorConfig {
    public static final String STATION_ID = "dwd_station_id";
    public static final String SOURCE_ID = "source_id";
    public static final String WMO_ID = "wmo_station_id";
    public static final String POLLING_INTERVAL = "polling_interval";
    public static final String TOPIC  = "kafka.topic";

    public static final ConfigDef configDef = new ConfigDef()
            .define(STATION_ID, Type.STRING, null, Importance.HIGH, "Station ID for the weather data")
            .define(SOURCE_ID, Type.STRING, "", Importance.LOW, "Source ID for additional metadata")
            .define(WMO_ID, Type.STRING, "", Importance.LOW, "WMO station ID for the weather data")
            .define(POLLING_INTERVAL, Type.STRING, "60", Importance.LOW, "Polling interval in seconds")
            .define(TOPIC, Type.STRING,"", Importance.HIGH, "Kafka topic for the weather data");
}
