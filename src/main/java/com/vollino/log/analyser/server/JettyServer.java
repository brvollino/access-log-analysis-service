package com.vollino.log.analyser.server;

import com.google.common.base.Preconditions;
import com.vollino.log.analyser.health.HealthCheckServlet;
import com.vollino.log.analyser.ingest.LogIngestionService;
import com.vollino.log.analyser.ingest.LogIngestionServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.net.URI;
import java.util.List;

/**
 * @author Bruno Vollino
 */
public class JettyServer {

    private Server server;

    public JettyServer(Integer port, List<HttpEndpoint> endpoints) {
        server = new Server(port);

        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);

        for (HttpEndpoint endpoint: endpoints) {
            handler.addServletWithMapping(new ServletHolder(endpoint.getHttpServlet()), endpoint.getPath());

        }

    }

    public void start() throws Exception {
        Preconditions.checkArgument(server.isStopped(), "The server is already running");
        server.start();
        server.dumpStdErr();
    }

    public void join() throws InterruptedException {
        server.join();
    }

    public void stop() throws Exception {
        if (server.isRunning()) {
            server.stop();
        }
    }

    public URI getUri() {
        return server.getURI();
    }
}
