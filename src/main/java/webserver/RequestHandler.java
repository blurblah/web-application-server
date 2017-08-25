package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();
            if(line == null) {
                return;
            }

            String url = getRequestedUri(line);
            // request method 확인
            String method = getRequestedMethod(line);

            String mimeType = "text/html";
            int requestBodyLength = 0;
            while(!line.isEmpty()) {
                log.debug(line);
                line = reader.readLine();
                if(line.startsWith("Accept: ")) mimeType = getAcceptedContentType(line);
                // post인 경우 Content-Length 추출
                if(line.startsWith("Content-Length: ")) requestBodyLength = getRequestBodyLength(line);
            }

            DataOutputStream dos = new DataOutputStream(out);
            if(method.equals("POST")) {
                if (url.equals("/user/create")) {
                    createUser(IOUtils.readData(reader, requestBodyLength));
                    url = "/index.html";
                    response302Header(dos, url);
                    return;
                }

                if(url.equals("/user/login")) {
                    boolean loggedIn = loginUser(IOUtils.readData(reader, requestBodyLength));
                    url = "/user/login_failed.html";
                    if(loggedIn) {
                        url = "/index.html";
                    }
                    response302HeaderWithCookie(dos, url, "logined=" + loggedIn);
                    return;
                }
            }

            byte[] body = makeBody(url);
            response200Header(dos, mimeType, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private int getRequestBodyLength(String headerLine) {
        String contentLength = headerLine.split(" ")[1].trim();
        return Integer.parseInt(contentLength);
    }

    private boolean loginUser(String query) {
        Map<String, String> params = HttpRequestUtils.parseQueryString(query);
        String userId = params.get("userId");
        String password = params.get("password");
        log.info("Request to login id: {}", userId);

        User user = DataBase.findUserById(userId);
        if(user != null && password.equals(user.getPassword())) {
            return true;
        }

        return false;
    }

    private void createUser(String query) {
        Map<String, String> params = HttpRequestUtils.parseQueryString(query);
        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        DataBase.addUser(user);
        log.info("Created user info:{}", user.toString());
    }

    private String getRequestedMethod(String reqLine) {
        String[] tokens = reqLine.split(" ");
        return tokens[0];
    }

    private String getRequestedUri(String reqLine) {
        String uri = "/index.html";

        String[] reqTokens = reqLine.split(" ");
        if(!reqTokens[1].equals("/")) {
            uri = reqTokens[1];
        }

        return uri;
    }

    private byte[] makeBody(String uri) throws IOException {
        byte[] body = "Hello World".getBytes();

        File f = new File("./webapp" + uri);
        if(f.exists() && f.isFile()) {
            body = Files.readAllBytes(f.toPath());
        }

        return body;
    }

    private String getAcceptedContentType(String headerLine) {
        return headerLine.substring("Accept: ".length()).split(",|;")[0].trim();
    }

    private void response200Header(DataOutputStream dos, String mimeType, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + mimeType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302HeaderWithCookie(DataOutputStream dos, String location, String cookie) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Set-Cookie: " + cookie + " \r\n");
            dos.writeBytes("Location: " + location + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
