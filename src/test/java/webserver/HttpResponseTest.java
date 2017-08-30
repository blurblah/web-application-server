package webserver;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by sunchanlee on 2017. 8. 30..
 */
public class HttpResponseTest {

    @Test
    public void responseCookies() throws Exception {
        HttpResponse response = createResponse("Http_Response_Cookie.txt");
        response.addHeader("Set-Cookie", "logined=true");
        response.sendRedirect("/index.html");
    }

    @Test
    public void responseForward() throws Exception {
        HttpResponse response = createResponse("Http_Response_Forward.txt");
        response.forward("/index.html");
    }

    @Test
    public void responseRedirect() throws Exception {
        HttpResponse response = createResponse("Http_Response_Redirect.txt");
        response.sendRedirect("/index.html");
    }

    private HttpResponse createResponse(String filename) throws FileNotFoundException {
        return new HttpResponse(createOutputStream(filename));
    }

    private OutputStream createOutputStream(String filename)
            throws FileNotFoundException {
        return new FileOutputStream(
                new File("./src/test/resources/" + filename));
    }
}