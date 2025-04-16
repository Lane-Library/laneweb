package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.strictMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.cocoon.pipeline.Pipeline;
import edu.stanford.irt.cocoon.sitemap.ComponentFactory;
import edu.stanford.irt.cocoon.sitemap.Sitemap;
import edu.stanford.irt.cocoon.sitemap.SitemapContext;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.status.Status;
import edu.stanford.irt.status.StatusItem;

public class IndexDotHtmlStatusProviderTest {

    private ComponentFactory componentFactory;

    private Pipeline pipeline;

    private Sitemap sitemap;

    private SourceResolver sourceResolver;

    private IndexDotHtmlStatusProvider statusProvider;

    @BeforeEach
    public void setUp() throws URISyntaxException {
        this.componentFactory = strictMock(ComponentFactory.class);
        this.sitemap = strictMock(Sitemap.class);
        this.sourceResolver = strictMock(SourceResolver.class);
        this.statusProvider = new IndexDotHtmlStatusProvider(this.sitemap, this.componentFactory,
                this.sourceResolver,
                250, new URI("file:/"), new URI("file:/"));
        this.pipeline = strictMock(Pipeline.class);
    }

    @Test
    public void testGetStatus() {
        expect(this.componentFactory.getComponent("edu.stanford.irt.cocoon.Model", Map.class))
                .andReturn(new HashMap<>());
        expect(this.sitemap.buildPipeline(isA(SitemapContext.class))).andReturn(this.pipeline);
        this.pipeline.process(isA(OutputStream.class));
        replay(this.componentFactory, this.sitemap, this.sourceResolver, this.pipeline);
        StatusItem item = this.statusProvider.getStatusItems().get(0);
        assertEquals(Status.OK, item.getStatus());
        assertTrue(Pattern.compile("index.html took \\dms").matcher(item.getMessage()).matches());
    }

    @Test
    public void testGetStatusException() {
        expect(this.componentFactory.getComponent("edu.stanford.irt.cocoon.Model", Map.class))
                .andReturn(new HashMap<>());
        expect(this.sitemap.buildPipeline(isA(SitemapContext.class))).andReturn(this.pipeline);
        this.pipeline.process(isA(OutputStream.class));
        expectLastCall().andThrow(new RuntimeException("oopsie"));
        replay(this.componentFactory, this.sitemap, this.sourceResolver, this.pipeline);
        StatusItem item = this.statusProvider.getStatusItems().get(0);
        assertEquals(Status.ERROR, item.getStatus());
        assertTrue(Pattern.compile("index.html status failed in \\dms: java.lang.RuntimeException: oopsie")
                .matcher(item.getMessage()).matches());
    }

    @Test
    public void testGetStatusWarn() throws URISyntaxException {
        expect(this.componentFactory.getComponent("edu.stanford.irt.cocoon.Model", Map.class))
                .andReturn(new HashMap<>());
        expect(this.sitemap.buildPipeline(isA(SitemapContext.class))).andReturn(this.pipeline);
        this.pipeline.process(isA(OutputStream.class));
        replay(this.componentFactory, this.sitemap, this.sourceResolver, this.pipeline);
        IndexDotHtmlStatusProvider provider = new IndexDotHtmlStatusProvider(this.sitemap,
                this.componentFactory,
                this.sourceResolver, -1, new URI("file:/"), new URI("file:/"));
        StatusItem item = provider.getStatusItems().get(0);
        assertEquals(Status.WARN, item.getStatus());
        assertTrue(Pattern.compile("index.html took \\dms").matcher(item.getMessage()).matches());
    }
}
