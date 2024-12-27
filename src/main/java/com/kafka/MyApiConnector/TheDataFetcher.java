package com.kafka.MyApiConnector;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.GsonBuilder;
import org.apache.kafka.common.config.AbstractConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;

public class TheDataFetcher extends TimerTask {

    private static final Logger log = LoggerFactory.getLogger(TheDataFetcher.class);
    private static final String API_URL = "https://api.brightsky.dev/current_weather";

    private final SortedSet<Response> fetchedRecords;
    private final Gson apiResponseParser = new Gson();
    private final URL urlObj;
    private Long offset;

    public TheDataFetcher(AbstractConfig config, Long offset) {
        log.info("Creating a data fetcher starting from offset {}", offset);
        this.fetchedRecords = Collections.synchronizedSortedSet(new TreeSet<>(new ResponseComparator()));
        this.urlObj = buildApiUrl(config);
        this.offset = offset;
    }

@Override
public void run() {
    log.info("Polling API at URL: {}", urlObj.toString());
    try (Reader reader = getApiReader()) {
        String rawJson = new BufferedReader(reader).lines().collect(Collectors.joining());
        log.info("Raw API response: {}", rawJson);

        Response apiResponse = apiResponseParser.fromJson(rawJson, Response.class);
        log.info("Parsed API response: {}", apiResponse);
        CurrentWeather currentWeather = apiResponse.getCurrent();
        if (currentWeather == null) {
            log.warn("No weather data found in the API response.");

        }
      //  long fetchedTimestamp = apiResponse.getEpochTimestamp();
        assert currentWeather != null;
        long fetchedTimestamp = apiResponse.parseEpochTimestamp(currentWeather.getTimestamp());
        log.info("Current offset: {}, Fetched timestamp: {}", offset, fetchedTimestamp);
       log.info("Parsed timestamp: {}, epochTimestamp: {}", currentWeather.getTimestamp(), currentWeather.getEpochTimestamp());
      //  log.info("Parsed timestamp: {}, epochTimestamp: {}", currentWeather.getTimestamp(), apiResponse.getEpochTimestamp());
        if (apiResponse.getCurrent() != null && fetchedTimestamp > offset) {
            synchronized (this) {
                fetchedRecords.add(apiResponse);
                offset = fetchedTimestamp;
                log.info("Added API response to fetchedRecords. Updated offset to: {}", offset);
            }
        } else {
            if (apiResponse.getCurrent() == null) {
                log.warn("Weather data is null.");
            }
            if (fetchedTimestamp <= offset) {
                log.warn("Fetched timestamp {} is not greater than offset {}. Ignoring response.", fetchedTimestamp, offset);
            }
        }
    } catch (IOException e) {
        log.error("Failed to fetch API data", e);
    }
}

    private URL buildApiUrl(AbstractConfig config) {
        String stationId = config.getString(ConnectorConfig.STATION_ID);
        String tz = "Europe/Berlin";
        String units = "dwd";

        String query = String.format("?dwd_station_id=%s&tz=%s&units=%s",
                URLEncoder.encode(stationId, StandardCharsets.UTF_8),
                URLEncoder.encode(tz, StandardCharsets.UTF_8),
                URLEncoder.encode(units, StandardCharsets.UTF_8));



        try {
            return URI.create(API_URL + query).toURL();
        } catch (MalformedURLException e) {
            log.error("Error constructing API URL", e);
            throw new RuntimeException(e);
        }
    }

    private Reader getApiReader() throws IOException {
        URLConnection connection = urlObj.openConnection();
        connection.setRequestProperty("Accept", "application/json");

        // Handle HTTP errors
        if (connection instanceof java.net.HttpURLConnection) {
            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connection;
            int responseCode = httpConn.getResponseCode();
            if (responseCode != 200) {
                throw new IOException("HTTP request failed with response code: " + responseCode);
            }
        }

        return new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
    }

    public synchronized List<Response> getResponses() {
      //  log.info("Retrieving responses from fetchedRecords...");
        List<Response> items = new ArrayList<>();

        while (!fetchedRecords.isEmpty()) {
            Response nextItem = fetchedRecords.first();
            log.info("Processing item: {}", nextItem);

            if (fetchedRecords.remove(nextItem)) {
                items.add(nextItem);
                log.info("Added item to the list and removed it from fetchedRecords: {}", nextItem);
            } else {
                log.error("Failed to remove item from fetchedRecords: {}", nextItem);
            }
        }

      // log.info("Returning {} responses from fetchedRecords.", items.size());
        return items;
    }

}
// Comparator for sorting Response objects by epoch timestamp
class ResponseComparator implements Comparator<Response> {
    @Override
    public int compare(Response o1, Response o2) {
        return Long.compare(o1.getEpochTimestamp(), o2.getEpochTimestamp());
    }
}