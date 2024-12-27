package com.kafka.MyApiConnector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TheSourceConnector extends SourceConnector {

    protected static final String VERSION = "0.0.1";


    private final Logger log = LoggerFactory.getLogger(TheSourceConnector.class);

    private Map<String, String> configProps = null;


    @Override
    public ConfigDef config() {
        return ConnectorConfig.configDef;
    }


    @Override
    public Class<? extends Task> taskClass() {
        return TheSourceTask.class;
    }


    @Override
    public void start(Map<String, String> props) {
        log.info("Starting connector with properties: {}", props);
        configProps = props;
    }



    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        if (maxTasks > 1) {
            log.warn("Only one task is supported. Ignoring tasks.max which is set to {}", maxTasks);
        }
        List<Map<String, String>> taskConfigs = new ArrayList<>(1);
        taskConfigs.add(configProps);
        return taskConfigs;
    }


    @Override
    public void stop() {
        log.info("Stopping connector");
    }


    @Override
    public String version() {
        return VERSION;
    }
}