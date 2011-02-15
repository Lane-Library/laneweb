package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

public class CMERedirectControllerTest {

    private CMERedirectController controller;

    private HttpServletRequest request;

    private HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        this.controller = new CMERedirectController();
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
    }

    @Test
    public void textCmeRedirect() throws Exception {
        this.response
                .sendRedirect("http://laneproxy.stanford.edu/login?url=http://www.uptodate.com/online/content/search.do?unid=nobody&srcsys=epic90710&eiv=2.1.0");
        replay(this.request, this.response);
        this.controller.cmeRedirect("nobody", "uptodate", this.request, this.response);
        verify(this.request, this.response);
    }

    @Test
    public void textCmeRedirectEmptyHost() throws Exception {
        expect(this.request.getQueryString()).andReturn("yo");
        this.response.sendRedirect("/cmeRedirectError.html?yo");
        replay(this.request, this.response);
        this.controller.cmeRedirect("nobody", "", this.request, this.response);
        verify(this.request, this.response);
    }

    @Test
    public void textCmeRedirectNullBadHost() throws Exception {
        expect(this.request.getQueryString()).andReturn("yo");
        this.response.sendRedirect("/cmeRedirectError.html?yo");
        replay(this.request, this.response);
        this.controller.cmeRedirect("nobody", "bad host", this.request, this.response);
        verify(this.request, this.response);
    }

    @Test
    public void textCmeRedirectNullBadHostEmptyEmrid() throws Exception {
        expect(this.request.getQueryString()).andReturn("yo");
        this.response.sendRedirect("/cmeRedirectError.html?yo");
        replay(this.request, this.response);
        this.controller.cmeRedirect(null, "bad host", this.request, this.response);
        verify(this.request, this.response);
    }

    @Test
    public void textCmeRedirectNullHost() throws Exception {
        expect(this.request.getQueryString()).andReturn("yo");
        this.response.sendRedirect("/cmeRedirectError.html?yo");
        replay(this.request, this.response);
        this.controller.cmeRedirect("nobody", null, this.request, this.response);
        verify(this.request, this.response);
    }
}
