package edu.stanford.irt.laneweb.voyager;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.avalon.framework.parameters.Parameters;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.user.User;
import edu.stanford.irt.laneweb.user.UserDao;

public class VoyagerActionTest {

    private VoyagerAction action;

    private Map<String, Object> objectModel;

    private HttpServletRequest request;
    
    private Parameters params;

    private User user;

    private UserDao userDao;

    private VoyagerLogin voyagerLogin;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.action = new VoyagerAction();
        this.voyagerLogin = createMock(VoyagerLogin.class);
        this.userDao = createMock(UserDao.class);
        this.objectModel = createMock(Map.class);
        this.request = createMock(HttpServletRequest.class);
        this.params = createMock(Parameters.class);
        this.user = createMock(User.class);
    }

    @Test
    public void testAct() throws Exception {
        expect(this.objectModel.get("request")).andReturn(this.request);
        replay(this.objectModel);
        replay(this.request);
        expect(this.params.getParameter("pid", null)).andReturn("123");
        expect(this.params.getParameter("query-string", null)).andReturn("a=b");
        replay(this.params);
        expect(this.userDao.createOrUpdateUser(this.request)).andReturn(this.user);
        replay(this.userDao);
        replay(this.user);
        expect(this.voyagerLogin.getVoyagerURL(this.user, "123", "a=b")).andReturn("hello");
        replay(this.voyagerLogin);
        this.action.setVoyagerLogin(this.voyagerLogin);
        this.action.setUserDao(this.userDao);
        assertEquals(this.action.act(null, null, this.objectModel, null, this.params).get("voyager-url"), "hello");
        verify(this.objectModel);
        verify(this.request);
        verify(this.params);
        verify(this.userDao);
        verify(this.user);
        verify(this.voyagerLogin);
    }
}
