package controller;

import db.DataBase;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpSession;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Created by sunchanlee on 2017. 8. 31..
 */
public class UserListController extends AbstractController {

    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        HttpSession session = request.getSession();
        String mimeType = request.getHeader("Accept");
        if(session.getAttribute("user") != null) {
            response.addHeader("Content-Type", mimeType);
            response.forwardWithBody(makeUserList().getBytes());
        } else {
            response.sendRedirect("/user/login.html");
        }
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) throws IOException {
    }

    private String makeUserList() {
        Collection<User> users = DataBase.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("<table border=1>");
        sb.append("<th>User ID</th>");
        sb.append("<th>Name</th>");
        sb.append("<th>Email</th>");
        for(User user : users) {
            sb.append("<tr>");
            sb.append("<td>" + user.getUserId() + "</td>");
            sb.append("<td>" + user.getName() + "</td>");
            sb.append("<td>" + user.getEmail() + "</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }
}
