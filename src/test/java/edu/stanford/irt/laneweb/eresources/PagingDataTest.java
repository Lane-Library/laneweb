package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class PagingDataTest {

    private PagingData data;

    private Collection<? extends Object> resources;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.resources = createMock(Collection.class);
        expect(this.resources.size()).andReturn(351);
        replay(this.resources);
        this.data = new PagingData(this.resources, 3, "/foo/bar");
        verify(this.resources);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        PagingData clone = (PagingData) this.data.clone();
        assertEquals(this.data.getLength(), clone.getLength());
        assertEquals(this.data.getPage(), clone.getPage());
        assertEquals(this.data.getPages(), clone.getPages());
        assertEquals(this.data.getPageSize(), clone.getPageSize());
        assertEquals(this.data.getSize(), clone.getSize());
        // etc if desired
    }

    @Test
    // TODO: can this be right?
    public void testGetAllLink() {
        assertEquals("/foo/barpage=all", this.data.getAllLink());
    }

    @Test
    public void testGetDisplayText() {
        assertEquals("Displaying 301 to 351 of ", this.data.getDisplayText());
    }

    @Test
    public void testGetLength() {
        assertEquals(51, this.data.getLength());
    }

    @Test
    public void testGetNoPageQuery() {
        assertEquals("/foo/bar", this.data.getNoPageQuery());
    }

    @Test
    // TODO: can this be right?
    public void testGetPage() {
        assertEquals(3, this.data.getPage());
    }

    @Test
    // TODO: can this be right?
    public void testGetPages() {
        assertEquals(3, this.data.getPage());
    }

    @Test
    public void testGetPageSize() {
        assertEquals(100, this.data.getPageSize());
    }

    @Test
    public void testGetSize() {
        assertEquals(351, this.data.getSize());
    }

    @Test
    public void testGetStart() {
        assertEquals(300, this.data.getStart());
    }
}
