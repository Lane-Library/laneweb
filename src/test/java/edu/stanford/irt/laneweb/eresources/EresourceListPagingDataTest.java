package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.solr.Eresource;

public class EresourceListPagingDataTest {

    private Eresource eresource;

    private PagingEresourceList eresources;

    private EresourceListPagingData pagingData;

    @Before
    public void setUp() throws Exception {
        this.eresources = createMock(PagingEresourceList.class);
        this.eresource = createMock(Eresource.class);
    }

    @Test
    public void testGetPagingLabels() {
        expect(this.eresources.size()).andReturn(256);
        expect(this.eresources.get(0)).andReturn(this.eresource);
        expect(this.eresource.getTitle()).andReturn("er title");
        expect(this.eresources.get(99)).andReturn(this.eresource);
        expect(this.eresource.getTitle()).andReturn("er title");
        expect(this.eresources.get(100)).andReturn(this.eresource);
        expect(this.eresource.getTitle()).andReturn("er title");
        expect(this.eresources.get(199)).andReturn(this.eresource);
        expect(this.eresource.getTitle()).andReturn("er title");
        expect(this.eresources.get(200)).andReturn(this.eresource);
        expect(this.eresource.getTitle()).andReturn("er title");
        expect(this.eresources.get(255)).andReturn(this.eresource);
        expect(this.eresource.getTitle()).andReturn("er title");
        replay(this.eresources, this.eresource);
        this.pagingData = new EresourceListPagingData(this.eresources, 0, "", "a");
        List<PagingLabel> labels = this.pagingData.getPagingLabels();
        assertEquals(3, labels.size());
        assertEquals(100, labels.get(0).getResults());
        assertEquals(100, labels.get(1).getResults());
        assertEquals(56, labels.get(2).getResults());
        for (PagingLabel pagingLabel : labels) {
            assertEquals("er title", pagingLabel.getStart());
            assertEquals("er title", pagingLabel.getEnd());
        }
        verify(this.eresources, this.eresource);
    }
}
