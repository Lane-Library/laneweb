package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.strictMock;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.status.Status;
import edu.stanford.irt.status.StatusItem;

public class EresourceStatusProviderTest {

    private SolrService solrService;

    private EresourceStatusProvider statusProvider;

    @Before
    public void setUp() {
        this.solrService = strictMock(SolrService.class);
        this.statusProvider = new EresourceStatusProvider(this.solrService, 100, 100);
    }

    @Test
    public void testGetStatusesException() {
        expect(this.solrService.recordCount()).andThrow(new RuntimeException("oopsie"));
        replay(this.solrService);
        StatusItem item = this.statusProvider.getStatusItems().get(0);
        assertEquals("solr record counts failed: java.lang.RuntimeException: oopsie", item.getMessage());
        assertSame(Status.ERROR, item.getStatus());
        verify(this.solrService);
    }

    @Test
    public void testGetStatusNotBothValues() {
        Map<String, Long> recordCount = new HashMap<>();
        recordCount.put("bib", Long.valueOf(200));
        expect(this.solrService.recordCount()).andReturn(recordCount);
        replay(this.solrService);
        List<StatusItem> items = this.statusProvider.getStatusItems();
        StatusItem item1 = items.get(0);
        StatusItem item2 = items.get(1);
        assertEquals("bib record count: 200", item1.getMessage());
        assertSame(Status.OK, item1.getStatus());
        assertEquals("pubmed record count: 0", item2.getMessage());
        assertSame(Status.ERROR, item2.getStatus());
        verify(this.solrService);
    }

    @Test
    public void testGetStatusNoValues() {
        Map<String, Long> recordCount = new HashMap<>();
        expect(this.solrService.recordCount()).andReturn(recordCount);
        replay(this.solrService);
        List<StatusItem> items = this.statusProvider.getStatusItems();
        StatusItem item1 = items.get(0);
        StatusItem item2 = items.get(1);
        assertEquals("bib record count: 0", item1.getMessage());
        assertEquals("pubmed record count: 0", item2.getMessage());
        assertSame(Status.ERROR, item1.getStatus());
        assertSame(Status.ERROR, item2.getStatus());
        verify(this.solrService);
    }
}
