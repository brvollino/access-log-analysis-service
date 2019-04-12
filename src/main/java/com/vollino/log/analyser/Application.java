package com.vollino.log.analyser;

import com.google.common.base.Preconditions;
import com.vollino.log.analyser.server.JettyServer;

/**
 * @author Bruno Vollino
 */
public class Application {

    public static void main(String[] args) throws Exception {
        Preconditions.checkArgument(args.length == 0, "The server port must be provided as an argument");
        JettyServer server = new JettyServer(Integer.parseInt(args[0]));
        server.start();
        server.join();
    }
}
