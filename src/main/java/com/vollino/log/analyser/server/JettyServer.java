package com.vollino.log.analyser.server;

import com.google.common.base.Preconditions;
import com.vollino.log.analyser.servlets.HealthCheckServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import java.net.URI;

/**
 * @author Bruno Vollino
 */
public class JettyServer {

    private Server server;
    private Integer port;

    public JettyServer(Integer port) {
        this.port = port;
        server = new Server(port);
        configureEndpoints();
    }

    private void configureEndpoints() {
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(HealthCheckServlet.class, "/health");
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
        Preconditions.checkArgument(server.isStarted(), "The server is not running");
        server.stop();
        this.port = null;
    }

    public URI getUri() {
        return server.getURI();
    }
}
