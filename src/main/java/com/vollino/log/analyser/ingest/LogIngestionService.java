package com.vollino.log.analyser.ingest;

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
public class LogIngestionService {

    private final RestHighLevelClient elasticSearchClient;

    public LogIngestionService(RestHighLevelClient elasticSearchClient) {
        this.elasticSearchClient = elasticSearchClient;
    }

    public void ingest(Stream<String> log) throws IOException {
        BulkRequest request = new BulkRequest();

        log.map(this::mapLine).forEach(request::add);

        elasticSearchClient.bulk(request);
    }

    private IndexRequest mapLine(String logLine) {
        String[] fields = logLine.split("\\s+");

        Instant timestamp = Instant.ofEpochMilli(Long.parseLong(fields[1]));
        ZonedDateTime utcDateTime = timestamp.atZone(ZoneOffset.UTC);

        ZonedDateTime firstDayOfWeek = utcDateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

        Map<String, String> doc = ImmutableMap.<String, String>builder()
                .put("url", fields[0])
                .put("timestamp", fields[1])
                .put("user_id", fields[2])
                .put("region", fields[3])
                .put("minute", utcDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .put("day", utcDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .put("first_day_of_week", firstDayOfWeek.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .put("year", Integer.toString(utcDateTime.getYear()))
                .build();

        return new IndexRequest("logs", "doc").source(doc);
    }
}
