package com.kafka.MyApiConnector;

import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTaskContext;
import org.apache.kafka.connect.storage.OffsetStorageReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ApiSourceTaskTest {

    private ApiSourceTask task;
    private DataFetcher dataFetcher;
    private RecordFactory recordFactory;
    private SourceTaskContext context;
    private OffsetStorageReader offsetReader;

    @BeforeEach
    public void setUp() {
        dataFetcher = mock(DataFetcher.class);
        recordFactory = mock(RecordFactory.class);
        context = mock(SourceTaskContext.class);
        offsetReader = mock(OffsetStorageReader.class);

        when(context.offsetStorageReader()).thenReturn(offsetReader);

        task = new ApiSourceTask(dataFetcher, recordFactory);
        task.initialize(context);
    }

    @Test
    public void testStart() throws IOException {
        Map<String, String> props = new HashMap<>();
        props.put(ApiSourceConnectorConfig.SOURCE_ID, "test-source");
        props.put(ApiSourceConnectorConfig.TOPIC, "test-topic");
        props.put(ApiSourceConnectorConfig.LATITUDE, "52.52");
        props.put(ApiSourceConnectorConfig.LONGITUDE, "13.4");

        when(recordFactory.getPersistedOffset(offsetReader)).thenReturn(0L);
        when(dataFetcher.fetchOnce()).thenReturn(Collections.emptyList());

        task.start(props);

        verify(recordFactory).getPersistedOffset(offsetReader);
        verify(dataFetcher).fetchOnce();
    }

    @Test
    public void testPoll() throws IOException, InterruptedException {
        Response response = new Response();
        CurrentWeather weather = new CurrentWeather();
        weather.setSourceId("test-source");
        response.setCurrentWeather(weather);

        SourceRecord record = mock(SourceRecord.class);

        when(dataFetcher.fetchOnce()).thenReturn(Collections.singletonList(response));
        when(recordFactory.createSourceRecord(response)).thenReturn(record);

        // Subclass to avoid sleep
        ApiSourceTask testTask = new ApiSourceTask(dataFetcher, recordFactory) {
            @Override
            protected void waitNextPoll() {
                // do nothing
            }
        };
        testTask.initialize(context);

        List<SourceRecord> records = testTask.poll();

        assertEquals(1, records.size());
        assertEquals(record, records.get(0));
        verify(dataFetcher).fetchOnce();
        verify(recordFactory).createSourceRecord(response);
    }
}
