package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.user.UserFactory;
import edu.stanford.irt.laneweb.user.User;

public class UserDataBinderTest {

    private UserDataBinder binder;

    private HttpServletRequest request;

    private HttpSession session;

    private User user;

    private UserFactory userFactory;

    @Before
    public void setUp() {
        this.userFactory = createMock(UserFactory.class);
        this.binder = new UserDataBinder(Arrays.asList(new UserFactory[] { this.userFactory, this.userFactory }));
        this.request = createMock(HttpServletRequest.class);
        this.session = createMock(HttpSession.class);
        this.user = createMock(User.class);
    }

    @Test
    public void testBind() {
        Map<String, Object> model = new HashMap<String, Object>();
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.USER)).andReturn(null);
        expect(this.userFactory.createUser(this.request)).andReturn(this.user);
        this.session.setAttribute(Model.USER, this.user);
        expect(this.user.getId()).andReturn("id@domain");
        expect(this.user.getHashedId()).andReturn("911531548a5ea68cf13f5e0506367956@domain");
        expect(this.user.getEmail()).andReturn("mail");
        expect(this.user.getName()).andReturn("name");
        expect(this.user.isStanfordUser()).andReturn(false);
        replay(this.request, this.session, this.user, this.userFactory);
        this.binder.bind(model, this.request);
        assertNotNull(model.get(Model.USER));
        assertEquals("id@domain", model.get(Model.USER_ID));
        assertEquals("mail", model.get(Model.EMAIL));
        assertEquals("name", model.get(Model.NAME));
        assertEquals("911531548a5ea68cf13f5e0506367956@domain", model.get(Model.AUTH));
        assertNull(model.get(Model.IS_ACTIVE_SUNETID));
        verify(this.request, this.session, this.user, this.userFactory);
    }

    @Test
    public void testBindNoEmailOrName() {
        Map<String, Object> model = new HashMap<String, Object>();
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.USER)).andReturn(null);
        expect(this.userFactory.createUser(this.request)).andReturn(this.user);
        expect(this.user.getId()).andReturn("id@domain");
        expect(this.user.getHashedId()).andReturn("911531548a5ea68cf13f5e0506367956@domain");
        expect(this.user.getEmail()).andReturn(null);
        expect(this.user.getName()).andReturn(null);
        expect(this.user.isStanfordUser()).andReturn(false);
        this.session.setAttribute(eq(Model.USER), isA(User.class));
        replay(this.request, this.session, this.user, this.userFactory);
        this.binder.bind(model, this.request);
        assertNotNull(model.get(Model.USER));
        assertEquals("id@domain", model.get(Model.USER_ID));
        assertNull(model.get(Model.EMAIL));
        assertNull(model.get(Model.NAME));
        assertEquals("911531548a5ea68cf13f5e0506367956@domain", model.get(Model.AUTH));
        assertNull(model.get(Model.IS_ACTIVE_SUNETID));
        verify(this.request, this.session, this.user, this.userFactory);
    }

    @Test
    public void testBindNoUser() {
        Map<String, Object> model = new HashMap<String, Object>();
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.USER)).andReturn(null);
        expect(this.userFactory.createUser(this.request)).andReturn(null).times(2);
        replay(this.request, this.session, this.user, this.userFactory);
        this.binder.bind(model, this.request);
        assertNull(model.get(Model.USER));
        verify(this.request, this.session, this.user, this.userFactory);
    }

    @Test
    public void testBindUserInSession() {
        Map<String, Object> model = new HashMap<String, Object>();
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.USER)).andReturn(this.user);
        expect(this.user.getId()).andReturn("id");
        expect(this.user.getEmail()).andReturn("mail");
        expect(this.user.getName()).andReturn("name");
        expect(this.user.getHashedId()).andReturn("hash");
        expect(this.user.isStanfordUser()).andReturn(false);
        replay(this.request, this.session, this.user, this.userFactory);
        this.binder.bind(model, this.request);
        assertSame(this.user, model.get(Model.USER));
        assertEquals("id", model.get(Model.USER_ID));
        assertEquals("mail", model.get(Model.EMAIL));
        assertEquals("name", model.get(Model.NAME));
        assertEquals("hash", model.get(Model.AUTH));
        assertNull(model.get(Model.IS_ACTIVE_SUNETID));
        verify(this.request, this.session, this.user, this.userFactory);
    }
}
