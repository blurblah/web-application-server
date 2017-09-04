package webserver;

import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunchanlee on 2017. 9. 4..
 */
public class RequestLine {

    private HttpMethod method;
    private String path;
    private Map<String, String> params;

    public RequestLine(String line) {
        this.params = new HashMap<String, String>();

        String[] requestTokens = line.split(" ");
        this.method = HttpMethod.valueOf(requestTokens[0]);
        this.path = requestTokens[1];
        int parameterIndex = requestTokens[1].indexOf("?");
        if(parameterIndex > -1) {
            this.path = requestTokens[1].substring(0, parameterIndex);
            this.params = HttpRequestUtils.parseQueryString(
                    requestTokens[1].substring(parameterIndex + 1));
        }
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParams() {
        return params;
    }
}
