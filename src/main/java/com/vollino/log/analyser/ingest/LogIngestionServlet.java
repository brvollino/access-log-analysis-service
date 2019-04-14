package com.vollino.log.analyser.ingest;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Bruno Vollino
 */
public class LogIngestionServlet extends HttpServlet {

    private final LogIngestionService logIngestionService;

    public LogIngestionServlet(LogIngestionService logIngestionService) {
        this.logIngestionService = logIngestionService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logIngestionService.ingest(req.getReader().lines());

        resp.setStatus(200);
    }
}
