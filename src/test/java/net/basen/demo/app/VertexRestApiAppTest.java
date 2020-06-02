package net.basen.demo.app;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.ServerSocket;

import static net.basen.demo.app.VertexRestApiApp.*;

@RunWith(VertxUnitRunner.class)
public class VertexRestApiAppTest {

    private Vertx vertx;
    private int port = 8081;

    @Before
    public void setup(TestContext testContext) throws IOException {
        vertx = Vertx.vertx();

        ServerSocket socket = new ServerSocket(0);
        port = socket.getLocalPort();
        socket.close();

        DeploymentOptions options = new DeploymentOptions()
                .setConfig(new JsonObject().put("http.port", port));

        vertx.deployVerticle(VertexRestApiApp.class.getName(),
                options, testContext.asyncAssertSuccess());
    }

    @After
    public void tearDown(TestContext testContext) {
        vertx.close(testContext.asyncAssertSuccess());
    }
    @Test
    public void testIndexRouter(TestContext testContext) {
        final Async async = testContext.async();

        vertx.createHttpClient()
                .getNow(port, "localhost", INDEX,
                        response -> response.handler(responseBody -> {
                            testContext.assertTrue(responseBody.toString()
                                    .contains("Welcome"));
                            async.complete();
                        }));
    }

    @Test
    public void testGetAllRouter(TestContext testContext) {
        final Async async = testContext.async();

        vertx.createHttpClient()
                .getNow(port, "localhost", GET_ALL,
                        response -> response.handler(responseBody -> {
                            testContext.assertTrue(responseBody.toString()
                                    .contains("Test1"));
                            async.complete();
                        }));
    }

    @Test
    public void testAllDataRouter(TestContext testContext) {
        final Async async = testContext.async();

        vertx.createHttpClient()
                .getNow(port, "localhost", ALL_DATA,
                        response -> response.handler(responseBody -> {
                            testContext.assertTrue(responseBody.toString()
                                    .contains("Test1"));
                            async.complete();
                        }));
    }
}