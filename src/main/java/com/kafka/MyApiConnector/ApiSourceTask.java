package com.kafka.MyApiConnector;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.apache.kafka.connect.storage.OffsetStorageReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class ApiSourceTask extends SourceTask {

    private static final Logger log = LoggerFactory.getLogger(ApiSourceTask.class);

    private DataFetcher dataFetcher;
    private RecordFactory recordFactory;

    public ApiSourceTask() {
    }

    // Constructor for testing
    ApiSourceTask(DataFetcher dataFetcher, RecordFactory recordFactory) {
        this.dataFetcher = dataFetcher;
        this.recordFactory = recordFactory;
    }

    @Override
    public String version() {
        return ApiSourceConnector.VERSION;
    }

    @Override
    public void start(Map<String, String> properties) {
        log.info("Starting SourceTask with properties: {}", properties);

        AbstractConfig config = new AbstractConfig(ApiSourceConnectorConfig.configDef, properties);
        if (this.recordFactory == null) {
            this.recordFactory = new RecordFactory(config);
        }

        OffsetStorageReader offsetReader = context.offsetStorageReader();
        Long restoredOffset = recordFactory.getPersistedOffset(offsetReader);
        log.info("Restored offset from storage: {}", restoredOffset);

        if (this.dataFetcher == null) {
            this.dataFetcher = new DataFetcher(config, restoredOffset);
        }

        try {
            List<Response> initial = dataFetcher.fetchOnce();
            if (initial.isEmpty()) {
                log.info("Initial fetch returned no data.");
            } else {
                log.info("Initial fetch returned {} record(s).", initial.size());
                for (Response r : initial) {
                    log.debug("Initial response: timestamp={}, sourceId={}",
                            r.getEpochTimestamp(), r.getCurrentStationId());
                }
            }
        } catch (IOException e) {
            log.error("Initial fetch failed", e);
        }
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        try {
            log.info("Polling for new weather API records...");
            List<Response> responses = dataFetcher.fetchOnce();
            if (responses == null || responses.isEmpty()) {
                log.info("No new API responses fetched.");
                return Collections.emptyList();
            }

            log.info("Fetched {} new responses.", responses.size());

            List<SourceRecord> records = new ArrayList<>();
            for (Response r : responses) {
                if (r.getCurrentWeather() == null) {
                    log.warn("--> Skipping response with null current weather.");
                    continue;
                }

                SourceRecord record = recordFactory.createSourceRecord(r);
                if (record != null) {
                    log.info("OK. Emitting SourceRecord → topic={}, key={}, timestamp={}, station={}",
                            record.topic(),
                            record.key(),
                            r.getEpochTimestamp(),
                            r.getCurrentStationId());
                    records.add(record);
                } else {
                    log.warn(" [WARN] RecordFactory returned null for a response. Skipping.");
                }
            }

            return records;
        } catch (IOException e) {
            log.error(" [WARN] IOException during poll(): {}", e.getMessage(), e);
            Thread.sleep(60_000); // Back off on network errors
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("[ERROR] Unexpected error during poll(): {}", e.getMessage(), e);
            Thread.sleep(60_000);
            return Collections.emptyList();
        } finally {
            waitNextPoll();
        }
    }

    protected void waitNextPoll() throws InterruptedException {
        Thread.sleep(30_000); // Always sleep between polls
    }

    @Override
    public void stop() {
        log.info("Stopping SourceTask");
        this.dataFetcher = null;
        this.recordFactory = null;
    }
}