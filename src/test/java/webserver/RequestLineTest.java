package webserver;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by sunchanlee on 2017. 9. 4..
 */
public class RequestLineTest {

    private RequestLine requestLine;
    private RequestLine requestLineWithParams;

    @Before
    public void setUp() throws Exception {
        requestLine = new RequestLine("GET /index.html HTTP/1.1");
        requestLineWithParams = new RequestLine("GET /user/create?userId=javajigi&password=pass HTTP/1.1");
    }

    @Test
    public void getMethod() throws Exception {
        assertEquals(HttpMethod.GET, requestLine.getMethod());
        assertEquals(HttpMethod.GET, requestLineWithParams.getMethod());
    }

    @Test
    public void getPath() throws Exception {
        assertEquals("/index.html", requestLine.getPath());
        assertEquals("/user/create", requestLineWithParams.getPath());
    }

    @Test
    public void getParams() throws Exception {
        Map<String, String> params = requestLineWithParams.getParams();
        assertEquals(2, params.size());
        assertEquals("javajigi", params.get("userId"));
        assertEquals("pass", params.get("password"));
    }

}