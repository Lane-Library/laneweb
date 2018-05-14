package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.user.LDAPDataAccess;
import edu.stanford.irt.laneweb.user.User;

public class ActiveSunetidDataBinderTest {

    private ActiveSunetidDataBinder dataBinder;

    private LDAPDataAccess ldapDataAccess;

    private HttpServletRequest request;

    private HttpSession session;

    private User user;

    @Before
    public void setUp() {
        this.ldapDataAccess = mock(LDAPDataAccess.class);
        this.dataBinder = new ActiveSunetidDataBinder(this.ldapDataAccess);
        this.request = mock(HttpServletRequest.class);
        this.session = mock(HttpSession.class);
        this.user = mock(User.class);
    }

    @Test
    public void testBind() {
        Map<String, Object> model = new HashMap<>();
        model.put(Model.USER, this.user);
        expect(this.user.isStanfordUser()).andReturn(true);
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.IS_ACTIVE_SUNETID)).andReturn(null);
        expect(this.user.getId()).andReturn("sunetid@stanford.edu");
        expect(this.ldapDataAccess.isActive("sunetid")).andReturn(true);
        this.session.setAttribute(Model.IS_ACTIVE_SUNETID, Boolean.TRUE);
        replay(this.ldapDataAccess, this.request, this.session, this.user);
        this.dataBinder.bind(model, this.request);
        assertSame(Boolean.TRUE, model.get(Model.IS_ACTIVE_SUNETID));
        verify(this.ldapDataAccess, this.request, this.session, this.user);
    }

    @Test
    public void testBindInSession() {
        Map<String, Object> model = new HashMap<>();
        model.put(Model.USER, this.user);
        expect(this.user.isStanfordUser()).andReturn(true);
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.IS_ACTIVE_SUNETID)).andReturn(Boolean.TRUE);
        replay(this.ldapDataAccess, this.request, this.session, this.user);
        this.dataBinder.bind(model, this.request);
        assertSame(Boolean.TRUE, model.get(Model.IS_ACTIVE_SUNETID));
        verify(this.ldapDataAccess, this.request, this.session, this.user);
    }

    @Test
    public void testBindNotActive() {
        Map<String, Object> model = new HashMap<>();
        model.put(Model.USER, this.user);
        expect(this.user.isStanfordUser()).andReturn(true);
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.IS_ACTIVE_SUNETID)).andReturn(null);
        expect(this.user.getId()).andReturn("sunetid@stanford.edu");
        expect(this.ldapDataAccess.isActive("sunetid")).andReturn(false);
        this.session.setAttribute(Model.IS_ACTIVE_SUNETID, Boolean.FALSE);
        replay(this.ldapDataAccess, this.request, this.session, this.user);
        this.dataBinder.bind(model, this.request);
        assertSame(Boolean.FALSE, model.get(Model.IS_ACTIVE_SUNETID));
        verify(this.ldapDataAccess, this.request, this.session, this.user);
    }

    @Test
    public void testBindNotStanford() {
        Map<String, Object> model = new HashMap<>();
        model.put(Model.USER, this.user);
        expect(this.user.isStanfordUser()).andReturn(false);
        replay(this.ldapDataAccess, this.request, this.session);
        this.dataBinder.bind(model, this.request);
        assertSame(Boolean.FALSE, model.get(Model.IS_ACTIVE_SUNETID));
        verify(this.ldapDataAccess, this.request, this.session);
    }

    @Test
    public void testBindNoUser() {
        Map<String, Object> model = new HashMap<>();
        replay(this.ldapDataAccess, this.request, this.session);
        this.dataBinder.bind(model, this.request);
        assertSame(Boolean.FALSE, model.get(Model.IS_ACTIVE_SUNETID));
        verify(this.ldapDataAccess, this.request, this.session);
    }
}
