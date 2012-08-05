package edu.stanford.irt.cocoon.pipeline.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.apache.excalibur.source.impl.validity.NOPValidity;
import org.junit.Before;
import org.junit.Test;

public class AbstractCacheableTransformerTest {

    private AbstractCacheableTransformer transformer;

    @Before
    public void setUp() throws Exception {
        this.transformer = new AbstractCacheableTransformer("type") {
        };
    }

    @Test
    public void testGetKey() {
        assertEquals("type", this.transformer.getKey());
    }

    @Test
    public void testGetType() {
        assertEquals("type", this.transformer.getType());
    }

    @Test
    public void testGetValidity() {
        assertSame(NOPValidity.SHARED_INSTANCE, this.transformer.getValidity());
    }
}
