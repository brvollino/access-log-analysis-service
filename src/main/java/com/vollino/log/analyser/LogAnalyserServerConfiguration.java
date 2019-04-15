package com.vollino.log.analyser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.vollino.log.analyser.health.HealthCheckServlet;
import com.vollino.log.analyser.ingest.LogIngestionService;
import com.vollino.log.analyser.ingest.LogIngestionServlet;
import com.vollino.log.analyser.metrics.LogMetricsService;
import com.vollino.log.analyser.metrics.LogMetricsServlet;
import com.vollino.log.analyser.server.HttpEndpoint;
import com.vollino.log.analyser.server.JettyServer;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.List;

/**
 * @author Bruno Vollino
 */
public class LogAnalyserServerConfiguration {

    private final int port;
    private final String elasticsearchEndpoint;

    private RestHighLevelClient elasticSearchRestHighLeveClient;
    private ObjectMapper objectMapper;

    public LogAnalyserServerConfiguration(int port, String elasticsearchEndpoint) {
        this.port = port;
        this.elasticsearchEndpoint = elasticsearchEndpoint;
    }

    public JettyServer jettyServer() {
        List<HttpEndpoint> endpoints = Lists.newArrayList(
                new HttpEndpoint("/health", healthCheckServlet()),
                new HttpEndpoint("/ingest", logIngestionServlet()),
                new HttpEndpoint("/metrics", logMetricsServlet())
        );

        return new JettyServer(port, endpoints);
    }

    private LogMetricsServlet logMetricsServlet() {
        return new LogMetricsServlet(logMetricService(), objectMapper());
    }

    private LogMetricsService logMetricService() {
        return new LogMetricsService(elasticSearchRestHighLevelClient());
    }

    private synchronized LogIngestionServlet logIngestionServlet() {
        return new LogIngestionServlet(logIngestionService());
    }

    private LogIngestionService logIngestionService() {
        return new LogIngestionService(elasticSearchRestHighLevelClient());
    }

    private RestHighLevelClient elasticSearchRestHighLevelClient() {
        if (elasticSearchRestHighLeveClient == null) {
            elasticSearchRestHighLeveClient = new RestHighLevelClient(
                    RestClient.builder(HttpHost.create(elasticsearchEndpoint)));
        }
        return elasticSearchRestHighLeveClient;
    }

    private HealthCheckServlet healthCheckServlet() {
        return new HealthCheckServlet();
    }

    private ObjectMapper objectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }
}
