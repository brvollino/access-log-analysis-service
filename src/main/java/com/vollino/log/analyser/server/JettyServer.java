package com.vollino.log.analyser.server;

import com.google.common.base.Preconditions;
import org.eclipse.jetty.server.Server;

import java.net.URI;

/**
 * @author Bruno Vollino
 */
public class JettyServer {

    private Server server;
    private Integer port;

    public JettyServer(Integer port) {
        this.port = port;
    }

    public void start() throws Exception {
        Preconditions.checkArgument(server == null, "The server is already running");
        server = new Server(port);
        server.start();
        server.dumpStdErr();
    }

    public void join() throws InterruptedException {
        server.join();
    }

    public void stop() throws Exception {
        Preconditions.checkArgument(server != null, "The server is not running");
        server.stop();
        this.port = null;
    }

    public URI getUri() {
        return server.getURI();
    }
}
