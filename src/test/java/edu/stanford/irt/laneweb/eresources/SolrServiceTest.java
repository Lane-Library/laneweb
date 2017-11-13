package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.FacetOptions.FacetSort;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.result.Cursor;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.SolrResultPage;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SolrServiceTest {

    private Cursor cursor;

    private SolrRepository repository;

    private SolrService solrService;

    private SolrTemplate template;

    @Before
    public void setUp() throws Exception {
        this.repository = createMock(SolrRepository.class);
        this.template = createMock(SolrTemplate.class);
        this.solrService = new SolrService(new SolrQueryParser(Collections.emptyList()), this.repository,
                this.template);
        this.cursor = createMock(Cursor.class);
    }

    @Test
    public final void testFacetByField() {
        FacetPage<Object> fpage = createMock(FacetPage.class);
        expect(this.template.queryForFacetPage(anyObject(), anyObject(), anyObject())).andReturn(fpage);
        replay(this.template, fpage);
        this.solrService.facetByField("query", "filters", "field", 0, 10, 1, FacetSort.COUNT);
        verify(this.template, fpage);
        reset(this.template, fpage);
        expect(this.template.queryForFacetPage(anyObject(), anyObject(), anyObject())).andReturn(fpage);
        replay(this.template, fpage);
        this.solrService.facetByField("query", "", "field", 0, 10, 1, FacetSort.COUNT);
        verify(this.template, fpage);
    }

    @Test
    public final void testFacetByManyFields() {
        FacetPage<Object> fpage = createMock(FacetPage.class);
        expect(this.template.queryForFacetPage(anyObject(), anyObject(), anyObject())).andReturn(fpage);
        replay(this.template, fpage);
        this.solrService.facetByManyFields("query", "filters", 1);
        verify(this.template, fpage);
        reset(this.template, fpage);
        expect(this.template.queryForFacetPage(anyObject(), anyObject(), anyObject())).andReturn(fpage);
        replay(this.template, fpage);
        this.solrService.facetByManyFields("query", "", 1);
        verify(this.template, fpage);
    }

    @Test
    public final void testGetCore() {
        expect(this.template.queryForCursor(anyObject(), isA(Query.class), anyObject())).andReturn(this.cursor);
        expect(this.cursor.hasNext()).andReturn(false);
        replay(this.template, this.cursor);
        this.solrService.getCore("type");
        verify(this.template, this.cursor);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testGetCoreException() {
        this.solrService.getCore(null);
    }

    @Test
    public final void testGetMesh() {
        expect(this.template.queryForCursor(anyObject(), isA(Query.class), anyObject())).andReturn(this.cursor);
        expect(this.cursor.hasNext()).andReturn(false);
        replay(this.template, this.cursor);
        this.solrService.getMesh("type", "mesh");
        verify(this.template, this.cursor);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testGetMeshException1() {
        this.solrService.getMesh("type", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testGetMeshException2() {
        this.solrService.getMesh(null, "mesh");
    }

    @Test
    public final void testGetTypeString() {
        expect(this.template.queryForCursor(anyObject(), isA(Query.class), anyObject())).andReturn(this.cursor);
        expect(this.cursor.hasNext()).andReturn(false);
        replay(this.template, this.cursor);
        this.solrService.getType("type");
        verify(this.template, this.cursor);
    }

    @Test
    public final void testGetTypeStringChar() {
        expect(this.template.queryForCursor(anyObject(), isA(Query.class), anyObject())).andReturn(this.cursor);
        expect(this.cursor.hasNext()).andReturn(false);
        replay(this.template, this.cursor);
        this.solrService.getType("type", 'a');
        verify(this.template, this.cursor);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testGetTypeStringCharException() {
        this.solrService.getType(null, 'a');
    }

    @Test
    public final void testGetTypeStringCharHash() {
        expect(this.template.queryForCursor(anyObject(), isA(Query.class), anyObject())).andReturn(this.cursor);
        expect(this.cursor.hasNext()).andReturn(false);
        replay(this.template, this.cursor);
        this.solrService.getType("type", '#');
        verify(this.template, this.cursor);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testGetTypeStringException() {
        this.solrService.getType(null);
    }

    @Test
    public final void testSearchCount() {
        Set<String> types = new TreeSet<>();
        types.add("type1");
        SolrResultPage page = createMock(SolrResultPage.class);
        Page<FacetFieldEntry> page1 = createMock(Page.class);
        FacetFieldEntry facetFieldEntry = createMock(FacetFieldEntry.class);
        Collection<Page<FacetFieldEntry>> facetResultPages = createMock(Collection.class);
        Iterator it1 = createMock(Iterator.class);
        Iterator it2 = createMock(Iterator.class);
        expect(this.repository.facetByType(eq("query"), isA(PageRequest.class))).andReturn(page);
        expect(page.getTotalElements()).andReturn((long) 20);
        expect(page.getFacetResultPages()).andReturn(facetResultPages);
        expect(facetResultPages.iterator()).andReturn(it1);
        expect(it1.hasNext()).andReturn(true);
        expect(it1.next()).andReturn(page1);
        expect(page1.iterator()).andReturn(it2);
        expect(it2.hasNext()).andReturn(true);
        expect(it2.next()).andReturn(facetFieldEntry);
        expect(facetFieldEntry.getValueCount()).andReturn((long) 10);
        expect(facetFieldEntry.getValue()).andReturn("type1");
        expect(it2.hasNext()).andReturn(true);
        expect(it2.next()).andReturn(facetFieldEntry);
        expect(facetFieldEntry.getValueCount()).andReturn((long) 10);
        expect(facetFieldEntry.getValue()).andReturn("Type1");
        expect(it2.hasNext()).andReturn(false);
        expect(it1.hasNext()).andReturn(false);
        replay(this.repository, facetResultPages, page, it1, it2, facetFieldEntry, page1);
        Map<String, Long> map = this.solrService.searchCount("query");
        verify(this.repository, facetResultPages, page, it1, it2, facetFieldEntry, page1);
        assertEquals(20, map.get("all").longValue());
        assertEquals(10, map.get("type1").longValue());
    }

    @Test
    public final void testSearchWithFilters() {
        Page<Eresource> page = createMock(Page.class);
        PageRequest pr = createMock(PageRequest.class);
        expect(this.repository.searchFindAllWithFilter("query", "field1:value AND field2:value", pr)).andReturn(page);
        replay(this.repository, page);
        this.solrService.searchWithFilters("query", "field1:value::field2:value", pr);
        verify(this.repository, page);
    }

    @Test
    public final void testSearchWithFiltersFacetsNull() {
        Page<Eresource> page = createMock(Page.class);
        PageRequest pr = createMock(PageRequest.class);
        expect(this.repository.searchFindAllWithFilter("query", "", pr)).andReturn(page);
        replay(this.repository, page);
        this.solrService.searchWithFilters("query", null, pr);
        verify(this.repository, page);
    }

    @Test
    public final void testSuggestFindAll() {
        expect(this.repository.suggestFindAll("query terms", "query +terms", new PageRequest(0, 10)))
                .andReturn(Collections.emptyList());
        replay(this.repository);
        this.solrService.suggestFindAll("query terms");
        verify(this.repository);
    }

    @Test
    public final void testSuggestFindByType() {
        expect(this.repository.suggestFindByType("term", "Type", new PageRequest(0, 10)))
                .andReturn(Collections.emptyList());
        replay(this.repository);
        this.solrService.suggestFindByType("term", "Type");
        verify(this.repository);
    }
}
