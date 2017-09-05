package webserver;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by sunchanlee on 2017. 9. 5..
 */
public class HttpSessionTest {

    private String id = "1234";
    private HttpSession session;

    @Before
    public void setUp() throws Exception {
        this.session = new HttpSession(this.id);
    }

    @Test
    public void getId() throws Exception {
        assertEquals(this.id, this.session.getId());
    }

    @Test
    public void setGetAttribute() throws Exception {
        this.session.setAttribute("key", "value");
        assertEquals("value", this.session.getAttribute("key"));
    }

    @Test
    public void removeAttribute() throws Exception {
        this.session.removeAttribute("key");
        assertNull(this.session.getAttribute("key"));
    }

    @Test
    public void invalidate() throws Exception {
        HttpSession s = HttpSessions.getSession(this.id);
        s.invalidate();
        assertNotEquals(s, HttpSessions.getSession(this.id));
    }
}