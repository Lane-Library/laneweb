package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.apache.cocoon.xml.XMLConsumer;
import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.resource.Resource;

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
        verify(this.eresources, this.eresource);
    }

    @Test
    public void testPage0ToSAX() throws SAXException {
        expect(this.eresources.toArray()).andReturn(this.eresourceArray);
        replay(this.eresources, this.eresource);
        PagingEresourceList list = new PagingEresourceList(this.eresources);
        assertEquals(256, list.size());
        assertEquals(0, list.getStart());
        assertEquals(100, list.getLength());
        assertEquals(0, list.getPage());
        assertEquals(3, list.getPages());
        verify(this.eresources, this.eresource);
    }

    @Test
    public void testPage1ToSAX() throws SAXException {
        expect(this.eresources.toArray()).andReturn(this.eresourceArray);
        replay(this.eresources, this.eresource);
        PagingEresourceList list = new PagingEresourceList(this.eresources, 1);
        assertEquals(256, list.size());
        assertEquals(100, list.getStart());
        assertEquals(100, list.getLength());
        assertEquals(1, list.getPage());
        assertEquals(3, list.getPages());
        verify(this.eresources, this.eresource);
    }

    @Test
    public void testPage2ToSAX() throws SAXException {
        expect(this.eresources.toArray()).andReturn(this.eresourceArray);
        replay(this.eresources, this.eresource);
        PagingEresourceList list = new PagingEresourceList(this.eresources, 2);
        assertEquals(256, list.size());
        assertEquals(200, list.getStart());
        assertEquals(56, list.getLength());
        assertEquals(2, list.getPage());
        assertEquals(3, list.getPages());
        verify(this.eresources, this.eresource);
    }

    @Test
    public void testPage3With596ToSAX() throws SAXException {
        this.eresourceArray = new Eresource[596];
        Arrays.fill(this.eresourceArray, this.eresource);
        expect(this.eresources.toArray()).andReturn(this.eresourceArray);
        replay(this.eresources, this.eresource);
        PagingEresourceList list = new PagingEresourceList(this.eresources, 3);
        assertEquals(596, list.size());
        assertEquals(447, list.getStart());
        assertEquals(149, list.getLength());
        assertEquals(3, list.getPage());
        assertEquals(4, list.getPages());
        verify(this.eresources, this.eresource);
    }
}
