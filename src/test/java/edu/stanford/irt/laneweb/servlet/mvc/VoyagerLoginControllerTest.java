package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.voyager.VoyagerLogin;

public class VoyagerLoginControllerTest {

    private HttpServletRequest request;

    private HttpServletResponse response;

    private HttpSession session;

    private VoyagerLogin voyagerLogin;

    private VoyagerLoginController voyagerLoginController;

    @Before
    public void setUp() throws Exception {
        this.voyagerLoginController = new VoyagerLoginController();
        this.voyagerLogin = createMock(VoyagerLogin.class);
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.session = createMock(HttpSession.class);
    }

    @Test
    public void testVoyagerLogin() throws Exception {
        expect(this.session.getAttribute("univid")).andReturn("1234").times(2);
        expect(this.request.getQueryString()).andReturn("a=b").times(2);
        expect(this.voyagerLogin.getVoyagerURL("1234", "123", "a=b")).andReturn("hello").times(2);
        this.response.sendRedirect("hello");
        this.response.sendRedirect("hello");
        replay(this.voyagerLogin, this.request, this.response, this.session);
        Map<String, VoyagerLogin> voyagerLogins = new HashMap<String, VoyagerLogin>();
        voyagerLogins.put(VoyagerLogin.class.getName() + "/lmldb", this.voyagerLogin);
        voyagerLogins.put(VoyagerLogin.class.getName() + "/jbldb", this.voyagerLogin);
        this.voyagerLoginController.setVoyagerLogins(voyagerLogins);
        this.voyagerLoginController.login("lmldb", "123", this.session, this.request, this.response);
        this.voyagerLoginController.login("jbldb", "123", this.session, this.request, this.response);
        verify(this.voyagerLogin, this.request, this.response);
    }
}
