package webserver;

import controller.Controller;
import controller.CreateUserController;
import controller.LoginController;
import controller.UserListController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunchanlee on 2017. 9. 4..
 */
public class RequestMapping {
    private static Map<String, Controller> controllers =
            new HashMap<String, Controller>();

    static {
        controllers.put("/user/create", new CreateUserController());
        controllers.put("/user/login", new LoginController());
        controllers.put("/user/list", new UserListController());
    }

    public static Controller getController(String url) {
        return controllers.get(url);
    }
}
