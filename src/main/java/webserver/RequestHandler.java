package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private Controller controller;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);
            String url = request.getPath();
            String method = request.getMethod();
            HttpResponse response = new HttpResponse(out);

            if(method.equals("POST")) {
                if (url.equals("/user/create")) {
                    controller = new CreateUserController();
                    controller.service(request, response);
                    return;
                } else if(url.equals("/user/login")) {
                    controller = new LoginController();
                    controller.service(request, response);
                    return;
                }
            }

            if(url.equals("/user/list")) {
                controller = new UserListController();
                controller.service(request, response);
            } else {
                response.addHeader("Content-Type", request.getHeader("Accept"));
                response.forward(url);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
