package webserver;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunchanlee on 2017. 9. 5..
 */
public class HttpSessions {
    private static Map<String, HttpSession> sessions = new HashMap<String, HttpSession>();

    public static HttpSession getSession(String id) {
        HttpSession s = sessions.get(id);
        if(s == null) {
            s = new HttpSession(id);
            sessions.put(id, s);
        }

        return s;
    }

    public static void remove(String id) {
        sessions.remove(id);
    }
}
