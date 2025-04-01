package edu.stanford.irt.laneweb.proxy;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.eresources.EresourceFacetService;
import edu.stanford.irt.laneweb.eresources.model.solr.FacetFieldEntry;
import edu.stanford.irt.laneweb.eresources.model.solr.FacetSort;

public class SolrProxyServersServiceTest {

    private String expectedOutput = "T bodoni.stanford.edu\n" + "U https://bodoni.stanford.edu\n"
            + "HJ bodoni.stanford.edu\n" + "HJ bodoni.stanford.edu:443\n" + "\n" + "T foo\n" + "U https://foo\n"
            + "HJ foo\n" + "HJ foo:443\n" + "\n" + "T library.stanford.edu\n" + "U https://library.stanford.edu\n"
            + "HJ library.stanford.edu\n" + "HJ library.stanford.edu:443\n" + "\n" + "T searchworks.stanford.edu\n"
            + "U https://searchworks.stanford.edu\n" + "HJ searchworks.stanford.edu\n"
            + "HJ searchworks.stanford.edu:443\n" + "\n";

    private SolrProxyServersService proxyService;

    private EresourceFacetService solrService;

    private Map<String, List<FacetFieldEntry>> fps;

    @BeforeEach
    public void setUp() throws Exception {
        this.solrService = mock(EresourceFacetService.class);
        this.proxyService = new SolrProxyServersService(this.solrService);
        FacetFieldEntry ffe = new FacetFieldEntry(null, "foo", 443);
        List<FacetFieldEntry> list = Collections.singletonList(ffe);
        this.fps = new HashMap<>();
        this.fps.put("proxyHosts", list);
    }

    @Test
    public final void testWrite() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        expect(this.solrService.facetByField("*", null, "proxyHosts", 100000, 1, FacetSort.INDEX)).andReturn(this.fps);
        replay(this.solrService);
        this.proxyService.write(baos);
        assertEquals(this.expectedOutput, baos.toString(StandardCharsets.UTF_8.name()));
        verify(this.solrService);
    }
}
