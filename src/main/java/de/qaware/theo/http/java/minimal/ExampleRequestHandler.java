package de.qaware.theo.http.java.minimal;

import com.google.gson.Gson;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.util.List;

public class ExampleRequestHandler implements HttpHandler {

    private final DbConnector dbConnector;
    private final Gson gson;

    public ExampleRequestHandler(DbConnector dbConnector, Gson gson) {
        this.dbConnector = dbConnector;
        this.gson = gson;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "application/json; charset=utf-8");
        final List<Customer> allCustomers = dbConnector.getAllCustomers();
        exchange.getResponseSender().send(gson.toJson(allCustomers));
    }
}
