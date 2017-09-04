package webserver;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by sunchanlee on 2017. 8. 29..
 */
public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private String method;
    private String path;
    private Map<String, String> headers;
    private Map<String, String> cookies;
    private Map<String, String> parameters;

    public HttpRequest(InputStream in) throws IOException {
        this.headers = Maps.newHashMap();
        this.parameters = Maps.newHashMap();

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = reader.readLine();
        if(line == null) return;

        RequestLine requestLine = new RequestLine(line);
        this.method = requestLine.getMethod();
        this.path = requestLine.getPath();
        this.parameters = requestLine.getParams();

        while(line != null && !line.isEmpty()) {
            log.debug(line);
            line = reader.readLine();
            parseHeader(line);
        }

        String contentLength = this.headers.get("Content-Length");
        if(contentLength != null) {
            this.parameters = HttpRequestUtils.parseQueryString(
                    IOUtils.readData(reader, Integer.parseInt(contentLength)));
        }
    }

    private void parseHeader(String header) {
        if(header == null || header.isEmpty()) {
            return;
        }

        if(header.startsWith("Cookie: ")) {
            this.cookies = HttpRequestUtils.parseCookies(header.substring("Cookie: ".length()));
            return;
        }

        HttpRequestUtils.Pair p = HttpRequestUtils.parseHeader(header);
        this.headers.put(p.getKey(), p.getValue());
    }

    public String getMethod() {
        return this.method;
    }

    public String getPath() {
        return this.path;
    }

    public String getHeader(String field) {
        return this.headers.get(field);
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public String getParameter(String key) {
        return this.parameters.get(key);
    }
}
