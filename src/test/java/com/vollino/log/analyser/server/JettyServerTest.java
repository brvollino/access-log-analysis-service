package com.vollino.log.analyser.server;

import com.vollino.log.analyser.LogAnalyserServerConfiguration;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URISyntaxException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

/**
 * @author Bruno Vollino
 */
public class JettyServerTest {

    private static final String ELASTIC_VERSION = "6.3.2";
    private static EmbeddedElastic embeddedElastic;
    private static Integer port;

    private static JettyServer server;
    private static RestHighLevelClient elasticSearchClient;

    @BeforeClass
    public static void setUpClass() throws Exception {
        port = findRandomPort();
        embeddedElastic = EmbeddedElastic.builder()
            .withElasticVersion(ELASTIC_VERSION)
            .withSetting(PopularProperties.TRANSPORT_TCP_PORT, findRandomPort())
            .withSetting(PopularProperties.HTTP_PORT, port)
            .withSetting(PopularProperties.CLUSTER_NAME, UUID.randomUUID())
            .withEsJavaOpts("-Xms128m -Xmx256m")
            .withStartTimeout(1, TimeUnit.MINUTES)
            .build()
            .start();
        String elasticsearchEndpoint = "http://localhost:" + port;
        elasticSearchClient = new RestHighLevelClient(RestClient.builder(
                HttpHost.create(elasticsearchEndpoint)));
        server = new LogAnalyserServerConfiguration(0, elasticsearchEndpoint).jettyServer();
        server.start();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        server.stop();
    }

    @After
    public void tearDown() throws IOException {
        try {
            elasticSearchClient.indices().delete(new DeleteIndexRequest("logs"));
        } catch (ElasticsearchException e) {
            if (e.status() != RestStatus.NOT_FOUND) {
                throw e;
            }
        }
    }

    private static Integer findRandomPort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

    @Test
    public void shouldBootServer() {
        when()
            .get(server.getUri())
        .then().statusCode(404);
    }

    @Test
    public void shouldGetAHealthCheck() throws URISyntaxException {
        when()
            .get(server.getUri().toString() + "health")
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
        .when()
            .post(server.getUri().toString() + "ingest")
        .then()
            .statusCode(200);
    }

    @Test
    public void shouldFindThe3TopHitUrls() throws URISyntaxException {
        given()
            .body(
                "/pets/exotic/cats/10 1037825323957 5b019db5-b3d0-46d2-9963-437860af707f 1\n" +
                "/pets/exotic/cats/10 1037825323957 5b019db5-b3d0-46d2-9963-437860af707f 2\n" +
                "/pets/guaipeca/dogs/1 1037825323957 5b019db5-b3d0-46d2-9963-437860af707g 1\n" +
                "/pets/guaipeca/dogs/1 1037825323957 5b019db5-b3d0-46d2-9963-437860af707g 3\n" +
                "/pets/exotic/capybara/10 1037825323957 5b019db5-b3d0-46d2-9963-437860af707f 1\n" +
                "/tiggers/bid/now 1037825323957 5b019db5-b3d0-46d2-9963-437860af707e 1\n" +
                "/tiggers/bid/now 1037825323957 5b019db5-b3d0-46d2-9963-437860af707e 2\n" +
                "/tiggers/bid/now 1037825323957 5b019db5-b3d0-46d2-9963-437860af707e 3\n"
            )
            .post(server.getUri().toString() + "ingest");


        when()
            .get(server.getUri().toString() + "metrics")
        .then()
            .statusCode(200);
    }
}