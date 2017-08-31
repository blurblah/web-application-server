package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        boolean loggedIn = loginUser(request.getParameter("userId"),
                request.getParameter("password"));
        String url = "/user/login_failed.html";
        if(loggedIn) {
            url = "/index.html";
        }
        response.addHeader("Set-Cookie", "logined=" + loggedIn);
        response.sendRedirect(url);
    }

    private boolean loginUser(String userId, String password) {
        log.info("Request to login id: {}", userId);

        User user = DataBase.findUserById(userId);
        if(user != null && password.equals(user.getPassword())) {
            return true;
        }

        return false;
    }
}
