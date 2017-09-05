package controller;

import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.IOException;

/**
 * Created by sunchanlee on 2017. 8. 31..
 */
public interface Controller {
    void service(HttpRequest request, HttpResponse response) throws IOException;
}
