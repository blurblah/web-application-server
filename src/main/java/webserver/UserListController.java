package webserver;

import db.DataBase;
import model.User;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Created by sunchanlee on 2017. 8. 31..
 */
public class UserListController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        Map<String, String> cookies = request.getCookies();
        String mimeType = request.getHeader("Accept");
        if(cookies.containsKey("logined") && Boolean.parseBoolean(cookies.get("logined"))) {
            response.addHeader("Content-Type", mimeType);
            response.forwardWithBody(makeUserList().getBytes());
        } else {
            response.sendRedirect("/user/login.html");
        }
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
