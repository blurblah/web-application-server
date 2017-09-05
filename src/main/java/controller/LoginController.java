package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpSession;

import java.io.IOException;

/**
 * Created by sunchanlee on 2017. 8. 31..
 */
public class LoginController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) throws IOException {
        User user = DataBase.findUserById(request.getParameter("userId"));
        String url = "/user/login_failed.html";
        HttpSession session = request.getSession();

        // login check
        if(user.getPassword().equals(request.getParameter("password"))) {
            url = "/index.html";
            session.setAttribute("user", user);
        }

        response.sendRedirect(url);
    }
}
