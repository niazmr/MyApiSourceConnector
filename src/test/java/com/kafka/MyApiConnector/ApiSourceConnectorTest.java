package com.kafka.MyApiConnector;

import org.apache.kafka.connect.connector.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiSourceConnectorTest {

    private ApiSourceConnector connector;

    @BeforeEach
    public void setUp() {
        connector = new ApiSourceConnector();
    }

    @Test
    public void testVersion() {
        assertEquals("0.0.1", connector.version());
    }

    @Test
    public void testTaskClass() {
        assertEquals(ApiSourceTask.class, connector.taskClass());
    }

    @Test
    public void testConfig() {
        assertEquals(ApiSourceConnectorConfig.configDef, connector.config());
    }

    @Test
    public void testTaskConfigs() {
        Map<String, String> props = new HashMap<>();
        props.put("prop1", "val1");
        connector.start(props);
        List<Map<String, String>> taskConfigs = connector.taskConfigs(2);
        assertEquals(1, taskConfigs.size());
        assertEquals("val1", taskConfigs.get(0).get("prop1"));
    }
}
