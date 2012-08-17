package edu.stanford.irt.cocoon.sitemap.select;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.cocoon.environment.SourceResolver;
import org.apache.excalibur.source.Source;
import org.junit.Before;
import org.junit.Test;

/**
 * A unit test for the ResourceExistsSelector class.
 */
public class ResourceExistsSelectorTest {

    private ResourceExistsSelector selector;

    private Source source;

    private SourceResolver sourceResolver;

    /**
     * Create the selector to test and supporting mock objects.
     */
    @Before
    public void setUp() {
        this.sourceResolver = createMock(SourceResolver.class);
        this.selector = new ResourceExistsSelector(this.sourceResolver);
        this.source = createMock(Source.class);
    }

    /**
     * Test for correct behavior when the resource does not exist.
     * 
     * @throws IOException
     */
    @Test
    public void testSelectFalse() throws IOException {
        expect(this.sourceResolver.resolveURI("foo")).andReturn(this.source);
        expect(this.source.exists()).andReturn(false);
        replay(this.sourceResolver, this.source);
        assertFalse(this.selector.select("foo", null, null));
        verify(this.sourceResolver, this.source);
    }

    /**
     * Test for correct behavior when the resource does exist.
     * 
     * @throws IOException
     */
    @Test
    public void testSelectThrowsException() throws IOException {
        expect(this.sourceResolver.resolveURI("foo")).andThrow(new IOException());
        replay(this.sourceResolver, this.source);
        assertFalse(this.selector.select("foo", null, null));
        verify(this.sourceResolver, this.source);
    }

    /**
     * Test for correct behavior when an IOException is thrown when resolving
     * the resource.
     * 
     * @throws IOException
     */
    @Test
    public void testSelectTrue() throws IOException {
        expect(this.sourceResolver.resolveURI("foo")).andReturn(this.source);
        expect(this.source.exists()).andReturn(true);
        replay(this.sourceResolver, this.source);
        assertTrue(this.selector.select("foo", null, null));
        verify(this.sourceResolver, this.source);
    }
}
