package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.IOException;

/**
 * Created by sunchanlee on 2017. 8. 31..
 */
public class CreateUserController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) throws IOException {
        User user = new User(request.getParameter("userId"), request.getParameter("password"),
                request.getParameter("name"), request.getParameter("email"));
        DataBase.addUser(user);
        log.info("Created user info:{}", user.toString());
        response.sendRedirect("/index.html");
    }
}
