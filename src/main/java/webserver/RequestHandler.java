package webserver;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private Map<String, Controller> controllerMap;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        initControllerMap();
    }

    private void initControllerMap() {
        this.controllerMap = Maps.newHashMap();
        this.controllerMap.put("/user/create", new CreateUserController());
        this.controllerMap.put("/user/login", new LoginController());
        this.controllerMap.put("/user/list", new UserListController());
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);
            String url = request.getPath();
            HttpResponse response = new HttpResponse(out);

            Controller controller = this.controllerMap.get(url);
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
