package com.vollino.log.analyser.ingest;

import com.google.common.collect.ImmutableMap;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

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

    private static final String LOGS_INDEX_NAME = "logs";

    private final RestHighLevelClient elasticSearchClient;

    public LogIngestionService(RestHighLevelClient elasticSearchClient) {
        this.elasticSearchClient = elasticSearchClient;
    }

    public void ingest(Stream<String> log) throws IOException {
        if (!logsIndexExists()) {
            createLogsIndex();
        }
        BulkRequest request = new BulkRequest();
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        request.waitForActiveShards(ActiveShardCount.ALL);

        log.map(this::mapLine).forEach(request::add);

        elasticSearchClient.bulk(request);
    }

    private boolean logsIndexExists() throws IOException {
        return elasticSearchClient.indices().exists(new GetIndexRequest().indices(LOGS_INDEX_NAME));
    }

    private void createLogsIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("logs");
        request.source("{\n" +
                "    \"settings\" : {\n" +
                "        \"number_of_shards\" : 10,\n" +
                "        \"number_of_replicas\" : 0\n" +
                "    },\n" +
                "    \"mappings\" : {\n" +
                "        \"_doc\" : {\n" +
                "            \"properties\" : {\n" +
                "                \"url\": {\"type\": \"keyword\"},\n" +
                "                \"timestamp\": {\"type\": \"long\"},\n" +
                "                \"user_id\": {\"type\": \"keyword\"},\n" +
                "                \"region\": {\"type\": \"long\"},\n" +
                "                \"minute\": {\"type\": \"date\"},\n" +
                "                \"day\": {\"type\": \"date\"},\n" +
                "                \"first_day_of_week\": {\"type\": \"date\"},\n" +
                "                \"year\": {\"type\": \"long\"}\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}", XContentType.JSON);

        elasticSearchClient.indices().create(request);
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

        return new IndexRequest(LOGS_INDEX_NAME, "_doc").source(doc);
    }
}
