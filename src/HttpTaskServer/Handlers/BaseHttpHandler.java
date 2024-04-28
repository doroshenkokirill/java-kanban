package HttpTaskServer.Handlers;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange exchange, String text) throws IOException {

    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        int responseCode = 406;
        String response = "Not Acceptable";
        exchange.sendResponseHeaders(responseCode, 0);

    }
}