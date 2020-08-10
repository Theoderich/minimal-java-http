package de.qaware.theo.http.java.minimal;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;

public class ServerApplication {

    public static void main(String[] args) throws Exception {
        DbConnector dbConnector = new DbConnector();
        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(new PathHandler().addExactPath("/hello", new ExampleRequestHandler(dbConnector, new Gson())))
                .build();
        server.start();
    }
}
