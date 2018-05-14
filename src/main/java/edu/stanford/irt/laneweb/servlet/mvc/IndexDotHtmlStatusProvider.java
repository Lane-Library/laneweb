package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.OutputStream;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.cocoon.pipeline.Pipeline;
import edu.stanford.irt.cocoon.sitemap.ComponentFactory;
import edu.stanford.irt.cocoon.sitemap.Sitemap;
import edu.stanford.irt.cocoon.sitemap.SitemapContextImpl;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.status.Status;
import edu.stanford.irt.status.StatusItem;
import edu.stanford.irt.status.StatusProvider;

public class IndexDotHtmlStatusProvider implements StatusProvider {

    private static class NullOutputStream extends OutputStream {

        @Override
        public void write(final byte[] b, final int off, final int len) {
            // do nothing
        }

        @Override
        public void write(final int b) {
            // do nothing
        }
    }

    private static final String FAIL_FORMAT = "index.html status failed in %dms: %s";

    private static final Logger log = LoggerFactory.getLogger(IndexDotHtmlStatusProvider.class);

    private static final String SUCCESS_FORMAT = "index.html took %dms";

    private URI classesURI;

    private ComponentFactory componentFactory;

    private URI contentBase;

    private int maxOKTime;

    private Sitemap sitemap;

    private SourceResolver sourceResolver;

    public IndexDotHtmlStatusProvider(final Sitemap sitemap, final ComponentFactory componentFactory,
            final SourceResolver sourceResolver, final int maxOKTime, final URI contentBase, final URI classesURI) {
        this.sitemap = sitemap;
        this.componentFactory = componentFactory;
        this.sourceResolver = sourceResolver;
        this.maxOKTime = maxOKTime;
        this.contentBase = contentBase;
        this.classesURI = classesURI;
    }

    @Override
    public List<StatusItem> getStatusItems() {
        Status status;
        String message;
        long time;
        Instant start = Instant.now();
        Map<String, Object> model = this.componentFactory.getComponent("edu.stanford.irt.cocoon.Model", Map.class);
        model.put(Model.SITEMAP_URI, "/index.html");
        model.put(Model.CONTENT_BASE, this.contentBase);
        model.put(Model.TEMPLATE, "template");
        model.put(Model.CLASSES_SERVICE_URI, this.classesURI);
        model.put(Sitemap.class.getName(), this.sitemap);
        try {
            Pipeline pipeline = this.sitemap
                    .buildPipeline(new SitemapContextImpl(model, this.componentFactory, this.sourceResolver));
            pipeline.process(new NullOutputStream());
            time = Duration.between(start, Instant.now()).toMillis();
            status = time < this.maxOKTime ? Status.OK : Status.WARN;
            message = String.format(SUCCESS_FORMAT, time);
        } catch (RuntimeException e) {
            status = Status.ERROR;
            time = Duration.between(start, Instant.now()).toMillis();
            message = String.format(FAIL_FORMAT, time, e);
            log.error(message, e);
        }
        return Collections.singletonList(new StatusItem(status, message));
    }
}
