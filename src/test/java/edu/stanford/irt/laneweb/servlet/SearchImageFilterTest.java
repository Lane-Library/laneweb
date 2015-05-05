package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.web.context.WebApplicationContext;

import edu.stanford.irt.solr.Image;
import edu.stanford.irt.solr.service.SolrImageService;

public class SearchImageFilterTest {

    private WebApplicationContext applicationContext;

    private FilterChain chain;

    private FilterConfig config;

    private FacetFieldEntry entry;

    private SearchImageFilter filter;

    private FacetPage<Image> imageFacetPage;

    private Page<FacetFieldEntry> page;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private SolrImageService service;

    private ServletContext servletContext;

    @Before
    public void setUp() {
        this.filter = new SearchImageFilter();
        this.config = createMock(FilterConfig.class);
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.chain = createMock(FilterChain.class);
        this.servletContext = createMock(ServletContext.class);
        this.applicationContext = createMock(WebApplicationContext.class);
        this.service = createMock(SolrImageService.class);
        this.imageFacetPage = createMock(FacetPage.class);
        this.page = createMock(Page.class);
        this.entry = createMock(FacetFieldEntry.class);
    }

    @Test
    public void testInit() {
        expect(this.config.getServletContext()).andReturn(this.servletContext);
        expect(this.servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE))
                .andReturn(this.applicationContext);
        expect(this.applicationContext.getBean("edu.stanford.irt.solr.service", SolrImageService.class)).andReturn(
                this.service);
        replay(this.config, this.servletContext, this.applicationContext, this.service);
        this.filter.init(this.config);
        verify(this.config, this.servletContext, this.applicationContext, this.service);
    }

    @Test
    public void testInternalDoFilter() throws IOException, ServletException {
        testInit();
        reset(this.service);
        expect(this.request.getParameter("source")).andReturn("pmc-images-all");
        expect(this.request.getParameter("auto")).andReturn(null);
        expect(this.request.getParameter("q")).andReturn("query");
        expect(this.service.facetOnCopyright("query")).andReturn(this.imageFacetPage);
        expect(this.imageFacetPage.getFacetResultPage("copyright")).andReturn(this.page);
        expect(this.page.getContent()).andReturn(Collections.singletonList(this.entry));
        expect(this.entry.getValueCount()).andReturn(2L).times(2);
        expect(this.entry.getValue()).andReturn("0");
        expect(this.request.getRequestURL()).andReturn(new StringBuffer("url"));
        expect(this.request.getQueryString()).andReturn("source=pmc-images-all");
        this.response.sendRedirect("url?source=images-all");
        replay(this.request, this.response, this.chain, this.service, this.imageFacetPage, this.page, this.entry);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.request, this.response, this.chain, this.service, this.imageFacetPage, this.page, this.entry);
    }

    @Test
    public void testInternalDoFilterAutoNo() throws IOException, ServletException {
        expect(this.request.getParameter("source")).andReturn("images-all");
        expect(this.request.getParameter("auto")).andReturn("no");
        this.chain.doFilter(this.request, this.response);
        replay(this.request, this.response, this.chain);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.request, this.response, this.chain);
    }

    @Test
    public void testInternalDoFilterCC() throws IOException, ServletException {
        testInit();
        reset(this.service);
        expect(this.request.getParameter("source")).andReturn("images-all");
        expect(this.request.getParameter("auto")).andReturn(null);
        expect(this.request.getParameter("q")).andReturn("query");
        expect(this.service.facetOnCopyright("query")).andReturn(this.imageFacetPage);
        expect(this.imageFacetPage.getFacetResultPage("copyright")).andReturn(this.page);
        expect(this.page.getContent()).andReturn(Collections.singletonList(this.entry));
        expect(this.entry.getValueCount()).andReturn(2L).times(2);
        expect(this.entry.getValue()).andReturn("10");
        expect(this.request.getRequestURL()).andReturn(new StringBuffer("url"));
        expect(this.request.getQueryString()).andReturn("source=images-all");
        this.response.sendRedirect("url?source=cc-images-all");
        replay(this.request, this.response, this.chain, this.service, this.imageFacetPage, this.page, this.entry);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.request, this.response, this.chain, this.service, this.imageFacetPage, this.page, this.entry);
    }

    @Test
    public void testInternalDoFilterDifferentSource() throws IOException, ServletException {
        expect(this.request.getParameter("source")).andReturn("somethingelse");
        expect(this.request.getParameter("auto")).andReturn(null);
        this.chain.doFilter(this.request, this.response);
        replay(this.request, this.response, this.chain);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.request, this.response, this.chain);
    }

    @Test
    public void testInternalDoFilterEmptySource() throws IOException, ServletException {
        testInit();
        reset(this.service);
        expect(this.request.getParameter("source")).andReturn("images-all");
        expect(this.request.getParameter("auto")).andReturn(null);
        expect(this.request.getParameter("q")).andReturn("query");
        expect(this.service.facetOnCopyright("query")).andReturn(this.imageFacetPage);
        expect(this.imageFacetPage.getFacetResultPage("copyright")).andReturn(this.page);
        expect(this.page.getContent()).andReturn(Collections.singletonList(this.entry));
        expect(this.entry.getValueCount()).andReturn(2L).times(2);
        expect(this.entry.getValue()).andReturn("X");
        expect(this.request.getRequestURL()).andReturn(new StringBuffer("url"));
        expect(this.request.getQueryString()).andReturn("source=images-all");
        this.response.sendRedirect("url?source=");
        replay(this.request, this.response, this.chain, this.service, this.imageFacetPage, this.page, this.entry);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.request, this.response, this.chain, this.service, this.imageFacetPage, this.page, this.entry);
    }

    @Test
    public void testInternalDoFilterNoResults() throws IOException, ServletException {
        testInit();
        reset(this.service);
        expect(this.request.getParameter("source")).andReturn("images-all");
        expect(this.request.getParameter("auto")).andReturn(null);
        expect(this.request.getParameter("q")).andReturn("query");
        expect(this.service.facetOnCopyright("query")).andReturn(this.imageFacetPage);
        expect(this.imageFacetPage.getFacetResultPage("copyright")).andReturn(this.page);
        expect(this.page.getContent()).andReturn(Collections.singletonList(this.entry));
        expect(this.entry.getValueCount()).andReturn(0L);
        this.chain.doFilter(this.request, this.response);
        replay(this.request, this.response, this.chain, this.service, this.imageFacetPage, this.page, this.entry);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.request, this.response, this.chain, this.service, this.imageFacetPage, this.page, this.entry);
    }

    @Test
    public void testInternalDoFilterNullSource() throws IOException, ServletException {
        expect(this.request.getParameter("source")).andReturn(null);
        expect(this.request.getParameter("auto")).andReturn(null);
        this.chain.doFilter(this.request, this.response);
        replay(this.request, this.response, this.chain);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.request, this.response, this.chain);
    }

    @Test
    public void testInternalDoFilterPMC() throws IOException, ServletException {
        testInit();
        reset(this.service);
        expect(this.request.getParameter("source")).andReturn("images-all");
        expect(this.request.getParameter("auto")).andReturn(null);
        expect(this.request.getParameter("q")).andReturn("query");
        expect(this.service.facetOnCopyright("query")).andReturn(this.imageFacetPage);
        expect(this.imageFacetPage.getFacetResultPage("copyright")).andReturn(this.page);
        expect(this.page.getContent()).andReturn(Collections.singletonList(this.entry));
        expect(this.entry.getValueCount()).andReturn(2L).times(2);
        expect(this.entry.getValue()).andReturn("15");
        expect(this.request.getRequestURL()).andReturn(new StringBuffer("url"));
        expect(this.request.getQueryString()).andReturn("source=images-all");
        this.response.sendRedirect("url?source=pmc-images-all");
        replay(this.request, this.response, this.chain, this.service, this.imageFacetPage, this.page, this.entry);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.request, this.response, this.chain, this.service, this.imageFacetPage, this.page, this.entry);
    }

    @Test
    public void testInternalDoFilterRL() throws IOException, ServletException {
        testInit();
        reset(this.service);
        expect(this.request.getParameter("source")).andReturn("images-all");
        expect(this.request.getParameter("auto")).andReturn(null);
        expect(this.request.getParameter("q")).andReturn("query");
        expect(this.service.facetOnCopyright("query")).andReturn(this.imageFacetPage);
        expect(this.imageFacetPage.getFacetResultPage("copyright")).andReturn(this.page);
        expect(this.page.getContent()).andReturn(Collections.singletonList(this.entry));
        expect(this.entry.getValueCount()).andReturn(2L).times(2);
        expect(this.entry.getValue()).andReturn("20");
        expect(this.request.getRequestURL()).andReturn(new StringBuffer("url"));
        expect(this.request.getQueryString()).andReturn("source=images-all");
        this.response.sendRedirect("url?source=rl-images-all");
        replay(this.request, this.response, this.chain, this.service, this.imageFacetPage, this.page, this.entry);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.request, this.response, this.chain, this.service, this.imageFacetPage, this.page, this.entry);
    }

    @Test
    public void testInternalDoFilterSourceHasContent() throws IOException, ServletException {
        testInit();
        reset(this.service);
        expect(this.request.getParameter("source")).andReturn("images-all");
        expect(this.request.getParameter("auto")).andReturn(null);
        expect(this.request.getParameter("q")).andReturn("query");
        expect(this.service.facetOnCopyright("query")).andReturn(this.imageFacetPage);
        expect(this.imageFacetPage.getFacetResultPage("copyright")).andReturn(this.page);
        expect(this.page.getContent()).andReturn(Collections.singletonList(this.entry));
        expect(this.entry.getValueCount()).andReturn(2L).times(2);
        expect(this.entry.getValue()).andReturn("0");
        this.chain.doFilter(this.request, this.response);
        replay(this.request, this.response, this.chain, this.service, this.imageFacetPage, this.page, this.entry);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.request, this.response, this.chain, this.service, this.imageFacetPage, this.page, this.entry);
    }
}
