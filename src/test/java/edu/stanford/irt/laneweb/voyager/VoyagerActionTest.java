package edu.stanford.irt.laneweb.voyager;

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

public class VoyagerActionTest {

    private VoyagerAction action;

    private VoyagerLogin voyagerLogin;

    private UserInfoHelper userInfoHelper;

    private Map objectModel;

    private Request request;

    private UserInfo userInfo;

    private LDAPPerson person;

    @Before
    public void setUp() throws Exception {
        this.action = new VoyagerAction();
        this.voyagerLogin = createMock(VoyagerLogin.class);
        this.userInfoHelper = createMock(UserInfoHelper.class);
        this.objectModel = createMock(Map.class);
        this.request = createMock(Request.class);
        this.userInfo = createMock(UserInfo.class);
        this.person = createMock(LDAPPerson.class);
    }

    @Test
    public void testAct() throws Exception {
        expect(this.objectModel.get("request")).andReturn(this.request);
        replay(this.objectModel);
        expect(this.request.getParameter("PID")).andReturn("123");
        expect(this.request.getQueryString()).andReturn("a=b");
        replay(this.request);
        expect(this.userInfoHelper.getUserInfo(this.request)).andReturn(
                this.userInfo);
        replay(this.userInfoHelper);
        expect(this.userInfo.getPerson()).andReturn(this.person);
        replay(this.userInfo);
        expect(this.voyagerLogin.getVoyagerURL(this.person, "123", "a=b"))
                .andReturn("hello");
        replay(this.voyagerLogin);
        this.action.setVoyagerLogin(this.voyagerLogin);
        this.action.setUserInfoHelper(this.userInfoHelper);
        assertEquals(this.action.act(null, null, this.objectModel, null, null)
                .get("voyager-url"), "hello");
        verify(this.objectModel);
        verify(this.request);
        verify(this.userInfoHelper);
        verify(this.userInfo);
        verify(this.voyagerLogin);
    }

}
