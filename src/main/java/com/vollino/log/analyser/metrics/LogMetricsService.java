package com.vollino.log.analyser.metrics;

import com.google.common.collect.ImmutableMap;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Bruno Vollino
 */
public class LogMetricsService {

    private final RestHighLevelClient elasticSearchClient;

    public LogMetricsService(RestHighLevelClient elasticSearchClient) {
        this.elasticSearchClient = elasticSearchClient;
    }

    public Metrics getMetrics(){
        //TODO implement
        return new Metrics();
    }
}
