package edu.stanford.irt.laneweb.webdash;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.apache.cocoon.environment.Request;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.directory.LDAPPerson;
import edu.stanford.irt.laneweb.UserInfo;
import edu.stanford.irt.laneweb.UserInfoHelper;

public class WebdashActionTest {

    private WebdashAction action;

    private Map objectModel;

    private Request request;

    private UserInfoHelper userInfoHelper;

    private UserInfo userInfo;

    private LDAPPerson person;

    private WebdashLogin webdashLogin;

    @Before
    public void setUp() throws Exception {
        this.action = new WebdashAction();
        this.objectModel = createMock(Map.class);
        this.request = createMock(Request.class);
        this.userInfoHelper = createMock(UserInfoHelper.class);
        this.userInfo = createMock(UserInfo.class);
        this.person = createMock(LDAPPerson.class);
        this.webdashLogin = createMock(WebdashLogin.class);
    }

    @Test
    public void testRegister() {
        expect(this.objectModel.get("request")).andReturn(this.request);
        replay(this.objectModel);
        expect(this.request.getParameter("nonce")).andReturn("nonce");
        expect(this.request.getParameter("system_user_id")).andReturn(null);
        replay(this.request);
        expect(this.userInfoHelper.getUserInfo(this.request)).andReturn(
                this.userInfo);
        replay(this.userInfoHelper);
        expect(this.userInfo.getPerson()).andReturn(this.person);
        replay(this.userInfo);
        replay(this.person);
        expect(this.webdashLogin.getWebdashURL(this.person, "nonce", null))
                .andReturn("register");
        replay(this.webdashLogin);
        this.action.setWebdashLogin(this.webdashLogin);
        this.action.setUserInfoHelper(this.userInfoHelper);
        Map result = this.action.act(null, null, this.objectModel, null, null);
        assertEquals("register", result.get("webdash-url"));
        verify(this.objectModel);
        verify(this.request);
        verify(this.userInfoHelper);
        verify(this.userInfo);
        verify(this.person);
        verify(this.webdashLogin);
    }

    @Test
    public void testLogin() {
        expect(this.objectModel.get("request")).andReturn(this.request);
        replay(this.objectModel);
        expect(this.request.getParameter("nonce")).andReturn("nonce");
        expect(this.request.getParameter("system_user_id"))
                .andReturn("ceyates");
        replay(this.request);
        expect(this.userInfoHelper.getUserInfo(this.request)).andReturn(
                this.userInfo);
        replay(this.userInfoHelper);
        expect(this.userInfo.getPerson()).andReturn(this.person);
        replay(this.userInfo);
        replay(this.person);
        expect(this.webdashLogin.getWebdashURL(this.person, "nonce", "ceyates"))
                .andReturn("login");
        replay(this.webdashLogin);
        this.action.setWebdashLogin(this.webdashLogin);
        this.action.setUserInfoHelper(this.userInfoHelper);
        Map result = this.action.act(null, null, this.objectModel, null, null);
        assertEquals("login", result.get("webdash-url"));
        verify(this.objectModel);
        verify(this.request);
        verify(this.userInfoHelper);
        verify(this.userInfo);
        verify(this.person);
        verify(this.webdashLogin);
    }

    @Test
    public void testError() {
        expect(this.objectModel.get("request")).andReturn(this.request);
        replay(this.objectModel);
        expect(this.request.getParameter("nonce")).andReturn(null);
        expect(this.request.getParameter("system_user_id"))
                .andReturn("ceyates");
        replay(this.request);
        expect(this.userInfoHelper.getUserInfo(this.request)).andReturn(
                this.userInfo);
        replay(this.userInfoHelper);
        expect(this.userInfo.getPerson()).andReturn(this.person);
        replay(this.userInfo);
        replay(this.person);
        expect(this.webdashLogin.getWebdashURL(this.person, null, "ceyates"))
                .andReturn("broken");
        replay(this.webdashLogin);
        this.action.setWebdashLogin(this.webdashLogin);
        this.action.setUserInfoHelper(this.userInfoHelper);
        Map result = this.action.act(null, null, this.objectModel, null, null);
        assertEquals("broken", result.get("webdash-url"));
        verify(this.objectModel);
        verify(this.request);
        verify(this.userInfoHelper);
        verify(this.userInfo);
        verify(this.person);
        verify(this.webdashLogin);
    }

}
