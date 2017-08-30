package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Map;

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
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest request = new HttpRequest(in);
            String url = request.getPath();
            String method = request.getMethod();
            String mimeType = request.getHeader("Accept");
            Map<String, String> cookies = request.getCookies();

            HttpResponse response = new HttpResponse(out);
            if(method.equals("POST")) {
                if (url.equals("/user/create")) {
                    createUser(request.getParameter("userId"), request.getParameter("password"),
                            request.getParameter("name"), request.getParameter("email"));
                    url = "/index.html";
                    response.sendRedirect(url);
                    return;
                } else if(url.equals("/user/login")) {
                    boolean loggedIn = loginUser(request.getParameter("userId"),
                            request.getParameter("password"));
                    url = "/user/login_failed.html";
                    if(loggedIn) {
                        url = "/index.html";
                    }
                    response.addHeader("Set-Cookie", "logined=" + loggedIn);
                    response.sendRedirect(url);
                    return;
                }
            }

            if(url.equals("/user/list")) {
                if(cookies.containsKey("logined") && Boolean.parseBoolean(cookies.get("logined"))) {
                    response.addHeader("Content-Type", mimeType);
                    response.forwardWithBody(makeUserList().getBytes());
                    return;
                } else {
                    response.sendRedirect("/user/login.html");
                    return;
                }
            }

            response.addHeader("Content-Type", mimeType);
            response.forward(url);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String makeUserList() {
        Collection<User> users = DataBase.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("<table border=1>");
        sb.append("<th>User ID</th>");
        sb.append("<th>Name</th>");
        sb.append("<th>Email</th>");
        for(User user : users) {
            sb.append("<tr>");
            sb.append("<td>" + user.getUserId() + "</td>");
            sb.append("<td>" + user.getName() + "</td>");
            sb.append("<td>" + user.getEmail() + "</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }

    private boolean loginUser(String userId, String password) {
        log.info("Request to login id: {}", userId);

        User user = DataBase.findUserById(userId);
        if(user != null && password.equals(user.getPassword())) {
            return true;
        }

        return false;
    }

    private void createUser(String userId, String password, String name, String email) {
        User user = new User(userId, password, name, email);
        DataBase.addUser(user);
        log.info("Created user info:{}", user.toString());
    }
}
