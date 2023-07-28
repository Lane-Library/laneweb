package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
        this.voyagerLogin = mock(VoyagerLogin.class);
        this.voyagerLoginController = new VoyagerLoginController(this.voyagerLogin, null, null);
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
    }

    @Test
    public void testVoyagerLogin() throws Exception {
        expect(this.request.getQueryString()).andReturn("a=b");
        expect(this.voyagerLogin.getVoyagerURL("1234", "123", "a=b")).andReturn("hello");
        this.response.sendRedirect("hello");
        replay(this.voyagerLogin, this.request, this.response);
        this.voyagerLoginController.login("123", "1234", this.request, this.response);
        verify(this.voyagerLogin, this.request, this.response);
    }
}
