package com.kafka.MyApiConnector;
import org.apache.kafka.common.config.ConfigDef;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ApiSourceConnectorConfigTest {

    @Test
    public void testConfigDef() {
        ConfigDef configDef = ApiSourceConnectorConfig.configDef;
        assertNotNull(configDef);
        assertTrue(configDef.names().contains(ApiSourceConnectorConfig.LATITUDE));
        assertTrue(configDef.names().contains(ApiSourceConnectorConfig.LONGITUDE));
        assertTrue(configDef.names().contains(ApiSourceConnectorConfig.STATION_ID));
        assertTrue(configDef.names().contains(ApiSourceConnectorConfig.SOURCE_ID));
        assertTrue(configDef.names().contains(ApiSourceConnectorConfig.TOPIC));
        assertTrue(configDef.names().contains(ApiSourceConnectorConfig.POLLING_INTERVAL));
    }
}
