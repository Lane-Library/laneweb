package edu.stanford.irt.laneweb.solr;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
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
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.SolrResultPage;

@SuppressWarnings({ "boxing", "rawtypes", "unchecked" })
public class SolrServiceTest {

    private static final class TestSolrSearchService extends SolrService {

        public TestSolrSearchService(final SolrTemplate solrTemplate, final SolrRepository solrRepository) {
            super.solrTemplate = solrTemplate;
            super.repository = solrRepository;
            super.parser = new SolrQueryParser(Collections.emptyList());
        }
    }

    private SolrRepository repository;

    private SolrService solrService;

    private SolrTemplate template;

    @Before
    public void setUp() throws Exception {
        this.repository = createMock(SolrRepository.class);
        this.template = createMock(SolrTemplate.class);
        this.solrService = new TestSolrSearchService(this.template, this.repository);
    }

    @Test
    public final void testFacetByField() {
        FacetPage<Object> fpage = createMock(FacetPage.class);
        expect(this.template.queryForFacetPage(anyObject(), anyObject())).andReturn(fpage);
        replay(this.template, fpage);
        this.solrService.facetByField("query", "filters", "field", 0, 10, 1, FacetSort.COUNT);
        verify(this.template, fpage);
        reset(this.template, fpage);
        expect(this.template.queryForFacetPage(anyObject(), anyObject())).andReturn(fpage);
        replay(this.template, fpage);
        this.solrService.facetByField("query", "", "field", 0, 10, 1, FacetSort.COUNT);
        verify(this.template, fpage);
    }

    @Test
    public final void testFacetByManyFields() {
        FacetPage<Object> fpage = createMock(FacetPage.class);
        expect(this.template.queryForFacetPage(anyObject(), anyObject())).andReturn(fpage);
        replay(this.template, fpage);
        this.solrService.facetByManyFields("query", "filters", 1);
        verify(this.template, fpage);
        reset(this.template, fpage);
        expect(this.template.queryForFacetPage(anyObject(), anyObject())).andReturn(fpage);
        replay(this.template, fpage);
        this.solrService.facetByManyFields("query", "", 1);
        verify(this.template, fpage);
    }

    @Test
    public final void testGetCore() {
        expect(this.repository.browseAllCoreByType(isA(String.class), isA(PageRequest.class)))
                .andReturn(Collections.emptyList());
        replay(this.repository);
        this.solrService.getCore("type");
        verify(this.repository);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testGetCoreException() {
        this.solrService.getCore(null);
    }

    @Test
    public final void testGetMesh() {
        expect(this.repository.browseAllByMeshAndType(isA(String.class), isA(String.class), isA(PageRequest.class)))
                .andReturn(Collections.emptyList());
        replay(this.repository);
        this.solrService.getMesh("type", "mesh");
        verify(this.repository);
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
    public final void testGetSubset() {
        expect(this.repository.browseAllBySubset(isA(String.class), isA(PageRequest.class)))
                .andReturn(Collections.emptyList());
        replay(this.repository);
        this.solrService.getSubset("subset");
        verify(this.repository);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testGetSubsetException() {
        this.solrService.getSubset(null);
    }

    @Test
    public final void testGetTypeString() {
        expect(this.repository.browseAllByType(isA(String.class), isA(PageRequest.class)))
                .andReturn(Collections.emptyList());
        replay(this.repository);
        this.solrService.getType("type");
        verify(this.repository);
    }

    @Test
    public final void testGetTypeStringChar() {
        expect(this.repository.browseByTypeTitleStartingWith(isA(String.class), isA(String.class),
                isA(PageRequest.class))).andReturn(Collections.emptyList());
        replay(this.repository);
        this.solrService.getType("type", 'a');
        verify(this.repository);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testGetTypeStringCharException() {
        this.solrService.getType(null, 'a');
    }

    @Test
    public final void testGetTypeStringCharHash() {
        PageRequest pageRequest = new PageRequest(0, Integer.MAX_VALUE);
        expect(this.repository.browseByTypeTitleStartingWith("Type", "1", pageRequest))
                .andReturn(Collections.emptyList());
        replay(this.repository);
        this.solrService.getType("type", '#');
        verify(this.repository);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testGetTypeStringException() {
        this.solrService.getType(null);
    }

    @Test
    public final void testSearchCount() {
        Set<String> types = new TreeSet<String>();
        types.add("type1");
        SolrResultPage page = createMock(SolrResultPage.class);
        Page<FacetFieldEntry> page1 = createMock(Page.class);
        FacetFieldEntry facetFieldEntry = createMock(FacetFieldEntry.class);
        Collection<Page<FacetFieldEntry>> facetResultPages = createMock(Collection.class);
        Iterator it1 = createMock(Iterator.class);
        Iterator it2 = createMock(Iterator.class);
        expect(this.repository.facetByType(isA(String.class), isA(PageRequest.class))).andReturn(page);
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
        Map<String, Integer> map = this.solrService.searchCount(types, "query");
        verify(this.repository, facetResultPages, page, it1, it2, facetFieldEntry, page1);
        assertEquals(20, (int) map.get("all"));
        assertEquals(10, (int) map.get("type1"));
    }

    @Test
    public final void testSearchFindAllNotRecordTypePubmed() {
        expect(this.repository.browseLinkscanLinks(isA(PageRequest.class))).andReturn(Collections.emptyList());
        replay(this.repository);
        this.solrService.getLinkscanLinks();
        verify(this.repository);
    }

    @Test
    public final void testSearchWithFilters() {
        Page<Eresource> page = createMock(Page.class);
        expect(this.repository.searchFindAllWithFilter("query", "field1:value AND field2:value",
                new PageRequest(0, 1, null))).andReturn(page);
        replay(this.repository, page);
        this.solrService.searchWithFilters("query", "field1:value::field2:value", new PageRequest(0, 1));
        verify(this.repository, page);
    }

    @Test
    public final void testSearchWithFiltersFacetsNull() {
        Page<Eresource> page = createMock(Page.class);
        expect(this.repository.searchFindAllWithFilter("query", "", new PageRequest(0, 1, null))).andReturn(page);
        replay(this.repository, page);
        this.solrService.searchWithFilters("query", null, new PageRequest(0, 1));
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
        this.solrService.suggestFindByType("term", "type");
        verify(this.repository);
    }
}