package webserver;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunchanlee on 2017. 9. 5..
 */
public class HttpSession {

    private String id;
    private Map<String, Object> attrMap;

    public HttpSession(String id) {
        this.id = id;
        this.attrMap = new HashMap<String, Object>();
    }

    public String getId() {
        return id;
    }

    public void setAttribute(String name, Object value) {
        this.attrMap.put(name, value);
    }

    public Object getAttribute(String name) {
        return this.attrMap.get(name);
    }

    public void removeAttribute(String name) {
        this.attrMap.remove(name);
    }

    public void invalidate() {
        HttpSessions.remove(this.id);
    }
}
