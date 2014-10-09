package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.user.User;
import edu.stanford.irt.laneweb.user.UserAttribute;

public class UserDataBinderTest {

    private UserDataBinder binder;

    private UserAttributesRequestParser parser1;

    private UserAttributesRequestParser parser2;

    private Map<Object, UserAttributesRequestParser> parsers;

    private HttpServletRequest request;

    private HttpSession session;

    private User user;

    @Before
    public void setUp() {
        this.parser1 = createMock(UserAttributesRequestParser.class);
        this.parser2 = createMock(UserAttributesRequestParser.class);
        this.parsers = new HashMap<Object, UserAttributesRequestParser>();
        this.parsers.put(null, this.parser1);
        this.parsers.put("provider", this.parser2);
        this.binder = new UserDataBinder(this.parsers);
        this.request = createMock(HttpServletRequest.class);
        this.session = createMock(HttpSession.class);
        this.user = createMock(User.class);
    }

    @Test
    public void testBind() {
        Map<String, Object> model = new HashMap<String, Object>();
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.USER)).andReturn(null);
        expect(this.request.getRemoteUser()).andReturn("user");
        expect(this.request.getAttribute("Shib-Identity-Provider")).andReturn("provider");
        expect(this.parser2.parse(this.request)).andReturn(Collections.<UserAttribute, String>emptyMap());
        this.session.setAttribute(eq(Model.USER), isA(User.class));
        replay(this.parser1, this.parser2, this.request, this.session, this.user);
        this.binder.bind(model, this.request);
        assertNotNull(model.get(Model.USER));
        verify(this.parser1, this.parser2, this.request, this.session, this.user);
    }

    @Test
    public void testBindNoRemoteUser() {
        Map<String, Object> model = new HashMap<String, Object>();
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.USER)).andReturn(null);
        expect(this.request.getRemoteUser()).andReturn(null);
        replay(this.parser1, this.parser2, this.request, this.session, this.user);
        this.binder.bind(model, this.request);
        assertNull(model.get(Model.USER));
        verify(this.parser1, this.parser2, this.request, this.session, this.user);
    }

    @Test
    public void testBindNullProvider() {
        Map<String, Object> model = new HashMap<String, Object>();
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.USER)).andReturn(null);
        expect(this.request.getRemoteUser()).andReturn("user");
        expect(this.request.getAttribute("Shib-Identity-Provider")).andReturn(null);
        expect(this.parser1.parse(this.request)).andReturn(Collections.<UserAttribute, String>emptyMap());
        this.session.setAttribute(eq(Model.USER), isA(User.class));
        replay(this.parser1, this.parser2, this.request, this.session, this.user);
        this.binder.bind(model, this.request);
        assertNotNull(model.get(Model.USER));
        verify(this.parser1, this.parser2, this.request, this.session, this.user);
    }

    @Test
    public void testBindUserInSession() {
        Map<String, Object> model = new HashMap<String, Object>();
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.USER)).andReturn(this.user);
        replay(this.parser1, this.parser2, this.request, this.session, this.user);
        this.binder.bind(model, this.request);
        assertSame(this.user, model.get(Model.USER));
        verify(this.parser1, this.parser2, this.request, this.session, this.user);
    }
}
