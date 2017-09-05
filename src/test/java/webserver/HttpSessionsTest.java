package webserver;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by sunchanlee on 2017. 9. 5..
 */
public class HttpSessionsTest {

    private HttpSession session;

    @Before
    public void setUp() throws Exception {
        this.session = HttpSessions.getSession("1234");
    }

    @Test
    public void getSession() throws Exception {
        HttpSession s = HttpSessions.getSession("1234");
        assertNotNull(s);
        assertEquals(s, this.session);
    }

    @Test
    public void remove() throws Exception {
        HttpSessions.remove("1234");
        HttpSession newSession = HttpSessions.getSession("1234");
        assertNotEquals(newSession, this.session);
    }
}