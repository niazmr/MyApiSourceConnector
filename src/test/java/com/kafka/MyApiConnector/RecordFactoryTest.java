package com.kafka.MyApiConnector;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.storage.OffsetStorageReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

public class RecordFactoryTest {

    private RecordFactory recordFactory;
    private AbstractConfig config;

    @BeforeEach
    public void setUp() {
        config = Mockito.mock(AbstractConfig.class);
        when(config.getString(ApiSourceConnectorConfig.SOURCE_ID)).thenReturn("test-source");
        when(config.getString(ApiSourceConnectorConfig.TOPIC)).thenReturn("test-topic");
        recordFactory = new RecordFactory(config);
    }

    @Test
    public void testGetPersistedOffset() {
        OffsetStorageReader offsetReader = Mockito.mock(OffsetStorageReader.class);
        Map<String, Object> offsetMap = new HashMap<>();
        offsetMap.put("offset", 12345L);
        when(offsetReader.offset(anyMap())).thenReturn(offsetMap);

        Long offset = recordFactory.getPersistedOffset(offsetReader);
        assertEquals(12345L, offset);
    }

    @Test
    public void testGetPersistedOffsetAsString() {
        OffsetStorageReader offsetReader = Mockito.mock(OffsetStorageReader.class);
        Map<String, Object> offsetMap = new HashMap<>();
        offsetMap.put("offset", "67890");
        when(offsetReader.offset(anyMap())).thenReturn(offsetMap);

        Long offset = recordFactory.getPersistedOffset(offsetReader);
        assertEquals(67890L, offset);
    }

    @Test
    public void testCreateSourceRecord() {
        Response response = new Response();
        CurrentWeather weather = new CurrentWeather();
        weather.setSourceId("test-source");
        weather.setTimestamp("2024-11-28T23:00:00Z");
        weather.setEpochTimestamp(1732834800L);
        response.setCurrentWeather(weather);
        SourceInfo sourceInfo = new SourceInfo();
        sourceInfo.setId("test-source");
        sourceInfo.setDwdStationId("10382");
        sourceInfo.setObservationType("synop");
        sourceInfo.setStationName("Berlin");
        sourceInfo.setWmoStationId("10382");
        sourceInfo.setLat(52.52f);
        sourceInfo.setLon(13.4f);
        response.setSources(Collections.singletonList(sourceInfo));

        SourceRecord record = recordFactory.createSourceRecord(response);

        assertNotNull(record);
        assertEquals("test-topic", record.topic());
        assertEquals(Collections.singletonMap("source_id", "test-source"), record.sourcePartition());
        assertEquals(Collections.singletonMap("offset", 1732834800L), record.sourceOffset());
    }
}
