package com.vollino.log.analyser.metrics;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Bruno Vollino
 */
public class LogMetricsServlet extends HttpServlet {

    private final LogMetricsService metricsService;

    public LogMetricsServlet(LogMetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        metricsService.getMetrics();

        resp.setStatus(200);
    }
}
