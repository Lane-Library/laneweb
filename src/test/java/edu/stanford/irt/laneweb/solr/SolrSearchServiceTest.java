package edu.stanford.irt.laneweb.solr;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
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
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.SolrResultPage;

import edu.stanford.irt.laneweb.eresources.Eresource;

@SuppressWarnings({ "boxing", "rawtypes", "unchecked" })
public class SolrSearchServiceTest {

    private static final class TestSolrSearchService extends SolrSearchService {

        public TestSolrSearchService(final SolrTemplate solrTemplate, final SolrRepository solrRepository) {
            super.solrTemplate = solrTemplate;
            super.repository = solrRepository;
        }
    }

    private SolrRepository repository;

    private SolrSearchService searchService;

    private SolrTemplate template;

    @Before
    public void setUp() throws Exception {
        this.repository = createMock(SolrRepository.class);
        this.template = createMock(SolrTemplate.class);
        this.searchService = new TestSolrSearchService(this.template, this.repository);
    }

    @Test
    public final void testGetCore() {
        expect(this.repository.browseAllCoreByType(isA(String.class), isA(PageRequest.class))).andReturn(
                Collections.emptyList());
        replay(this.repository);
        this.searchService.getCore("type");
        verify(this.repository);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testGetCoreException() {
        this.searchService.getCore(null);
    }

    @Test
    public final void testGetMesh() {
        expect(this.repository.browseAllByMeshAndType(isA(String.class), isA(String.class), isA(PageRequest.class)))
        .andReturn(Collections.emptyList());
        replay(this.repository);
        this.searchService.getMesh("type", "mesh");
        verify(this.repository);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testGetMeshException1() {
        this.searchService.getMesh("type", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testGetMeshException2() {
        this.searchService.getMesh(null, "mesh");
    }

    @Test
    public final void testGetSubset() {
        expect(this.repository.browseAllBySubset(isA(String.class), isA(PageRequest.class))).andReturn(
                Collections.emptyList());
        replay(this.repository);
        this.searchService.getSubset("subset");
        verify(this.repository);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testGetSubsetException() {
        this.searchService.getSubset(null);
    }

    @Test
    public final void testGetTypeString() {
        expect(this.repository.browseAllByType(isA(String.class), isA(PageRequest.class))).andReturn(
                Collections.emptyList());
        replay(this.repository);
        this.searchService.getType("type");
        verify(this.repository);
    }

    @Test
    public final void testGetTypeStringChar() {
        expect(
                this.repository.browseByTypeTitleStartingWith(isA(String.class), isA(String.class),
                        isA(PageRequest.class))).andReturn(Collections.emptyList());
        replay(this.repository);
        this.searchService.getType("type", 'a');
        verify(this.repository);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testGetTypeStringCharException() {
        this.searchService.getType(null, 'a');
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testGetTypeStringException() {
        this.searchService.getType(null);
    }

    @Test
    public final void testSearch() {
        Page<Eresource> page = createMock(Page.class);
        expect(this.repository.searchFindAllWithFilter(isA(String.class), isA(String.class), isA(PageRequest.class)))
        .andReturn(page);
        expect(page.getContent()).andReturn(Collections.emptyList());
        replay(this.repository, page);
        this.searchService.search("query");
        verify(this.repository, page);
    }

    @Test
    public final void testSearchCount() {
        Set<String> types = new TreeSet<String>();
        types.add("type");
        SolrResultPage page = createMock(SolrResultPage.class);
        Collection<Page<FacetFieldEntry>> facetResultPages = createMock(Collection.class);
        Iterator it = createMock(Iterator.class);
        expect(this.repository.facetByType(isA(String.class), isA(PageRequest.class))).andReturn(page);
        expect(page.getTotalElements()).andReturn((long) 10);
        expect(page.getFacetResultPages()).andReturn(facetResultPages);
        expect(facetResultPages.iterator()).andReturn(it);
        expect(it.hasNext()).andReturn(false);
        replay(this.repository, facetResultPages, page, it);
        Map<String, Integer> map = this.searchService.searchCount(types, "query");
        verify(this.repository, facetResultPages, page, it);
        assertEquals(10, (int) map.get("all"));
    }

    @Test
    public final void testSearchFindAllNotRecordTypePubmed() {
        expect(this.repository.searchFindAllNotRecordTypePubmed(isA(PageRequest.class))).andReturn(
                Collections.emptyList());
        replay(this.repository);
        this.searchService.searchFindAllNotRecordTypePubmed();
        verify(this.repository);
    }

    @Test
    public final void testSearchTypeStringString() {
        Page<Eresource> page = createMock(Page.class);
        expect(this.repository.searchFindByType(isA(String.class), isA(String.class), isA(PageRequest.class)))
        .andReturn(page);
        expect(page.getContent()).andReturn(Collections.emptyList());
        replay(this.repository, page);
        this.searchService.searchType("type", "query");
        verify(this.repository, page);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSearchTypeStringStringException() {
        this.searchService.searchType(null, "query");
    }

    @Test
    public final void testSearchTypeStringStringPageable() {
        Page<Eresource> page = createMock(Page.class);
        expect(this.repository.searchFindByType(isA(String.class), isA(String.class), isA(PageRequest.class)))
        .andReturn(page);
        replay(this.repository, page);
        this.searchService.searchType("type", "query", new PageRequest(0, 1));
        verify(this.repository, page);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSearchTypeStringStringPageableException() {
        this.searchService.searchType(null, "query", new PageRequest(0, 1));
    }

    @Test
    public final void testSearchWithFilters() {
        Page<Eresource> page = createMock(Page.class);
        expect(
                this.repository.searchFindAllWithFilter("query", "field1:value AND field2:value", new PageRequest(0, 1,
                        null))).andReturn(page);
        replay(this.repository, page);
        this.searchService.searchWithFilters("query", "field1:value::field2:value", new PageRequest(0, 1));
        verify(this.repository, page);
    }

    @Test
    public final void testSuggestFindAll() {
        expect(this.repository.suggestFindAll("query terms", "query +terms", new PageRequest(0, 10))).andReturn(
                Collections.emptyList());
        replay(this.repository);
        this.searchService.suggestFindAll("query terms");
        verify(this.repository);
    }

    @Test
    public final void testSuggestFindByType() {
        expect(this.repository.suggestFindByType("term", "Type", new PageRequest(0, 10))).andReturn(
                Collections.emptyList());
        replay(this.repository);
        this.searchService.suggestFindByType("term", "type");
        verify(this.repository);
    }
}
