package webserver;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Created by sunchanlee on 2017. 8. 29..
 */
public class HttpRequestTest {
    private String testDirectory = "./src/test/resources/";
    private HttpRequest getRequest;
    private HttpRequest postRequest;

    @Before
    public void setUp() throws Exception {
        InputStream getStream = new FileInputStream(new File(testDirectory + "Http_Get.txt"));
        InputStream postStream = new FileInputStream(new File(testDirectory + "Http_Post.txt"));
        this.getRequest = new HttpRequest(getStream);
        this.postRequest = new HttpRequest(postStream);
    }

    @Test
    public void getMethodGetRequest() throws Exception {
        assertEquals("GET", getRequest.getMethod());
    }

    @Test
    public void getMethodPostRequest() throws Exception {
        assertEquals("POST", postRequest.getMethod());
    }

    @Test
    public void getPathGetRequest() throws Exception {
        assertEquals("/user/create", getRequest.getPath());
    }

    @Test
    public void getPathPostRequest() throws Exception {
        assertEquals("/user/create", postRequest.getPath());
    }

    @Test
    public void getHeaderGetRequest() throws Exception {
        assertEquals("localhost:8000", getRequest.getHeader("Host"));
        assertEquals("keep-alive", getRequest.getHeader("Connection"));
        assertEquals("*/*", getRequest.getHeader("Accept"));
    }

    @Test
    public void getHeaderPostRequest() throws Exception {
        assertEquals("localhost:8000", postRequest.getHeader("Host"));
        assertEquals("keep-alive", postRequest.getHeader("Connection"));
        assertEquals("45", postRequest.getHeader("Content-Length"));
        assertEquals("application/x-www-form-urlencoded", postRequest.getHeader("Content-Type"));
        assertEquals("*/*", postRequest.getHeader("Accept"));
    }

    @Test
    public void getParameterGetRequest() throws Exception {
        assertEquals("javajigi", getRequest.getParameter("userId"));
        assertEquals("password", getRequest.getParameter("password"));
        assertEquals("tester", getRequest.getParameter("name"));
    }

    @Test
    public void getParameterPostRequest() throws Exception {
        assertEquals("javajigi", postRequest.getParameter("userId"));
        assertEquals("password", postRequest.getParameter("password"));
        assertEquals("tester", postRequest.getParameter("name"));
    }
}