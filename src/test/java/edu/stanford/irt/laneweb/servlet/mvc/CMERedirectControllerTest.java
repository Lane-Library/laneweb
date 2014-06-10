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
        this.map = new HashMap<String, Object>();
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
    public void textCmeEmridAndSunetidNoProxyRedirect() throws Exception {
        this.response
                .sendRedirect("http://www.uptodate.com/online/content/search.do?unid=emrid&srcsys=epic90710&eiv=2.1.0");
        replay(this.request, this.response);
        this.controller.cmeRedirect("hashedSunet", "/basepath", "emrid", false, "http://www.uptodate.com/",
                this.request, this.response);
        verify(this.request, this.response);
    }

    @Test
    public void textCmeEmridAndSunetidRedirect() throws Exception {
        this.response
                .sendRedirect("http://laneproxy.stanford.edu/login?url=http://www.uptodate.com/online/content/search.do?unid=emrid&srcsys=epic90710&eiv=2.1.0");
        replay(this.request, this.response);
        this.controller.cmeRedirect("hashedSunet", "/basepath", "emrid", true, "http://www.uptodate.com/",
                this.request, this.response);
        verify(this.request, this.response);
    }

    @Test
    public void textCmeEmridRedirect() throws Exception {
        this.response
                .sendRedirect("http://laneproxy.stanford.edu/login?url=http://www.uptodate.com/online/content/search.do?unid=emrid&srcsys=epic90710&eiv=2.1.0");
        replay(this.request, this.response);
        this.controller.cmeRedirect(null, "/basepath", "emrid", true, "http://www.uptodate.com/", this.request,
                this.response);
        verify(this.request, this.response);
    }

    @Test
    public void textCmeEmridSecureRedirect() throws Exception {
        this.response
                .sendRedirect("http://laneproxy.stanford.edu/login?url=http://www.uptodate.com/online/content/search.do?unid=emrid&srcsys=epic90710&eiv=2.1.0");
        replay(this.request, this.response);
        this.controller.cmeSecureRedirect(null, "emrid", true, "http://www.uptodate.com/online", this.request,
                this.response);
        verify(this.request, this.response);
    }

    @Test
    public void textCmeRedirectBadHost() throws Exception {
        expect(this.request.getQueryString()).andReturn("yo");
        this.response.sendRedirect("/cmeRedirectError.html?yo");
        replay(this.request, this.response);
        this.controller.cmeRedirect("sunetid", "/basepath", "emrid", true, "http://www.badhost.com", this.request,
                this.response);
        verify(this.request, this.response);
    }

    @Test
    public void textCmeRedirectEmptyHost() throws Exception {
        expect(this.request.getQueryString()).andReturn("yo");
        this.response.sendRedirect("/cmeRedirectError.html?yo");
        replay(this.request, this.response);
        this.controller.cmeRedirect(null, "/basepath", "emrid", true, "", this.request, this.response);
        verify(this.request, this.response);
    }

    @Test
    public void textCmeRedirectNonstandardHost() throws Exception {
        this.response.sendRedirect("http://laneproxy.stanford.edu/login?url=http://www.uptodate.com/help/manual/cme");
        replay(this.request, this.response);
        this.controller.cmeRedirect("hashedSunet", "/basepath", null, true, "http://www.uptodate.com/help/manual/cme",
                this.request, this.response);
        verify(this.request, this.response);
    }

    @Test
    public void textCmeRedirectNullEmridNullSunet() throws Exception {
        this.response.sendRedirect("/basepath/secure/redirect/cme?url=http://www.uptodate.com/online");
        replay(this.request, this.response);
        this.controller.cmeRedirect(null, "/basepath", null, true, "http://www.uptodate.com/online", this.request,
                this.response);
        verify(this.request, this.response);
    }

    @Test(expected = IllegalArgumentException.class)
    public void textCmeRedirectNullHost() throws Exception {
        expect(this.request.getQueryString()).andReturn("yo");
        this.response.sendRedirect("/cmeRedirectError.html?yo");
        replay(this.request, this.response);
        this.controller.cmeRedirect(null, "/basepath", "emrid", true, null, this.request, this.response);
        verify(this.request, this.response);
    }

    @Test(expected = IllegalArgumentException.class)
    public void textCmeRedirectNullHostNullQueryString() throws Exception {
        expect(this.request.getQueryString()).andReturn(null);
        this.response.sendRedirect("/cmeRedirectError.html");
        replay(this.request, this.response);
        this.controller.cmeRedirect(null, "/basepath", "emrid", true, null, this.request, this.response);
        verify(this.request, this.response);
    }

    @Test
    public void textCmeRedirectSunetid() throws Exception {
        this.response
                .sendRedirect("http://laneproxy.stanford.edu/login?url=http://www.uptodate.com/online/content/search.do?foo=bar&unid=hashedSunet&srcsys=EZPX90710&eiv=2.1.0");
        replay(this.request, this.response);
        this.controller.cmeRedirect("hashedSunet", "/basepath", null, true,
                "http://www.uptodate.com/online/content/search.do?foo=bar", this.request, this.response);
        verify(this.request, this.response);
    }

    @Test(expected = IllegalArgumentException.class)
    public void textCmeSecureRedirectNullHost() throws Exception {
        expect(this.request.getQueryString()).andReturn("yo");
        this.response.sendRedirect("/cmeRedirectError.html?yo");
        replay(this.request, this.response);
        this.controller.cmeSecureRedirect(null, "emrid", true, null, this.request, this.response);
        verify(this.request, this.response);
    }
}
