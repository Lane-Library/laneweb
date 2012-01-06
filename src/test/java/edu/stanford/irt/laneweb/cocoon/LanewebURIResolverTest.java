package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceResolver;
import org.junit.Before;
import org.junit.Test;

public class LanewebURIResolverTest {

    private Source source;

    private SourceResolver sourceResolver;

    private LanewebURIResolver uriResolver;

    @Before
    public void setUp() throws Exception {
        this.sourceResolver = createMock(SourceResolver.class);
        this.source = createMock(Source.class);
        this.uriResolver = new LanewebURIResolver(this.sourceResolver);
    }

    @Test
    public void testResolve() throws TransformerException, IOException {
        expect(this.sourceResolver.resolveURI(null)).andReturn(this.source);
        expect(this.source.getInputStream()).andReturn(null);
        expect(this.source.getURI()).andReturn(null);
        replay(this.sourceResolver, this.source);
        this.uriResolver.resolve(null, null);
        verify(this.sourceResolver, this.source);
    }
}
