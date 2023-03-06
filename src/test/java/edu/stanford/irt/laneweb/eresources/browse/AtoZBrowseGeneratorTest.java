package edu.stanford.irt.laneweb.eresources.browse;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.FacetOptions.FacetSort;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;

import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.SolrService;
import edu.stanford.irt.laneweb.model.Model;

public class AtoZBrowseGeneratorTest {

    private AtoZBrowseGenerator generator;

    private SAXStrategy<List<BrowseLetter>> saxStrategy;

    private SolrService solrService;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.solrService = mock(SolrService.class);
        this.saxStrategy = mock(SAXStrategy.class);
        this.xmlConsumer = mock(XMLConsumer.class);
        this.generator = new AtoZBrowseGenerator("type", this.solrService, this.saxStrategy);
    }

    @Test
    public void testDoGenerate() {
        FacetPage<Eresource> facetpage = mock(FacetPage.class);
        Collection<Page<FacetFieldEntry>> facetResultPages = mock(Collection.class);
        Iterator<Page<FacetFieldEntry>> it1 = mock(Iterator.class);
        Iterator<FacetFieldEntry> it2 = mock(Iterator.class);
        Page<FacetFieldEntry> page1 = mock(Page.class);
        FacetFieldEntry ffe = mock(FacetFieldEntry.class);
        this.generator.setParameters(Collections.singletonMap(Model.QUERY, "foo"));
        this.saxStrategy.toSAX(isA(List.class), eq(this.xmlConsumer));
        expect(this.solrService.facetByField("advanced:true recordType:bib AND isRecent:1",
                "foo", "title_starts", 0, 200, 0, FacetSort.INDEX)).andReturn(facetpage);
        expect(facetpage.getFacetResultPages()).andReturn(facetResultPages);
        expect(facetResultPages.iterator()).andReturn(it1);
        expect(it1.hasNext()).andReturn(true);
        expect(it1.next()).andReturn(page1);
        expect(page1.iterator()).andReturn(it2);
        expect(it2.hasNext()).andReturn(true);
        expect(it2.next()).andReturn(ffe);
        expect(ffe.getValue()).andReturn("ertlswa");
        expect(ffe.getValueCount()).andReturn(new Long(4));
        expect(it2.hasNext()).andReturn(false);
        expect(it1.hasNext()).andReturn(false);
        replay(this.solrService, this.saxStrategy, this.xmlConsumer, facetpage, facetResultPages, it1, it2, page1, ffe);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.solrService, this.saxStrategy, this.xmlConsumer, facetpage, facetResultPages, it1, it2, page1, ffe);
    }

    @Test
    public void testDoGenerateEmpty() {
        replay(this.solrService, this.saxStrategy, this.xmlConsumer);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.solrService, this.saxStrategy, this.xmlConsumer);
    }

    @Test
    public void testGetKey() {
        this.generator.setParameters(Collections.singletonMap(Model.QUERY, "foo"));
        assertEquals("q=foo", this.generator.getKey());
    }

    @Test
    public void testGetType() {
        assertEquals("type", this.generator.getType());
    }

    @Test
    public void testGetValidity() {
        assertTrue(this.generator.getValidity().isValid());
    }

    @Test
    public void testGetValidityAgain() {
        Validity validity = this.generator.getValidity();
        assertSame(validity, this.generator.getValidity());
    }

    @Test
    public void testSetEmptyParameters() {
        this.generator.setParameters(Collections.emptyMap());
        assertEquals("q=null", this.generator.getKey());
    }

    @Test
    public void testSetParameters() {
        this.generator.setParameters(Collections.singletonMap(Model.QUERY, "some-query"));
        assertEquals("q=some-query", this.generator.getKey());
    }
}
