package com.vollino.log.analyser.server;

import com.vollino.log.analyser.LogAnalyserServerFactory;
import com.vollino.log.analyser.server.JettyServer;
import io.restassured.RestAssured;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Bruno Vollino
 */
public class JettyServerTest {

    private static JettyServer server;

    @BeforeClass
    public static void setUpClass() throws Exception {
        server = new LogAnalyserServerFactory().create(0);
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

    @Test
    public void shouldGetAHealthCheck() throws URISyntaxException {
        get(server.getUri().toString() + "health")
                .then().statusCode(200);
    }

    @Test
    public void shouldIngestLogEntries() throws URISyntaxException {
        given()
            .body(
                "/pets/exotic/cats/10 1037825323957 5b019db5-b3d0-46d2-9963-437860af707f 1\n" +
                "/pets/guaipeca/dogs/1 1037825323957 5b019db5-b3d0-46d2-9963-437860af707g 2\n" +
                "/tiggers/bid/now 1037825323957 5b019db5-b3d0-46d2-9963-437860af707e 3\n"
            )
            .post(server.getUri().toString() + "ingest")
            .then()
                .statusCode(200)
                .body(equalTo("3"));
    }
}