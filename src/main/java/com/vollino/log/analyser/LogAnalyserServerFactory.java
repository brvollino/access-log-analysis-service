package com.vollino.log.analyser;

import com.google.common.collect.Lists;
import com.vollino.log.analyser.health.HealthCheckServlet;
import com.vollino.log.analyser.ingest.LogIngestionService;
import com.vollino.log.analyser.ingest.LogIngestionServlet;
import com.vollino.log.analyser.server.HttpEndpoint;
import com.vollino.log.analyser.server.JettyServer;

import java.util.List;

/**
 * @author Bruno Vollino
 */
public class LogAnalyserServerFactory {

    public JettyServer create(int port, String elasticsearchEndpoint) {
        List<HttpEndpoint> endpoints = Lists.newArrayList(
            new HttpEndpoint("/health", healthCheckServlet()),
            new HttpEndpoint("/ingest", logIngestionServlet())
        );

        return new JettyServer(port, endpoints);
    }

    private LogIngestionServlet logIngestionServlet() {
        return new LogIngestionServlet(new LogIngestionService());
    }

    private HealthCheckServlet healthCheckServlet() {
        return new HealthCheckServlet();
    }
}
