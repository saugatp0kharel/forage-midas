package devops.demo;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class HelloServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0); // change to 8081 if 8080 is busy
        server.createContext("/", new HelloHandler());
        server.start();
        System.out.println("Server started on http://localhost:8080");
    }

    static class HelloHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = """
                <html>
                <body style='font-family:Arial;text-align:center;margin-top:60px'>
                    <h1>Hello, DevOps!</h1>
                    <p>Welcome to our first DevOps demo application.</p>
                    <p><b>My First CI/CD pipeline has been successfully built and deployed. It works!</b></p>
                    <p>Computer Science – Saugat Pokharel</p>
                </body>
                </html>
            """;

            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
