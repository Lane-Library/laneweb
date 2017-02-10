package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;

import java.io.Serializable;

import javax.cache.Cache;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.cache.CachedResponse;
import edu.stanford.irt.cocoon.cache.validity.AlwaysValid;
import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.cocoon.xml.XMLException;
import edu.stanford.irt.laneweb.LanewebException;

public class CachedXMLSourceResolverTest {

    private Cache<Serializable, CachedResponse> cache;

    private CachedXMLSourceResolver resolver;

    private SAXParser saxParser;

    private Source source;

    private SourceResolver sourceResolver;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        this.cache = createMock(Cache.class);
        this.sourceResolver = createMock(SourceResolver.class);
        this.saxParser = createMock(SAXParser.class);
        this.resolver = new CachedXMLSourceResolver(this.saxParser, this.cache, this.sourceResolver);
        this.source = createMock(Source.class);
    }

    @Test
    public void testCreateSource() {
        replay(this.cache, this.sourceResolver, this.saxParser);
        assertNotNull(this.resolver.createSource(new byte[0], "uri", AlwaysValid.SHARED_INSTANCE));
        verify(this.cache, this.sourceResolver, this.saxParser);
    }

    @Test
    public void testGetBytesFromSource() {
        this.saxParser.parse(eq(this.source), isA(XMLConsumer.class));
        replay(this.cache, this.sourceResolver, this.saxParser, this.source);
        assertNotNull(this.resolver.getBytesFromSource(this.source));
        verify(this.cache, this.sourceResolver, this.saxParser, this.source);
    }

    @Test(expected = LanewebException.class)
    public void testGetBytesFromSourceThrowsException() {
        this.saxParser.parse(eq(this.source), isA(XMLConsumer.class));
        expectLastCall().andThrow(new XMLException("oops"));
        replay(this.cache, this.sourceResolver, this.saxParser, this.source);
        assertNotNull(this.resolver.getBytesFromSource(this.source));
        verify(this.cache, this.sourceResolver, this.saxParser, this.source);
    }
}
