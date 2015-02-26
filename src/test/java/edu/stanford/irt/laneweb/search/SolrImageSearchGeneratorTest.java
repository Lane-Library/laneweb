package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.solr.Image;
import edu.stanford.irt.solr.service.SolrImageService;

public class SolrImageSearchGeneratorTest {

    private SolrImageSearchGenerator generator;

    private Map<String, Object> model;

    private SAXStrategy<Map<String, Object>> saxStrategy;

    private SolrImageService service;

    private FacetPage<Image> facetPage;
    
    private Page<FacetFieldEntry> facetEntry;
    
    
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        this.service = createMock(SolrImageService.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.facetPage =  createMock(FacetPage.class);
        this.facetEntry = createMock(Page.class);
        this.generator = new SolrImageSearchGenerator(this.service, this.saxStrategy);
        this.model = new HashMap<String, Object>();
    }

    @Test
    public void testDoSearchCategoryCC() {
        this.model.put(Model.QUERY, "query");
        this.model.put(Model.SOURCE, "cc-");
        Capture<Pageable> pageable = newCapture();
        expect(this.service.findByTitleOrDescriptionFilterOnCopyright(eq("query"), eq("10"), capture(pageable)))
        .andReturn(null);
        expect(this.service.facetOnWebsiteId(eq("query"), eq("10"))).andReturn(this.facetPage);
        replay(this.service, this.saxStrategy);
        this.generator.setModel(this.model);
        Map<String, Object> result = this.generator.doSearch("query");
        assertEquals("/search.html?q=query&source=cc-&page=", result.get("path"));
        assertEquals("query", result.get(Model.QUERY));
        assertEquals("Broad Reuse Rights", result.get("tab"));
        assertEquals(52, pageable.getValue().getPageSize());
        assertEquals(0, pageable.getValue().getPageNumber());
        verify(this.service, this.saxStrategy);
    }

    @Test
    public void testDoSearchCategoryOther() {
        this.model.put(Model.QUERY, "query");
        this.model.put(Model.SOURCE, "foo");
        Capture<Pageable> pageable = newCapture();
        expect(this.service.findByTitleOrDescriptionFilterOnCopyright(eq("query"), eq("0"), capture(pageable)))
        .andReturn(null);
        expect(this.service.facetOnWebsiteId(eq("query"), eq("0"))).andReturn(this.facetPage);
        replay(this.service, this.saxStrategy);
        this.generator.setModel(this.model);
        Map<String, Object> result = this.generator.doSearch("query");
        assertEquals("/search.html?q=query&source=foo&page=", result.get("path"));
        assertEquals("query", result.get(Model.QUERY));
        assertEquals("Maximum Reuse Rights", result.get("tab"));
        assertEquals(52, pageable.getValue().getPageSize());
        assertEquals(0, pageable.getValue().getPageNumber());
        verify(this.service, this.saxStrategy);
    }

    @Test
    public void testDoSearchCategoryPMC() {
        this.model.put(Model.QUERY, "query");
        this.model.put(Model.SOURCE, "pmc-");
        Capture<Pageable> pageable = newCapture();
        expect(this.service.findByTitleOrDescriptionFilterOnCopyright(eq("query"), eq("15"), capture(pageable)))
        .andReturn(null);
        expect(this.service.facetOnWebsiteId(eq("query"), eq("15"))).andReturn(this.facetPage);
        replay(this.service, this.saxStrategy);
        this.generator.setModel(this.model);
        Map<String, Object> result = this.generator.doSearch("query");
        assertEquals("/search.html?q=query&source=pmc-&page=", result.get("path"));
        assertEquals("query", result.get(Model.QUERY));
        assertEquals("Possibly Reuse Rights", result.get("tab"));
        assertEquals(52, pageable.getValue().getPageSize());
        assertEquals(0, pageable.getValue().getPageNumber());
        verify(this.service, this.saxStrategy);
    }

    @Test
    public void testDoSearchCategoryRL() {
        this.model.put(Model.QUERY, "query");
        this.model.put(Model.SOURCE, "rl-");
        Capture<Pageable> pageable = newCapture();
        expect(this.service.findByTitleOrDescriptionFilterOnCopyright(eq("query"), eq("20"), capture(pageable)))
        .andReturn(null);
        expect(this.service.facetOnWebsiteId(eq("query"), eq("20"))).andReturn(this.facetPage);
        replay(this.service, this.saxStrategy);
        this.generator.setModel(this.model);
        Map<String, Object> result = this.generator.doSearch("query");
        assertEquals("/search.html?q=query&source=rl-&page=", result.get("path"));
        assertEquals("query", result.get(Model.QUERY));
        assertEquals("Restrictive Reuse Rights", result.get("tab"));
        assertEquals(52, pageable.getValue().getPageSize());
        assertEquals(0, pageable.getValue().getPageNumber());
        verify(this.service, this.saxStrategy);
    }

    @Test
    public void testDoSearchDefault() {
        Capture<Pageable> pageable = newCapture();
        expect(this.service.findByTitleOrDescriptionFilterOnCopyright(eq("query"), eq("0"), capture(pageable)))
        .andReturn(null);
        expect(this.service.facetOnWebsiteId(eq("query"), eq("0"))).andReturn(this.facetPage);
        replay(this.service, this.saxStrategy);
        this.generator.setModel(this.model);
        this.generator.doSearch("query");
        assertEquals(52, pageable.getValue().getPageSize());
        assertEquals(0, pageable.getValue().getPageNumber());
        verify(this.service, this.saxStrategy);
    }

    @Test
    public void testDoSearchPage() {
        this.model.put(Model.PAGE, "2");
        Capture<Pageable> pageable = newCapture();
        expect(this.service.findByTitleOrDescriptionFilterOnCopyright(eq("query"), eq("0"), capture(pageable)))
        .andReturn(null);
        expect(this.service.facetOnWebsiteId(eq("query"), eq("0"))).andReturn(this.facetPage);
        replay(this.service, this.saxStrategy);
        this.generator.setModel(this.model);
        this.generator.doSearch("query");
        assertEquals(52, pageable.getValue().getPageSize());
        assertEquals(1, pageable.getValue().getPageNumber());
        verify(this.service, this.saxStrategy);
    }
}
