package webserver;

import java.io.IOException;

/**
 * Created by sunchanlee on 2017. 8. 31..
 */
public abstract class AbstractController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        String method = request.getMethod();
        if(method.equals("POST")) {
            doPost(request, response);
        } else {
            doGet(request, response);
        }
    }

    public abstract void doGet(HttpRequest request, HttpResponse response) throws IOException;
    public abstract void doPost(HttpRequest request, HttpResponse response) throws IOException;
}
