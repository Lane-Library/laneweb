package edu.stanford.irt.laneweb.webdash;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.junit.Before;
import org.junit.Test;

public class WebdashActionTest {

    private WebdashAction action;

    private Parameters params;

    private WebdashLogin webdashLogin;

    @Before
    public void setUp() throws Exception {
        this.action = new WebdashAction();
        this.params = createMock(Parameters.class);
        this.webdashLogin = createMock(WebdashLogin.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testError() {
        expect(this.params.getParameter("nonce", null)).andReturn(null);
        expect(this.params.getParameter("system-user-id", null)).andReturn("ceyates");
        expect(this.params.getParameter("sunet-id", null)).andReturn(null);
        expect(this.params.getParameter("name", null)).andReturn(null);
        expect(this.params.getParameter("affiliation", null)).andReturn(null);
        replay(this.params);
        expect(this.webdashLogin.getWebdashURL(null, null, null, null, "ceyates")).andReturn("broken");
        replay(this.webdashLogin);
        this.action.setWebdashLogin(this.webdashLogin);
        Map result = this.action.act(null, null, null, null, this.params);
        assertEquals("broken", result.get("webdash-url"));
        verify(this.params);
        verify(this.webdashLogin);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testLogin() {
        expect(this.params.getParameter("nonce", null)).andReturn("nonce");
        expect(this.params.getParameter("system-user-id", null)).andReturn("ceyates");
        expect(this.params.getParameter("sunet-id", null)).andReturn(null);
        expect(this.params.getParameter("name", null)).andReturn(null);
        expect(this.params.getParameter("affiliation", null)).andReturn(null);
        replay(this.params);
        expect(this.webdashLogin.getWebdashURL(null, null, null, "nonce", "ceyates")).andReturn("login");
        replay(this.webdashLogin);
        this.action.setWebdashLogin(this.webdashLogin);
        Map result = this.action.act(null, null, null, null, this.params);
        assertEquals("login", result.get("webdash-url"));
        verify(this.params);
        verify(this.webdashLogin);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRegister() {
        expect(this.params.getParameter("nonce", null)).andReturn("nonce");
        expect(this.params.getParameter("system-user-id", null)).andReturn(null);
        expect(this.params.getParameter("sunet-id", null)).andReturn(null);
        expect(this.params.getParameter("name", null)).andReturn(null);
        expect(this.params.getParameter("affiliation", null)).andReturn(null);
        replay(this.params);
        expect(this.webdashLogin.getWebdashURL(null, null, null, "nonce", null)).andReturn("register");
        replay(this.webdashLogin);
        this.action.setWebdashLogin(this.webdashLogin);
        Map result = this.action.act(null, null, null, null, this.params);
        assertEquals("register", result.get("webdash-url"));
        verify(this.params);
        verify(this.webdashLogin);
    }
}
