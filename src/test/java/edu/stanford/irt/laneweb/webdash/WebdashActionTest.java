package edu.stanford.irt.laneweb.webdash;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.Request;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.directory.LDAPPerson;
import edu.stanford.irt.laneweb.UserInfo;
import edu.stanford.irt.laneweb.UserInfoHelper;

public class WebdashActionTest {

    private WebdashAction action;

    private Map objectModel;

    private Parameters parameters;

    private Request request;

    private UserInfoHelper userInfoHelper;

    private UserInfo userInfo;

    private LDAPPerson person;

    private WebdashLogin webdashLogin;

    @Before
    public void setUp() throws Exception {
        this.action = new WebdashAction();
        this.objectModel = createMock(Map.class);
        this.parameters = createMock(Parameters.class);
        this.request = createMock(Request.class);
        this.userInfoHelper = createMock(UserInfoHelper.class);
        this.userInfo = createMock(UserInfo.class);
        this.person = createMock(LDAPPerson.class);
        this.webdashLogin = createMock(WebdashLogin.class);
    }

    @Test
    public void testRegister() {
        expect(this.parameters.getParameter("nonce", null)).andReturn("nonce");
        expect(this.parameters.getParameter("system-user-id", null)).andReturn(
                null);
        replay(this.parameters);
        expect(this.objectModel.get("request")).andReturn(this.request);
        replay(this.objectModel);
        replay(this.request);
        expect(this.userInfoHelper.getUserInfo(this.request)).andReturn(
                this.userInfo);
        replay(this.userInfoHelper);
        expect(this.userInfo.getLdapPerson()).andReturn(this.person);
        replay(this.userInfo);
        replay(this.person);
        expect(this.webdashLogin.getRegistrationURL(this.person, "nonce"))
                .andReturn("register");
        replay(this.webdashLogin);
        this.action.setWebdashLogin(this.webdashLogin);
        this.action.setUserInfoHelper(this.userInfoHelper);
        Map result = this.action.act(null, null, this.objectModel, null,
                this.parameters);
        assertEquals(result.get("webdash-url"), "register");
        verify(this.parameters);
        verify(this.objectModel);
        verify(this.request);
        verify(this.userInfoHelper);
        verify(this.userInfo);
        verify(this.person);
        verify(this.webdashLogin);
    }

    @Test
    public void testLogin() {
        expect(this.parameters.getParameter("nonce", null)).andReturn("nonce");
        expect(this.parameters.getParameter("system-user-id", null)).andReturn(
                "ceyates");
        replay(this.parameters);
        expect(this.objectModel.get("request")).andReturn(this.request);
        replay(this.objectModel);
        replay(this.request);
        expect(this.userInfoHelper.getUserInfo(this.request)).andReturn(
                this.userInfo);
        replay(this.userInfoHelper);
        expect(this.userInfo.getLdapPerson()).andReturn(this.person);
        replay(this.userInfo);
        replay(this.person);
        expect(this.webdashLogin.getLoginURL(this.person, "nonce")).andReturn(
                "login");
        replay(this.webdashLogin);
        this.action.setWebdashLogin(this.webdashLogin);
        this.action.setUserInfoHelper(this.userInfoHelper);
        Map result = this.action.act(null, null, this.objectModel, null,
                this.parameters);
        assertEquals(result.get("webdash-url"), "login");
        verify(this.parameters);
        verify(this.objectModel);
        verify(this.request);
        verify(this.userInfoHelper);
        verify(this.userInfo);
        verify(this.person);
        verify(this.webdashLogin);
    }

    @Test
    public void testError() {
        expect(this.parameters.getParameter("nonce", null)).andReturn("nonce");
        expect(this.parameters.getParameter("system-user-id", null)).andReturn(
                "ceyates");
        replay(this.parameters);
        expect(this.objectModel.get("request")).andReturn(this.request);
        replay(this.objectModel);
        replay(this.request);
        expect(this.userInfoHelper.getUserInfo(this.request)).andReturn(
                this.userInfo);
        replay(this.userInfoHelper);
        expect(this.userInfo.getLdapPerson()).andReturn(this.person);
        replay(this.userInfo);
        replay(this.person);
        expect(this.webdashLogin.getLoginURL(this.person, "nonce")).andThrow(
                new IllegalArgumentException("broken"));
        replay(this.webdashLogin);
        this.action.setWebdashLogin(this.webdashLogin);
        this.action.setUserInfoHelper(this.userInfoHelper);
        this.action.enableLogging(createMock(Logger.class));
        Map result = this.action.act(null, null, this.objectModel, null,
                this.parameters);
        assertEquals(result.get("webdash-url"),
                "/error_webdash.html?error=broken");
        verify(this.parameters);
        verify(this.objectModel);
        verify(this.request);
        verify(this.userInfoHelper);
        verify(this.userInfo);
        verify(this.person);
        verify(this.webdashLogin);
    }

}
