package com.kafka.MyApiConnector;

import com.google.gson.Gson;
import org.apache.kafka.common.config.AbstractConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class TheDataFetcher extends TimerTask {

    private static final Logger log = LoggerFactory.getLogger(TheDataFetcher.class);
    private static final String API_CURRENT_URL = "https://api.brightsky.dev/current_weather";

    private final SortedSet<Response> fetchedRecords;
    private final Gson apiResponseParser;
    private final String latitude;
    private final String longitude;
    private final String dwdStationId;
    private final int pollingInterval;
    private final boolean disableOffsetCheck;
    private long offset;
    private boolean isFirstFetch = true;

    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 2000;

    // === LRU Cache to prevent duplicate timestamp emissions ===
    private final Set<Long> recentTimestamps = Collections.newSetFromMap(
            new LinkedHashMap<>() {
                protected boolean removeEldestEntry(Map.Entry<Long, Boolean> eldest) {
                    return size() > 50; // Keep only last 50 timestamps
                }
            }
    );

    public TheDataFetcher(AbstractConfig config, Long offset) {
        this.latitude = config.getString("latitude");
        this.longitude = config.getString("longitude");
        this.dwdStationId = config.getString("dwd_station_id");

        if ((latitude == null || longitude == null || latitude.isEmpty() || longitude.isEmpty())
                && (dwdStationId == null || dwdStationId.isEmpty())) {
            throw new IllegalArgumentException("Either latitude/longitude or dwd_station_id must be specified");
        }

        this.pollingInterval = config.getInt("polling_interval");
        this.disableOffsetCheck = config.getBoolean("disable.offset.check");

        this.offset = (offset == null) ? 0L : offset;
        this.fetchedRecords = Collections.synchronizedSortedSet(new TreeSet<>(new ResponseComparator()));
        this.apiResponseParser = new Gson();

        log.info("Initialized TheDataFetcher with offset={} | lat={}, lon={}, dwdStationId={} | interval={}s | disableOffsetCheck={}",
                this.offset, latitude, longitude, dwdStationId, pollingInterval, disableOffsetCheck);
    }

    @Override
    public void run() {
        try {
            List<Response> responses = fetchOnceWithRetry();
            if (responses.isEmpty()) {
                log.info("No new API responses fetched.");
            } else {
                synchronized (this) {
                    this.fetchedRecords.addAll(responses);
                }
                log.info("Fetched and buffered {} new response(s).", responses.size());
            }
        } catch (Exception e) {
            log.error("Scheduled fetch failed with exception", e);
        }
    }

    public List<Response> fetchOnceWithRetry() {
        int attempts = 0;
        while (attempts < MAX_RETRIES) {
            try {
                return fetchOnce();
            } catch (IOException e) {
                attempts++;
                log.warn("Fetch attempt {} failed: {}", attempts, e.getMessage());
                if (attempts >= MAX_RETRIES) {
                    log.error("All {} fetch attempts failed", MAX_RETRIES, e);
                    break;
                }
                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        return Collections.emptyList();
    }

    public List<Response> fetchOnce() throws IOException {
        log.info("Performing one-time fetch from /current_weather...");
        List<Response> singleShot = new ArrayList<>();

        try (Reader reader = getApiReader()) {
            String rawJson = new BufferedReader(reader).lines().collect(Collectors.joining());
            log.info("Fetched current_weather JSON ({} bytes)", rawJson.length());
            log.debug("Raw API JSON: {}", rawJson);

            Response apiResponse = apiResponseParser.fromJson(rawJson, Response.class);
            if (apiResponse == null || apiResponse.getCurrentWeather() == null) {
                log.warn("No current weather data found in /current_weather");
                return singleShot;
            }

            long ts = apiResponse.getEpochTimestamp();
            String src = apiResponse.getCurrentWeather().getSourceId();

            boolean isNew = disableOffsetCheck || isFirstFetch || ts > offset;
            boolean isUnique = !recentTimestamps.contains(ts);

            log.debug("Evaluating record → timestamp={}, offset={}, new={}, unique={}", ts, offset, isNew, isUnique);

            if (isNew && isUnique) {
                offset = ts;
                recentTimestamps.add(ts);
                singleShot.add(apiResponse);
                log.info("Accepted API response: ts={} src={}", ts, src);
                isFirstFetch = false;
            } else {
                log.info("--> Skipping duplicate or stale record: ts={} src={}", ts, src);
            }
        }

        return singleShot;
    }

    private Reader getApiReader() throws IOException {
        String fullUrl;
        if (dwdStationId != null && !dwdStationId.isEmpty()) {
            fullUrl = API_CURRENT_URL + "?dwd_station_id=" + URLEncoder.encode(dwdStationId, StandardCharsets.UTF_8);
        } else {
            fullUrl = API_CURRENT_URL + "?lat=" + URLEncoder.encode(latitude, StandardCharsets.UTF_8)
                    + "&lon=" + URLEncoder.encode(longitude, StandardCharsets.UTF_8);
        }

        log.info("Calling BrightSky API: {}", fullUrl);
        URL url = new URL(fullUrl);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestProperty("Accept", "application/json");

        int responseCode = httpConn.getResponseCode();
        log.info("BrightSky API responded with status: {}", responseCode);
        if (responseCode != 200) {
            throw new IOException("HTTP request failed with code: " + responseCode);
        }

        return new InputStreamReader(httpConn.getInputStream(), StandardCharsets.UTF_8);
    }

    public synchronized List<Response> getResponses() {
        List<Response> items = new ArrayList<>();
        while (!fetchedRecords.isEmpty()) {
            Response next = fetchedRecords.first();
            if (fetchedRecords.remove(next)) {
                items.add(next);
            }
        }
        log.info("Returning {} response(s) to SourceTask.poll()", items.size());
        return items;
    }

    public int getPollingInterval() {
        return pollingInterval;
    }
}