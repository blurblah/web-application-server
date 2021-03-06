package webserver;

import controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);
            String url = request.getPath();
            HttpResponse response = new HttpResponse(out);

            Map<String, String> cookies = request.getCookies();
            if(cookies.get("JSESSIONID") == null) {
                response.addHeader("Set-Cookie", UUID.randomUUID().toString());
            }

            Controller controller = RequestMapping.getController(url);
            if(controller != null) {
                controller.service(request, response);
                return;
            }

            response.addHeader("Content-Type", request.getHeader("Accept"));
            response.forward(url);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
