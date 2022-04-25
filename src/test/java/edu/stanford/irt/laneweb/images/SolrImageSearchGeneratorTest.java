package edu.stanford.irt.laneweb.images;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.solr.Image;
import edu.stanford.irt.solr.service.SolrImageService;

public class SolrImageSearchGeneratorTest {

    private FacetPage<Image> facetPage;

    private SolrImageSearchGenerator generator;

    private Map<String, Object> model;

    private SAXStrategy<SolrImageSearchResult> saxStrategy;

    private SolrImageService service;

    @Before
    public void setUp() {
        this.service = mock(SolrImageService.class);
        this.saxStrategy = mock(SAXStrategy.class);
        this.facetPage = mock(FacetPage.class);
        this.generator = new SolrImageSearchGenerator(this.service, this.saxStrategy);
        this.model = new HashMap<>();
        this.model.put(Model.QUERY, "query");
    }

    @Test
    public void testDoSearchCategoryCC() {
        this.model.put(Model.QUERY, "query");
        this.model.put(Model.SOURCE, "cc-");
        this.model.put(Model.BASE_PATH, "");
        Capture<Pageable> pageable = newCapture();
        expect(this.service.findByTitleAndDescriptionFilterOnCopyright(eq("query"), eq("10"), capture(pageable)))
                .andReturn(null);
        expect(this.service.facetOnWebsiteId(eq("query"), eq("10"))).andReturn(this.facetPage);
        replay(this.service, this.saxStrategy);
        this.generator.setModel(this.model);
        SolrImageSearchResult result = this.generator.doSearch("query");
        assertEquals("/search.html?q=query&source=cc-", result.getPath());
        assertEquals("query", result.getQuery());
        assertEquals("Broad Reuse Rights", result.getTab());
        assertEquals(50, pageable.getValue().getPageSize());
        assertEquals(0, pageable.getValue().getPageNumber());
        verify(this.service, this.saxStrategy);
    }

    @Test
    public void testDoSearchCategoryOther() {
        this.model.put(Model.QUERY, "query");
        this.model.put(Model.SOURCE, "images-");
        Capture<Pageable> pageable = newCapture();
        expect(this.service.findByTitleAndDescriptionFilterOnCopyright(eq("query"), eq("0"), capture(pageable)))
                .andReturn(null);
        expect(this.service.facetOnWebsiteId(eq("query"), eq("0"))).andReturn(this.facetPage);
        replay(this.service, this.saxStrategy);
        this.generator.setModel(this.model);
        SolrImageSearchResult result = this.generator.doSearch("query");
        assertEquals("/search.html?q=query&source=images-", result.getPath());
        assertEquals("query", result.getQuery());
        assertEquals("Maximum Reuse Rights", result.getTab());
        assertEquals(50, pageable.getValue().getPageSize());
        assertEquals(0, pageable.getValue().getPageNumber());
        verify(this.service, this.saxStrategy);
    }

    @Test
    public void testDoSearchCategoryPMC() {
        this.model.put(Model.QUERY, "query");
        this.model.put(Model.SOURCE, "pmc-");
        Capture<Pageable> pageable = newCapture();
        expect(this.service.findByTitleAndDescriptionFilterOnCopyright(eq("query"), eq("15"), capture(pageable)))
                .andReturn(null);
        expect(this.service.facetOnWebsiteId(eq("query"), eq("15"))).andReturn(this.facetPage);
        replay(this.service, this.saxStrategy);
        this.generator.setModel(this.model);
        SolrImageSearchResult result = this.generator.doSearch("query");
        assertEquals("/search.html?q=query&source=pmc-", result.getPath());
        assertEquals("query", result.getQuery());
        assertEquals("Possible Reuse Rights", result.getTab());
        assertEquals(50, pageable.getValue().getPageSize());
        assertEquals(0, pageable.getValue().getPageNumber());
        verify(this.service, this.saxStrategy);
    }

    @Test
    public void testDoSearchCategoryRL() {
        this.model.put(Model.QUERY, "query");
        this.model.put(Model.SOURCE, "rl-");
        Capture<Pageable> pageable = newCapture();
        expect(this.service.findByTitleAndDescriptionFilterOnCopyright(eq("query"), eq("20"), capture(pageable)))
                .andReturn(null);
        expect(this.service.facetOnWebsiteId(eq("query"), eq("20"))).andReturn(this.facetPage);
        replay(this.service, this.saxStrategy);
        this.generator.setModel(this.model);
        SolrImageSearchResult result = this.generator.doSearch("query");
        assertEquals("/search.html?q=query&source=rl-", result.getPath());
        assertEquals("query", result.getQuery());
        assertEquals("Restrictive Reuse Rights", result.getTab());
        assertEquals(50, pageable.getValue().getPageSize());
        assertEquals(0, pageable.getValue().getPageNumber());
        verify(this.service, this.saxStrategy);
    }

    @Test
    public void testDoSearchDefault() {
        Capture<Pageable> pageable = newCapture();
        expect(this.service.findByTitleAndDescriptionFilterOnCopyright(eq("query"), eq("0"), capture(pageable)))
                .andReturn(null);
        expect(this.service.facetOnWebsiteId(eq("query"), eq("0"))).andReturn(this.facetPage);
        replay(this.service, this.saxStrategy);
        this.generator.setModel(this.model);
        this.generator.doSearch("query");
        assertEquals(50, pageable.getValue().getPageSize());
        assertEquals(0, pageable.getValue().getPageNumber());
        verify(this.service, this.saxStrategy);
    }

    @Test
    public void testDoSearchPage() {
        this.model.put(Model.PAGE, "2");
        Capture<Pageable> pageable = newCapture();
        expect(this.service.findByTitleAndDescriptionFilterOnCopyright(eq("query"), eq("0"), capture(pageable)))
                .andReturn(null);
        expect(this.service.facetOnWebsiteId(eq("query"), eq("0"))).andReturn(this.facetPage);
        replay(this.service, this.saxStrategy);
        this.generator.setModel(this.model);
        this.generator.doSearch("query");
        assertEquals(50, pageable.getValue().getPageSize());
        assertEquals(1, pageable.getValue().getPageNumber());
        verify(this.service, this.saxStrategy);
    }
}
