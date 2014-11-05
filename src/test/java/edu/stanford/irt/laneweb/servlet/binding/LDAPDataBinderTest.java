package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.user.LDAPData;
import edu.stanford.irt.laneweb.user.LDAPDataAccess;

public class LDAPDataBinderTest {

    private LDAPDataBinder dataBinder;

    private LDAPData ldapData;

    private LDAPDataAccess ldapDataAccess;

    private Map<String, Object> model;

    private HttpServletRequest request;

    private HttpSession session;

    @Before
    public void setUp() throws Exception {
        this.ldapDataAccess = createMock(LDAPDataAccess.class);
        this.dataBinder = new LDAPDataBinder(this.ldapDataAccess);
        this.model = new HashMap<String, Object>();
        this.request = createMock(HttpServletRequest.class);
        this.session = createMock(HttpSession.class);
        this.ldapData = createMock(LDAPData.class);
    }

    @Test
    public void testBind() {
        this.model.put(Model.USER_ID, "sunetid@unknown");
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.NAME)).andReturn(null);
        expect(this.session.getAttribute(Model.UNIVID)).andReturn(null);
        expect(this.session.getAttribute(Model.IS_ACTIVE_SUNETID)).andReturn(null);
        expect(this.session.getAttribute(Model.EMAIL)).andReturn(null);
        expect(this.ldapDataAccess.getLdapDataForSunetid("sunetid")).andReturn(this.ldapData);
        expect(this.ldapData.getName()).andReturn("name");
        expect(this.ldapData.getUnivId()).andReturn("univid");
        expect(this.ldapData.isActive()).andReturn(true);
        expect(this.ldapData.getEmailAddress()).andReturn("email");
        this.session.setAttribute(Model.NAME, "name");
        this.session.setAttribute(Model.UNIVID, "univid");
        this.session.setAttribute(Model.IS_ACTIVE_SUNETID, Boolean.TRUE);
        this.session.setAttribute(Model.EMAIL, "email");
        replay(this.ldapDataAccess, this.request, this.session, this.ldapData);
        this.dataBinder.bind(this.model, this.request);
        assertEquals("name", this.model.get(Model.NAME));
        assertEquals("univid", this.model.get(Model.UNIVID));
        assertEquals(Boolean.TRUE, this.model.get(Model.IS_ACTIVE_SUNETID));
        assertEquals("email", this.model.get(Model.EMAIL));
        verify(this.ldapDataAccess, this.request, this.session, this.ldapData);
    }

    @Test
    public void testBindNameOnlyInSession() {
        this.model.put(Model.USER_ID, "sunetid@stanford.edu");
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.NAME)).andReturn("name");
        expect(this.session.getAttribute(Model.UNIVID)).andReturn(null);
        expect(this.session.getAttribute(Model.IS_ACTIVE_SUNETID)).andReturn(null);
        expect(this.session.getAttribute(Model.EMAIL)).andReturn(null);
        replay(this.ldapDataAccess, this.request, this.session, this.ldapData);
        this.dataBinder.bind(this.model, this.request);
        assertEquals("name", this.model.get(Model.NAME));
        assertNull(this.model.get(Model.UNIVID));
        assertNull(this.model.get(Model.IS_ACTIVE_SUNETID));
        assertNull(this.model.get(Model.EMAIL));
        verify(this.ldapDataAccess, this.request, this.session, this.ldapData);
    }

    @Test
    public void testBindNullSunetid() {
        replay(this.ldapDataAccess, this.request, this.session, this.ldapData);
        this.dataBinder.bind(this.model, this.request);
        assertNull(this.model.get(Model.NAME));
        assertNull(this.model.get(Model.UNIVID));
        assertNull(this.model.get(Model.IS_ACTIVE_SUNETID));
        assertNull(this.model.get(Model.EMAIL));
        verify(this.ldapDataAccess, this.request, this.session, this.ldapData);
    }

    @Test
    public void testBindValuesInSession() {
        this.model.put(Model.USER_ID, "sunetid@stanford.edu");
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.NAME)).andReturn("name");
        expect(this.session.getAttribute(Model.UNIVID)).andReturn("univid");
        expect(this.session.getAttribute(Model.IS_ACTIVE_SUNETID)).andReturn(Boolean.TRUE);
        expect(this.session.getAttribute(Model.EMAIL)).andReturn("email");
        replay(this.ldapDataAccess, this.request, this.session, this.ldapData);
        this.dataBinder.bind(this.model, this.request);
        assertEquals("name", this.model.get(Model.NAME));
        assertEquals("univid", this.model.get(Model.UNIVID));
        assertEquals(Boolean.TRUE, this.model.get(Model.IS_ACTIVE_SUNETID));
        assertEquals("email", this.model.get(Model.EMAIL));
        verify(this.ldapDataAccess, this.request, this.session, this.ldapData);
    }
}
