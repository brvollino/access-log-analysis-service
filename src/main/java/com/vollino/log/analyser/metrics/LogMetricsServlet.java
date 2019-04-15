package com.vollino.log.analyser.metrics;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Bruno Vollino
 */
public class LogMetricsServlet extends HttpServlet {

    private final LogMetricsService metricsService;
    private final ObjectMapper objectMapper;

    public LogMetricsServlet(LogMetricsService metricsService, ObjectMapper objectMapper) {
        this.metricsService = metricsService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Metrics metrics = metricsService.getMetrics();

        String body = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(metrics);

        resp.setStatus(200);
        resp.setContentType("application/json");
        resp.getWriter().print(body);
    }
}
