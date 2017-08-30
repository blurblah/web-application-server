package webserver;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by sunchanlee on 2017. 8. 30..
 */
public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private OutputStream out;
    private Map<String, String> cookies;

    public HttpResponse(OutputStream out) {
        this.out = out;
        this.cookies = Maps.newHashMap();
    }

    public void addHeader(String key, String value) {
        this.cookies.put(key, value);
    }

    public void forward(String url) throws IOException {
        this.out.write(makeHeader(200, this.cookies).getBytes());
        this.out.write(makeBody(url));
    }

    public void sendRedirect(String url) throws IOException {
        // send header
        addHeader("Location", url);
        this.out.write(makeHeader(302, this.cookies).getBytes());
    }

    private String makeHeader(int status, Map<String, String> cookies) {
        String statusMessage = "OK";
        if(status == 302) statusMessage = "Found";

        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 " + status + " " + statusMessage + "\r\n");
        Iterator<String> keys = cookies.keySet().iterator();
        while(keys.hasNext()) {
            String key = keys.next();
            sb.append(key + ": " + cookies.get(key) + "\r\n");
        }
        sb.append("\r\n");
        return sb.toString();
    }

    private byte[] makeBody(String uri) throws IOException {
        byte[] body = "Hello World".getBytes();

        if(uri.equals("/")) {
            uri = "/index.html";
        }

        File f = new File("./webapp" + uri);
        if(f.exists() && f.isFile()) {
            body = Files.readAllBytes(f.toPath());
        }

        return body;
    }
}
