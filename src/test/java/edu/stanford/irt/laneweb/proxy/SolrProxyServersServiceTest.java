package edu.stanford.irt.laneweb.proxy;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.FacetOptions.FacetSort;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;

import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.search.redesign.FacetService;

public class SolrProxyServersServiceTest {

    private String expectedOutput = "T bodoni.stanford.edu\n" + "U https://bodoni.stanford.edu\n"
            + "HJ bodoni.stanford.edu\n" + "HJ bodoni.stanford.edu:443\n" + "\n" + "T foo\n" + "U https://foo\n"
            + "HJ foo\n" + "HJ foo:443\n" + "\n" + "T library.stanford.edu\n" + "U https://library.stanford.edu\n"
            + "HJ library.stanford.edu\n" + "HJ library.stanford.edu:443\n" + "\n" + "T searchworks.stanford.edu\n"
            + "U https://searchworks.stanford.edu\n" + "HJ searchworks.stanford.edu\n"
            + "HJ searchworks.stanford.edu:443\n" + "\n";

    private SolrProxyServersService proxyService;

    private FacetService solrService;

    @Before
    public void setUp() throws Exception {
        this.solrService = mock(FacetService.class);
        this.proxyService = new SolrProxyServersService(this.solrService);
    }

    @Test
    public final void testWrite() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FacetPage<Eresource> fps = mock(FacetPage.class);
        expect(this.solrService.facetByField("*", null, "proxyHosts", 100000, 1, FacetSort.INDEX)).andReturn(fps);
        Collection<Page<FacetFieldEntry>> facetResultPages = mock(Collection.class);
        Iterator<Page<FacetFieldEntry>> it1 = mock(Iterator.class);
        Iterator<FacetFieldEntry> it2 = mock(Iterator.class);
        Page<FacetFieldEntry> page1 = mock(Page.class);
        FacetFieldEntry ffe = mock(FacetFieldEntry.class);
        expect(fps.getFacetResultPages()).andReturn(facetResultPages);
        expect(facetResultPages.iterator()).andReturn(it1);
        expect(it1.hasNext()).andReturn(true);
        expect(it1.next()).andReturn(page1);
        expect(page1.iterator()).andReturn(it2);
        expect(it2.hasNext()).andReturn(true);
        expect(it2.next()).andReturn(ffe);
        expect(ffe.getValue()).andReturn("foo");
        expect(it2.hasNext()).andReturn(false);
        expect(it1.hasNext()).andReturn(false);
        replay(this.solrService, fps, facetResultPages, it1, it2, page1, ffe);
        this.proxyService.write(baos);
        assertEquals(this.expectedOutput, baos.toString(StandardCharsets.UTF_8.name()));
        verify(this.solrService, fps, facetResultPages, it1, it2, page1, ffe);
    }
}
