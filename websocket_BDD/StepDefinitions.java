package bdd.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.WebSocket;
import io.vertx.core.http.WebSocketConnectOptions;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepDefinitions {

    private WebSocket clientWebSocket;
    private String serverResponse;
    private CountDownLatch latch;
    private HttpServer server;

    @Given("a Vert.x WebSocket server is running on {string}")
    public void aVertxWebSocketServerIsRunningOn(String serverUrl) {
        latch = new CountDownLatch(1);

        Vertx vertx = Vertx.vertx();

        server = vertx.createHttpServer();
        server.webSocketHandler(webSocket -> {
            webSocket.textMessageHandler(message -> {
                // Process the received message and send a response
                webSocket.writeTextMessage("Message received: " + message);
            });
        });
        server.listen(8080, "localhost", ar -> {
            if (ar.succeeded()) {
                System.out.println("WebSocket server started on port 8080");
            } else {
                System.err.println("Failed to start WebSocket server: " + ar.cause().getMessage());
            }
        });
    }

    @When("the client sends the message {string}")
    public void theClientSendsTheMessage(String message) {
        Vertx vertx = Vertx.vertx();
        WebSocketConnectOptions options = new WebSocketConnectOptions().setHost("localhost").setPort(8080).setURI("/socket");
        vertx.createHttpClient().webSocket(options, ar -> {
            if (ar.succeeded()) {
                clientWebSocket = ar.result();
                clientWebSocket.textMessageHandler(response -> {
                    serverResponse = response;
                    latch.countDown();
                });
                clientWebSocket.writeTextMessage(message);
            } else {
                System.err.println("Failed to connect to WebSocket server: " + ar.cause().getMessage());
            }
        });
    }

    @Then("the server should respond with the message {string}")
    public void theServerShouldRespondWithTheMessage(String expectedResponse) throws InterruptedException {
        latch.await();
        assertEquals(expectedResponse, serverResponse);
        clientWebSocket.close();
    }

    @Then("Close the server")
    public void closeServer(){
        server.close();
    }
}
