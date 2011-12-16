package edu.stanford.irt.laneweb.cocoon.source;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import org.apache.excalibur.source.Source;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.core.io.ResourceLoader;


public class SourceEditorRegistrarTest {
    
    private SourceEditorRegistrar registrar;
    
    private ResourceLoader loader;
    
    private PropertyEditorRegistry registry;

    @Before
    public void setUp() throws Exception {
        this.loader = createMock(ResourceLoader.class);
        this.registrar = new SourceEditorRegistrar();
        this.registrar.setResourceLoader(this.loader);
        this.registry = createMock(PropertyEditorRegistry.class);
    }

    @Test
    public void testRegisterCustomEditors() {
        this.registry.registerCustomEditor(eq(Source.class), isA(SourceEditor.class));
        replay(this.loader, this.registry);
        this.registrar.registerCustomEditors(this.registry);
        verify(this.loader, this.registry);
    }
}
