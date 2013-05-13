package edu.stanford.irt.laneweb.resource;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PagingDataTest {

    private PagingData data;

    private List<Object> resources;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.resources = createMock(List.class);
        expect(this.resources.size()).andReturn(351);
        replay(this.resources);
        this.data = new PagingData(this.resources, 3, "/foo/bar");
        verify(this.resources);
    }

    @Test
    public void testGetLength() {
        assertEquals(51, this.data.getLength());
    }

    @Test
    public void testGetPage() {
        assertEquals(3, this.data.getPage());
    }

    @Test
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
