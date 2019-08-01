package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;

import edu.stanford.irt.solr.Image;
import edu.stanford.irt.solr.service.SolrImageService;

public class SearchImageInterceptorTest {

    private FacetFieldEntry entry;

    private SearchImageInterceptor filter;

    private FacetPage<Image> imageFacetPage;

    private Page<FacetFieldEntry> page;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private SolrImageService service;

    @Before
    public void setUp() {
        this.service = mock(SolrImageService.class);
        this.filter = new SearchImageInterceptor(this.service);
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.imageFacetPage = mock(FacetPage.class);
        this.page = mock(Page.class);
        this.entry = mock(FacetFieldEntry.class);
    }

    @Test
    public void testInternalDoFilter() throws IOException {
        reset(this.service);
        expect(this.request.getParameter("source")).andReturn("pmc-images-all");
        expect(this.request.getParameter("auto")).andReturn(null);
        expect(this.request.getParameter("q")).andReturn("query");
        expect(this.service.facetOnCopyright("query")).andReturn(this.imageFacetPage);
        expect(this.imageFacetPage.getFacetResultPage("copyright")).andReturn(this.page);
        expect(this.page.getContent()).andReturn(Collections.singletonList(this.entry));
        expect(this.entry.getValueCount()).andReturn(2L);
        expect(this.entry.getValue()).andReturn("0");
        expect(this.request.getRequestURL()).andReturn(new StringBuffer("url"));
        expect(this.request.getQueryString()).andReturn("source=pmc-images-all");
        this.response.sendRedirect("url?source=images-all&auto=no");
        replay(this.request, this.response, this.service, this.imageFacetPage, this.page, this.entry);
        assertFalse(this.filter.preHandle(this.request, this.response, null));
        verify(this.request, this.response, this.service, this.imageFacetPage, this.page, this.entry);
    }

    @Test
    public void testInternalDoFilterAutoNo() throws IOException {
        expect(this.request.getParameter("source")).andReturn("images-all");
        expect(this.request.getParameter("auto")).andReturn("no");
        replay(this.request, this.response);
        assertTrue(this.filter.preHandle(this.request, this.response, null));
        verify(this.request, this.response);
    }

    @Test
    public void testInternalDoFilterCC() throws IOException {
        reset(this.service);
        expect(this.request.getParameter("source")).andReturn("images-all");
        expect(this.request.getParameter("auto")).andReturn(null);
        expect(this.request.getParameter("q")).andReturn("query");
        expect(this.service.facetOnCopyright("query")).andReturn(this.imageFacetPage);
        expect(this.imageFacetPage.getFacetResultPage("copyright")).andReturn(this.page);
        expect(this.page.getContent()).andReturn(Collections.singletonList(this.entry));
        expect(this.entry.getValueCount()).andReturn(2L);
        expect(this.entry.getValue()).andReturn("10");
        expect(this.request.getRequestURL()).andReturn(new StringBuffer("url"));
        expect(this.request.getQueryString()).andReturn("source=images-all");
        this.response.sendRedirect("url?source=cc-images-all&auto=no");
        replay(this.request, this.response, this.service, this.imageFacetPage, this.page, this.entry);
        assertFalse(this.filter.preHandle(this.request, this.response, null));
        verify(this.request, this.response, this.service, this.imageFacetPage, this.page, this.entry);
    }

    @Test
    public void testInternalDoFilterDifferentSource() throws IOException {
        expect(this.request.getParameter("source")).andReturn("somethingelse");
        expect(this.request.getParameter("auto")).andReturn(null);
        replay(this.request, this.response);
        assertTrue(this.filter.preHandle(this.request, this.response, null));
        verify(this.request, this.response);
    }

   
    @Test
    public void testInternalDoFilterNoResults() throws IOException {
        reset(this.service);
        expect(this.request.getParameter("source")).andReturn("images-all");
        expect(this.request.getParameter("auto")).andReturn(null);
        expect(this.request.getParameter("q")).andReturn("query");
        expect(this.service.facetOnCopyright("query")).andReturn(this.imageFacetPage);
        expect(this.imageFacetPage.getFacetResultPage("copyright")).andReturn(this.page);
        expect(this.page.getContent()).andReturn(Collections.singletonList(this.entry));
        expect(this.entry.getValueCount()).andReturn(0L);
        replay(this.request, this.response, this.service, this.imageFacetPage, this.page, this.entry);
        assertTrue(this.filter.preHandle(this.request, this.response, null));
        verify(this.request, this.response, this.service, this.imageFacetPage, this.page, this.entry);
    }

    @Test
    public void testInternalDoFilterNullSource() throws IOException {
        expect(this.request.getParameter("source")).andReturn(null);
        expect(this.request.getParameter("auto")).andReturn(null);
        replay(this.request, this.response);
        assertTrue(this.filter.preHandle(this.request, this.response, null));
        verify(this.request, this.response);
    }

    @Test
    public void testInternalDoFilterPMC() throws IOException {
        reset(this.service);
        expect(this.request.getParameter("source")).andReturn("images-all");
        expect(this.request.getParameter("auto")).andReturn(null);
        expect(this.request.getParameter("q")).andReturn("query");
        expect(this.service.facetOnCopyright("query")).andReturn(this.imageFacetPage);
        expect(this.imageFacetPage.getFacetResultPage("copyright")).andReturn(this.page);
        expect(this.page.getContent()).andReturn(Collections.singletonList(this.entry));
        expect(this.entry.getValueCount()).andReturn(2L);
        expect(this.entry.getValue()).andReturn("15");
        expect(this.request.getRequestURL()).andReturn(new StringBuffer("url"));
        expect(this.request.getQueryString()).andReturn("source=images-all");
        this.response.sendRedirect("url?source=pmc-images-all&auto=no");
        replay(this.request, this.response, this.service, this.imageFacetPage, this.page, this.entry);
        assertFalse(this.filter.preHandle(this.request, this.response, null));
        verify(this.request, this.response, this.service, this.imageFacetPage, this.page, this.entry);
    }

    @Test
    public void testInternalDoFilterRL() throws IOException {
        reset(this.service);
        expect(this.request.getParameter("source")).andReturn("images-all");
        expect(this.request.getParameter("auto")).andReturn(null);
        expect(this.request.getParameter("q")).andReturn("query");
        expect(this.service.facetOnCopyright("query")).andReturn(this.imageFacetPage);
        expect(this.imageFacetPage.getFacetResultPage("copyright")).andReturn(this.page);
        expect(this.page.getContent()).andReturn(Collections.singletonList(this.entry));
        expect(this.entry.getValueCount()).andReturn(2L);
        expect(this.entry.getValue()).andReturn("20");
        expect(this.request.getRequestURL()).andReturn(new StringBuffer("url"));
        expect(this.request.getQueryString()).andReturn("source=images-all");
        this.response.sendRedirect("url?source=rl-images-all&auto=no");
        replay(this.request, this.response, this.service, this.imageFacetPage, this.page, this.entry);
        assertFalse(this.filter.preHandle(this.request, this.response, null));
        verify(this.request, this.response, this.service, this.imageFacetPage, this.page, this.entry);
    }

    @Test
    public void testInternalDoFilterSourceHasContent() throws IOException {
        reset(this.service);
        expect(this.request.getParameter("source")).andReturn("images-all");
        expect(this.request.getParameter("auto")).andReturn(null);
        expect(this.request.getParameter("q")).andReturn("query");
        expect(this.service.facetOnCopyright("query")).andReturn(this.imageFacetPage);
        expect(this.imageFacetPage.getFacetResultPage("copyright")).andReturn(this.page);
        expect(this.page.getContent()).andReturn(Collections.singletonList(this.entry));
        expect(this.entry.getValueCount()).andReturn(2L);
        expect(this.entry.getValue()).andReturn("0");
        replay(this.request, this.response, this.service, this.imageFacetPage, this.page, this.entry);
        assertTrue(this.filter.preHandle(this.request, this.response, null));
        verify(this.request, this.response, this.service, this.imageFacetPage, this.page, this.entry);
    }
}
