package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.voyager.VoyagerLogin;

public class VoyagerLoginControllerTest {

    private HttpServletRequest request;

    private HttpServletResponse response;

    private VoyagerLogin voyagerLogin;

    private VoyagerLoginController voyagerLoginController;

    @Before
    public void setUp() throws Exception {
        this.voyagerLoginController = new VoyagerLoginController();
        this.voyagerLogin = createMock(VoyagerLogin.class);
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
    }

    @Test
    public void testVoyagerLogin() throws Exception {
        expect(this.request.getQueryString()).andReturn("a=b").times(2);
        expect(this.voyagerLogin.getVoyagerURL("1234", "123", "a=b")).andReturn("hello").times(2);
        this.response.sendRedirect("hello");
        this.response.sendRedirect("hello");
        replay(this.voyagerLogin, this.request, this.response);
        Map<String, VoyagerLogin> voyagerLogins = new HashMap<String, VoyagerLogin>();
        voyagerLogins.put(VoyagerLogin.class.getName() + "/lmldb", this.voyagerLogin);
        voyagerLogins.put(VoyagerLogin.class.getName() + "/jbldb", this.voyagerLogin);
        this.voyagerLoginController.setVoyagerLogins(voyagerLogins);
        this.voyagerLoginController.login("lmldb", "1234", "123", this.request, this.response);
        this.voyagerLoginController.login("jbldb", "1234", "123", this.request, this.response);
        verify(this.voyagerLogin, this.request, this.response);
    }
}
