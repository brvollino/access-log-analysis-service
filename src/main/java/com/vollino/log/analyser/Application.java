package com.vollino.log.analyser;

import com.google.common.base.Preconditions;
import com.vollino.log.analyser.server.JettyServer;

/**
 * @author Bruno Vollino
 */
public class Application {

    public static void main(String[] args) throws Exception {
        Preconditions.checkArgument(args.length >= 2, "The server port must be provided as an argument");
        int port = Integer.parseInt(args[0]);
        String elasticsearchEndpoint = args[1];
        JettyServer server = new LogAnalyserServerConfiguration(port, elasticsearchEndpoint).jettyServer();
        server.start();
        server.join();
    }
}
