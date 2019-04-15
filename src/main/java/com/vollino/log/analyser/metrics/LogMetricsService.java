package com.vollino.log.analyser.metrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Bruno Vollino
 */
public class LogMetricsService {

    private final RestHighLevelClient elasticSearchClient;

    public LogMetricsService(RestHighLevelClient elasticSearchClient) {
        this.elasticSearchClient = elasticSearchClient;
    }

    public Metrics getMetrics() throws IOException {
        Metrics metrics = new Metrics();
        metrics.setTopHitUrls(queryTopUrls());

        return metrics;
    }

    public List<ValueHitsPair> queryTopUrls() throws IOException {
        SearchRequest searchRequest = new SearchRequest("logs");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermsAggregationBuilder aggregation = AggregationBuilders
                .terms("by_url")
                .field("url")
                .shardSize(30)
                .size(3);
        searchSourceBuilder.aggregation(aggregation);

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = elasticSearchClient.search(searchRequest);
        Terms byUrlAggregation = searchResponse.getAggregations().get("by_url");

        return byUrlAggregation.getBuckets().stream().map(bucket ->
                new ValueHitsPair(bucket.getKeyAsString(), bucket.getDocCount()))
                .collect(Collectors.toList());
    }
}
