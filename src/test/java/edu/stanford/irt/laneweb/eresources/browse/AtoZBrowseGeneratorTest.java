package edu.stanford.irt.laneweb.eresources.browse;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
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
        this.generator.setParameters(Collections.singletonMap(Model.TYPE, "foo"));
        this.saxStrategy.toSAX(isA(List.class), eq(this.xmlConsumer));
        expect(this.solrService.facetByField("advanced:true recordType:bib AND (isRecent:1 OR isLaneConnex:1)",
                "type:foo", "title_starts", 0, 200, 0, FacetSort.INDEX)).andReturn(facetpage);
        expect(facetpage.getFacetResultPages()).andReturn(facetResultPages);
        expect(facetResultPages.iterator()).andReturn(it1);
        expect(it1.hasNext()).andReturn(false);
        replay(this.solrService, this.saxStrategy, this.xmlConsumer, facetpage, facetResultPages, it1);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.solrService, this.saxStrategy, this.xmlConsumer, facetpage, facetResultPages, it1);
    }

    @Test
    public void testDoGenerateEmpty() {
        replay(this.solrService, this.saxStrategy, this.xmlConsumer);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.solrService, this.saxStrategy, this.xmlConsumer);
    }

    @Test
    public void testGetKey() {
        this.generator.setParameters(Collections.singletonMap(Model.TYPE, "foo"));
        assertEquals("r=null;t=foo", this.generator.getKey());
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
        assertTrue(validity == this.generator.getValidity());
    }

    @Test
    public void testSetEmptyModelAndParameters() {
        this.generator.setModel(Collections.emptyMap());
        this.generator.setParameters(Collections.emptyMap());
        assertEquals("r=null;t=null", this.generator.getKey());
    }

    @Test
    public void testSetModelAndParameters() {
        this.generator.setModel(Collections.singletonMap(Model.REQUEST_URI, "request-uri"));
        this.generator.setParameters(Collections.singletonMap(Model.TYPE, "some-type"));
        assertEquals("r=request-uri;t=some-type", this.generator.getKey());
    }
}
