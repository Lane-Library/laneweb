package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

import javax.cache.Cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.CocoonException;
import edu.stanford.irt.cocoon.cache.CachedResponse;
import edu.stanford.irt.cocoon.cache.validity.AlwaysValid;
import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.XMLByteStreamInterpreter;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.cocoon.xml.XMLizable;
import edu.stanford.irt.laneweb.LanewebException;

public class CachedXMLSourceResolverTest {

    private Cache<Serializable, CachedResponse> cache;

    private CachedXMLSourceResolver resolver;

    private SAXParser saxParser;

    private Source source;

    private SourceResolver sourceResolver;

    private XMLByteStreamInterpreter xmlByteStreamInterpreter;

    @BeforeEach
    public void setUp() {
        this.cache = mock(Cache.class);
        this.sourceResolver = mock(SourceResolver.class);
        this.saxParser = mock(SAXParser.class);
        this.xmlByteStreamInterpreter = mock(XMLByteStreamInterpreter.class);
        this.resolver = new CachedXMLSourceResolver(this.saxParser, this.cache, this.sourceResolver,
                this.xmlByteStreamInterpreter);
        this.source = mock(Source.class);
    }

    @Test
    public void testCreateSource() {
        replay(this.cache, this.sourceResolver, this.saxParser, this.xmlByteStreamInterpreter);
        assertNotNull(this.resolver.createSource(new byte[0], "uri", AlwaysValid.SHARED_INSTANCE));
        verify(this.cache, this.sourceResolver, this.saxParser, this.xmlByteStreamInterpreter);
    }

    @Test
    public void testGetBytesFromSource() {
        this.saxParser.parse(eq(this.source), isA(XMLConsumer.class));
        replay(this.cache, this.sourceResolver, this.saxParser, this.source, this.xmlByteStreamInterpreter);
        assertNotNull(this.resolver.getBytesFromSource(this.source));
        verify(this.cache, this.sourceResolver, this.saxParser, this.source, this.xmlByteStreamInterpreter);
    }

    @Test
    public void testGetBytesFromSourceThrowsException() {
        this.saxParser.parse(eq(this.source), isA(XMLConsumer.class));
        expectLastCall().andThrow(new CocoonException("oops"));
        expect(this.source.getURI()).andReturn("uri");
        replay(this.cache, this.sourceResolver, this.saxParser, this.source, this.xmlByteStreamInterpreter);
        assertThrows(LanewebException.class, () -> {
            assertNotNull(this.resolver.getBytesFromSource(this.source));
        });
        verify(this.cache, this.sourceResolver, this.saxParser, this.source, this.xmlByteStreamInterpreter);
    }

    @Test
    public void testToSAX() throws URISyntaxException, SAXException {
        CachedResponse response = new CachedResponse(AlwaysValid.SHARED_INSTANCE, new byte[0]);
        expect(this.cache.get(new URI("file:/"))).andReturn(response);
        this.xmlByteStreamInterpreter.deserialize(new byte[0], null);
        replay(this.cache, this.sourceResolver, this.saxParser, this.source, this.xmlByteStreamInterpreter);
        XMLizable source = (XMLizable) this.resolver.resolveURI(new URI("file:/"));
        source.toSAX(null);
        verify(this.cache, this.sourceResolver, this.saxParser, this.source, this.xmlByteStreamInterpreter);
    }
}
