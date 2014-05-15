package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.servlet.binding.CompositeDataBinder;

public class CMERedirectControllerTest {

    private CMERedirectController controller;

    private CompositeDataBinder dataBinder;

    private HttpServletRequest request;

    private HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        this.dataBinder = createMock(CompositeDataBinder.class);
        this.controller = new CMERedirectController(this.dataBinder);
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
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

    @Test
    public void textCmeSunetidRedirect() throws Exception {
        this.response
                .sendRedirect("http://laneproxy.stanford.edu/login?url=http://www.uptodate.com/online/content/search.do?unid=hashedSunet&srcsys=EZPX90710&eiv=2.1.0");
        replay(this.request, this.response);
        this.controller.cmeRedirect("hashedSunet", "/basepath", null, true, "http://www.uptodate.com/", this.request,
                this.response);
        verify(this.request, this.response);
    }
}
