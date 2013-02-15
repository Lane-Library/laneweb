package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.resource.PagingData;

public class PagingEresourceListTest {

    private Eresource eresource;

    private Eresource[] eresourceArray = new Eresource[256];

    private Collection<Eresource> eresources;

    private PagingData pagingData;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.eresources = createMock(Collection.class);
        this.eresource = createMock(Eresource.class);
        this.pagingData = createMock(PagingData.class);
        Arrays.fill(this.eresourceArray, this.eresource);
    }

    @Test
    public void testGetPagingLabels256() throws SAXException {
        expect(this.eresources.toArray()).andReturn(this.eresourceArray);
        expect(this.pagingData.getPages()).andReturn(3);
        expect(this.pagingData.getPage()).andReturn(0);
        expect(this.pagingData.getPageSize()).andReturn(100);
        expect(this.eresource.getTitle()).andReturn("er title").times(6);
        replay(this.eresources, this.eresource, this.pagingData);
        PagingEresourceList list = new PagingEresourceList(this.eresources, this.pagingData);
        assertEquals(256, list.size());
        assertEquals(3, list.getPagingLabels().size());
        assertEquals(100, list.getPagingLabels().get(0).getResults());
        assertEquals(100, list.getPagingLabels().get(1).getResults());
        assertEquals(56, list.getPagingLabels().get(2).getResults());
        for (PagingLabel pagingLabel : list.getPagingLabels()) {
            assertEquals("er title", pagingLabel.getStart());
            assertEquals("er title", pagingLabel.getEnd());
        }
        verify(this.eresources, this.eresource, this.pagingData);
    }

    @Test
    public void testPagingEresourceList256() throws SAXException {
        expect(this.eresources.toArray()).andReturn(this.eresourceArray);
        replay(this.eresources, this.eresource, this.pagingData);
        PagingEresourceList list = new PagingEresourceList(this.eresources, this.pagingData);
        assertEquals(256, list.size());
        verify(this.eresources, this.eresource, this.pagingData);
    }
}
