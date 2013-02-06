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

public class PagingEresourceListTest {

    private Eresource eresource;

    private Eresource[] eresourceArray = new Eresource[256];

    private Collection<Eresource> eresources;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.eresources = createMock(Collection.class);
        this.eresource = createMock(Eresource.class);
        Arrays.fill(this.eresourceArray, this.eresource);
    }

    @Test
    public void testAllPagesToSAX() throws SAXException {
        expect(this.eresources.toArray()).andReturn(this.eresourceArray);
        replay(this.eresources, this.eresource);
        PagingEresourceList list = new PagingEresourceList(this.eresources, -1);
        assertEquals(256, list.size());
        assertEquals(0, list.getStart());
        assertEquals(256, list.getLength());
        assertEquals(-1, list.getPage());
        assertEquals(3, list.getPages());
        assertEquals(0, list.getPagingLabels().size());
        verify(this.eresources, this.eresource);
    }

    @Test
    public void testPage0ToSAX() throws SAXException {
        expect(this.eresources.toArray()).andReturn(this.eresourceArray);
        expect(this.eresource.getTitle()).andReturn("er title").times(6);
        replay(this.eresources, this.eresource);
        PagingEresourceList list = new PagingEresourceList(this.eresources);
        assertEquals(256, list.size());
        assertEquals(0, list.getStart());
        assertEquals(100, list.getLength());
        assertEquals(0, list.getPage());
        assertEquals(3, list.getPages());
        assertEquals(3, list.getPagingLabels().size());
        assertEquals(100, list.getPagingLabels().get(0).getResults());
        assertEquals(100, list.getPagingLabels().get(1).getResults());
        assertEquals(56, list.getPagingLabels().get(2).getResults());
        for (PagingLabel pagingLabel : list.getPagingLabels()) {
            assertEquals("er title", pagingLabel.getStart());
            assertEquals("er title", pagingLabel.getEnd());
        }
        verify(this.eresources, this.eresource);
    }

    @Test
    public void testPage1ToSAX() throws SAXException {
        expect(this.eresources.toArray()).andReturn(this.eresourceArray);
        expect(this.eresource.getTitle()).andReturn("er title").times(6);
        replay(this.eresources, this.eresource);
        PagingEresourceList list = new PagingEresourceList(this.eresources, 1);
        assertEquals(256, list.size());
        assertEquals(100, list.getStart());
        assertEquals(100, list.getLength());
        assertEquals(1, list.getPage());
        assertEquals(3, list.getPages());
        assertEquals(3, list.getPagingLabels().size());
        assertEquals(100, list.getPagingLabels().get(0).getResults());
        assertEquals(100, list.getPagingLabels().get(1).getResults());
        assertEquals(56, list.getPagingLabels().get(2).getResults());
        for (PagingLabel pagingLabel : list.getPagingLabels()) {
            assertEquals("er title", pagingLabel.getStart());
            assertEquals("er title", pagingLabel.getEnd());
        }
        verify(this.eresources, this.eresource);
    }

    @Test
    public void testPage2ToSAX() throws SAXException {
        expect(this.eresources.toArray()).andReturn(this.eresourceArray);
        expect(this.eresource.getTitle()).andReturn("er title").times(6);
        replay(this.eresources, this.eresource);
        PagingEresourceList list = new PagingEresourceList(this.eresources, 2);
        assertEquals(256, list.size());
        assertEquals(200, list.getStart());
        assertEquals(56, list.getLength());
        assertEquals(2, list.getPage());
        assertEquals(3, list.getPages());
        assertEquals(3, list.getPagingLabels().size());
        assertEquals(100, list.getPagingLabels().get(0).getResults());
        assertEquals(100, list.getPagingLabels().get(1).getResults());
        assertEquals(56, list.getPagingLabels().get(2).getResults());
        for (PagingLabel pagingLabel : list.getPagingLabels()) {
            assertEquals("er title", pagingLabel.getStart());
            assertEquals("er title", pagingLabel.getEnd());
        }
        verify(this.eresources, this.eresource);
    }

    @Test
    public void testPage3With596ToSAX() throws SAXException {
        this.eresourceArray = new Eresource[596];
        Arrays.fill(this.eresourceArray, this.eresource);
        expect(this.eresources.toArray()).andReturn(this.eresourceArray);
        expect(this.eresource.getTitle()).andReturn("er title").times(8);
        replay(this.eresources, this.eresource);
        PagingEresourceList list = new PagingEresourceList(this.eresources, 3);
        assertEquals(596, list.size());
        assertEquals(447, list.getStart());
        assertEquals(149, list.getLength());
        assertEquals(3, list.getPage());
        assertEquals(4, list.getPages());
        assertEquals(4, list.getPagingLabels().size());
        for (PagingLabel pagingLabel : list.getPagingLabels()) {
            assertEquals(149, pagingLabel.getResults());
            assertEquals("er title", pagingLabel.getStart());
            assertEquals("er title", pagingLabel.getEnd());
        }
        verify(this.eresources, this.eresource);
    }
}
