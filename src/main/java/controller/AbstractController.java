package controller;

import webserver.HttpMethod;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.IOException;

/**
 * Created by sunchanlee on 2017. 8. 31..
 */
public abstract class AbstractController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        HttpMethod method = request.getMethod();
        if(method == HttpMethod.POST) {
            doPost(request, response);
        } else {
            doGet(request, response);
        }
    }

    public abstract void doGet(HttpRequest request, HttpResponse response) throws IOException;
    public abstract void doPost(HttpRequest request, HttpResponse response) throws IOException;
}
