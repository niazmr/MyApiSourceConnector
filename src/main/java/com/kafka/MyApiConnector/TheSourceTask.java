package com.kafka.MyApiConnector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.stream.Collectors;

//import okhttp3.Response;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.apache.kafka.connect.storage.OffsetStorageReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TheSourceTask extends SourceTask {

    private static Logger log = LoggerFactory.getLogger(TheSourceTask.class);

    // fires at regular/fixed poll intervals
    private Timer fetchTimer;
    // invoked by the timer to call the weather API
    private TheDataFetcher dataFetcher;

    // factory class for creating Connect records from the API responses
    private TheRecordFactory recordFactory;

    @Override
    public void start(Map<String, String> properties) {
        log.info("Starting task {}", properties);

        AbstractConfig config = new AbstractConfig(ConnectorConfig.configDef, properties);
        recordFactory = new TheRecordFactory(config);

        // Use the new getPersistedOffset method
        Long offset = recordFactory.getPersistedOffset(getOffsetStorageReader());

        dataFetcher = new TheDataFetcher(config, offset);

        fetchTimer = new Timer();
        int pollInterval = Integer.parseInt(config.getString(ConnectorConfig.POLLING_INTERVAL)) * 1000;
        fetchTimer.scheduleAtFixedRate(dataFetcher, 0, pollInterval);
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
    //    log.info("Polling for API responses...");
        List<Response> responses = dataFetcher.getResponses();

        List<SourceRecord> records = responses.stream()
                .map(recordFactory::createSourceRecord)
                .filter(record -> record != null) // Filter out null records
                .collect(Collectors.toList());
        return records;
    }

    @Override
    public void stop() {
        log.info("Stopping task");

        if (fetchTimer != null) {
            fetchTimer.cancel();
        }
        fetchTimer = null;
        dataFetcher = null;
        recordFactory = null;
    }

    @Override
    public String version() {
        return TheSourceConnector.VERSION;
    }

    private OffsetStorageReader getOffsetStorageReader () {
        if (context == null) {
            log.debug("No context - assuming that this is the first time the Connector has run");
            return null;
        }
        else if (context.offsetStorageReader() == null) {
            log.debug("No offset reader - assuming that this is the first time the Connector has run");
            return null;
        }
        else {
            return context.offsetStorageReader();
        }
    }
}