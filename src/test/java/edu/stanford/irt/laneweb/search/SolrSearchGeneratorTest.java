package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
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

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.solr.SolrSearchService;

public class SolrSearchGeneratorTest {

    private SolrSearchGenerator generator;

    private Map<String, Object> model;

    private SAXStrategy<Map<String, Object>> saxStrategy;

    private SolrSearchService service;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        this.service = createMock(SolrSearchService.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new SolrSearchGenerator(this.service, this.saxStrategy);
        this.model = new HashMap<String, Object>();
    }

    @Test
    public void testDoSearch() {
        this.model.put(Model.QUERY, "query");
        this.model.put(Model.FACETS, "facets");
        Capture<Pageable> pageable = newCapture();
        expect(this.service.searchWithFilters(eq("query"), eq("facets"), capture(pageable))).andReturn(null);
        replay(this.service, this.saxStrategy);
        this.generator.setModel(this.model);
        Map<String, Object> result = this.generator.doSearch("query");
        assertEquals("query", result.get("searchTerm"));
        assertEquals(50, pageable.getValue().getPageSize());
        assertEquals(0, pageable.getValue().getPageNumber());
        assertEquals(null, pageable.getValue().getSort());
        verify(this.service, this.saxStrategy);
    }

    @Test
    public void testDoSearchWithPageNumber() {
        this.model.put(Model.QUERY, "query");
        this.model.put(Model.PAGE, "5");
        Capture<Pageable> pageable = newCapture();
        expect(this.service.searchWithFilters(eq("query"), eq(null), capture(pageable))).andReturn(null);
        replay(this.service, this.saxStrategy);
        this.generator.setModel(this.model);
        Map<String, Object> result = this.generator.doSearch("query");
        assertEquals("query", result.get("searchTerm"));
        assertEquals(50, pageable.getValue().getPageSize());
        assertEquals(4, pageable.getValue().getPageNumber());
        verify(this.service, this.saxStrategy);
    }

    @Test
    public void testDoSearchWithSort() {
        this.model.put(Model.QUERY, "query");
        this.model.put(Model.FACETS, "recordType:\"pubmed\"");
        this.model.put(Model.SORT, "authors_sort asc,title_sort asc");
        Capture<Pageable> pageable = newCapture();
        expect(this.service.searchWithFilters(eq("query"), eq("recordType:\"pubmed\""), capture(pageable))).andReturn(
                null);
        replay(this.service, this.saxStrategy);
        this.generator.setModel(this.model);
        Map<String, Object> result = this.generator.doSearch("query");
        assertEquals("query", result.get("searchTerm"));
        assertEquals(50, pageable.getValue().getPageSize());
        assertEquals(0, pageable.getValue().getPageNumber());
        assertEquals("authors_sort: ASC,title_sort: ASC", pageable.getValue().getSort().toString());
        verify(this.service, this.saxStrategy);
    }

    @Test
    public void testDoSearchWithType() {
        this.model.put(Model.QUERY, "query");
        this.model.put(Model.TYPE, "type");
        this.model.put(Model.SORT, "unparsable");
        Capture<Pageable> pageable = newCapture();
        expect(this.service.searchType(eq("type"), eq("query"), capture(pageable))).andReturn(null);
        replay(this.service, this.saxStrategy);
        this.generator.setModel(this.model);
        Map<String, Object> result = this.generator.doSearch("query");
        assertEquals("query", result.get("searchTerm"));
        assertEquals(50, pageable.getValue().getPageSize());
        assertEquals(0, pageable.getValue().getPageNumber());
        assertEquals(null, pageable.getValue().getSort());
        verify(this.service, this.saxStrategy);
    }
}