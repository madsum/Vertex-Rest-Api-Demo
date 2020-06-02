package net.basen.demo.app;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import net.basen.demo.entity.SubEntity;


public class VertexRestApiApp extends AbstractVerticle {

    public static final String INDEX = "/";
    public static final String GET_ALL = "/getAll";
    public static final String ALL_DATA = "/getData";
    public static final String ADD = "/addData";
    public static final String DELETE = "/delete/:name";
    private final SubEntity finalSubEntity = new SubEntity();

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new VertexRestApiApp());
    }

    public void start(Promise<Void> promise) {
        createSomeData();
        startWebApp(promise);
    }

    private void createSomeData() {
        SubEntity subEntity1 = new SubEntity("Test1", 1);
        SubEntity subEntity2 = new SubEntity("Test2", 2);
        SubEntity subEntity3 = new SubEntity("Test3", 3);
        finalSubEntity.addSubEntity(subEntity1);
        finalSubEntity.addSubEntity(subEntity2);
        finalSubEntity.addSubEntity(subEntity3);
    }

    private  void startWebApp(Promise<Void> promise) {
        // Create a router object.
        Router router = Router.router(vertx);

        // add routes
        router.route(INDEX).handler(this::getIndex);
        router.get(GET_ALL).handler(this::getAll);
        router.get(ALL_DATA).handler(this::getData);
        router.post(ADD).handler(BodyHandler.create()).handler(this::addData);
        router.delete(ALL_DATA).handler(this::getData);
        router.delete(DELETE).handler(this::deleteData);

        // create http server
        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(config().getInteger("http.port", 9090), result -> {
                    if (result.succeeded()) {
                        promise.complete();
                    } else {
                        promise.fail(result.cause());
                    }
                });
    }

    private void getIndex(RoutingContext routingContext){
        HttpServerResponse response = routingContext.response();
        response
                .putHeader("content-type", "text/html")
                .end("Welcome to Basen Demo API Service");
    }

    private void getAll(RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json")
                .end(Json.encodePrettily(finalSubEntity.getSubEntities()));
    }

    private void getData(RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json")
                .end(Json.encodePrettily(finalSubEntity.getData()));
    }

    private void addData(RoutingContext routingContext) {
        SubEntity subEntity = Json.decodeValue(
                routingContext.getBody(), SubEntity.class);
        finalSubEntity.addSubEntity(subEntity);
        HttpServerResponse serverResponse = routingContext.response();
        serverResponse.setChunked(true);
        serverResponse.end("Entity added successfully...\n"+subEntity);
    }

    private void deleteData(RoutingContext routingContext) {
        String paramName = routingContext.request().getParam("name");
        if (paramName == null || paramName.isEmpty() ) {
            routingContext.response().setStatusCode(404).end("Entity not found");
        } else {
            finalSubEntity.getSubEntities().removeIf(entity ->
                    entity.getName().equalsIgnoreCase(paramName));
            routingContext.response().setStatusCode(200)
                    .end(paramName+" entity removed successfully");
        }
    }
}
