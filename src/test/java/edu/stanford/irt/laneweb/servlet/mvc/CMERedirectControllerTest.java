package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.CompositeDataBinder;

public class CMERedirectControllerTest {

    private CMERedirectController controller;

    private CompositeDataBinder dataBinder;

    private Map<String, Object> map;

    private org.springframework.ui.Model model;

    private HttpServletRequest request;

    private HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        this.dataBinder = createMock(CompositeDataBinder.class);
        this.controller = new CMERedirectController(this.dataBinder);
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.model = createMock(org.springframework.ui.Model.class);
        this.map = new HashMap<>();
    }

    @SuppressWarnings("boxing")
    @Test
    public void testBind() {
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.model.containsAttribute(Model.AUTH)).andReturn(false);
        expect(this.model.addAttribute(Model.AUTH, null)).andReturn(this.model);
        expect(this.model.containsAttribute(Model.BASE_PATH)).andReturn(false);
        expect(this.model.addAttribute(Model.BASE_PATH, null)).andReturn(this.model);
        expect(this.model.containsAttribute(Model.EMRID)).andReturn(false);
        expect(this.model.addAttribute(Model.EMRID, null)).andReturn(this.model);
        expect(this.model.containsAttribute(Model.PROXY_LINKS)).andReturn(false);
        expect(this.model.addAttribute(Model.PROXY_LINKS, false)).andReturn(this.model);
        this.dataBinder.bind(this.map, this.request);
        replay(this.dataBinder, this.model);
        this.controller.bind(this.request, this.model);
        verify(this.dataBinder, this.model);
    }

    @SuppressWarnings("boxing")
    @Test
    public void testBindData() {
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.model.containsAttribute(Model.AUTH)).andReturn(true);
        expect(this.model.containsAttribute(Model.BASE_PATH)).andReturn(true);
        expect(this.model.containsAttribute(Model.EMRID)).andReturn(true);
        expect(this.model.containsAttribute(Model.PROXY_LINKS)).andReturn(true);
        this.dataBinder.bind(this.map, this.request);
        replay(this.dataBinder, this.model);
        this.controller.bind(this.request, this.model);
        verify(this.dataBinder, this.model);
    }

    @Test
    public void testCmeEmridAndUserIdNoProxyRedirect() throws Exception {
        this.response.sendRedirect("http://www.uptodate.com/contents/search?unid=emrid&srcsys=epic90710&eiv=2.1.0");
        replay(this.request, this.response);
        this.controller.cmeRedirect("hashedUser", "/basepath", "emrid", false, "http://www.uptodate.com/", this.request,
                this.response);
        verify(this.request, this.response);
    }

    @Test
    public void testCmeEmridAndUserIdRedirect() throws Exception {
        this.response.sendRedirect(
                "https://login.laneproxy.stanford.edu/login?url=http://www.uptodate.com/contents/search?unid=emrid&srcsys=epic90710&eiv=2.1.0");
        replay(this.request, this.response);
        this.controller.cmeRedirect("hashedUser", "/basepath", "emrid", true, "http://www.uptodate.com/", this.request,
                this.response);
        verify(this.request, this.response);
    }

    @Test
    public void testCmeEmridRedirect() throws Exception {
        this.response.sendRedirect(
                "https://login.laneproxy.stanford.edu/login?url=http://www.uptodate.com/contents/search?unid=emrid&srcsys=epic90710&eiv=2.1.0");
        replay(this.request, this.response);
        this.controller.cmeRedirect(null, "/basepath", "emrid", true, "http://www.uptodate.com/", this.request,
                this.response);
        verify(this.request, this.response);
    }

    @Test
    public void testCmeEmridSecureRedirect() throws Exception {
        this.response.sendRedirect(
                "https://login.laneproxy.stanford.edu/login?url=http://www.uptodate.com/contents/search?unid=emrid&srcsys=epic90710&eiv=2.1.0");
        replay(this.request, this.response);
        this.controller.cmeSecureRedirect(null, "emrid", true, "http://www.uptodate.com/online", this.request,
                this.response);
        verify(this.request, this.response);
    }

    @Test
    public void testCmeEmridSecureRedirect2() throws Exception {
        this.response.sendRedirect(
                "https://login.laneproxy.stanford.edu/login?url=http://www.uptodate.com/contents/search?unid=emrid&srcsys=epic90710&eiv=2.1.0");
        replay(this.request, this.response);
        this.controller.cmeSecureRedirect(null, "emrid", true, "http://www.uptodate.com/contents/search", this.request,
                this.response);
        verify(this.request, this.response);
    }

    @Test
    public void testCmeRedirectBadHost() throws Exception {
        expect(this.request.getQueryString()).andReturn("yo");
        this.response.sendRedirect("/cmeRedirectError.html?yo");
        replay(this.request, this.response);
        this.controller.cmeRedirect("userid", "/basepath", "emrid", true, "http://www.badhost.com", this.request,
                this.response);
        verify(this.request, this.response);
    }

    @Test
    public void testCmeRedirectEmptyHost() throws Exception {
        expect(this.request.getQueryString()).andReturn("yo");
        this.response.sendRedirect("/cmeRedirectError.html?yo");
        replay(this.request, this.response);
        this.controller.cmeRedirect(null, "/basepath", "emrid", true, "", this.request, this.response);
        verify(this.request, this.response);
    }

    @Test
    public void testCmeRedirectNonstandardHost() throws Exception {
        this.response
                .sendRedirect("https://login.laneproxy.stanford.edu/login?url=http://www.uptodate.com/help/manual/cme");
        replay(this.request, this.response);
        this.controller.cmeRedirect("hashedUser", "/basepath", null, true, "http://www.uptodate.com/help/manual/cme",
                this.request, this.response);
        verify(this.request, this.response);
    }

    @Test
    public void testCmeRedirectNullEmridNullUser() throws Exception {
        this.response.sendRedirect("/basepath/secure/redirect/cme?url=http://www.uptodate.com/online");
        replay(this.request, this.response);
        this.controller.cmeRedirect(null, "/basepath", null, true, "http://www.uptodate.com/online", this.request,
                this.response);
        verify(this.request, this.response);
    }

    @Test
    public void testCmeRedirectUserId() throws Exception {
        this.response.sendRedirect(
                "https://login.laneproxy.stanford.edu/login?url=http://www.uptodate.com/online/content/search.do?foo=bar&unid=hashedUser&srcsys=EZPX90710&eiv=2.1.0");
        replay(this.request, this.response);
        this.controller.cmeRedirect("hashedUser", "/basepath", null, true,
                "http://www.uptodate.com/online/content/search.do?foo=bar", this.request, this.response);
        verify(this.request, this.response);
    }

    @Test
    public void testCmeRedirectUserIdWithDomain() throws Exception {
        this.response.sendRedirect(
                "https://login.laneproxy.stanford.edu/login?url=http://www.uptodate.com/online/content/search.do?foo=bar&unid=hashedUser&srcsys=EZPX90710&eiv=2.1.0");
        replay(this.request, this.response);
        this.controller.cmeRedirect("hashedUser@lpch.net", "/basepath", null, true,
                "http://www.uptodate.com/online/content/search.do?foo=bar", this.request, this.response);
        verify(this.request, this.response);
    }

    @Test
    public void testLpchEmridAndUserIdNoProxyRedirect() throws Exception {
        this.response
                .sendRedirect("http://www.uptodate.com/contents/search?unid=lpch-emrid&srcsys=EPICLPCH90710&eiv=2.1.0");
        replay(this.request, this.response);
        this.controller.cmeRedirect("hashedUser", "/basepath", "lpch-emrid", false, "http://www.uptodate.com/",
                this.request, this.response);
        verify(this.request, this.response);
    }

    @Test
    public void testLpchEmridAndUserIdRedirect() throws Exception {
        this.response.sendRedirect(
                "https://login.laneproxy.stanford.edu/login?url=http://www.uptodate.com/contents/search?unid=lpch-emrid&srcsys=EPICLPCH90710&eiv=2.1.0");
        replay(this.request, this.response);
        this.controller.cmeRedirect("hashedUser", "/basepath", "lpch-emrid", true, "http://www.uptodate.com/",
                this.request, this.response);
        verify(this.request, this.response);
    }
}
