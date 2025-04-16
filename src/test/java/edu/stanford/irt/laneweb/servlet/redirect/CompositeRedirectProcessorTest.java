package edu.stanford.irt.laneweb.servlet.redirect;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class CompositeRedirectProcessorTest {

    private RedirectProcessor child;

    private CompositeRedirectProcessor processor;

    public CompositeRedirectProcessorTest() {
        this.child = mock(RedirectProcessor.class);
        this.processor = new CompositeRedirectProcessor(
                Arrays.asList(new RedirectProcessor[] { this.child, this.child }));
    }

    @Test
    public void testGetRedirectURL() {
        expect(this.child.getRedirectURL("uri", "basePath", "queryString")).andReturn(null);
        expect(this.child.getRedirectURL("uri", "basePath", "queryString")).andReturn("redirect");
        replay(this.child);
        assertEquals("redirect", this.processor.getRedirectURL("uri", "basePath", "queryString"));
        verify(this.child);
    }

    @Test
    public void testGetRedirectURLNoRedirect() {
        expect(this.child.getRedirectURL("uri", "basePath", "queryString")).andReturn(null);
        expect(this.child.getRedirectURL("uri", "basePath", "queryString")).andReturn(null);
        replay(this.child);
        assertNull(this.processor.getRedirectURL("uri", "basePath", "queryString"));
        verify(this.child);
    }
}
