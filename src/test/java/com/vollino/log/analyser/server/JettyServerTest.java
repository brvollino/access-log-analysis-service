package com.vollino.log.analyser.server;

import com.vollino.log.analyser.server.JettyServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.get;

/**
 * @author Bruno Vollino
 */
public class JettyServerTest {

    private static JettyServer server;

    @BeforeClass
    public static void setUpClass() throws Exception {
        server = new JettyServer(0);
        server.start();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        server.stop();
    }

    @Test
    public void shouldBootServer() {
        get(server.getUri())
            .then().statusCode(404);
    }
}