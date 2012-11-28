package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;
import java.util.Date;

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

    private SHCCodec codec;

    private SHCLoginController controller;

    private LDAPData ldapData;

    private LDAPDataAccess ldapDataAccess;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private HttpSession session;

    private String validTimestamp;

    @Before
    public void setUp() throws Exception {
        this.validTimestamp = Long.toString(new Date().getTime());
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
        expect(this.codec.decrypt(this.validTimestamp)).andReturn(this.validTimestamp);
        expect(this.codec.decrypt("emrid")).andReturn("emrid");
        expect(this.codec.decrypt("univid")).andReturn("univid");
        this.session.setAttribute(Model.EMRID, "epic-emrid");
        this.session.setAttribute(Model.UNIVID, "univid");
        expect(this.session.getAttribute(Model.UNIVID)).andReturn("univid");
        expect(this.session.getAttribute(Model.SUNETID)).andReturn("ditenus");
        expect(this.request.getServerName()).andReturn("server");
        expect(this.request.getContextPath()).andReturn("");
        this.response.sendRedirect("https://server/portals/shc.html?sourceid=shc&u=emrid");
        replay(this.codec, this.ldapDataAccess, this.request, this.response, this.session);
        this.controller.login("emrid", "univid", this.validTimestamp, this.request, this.response);
        verify(this.codec, this.ldapDataAccess, this.request, this.response, this.session);
    }

    @Test
    public void testLoginExpiredTimestamp() throws IOException {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.codec.decrypt("123456789")).andReturn("123456789");
        expect(this.request.getServerName()).andReturn("server");
        expect(this.request.getContextPath()).andReturn("");
        this.response
                .sendRedirect("https://server/portals/shc.html?sourceid=shc&u=emrid&error=invalid+or+missing+timestamp%3A+123456789");
        replay(this.codec, this.ldapDataAccess, this.request, this.response, this.session, this.ldapData);
        this.controller.login("emrid", "univid", "123456789", this.request, this.response);
        verify(this.codec, this.ldapDataAccess, this.request, this.response, this.session, this.ldapData);
    }

    @Test
    public void testLoginSunetidNotActive() throws IOException {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.codec.decrypt(this.validTimestamp)).andReturn(this.validTimestamp);
        expect(this.codec.decrypt("emrid")).andReturn("emrid");
        expect(this.codec.decrypt("univid")).andReturn("univid");
        this.session.setAttribute(Model.EMRID, "epic-emrid");
        this.session.setAttribute(Model.UNIVID, "univid");
        expect(this.session.getAttribute(Model.UNIVID)).andReturn("univid");
        expect(this.session.getAttribute(Model.SUNETID)).andReturn(null);
        expect(this.ldapDataAccess.getLdapDataForUnivid("univid")).andReturn(this.ldapData);
        expect(this.ldapData.isActive()).andReturn(Boolean.FALSE);
        expect(this.request.getServerName()).andReturn("server");
        expect(this.request.getContextPath()).andReturn("");
        this.response
                .sendRedirect("https://server/portals/shc.html?sourceid=shc&u=emrid&error=missing+active+sunetid+for+univid%3A+univid");
        replay(this.codec, this.ldapDataAccess, this.request, this.response, this.session, this.ldapData);
        this.controller.login("emrid", "univid", this.validTimestamp, this.request, this.response);
        verify(this.codec, this.ldapDataAccess, this.request, this.response, this.session, this.ldapData);
    }

    @Test
    public void testLoginSunetidNotInSession() throws IOException {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.codec.decrypt(this.validTimestamp)).andReturn(this.validTimestamp);
        expect(this.codec.decrypt("emrid")).andReturn("emrid");
        expect(this.codec.decrypt("univid")).andReturn("univid");
        this.session.setAttribute(Model.EMRID, "epic-emrid");
        this.session.setAttribute(Model.UNIVID, "univid");
        expect(this.session.getAttribute(Model.UNIVID)).andReturn("univid");
        expect(this.session.getAttribute(Model.SUNETID)).andReturn(null);
        expect(this.ldapDataAccess.getLdapDataForUnivid("univid")).andReturn(this.ldapData);
        expect(this.ldapData.getSunetId()).andReturn("ditenus");
        expect(this.ldapData.isActive()).andReturn(Boolean.TRUE);
        this.session.setAttribute(Model.SUNETID, "ditenus");
        expect(this.request.getServerName()).andReturn("server");
        expect(this.request.getContextPath()).andReturn("");
        this.response.sendRedirect("https://server/portals/shc.html?sourceid=shc&u=emrid");
        replay(this.codec, this.ldapDataAccess, this.request, this.response, this.session, this.ldapData);
        this.controller.login("emrid", "univid", this.validTimestamp, this.request, this.response);
        verify(this.codec, this.ldapDataAccess, this.request, this.response, this.session, this.ldapData);
    }

    @Test
    public void testLoginTimeskewBack() throws IOException {
        String backwardSkewedTimestamp = Long.toString(Long.parseLong(this.validTimestamp) - 59000);
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.codec.decrypt(backwardSkewedTimestamp)).andReturn(backwardSkewedTimestamp);
        expect(this.codec.decrypt("emrid")).andReturn("emrid");
        expect(this.codec.decrypt("univid")).andReturn("univid");
        this.session.setAttribute(Model.EMRID, "epic-emrid");
        this.session.setAttribute(Model.UNIVID, "univid");
        expect(this.session.getAttribute(Model.UNIVID)).andReturn("univid");
        expect(this.session.getAttribute(Model.SUNETID)).andReturn("ditenus");
        expect(this.request.getServerName()).andReturn("server");
        expect(this.request.getContextPath()).andReturn("");
        this.response.sendRedirect("https://server/portals/shc.html?sourceid=shc&u=emrid");
        replay(this.codec, this.ldapDataAccess, this.request, this.response, this.session);
        this.controller.login("emrid", "univid", backwardSkewedTimestamp, this.request, this.response);
        verify(this.codec, this.ldapDataAccess, this.request, this.response, this.session);
    }

    @Test
    public void testLoginTimeskewForward() throws IOException {
        String forwardSkewedTimestamp = Long.toString(Long.parseLong(this.validTimestamp) + 59000);
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.codec.decrypt(forwardSkewedTimestamp)).andReturn(forwardSkewedTimestamp);
        expect(this.codec.decrypt("emrid")).andReturn("emrid");
        expect(this.codec.decrypt("univid")).andReturn("univid");
        this.session.setAttribute(Model.EMRID, "epic-emrid");
        this.session.setAttribute(Model.UNIVID, "univid");
        expect(this.session.getAttribute(Model.UNIVID)).andReturn("univid");
        expect(this.session.getAttribute(Model.SUNETID)).andReturn("ditenus");
        expect(this.request.getServerName()).andReturn("server");
        expect(this.request.getContextPath()).andReturn("");
        this.response.sendRedirect("https://server/portals/shc.html?sourceid=shc&u=emrid");
        replay(this.codec, this.ldapDataAccess, this.request, this.response, this.session);
        this.controller.login("emrid", "univid", forwardSkewedTimestamp, this.request, this.response);
        verify(this.codec, this.ldapDataAccess, this.request, this.response, this.session);
    }
}
