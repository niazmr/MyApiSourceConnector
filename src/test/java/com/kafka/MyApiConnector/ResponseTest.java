package com.kafka.MyApiConnector;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResponseTest {

    @Test
    public void testGsonMapping() {
        String json = "{" +
                "  \"weather\": {" +
                "    \"source_id\": \"12345\"," +
                "    \"timestamp\": \"2024-11-28T23:00:00Z\"," +
                "    \"condition\": \"clear-night\"," +
                "    \"temperature\": 5.5," +
                "    \"cloud_cover\": 10," +
                "    \"relative_humidity\": 80" +
                "  }," +
                "  \"sources\": [" +
                "    {" +
                "      \"id\": \"12345\"," +
                "      \"station_name\": \"Berlin\"," +
                "      \"lat\": 52.52" +
                "    }" +
                "  ]" +
                "}";

        Gson gson = new Gson();
        Response response = gson.fromJson(json, Response.class);

        assertNotNull(response);
        assertNotNull(response.getCurrentWeather());
        assertEquals("12345", response.getCurrentWeather().getSourceId());
        assertEquals(5.5f, response.getCurrentWeather().getTemperature());
        assertEquals(10, response.getCurrentWeather().getCloudCover());

        assertNotNull(response.getSources());
        assertEquals(1, response.getSources().size());
        assertEquals("Berlin", response.getSources().get(0).getStationName());
        assertEquals(52.52f, response.getSources().get(0).getLat());
    }

    @Test
    public void testSetEpochTimestamp() {
        Response response = new Response();
        response.setEpochTimestamp("2024-11-28T23:00:00Z");
        assertEquals(1732834800L, response.getEpochTimestamp());
    }

    @Test
    public void testEqualsAndHashCode() {
        Response r1 = new Response();
        CurrentWeather w1 = new CurrentWeather();
        w1.setSourceId("S1");
        w1.setEpochTimestamp(1000L);
        r1.setCurrentWeather(w1);

        Response r2 = new Response();
        CurrentWeather w2 = new CurrentWeather();
        w2.setSourceId("S1");
        w2.setEpochTimestamp(1000L);
        r2.setCurrentWeather(w2);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());

        w2.setEpochTimestamp(2000L);
        assertNotEquals(r1, r2);
    }
}
