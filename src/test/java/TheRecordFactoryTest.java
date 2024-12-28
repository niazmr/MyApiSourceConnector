
//package com.kafka.MyApiConnector;

import com.kafka.MyApiConnector.*;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.connect.storage.OffsetStorageReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*; // JUnit 5 assertions
import static org.mockito.Mockito.*; // Mockito for mocking

class TheRecordFactoryTest {

    private TheRecordFactory recordFactory;
    private AbstractConfig config;
    private OffsetStorageReader offsetReader;

    @BeforeEach // Replaces @Before in JUnit 4
    void setUp() {
        // Mock AbstractConfig
        config = Mockito.mock(AbstractConfig.class);
        when(config.getString(ConnectorConfig.SOURCE_ID)).thenReturn("test-source-id");
        when(config.getString(ConnectorConfig.TOPIC)).thenReturn("test-topic");

        // Initialize TheRecordFactory
        recordFactory = new TheRecordFactory(config);

        // Mock OffsetStorageReader
        offsetReader = Mockito.mock(OffsetStorageReader.class);
    }

    @Test
    void testCreateSourceRecord() {
        // Create test weather data
        CurrentWeather weather = new CurrentWeather();
        weather.setSourceId("test-source-id");
        weather.setTimestamp("2024-12-27T21:30:00+01:00");
        weather.setTemperature(20.5f);
        weather.setCondition("Clear");
        weather.setIcon("sunny");
        weather.setCloudCover(0);

        // Create test source info
        SourceInfo sourceInfo = new SourceInfo();
        sourceInfo.setId("source-1");
        sourceInfo.setDwdStationId("dwd-1234");
        sourceInfo.setObservationType("automated");
        sourceInfo.setStationName("Test Station");
        sourceInfo.setLat(52.5200f);
        sourceInfo.setLon(13.4050f);
        sourceInfo.setWmoStationId("WMO-1234");
        sourceInfo.setHeight(34.5f);

        // Set weather and source info in the response
        Response response = new Response();
        response.setCurrent(weather);
        response.setSources(Collections.singletonList(sourceInfo)); // Add sources

        // Create source record
        SourceRecord record = recordFactory.createSourceRecord(response);

        // Assertions
        assertNotNull(record); // Ensure record is not null
        assertEquals("test-topic", record.topic()); // Verify topic
        assertEquals("test-source-id", record.sourcePartition().get("source_id"));

        // Verify weather data
        Struct weatherStruct = ((Struct) record.value()).getStruct("Weather-Info");
        assertNotNull(weatherStruct);
        assertEquals("test-source-id", weatherStruct.getString("source_id"));
        assertEquals("Clear", weatherStruct.getString("condition"));
        assertEquals(20.5f, weatherStruct.getFloat32("temperature"));

        // Verify source info
        Struct sourceStruct = ((Struct) record.value()).getStruct("Source-Info");
        assertNotNull(sourceStruct);
        assertEquals("source-1", sourceStruct.getString("id"));
        assertEquals("dwd-1234", sourceStruct.getString("dwd_station_id"));
        assertEquals("automated", sourceStruct.getString("observation_type"));
    }

    @Test
    void testGetPersistedOffset() {
        // Mock offset retrieval
        Map<String, Object> persistedOffset = Collections.singletonMap("offset", 12345L);
        when(offsetReader.offset(anyMap())).thenReturn(persistedOffset);

        Long offset = recordFactory.getPersistedOffset(offsetReader);

        // Assertions
        assertEquals(12345L, offset);
    }

    @Test
    void testGetPersistedOffsetNoOffset() {
        when(offsetReader.offset(anyMap())).thenReturn(null); // No offset
        Long offset = recordFactory.getPersistedOffset(offsetReader);
        assertEquals(0L, offset); // Should default to 0
    }
    @Test
    void testValidTimestampWithZone() {
        CurrentWeather weather = new CurrentWeather();
        weather.setTimestamp("2024-12-27T21:30:00Z"); // UTC time
        assertTrue(weather.getEpochTimestamp() > 0);
    }

    @Test
    void testValidTimestamp() {
        CurrentWeather weather = new CurrentWeather();
        weather.setTimestamp("2024-12-27T21:30:00+01:00"); // Valid ISO-8601 format
        assertTrue(weather.getEpochTimestamp() > 0);
    }
    @Test
    void testDefaultTimestamp() {
        CurrentWeather weather = new CurrentWeather();
        weather.setTimestamp("1970-01-01T00:00:00Z"); // Default UNIX epoch time
        assertEquals(0L, weather.getEpochTimestamp());
    }

}