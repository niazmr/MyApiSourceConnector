package com.kafka.MyApiConnector;

import org.apache.kafka.common.config.AbstractConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class DataFetcherTest {

    private AbstractConfig config;

    @BeforeEach
    public void setUp() {
        config = Mockito.mock(AbstractConfig.class);
        when(config.getString("latitude")).thenReturn("52.52");
        when(config.getString("longitude")).thenReturn("13.4");
        when(config.getInt("polling_interval")).thenReturn(60);
        when(config.getBoolean("disable.offset.check")).thenReturn(false);
    }

    @Test
    public void testBuildUrlWithLatLon() {
        DataFetcher fetcher = new DataFetcher(config, 0L);
        String url = fetcher.buildUrl();
        assertTrue(url.contains("lat=52.52"));
        assertTrue(url.contains("lon=13.4"));
    }

    @Test
    public void testBuildUrlWithStationId() {
        when(config.getString("dwd_station_id")).thenReturn("10382");
        DataFetcher fetcher = new DataFetcher(config, 0L);
        String url = fetcher.buildUrl();
        assertTrue(url.contains("dwd_station_id=10382"));
    }

    @Test
    public void testFetchOnce() throws IOException {
        String json = "{" +
                "  \"weather\": {" +
                "    \"source_id\": \"12345\"," +
                "    \"timestamp\": \"2024-11-28T23:00:00Z\"" +
                "  }" +
                "}";

        DataFetcher fetcher = new DataFetcher(config, 0L) {
            @Override
            protected Reader getApiReader() {
                return new StringReader(json);
            }
        };

        List<Response> responses = fetcher.fetchOnce();
        assertEquals(1, responses.size());
        assertEquals("12345", responses.get(0).getCurrentWeather().getSourceId());
    }

    @Test
    public void testDuplicateFiltering() throws IOException {
        String json = "{" +
                "  \"weather\": {" +
                "    \"source_id\": \"12345\"," +
                "    \"timestamp\": \"2024-11-28T23:00:00Z\"" +
                "  }" +
                "}";

        DataFetcher fetcher = new DataFetcher(config, 1732834800L) { // 2024-11-28T23:00:00Z is 1732834800
            @Override
            protected Reader getApiReader() {
                return new StringReader(json);
            }
        };

        List<Response> responses = fetcher.fetchOnce();
        assertEquals(0, responses.size(), "Should skip record with timestamp equal to offset");
    }
}
