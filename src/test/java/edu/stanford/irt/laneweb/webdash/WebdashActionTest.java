package edu.stanford.irt.laneweb.webdash;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.user.User;
import edu.stanford.irt.laneweb.user.UserDao;

public class WebdashActionTest {

    private WebdashAction action;

    private Map<String, Object> objectModel;

    private HttpServletRequest request;

    private User user;

    private UserDao userDao;

    private WebdashLogin webdashLogin;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.action = new WebdashAction();
        this.objectModel = createMock(Map.class);
        this.request = createMock(HttpServletRequest.class);
        this.userDao = createMock(UserDao.class);
        this.user = createMock(User.class);
        this.webdashLogin = createMock(WebdashLogin.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testError() {
        expect(this.objectModel.get("request")).andReturn(this.request);
        replay(this.objectModel);
        expect(this.request.getParameter("nonce")).andReturn(null);
        expect(this.request.getParameter("system_user_id")).andReturn("ceyates");
        replay(this.request);
        expect(this.userDao.createOrUpdateUser(this.request)).andReturn(this.user);
        replay(this.userDao);
        replay(this.user);
        expect(this.webdashLogin.getWebdashURL(this.user, null, "ceyates")).andReturn("broken");
        replay(this.webdashLogin);
        this.action.setWebdashLogin(this.webdashLogin);
        this.action.setUserDao(this.userDao);
        Map result = this.action.act(null, null, this.objectModel, null, null);
        assertEquals("broken", result.get("webdash-url"));
        verify(this.objectModel);
        verify(this.request);
        verify(this.userDao);
        verify(this.user);
        verify(this.webdashLogin);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testLogin() {
        expect(this.objectModel.get("request")).andReturn(this.request);
        replay(this.objectModel);
        expect(this.request.getParameter("nonce")).andReturn("nonce");
        expect(this.request.getParameter("system_user_id")).andReturn("ceyates");
        replay(this.request);
        expect(this.userDao.createOrUpdateUser(this.request)).andReturn(this.user);
        replay(this.userDao);
        replay(this.user);
        expect(this.webdashLogin.getWebdashURL(this.user, "nonce", "ceyates")).andReturn("login");
        replay(this.webdashLogin);
        this.action.setWebdashLogin(this.webdashLogin);
        this.action.setUserDao(this.userDao);
        Map result = this.action.act(null, null, this.objectModel, null, null);
        assertEquals("login", result.get("webdash-url"));
        verify(this.objectModel);
        verify(this.request);
        verify(this.userDao);
        verify(this.user);
        verify(this.webdashLogin);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRegister() {
        expect(this.objectModel.get("request")).andReturn(this.request);
        replay(this.objectModel);
        expect(this.request.getParameter("nonce")).andReturn("nonce");
        expect(this.request.getParameter("system_user_id")).andReturn(null);
        replay(this.request);
        expect(this.userDao.createOrUpdateUser(this.request)).andReturn(this.user);
        replay(this.userDao);
        replay(this.user);
        expect(this.webdashLogin.getWebdashURL(this.user, "nonce", null)).andReturn("register");
        replay(this.webdashLogin);
        this.action.setWebdashLogin(this.webdashLogin);
        this.action.setUserDao(this.userDao);
        Map result = this.action.act(null, null, this.objectModel, null, null);
        assertEquals("register", result.get("webdash-url"));
        verify(this.objectModel);
        verify(this.request);
        verify(this.userDao);
        verify(this.user);
        verify(this.webdashLogin);
    }
}
