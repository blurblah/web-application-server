package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.nio.file.Files.readAllBytes;

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

            String[] reqTokens = line.split(" ");
            String mimeType = "text/html";
            while(!line.isEmpty()) {
                log.debug(line);
                line = reader.readLine();
                if(line.startsWith("Accept: ")) {
                    mimeType = line.substring("Accept: ".length()).split(",|;")[0].trim();
                }
            }

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = "Hello World".getBytes();

            if(reqTokens != null && reqTokens.length > 1 && reqTokens[0].equals("GET")) {
                String requestUri = "/index.html";
                if(!reqTokens[1].equals("/")) {
                    requestUri = reqTokens[1];
                }

                File f = new File("./webapp" + requestUri);
                if(f.exists() && f.isFile()) {
                    body = Files.readAllBytes(f.toPath());
                }
            }

            response200Header(dos, mimeType, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
