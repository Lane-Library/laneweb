package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.ldap.LDAPData;
import edu.stanford.irt.laneweb.ldap.LDAPDataAccess;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.SHCCodec;


public class SHCLoginControllerTest {
    
    private SHCLoginController controller;
    
    private SHCCodec codec;
    
    private LDAPDataAccess ldapDataAccess;

    private HttpServletRequest request;

    private HttpServletResponse response;
    
    private HttpSession session;

    private LDAPData ldapData;

    @Before
    public void setUp() throws Exception {
        this.codec = createMock(SHCCodec.class);
        this.ldapDataAccess = createMock(LDAPDataAccess.class);
        this.controller = new SHCLoginController(this.codec, this.ldapDataAccess);
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.session = createMock(HttpSession.class);
        this.ldapData = createMock(LDAPData.class);
    }

    @Test
    public void testLogin() throws IOException {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.codec.decrypt("emrid")).andReturn("emrid");
        expect(this.codec.decrypt("univid")).andReturn("univid");
        this.session.setAttribute(Model.EMRID, "epic-emrid");
        this.session.setAttribute(Model.UNIVID, "univid");
        expect(this.session.getAttribute(Model.SUNETID)).andReturn("ditenus");
        expect(this.request.getContextPath()).andReturn("");
        this.response.sendRedirect("/portals/shc.html?sourceid=shc");
        replay(this.codec, this.ldapDataAccess, this.request, this.response, this.session);
        this.controller.login("emrid", "univid", this.request, this.response);
        verify(this.codec, this.ldapDataAccess, this.request, this.response, this.session);
    }

    @Test
    public void testLoginSunetidNotInSession() throws IOException {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.codec.decrypt("emrid")).andReturn("emrid");
        expect(this.codec.decrypt("univid")).andReturn("univid");
        this.session.setAttribute(Model.EMRID, "epic-emrid");
        this.session.setAttribute(Model.UNIVID, "univid");
        expect(this.session.getAttribute(Model.SUNETID)).andReturn(null);
        expect(this.ldapDataAccess.getLdapDataForUnivid("univid")).andReturn(this.ldapData);
        expect(this.ldapData.getSunetId()).andReturn("ditenus");
        this.session.setAttribute(Model.SUNETID, "ditenus");
        expect(this.request.getContextPath()).andReturn("");
        this.response.sendRedirect("/portals/shc.html?sourceid=shc");
        replay(this.codec, this.ldapDataAccess, this.request, this.response, this.session, this.ldapData);
        this.controller.login("emrid", "univid", this.request, this.response);
        verify(this.codec, this.ldapDataAccess, this.request, this.response, this.session, this.ldapData);
    }
}
