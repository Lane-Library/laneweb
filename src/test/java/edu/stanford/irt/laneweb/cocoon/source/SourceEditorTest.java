package edu.stanford.irt.laneweb.cocoon.source;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;


public class SourceEditorTest {
    
    private SourceEditor editor;
    private ResourceLoader resourceLoader;
    private Resource resource;

    @Before
    public void setUp() throws Exception {
        this.resourceLoader = createMock(ResourceLoader.class);
        this.editor = new SourceEditor(this.resourceLoader);
        this.resource = createMock(Resource.class);
    }

    @Test
    public void testSetAsTextString() {
        expect(this.resourceLoader.getResource("foo")).andReturn(this.resource);
        replay(this.resourceLoader, this.resource);
        this.editor.setAsText("foo");
        assertEquals(SpringResourceSource.class, this.editor.getValue().getClass());
        verify(this.resourceLoader, this.resource);
    }
}
