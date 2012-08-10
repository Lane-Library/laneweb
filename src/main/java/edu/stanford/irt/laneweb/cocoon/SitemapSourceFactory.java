package edu.stanford.irt.laneweb.cocoon;

import java.io.ByteArrayOutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.cocoon.Processor;
import org.apache.cocoon.components.pipeline.ProcessingPipeline;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.environment.wrapper.RequestParameters;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceFactory;

import edu.stanford.irt.cocoon.sitemap.source.SitemapSource;
import edu.stanford.irt.cocoon.source.SourceException;
import edu.stanford.irt.laneweb.model.Model;

public abstract class SitemapSourceFactory implements SourceFactory {

    private Processor processor;

    public SitemapSourceFactory(final Processor processor) {
        this.processor = processor;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Source getSource(final String uri, final Map parameters) {
        Map<String, Object> newObjectModel = new HashMap<String, Object>(getModel());
        int startOfPath = uri.indexOf(":/") + 2;
        String sitemapURI = uri.substring(startOfPath);
        int qMark = sitemapURI.indexOf('?');
        if (qMark > -1) {
            sitemapURI = sitemapURI.substring(qMark);
            // add uri parameters to newObjectModel
            RequestParameters params = new RequestParameters(sitemapURI.substring(qMark + 1));
            for (Enumeration<String> names = params.getParameterNames(); names.hasMoreElements();) {
                String name = names.nextElement();
                String[] value = params.getParameterValues(name);
                if (value.length == 1) {
                    newObjectModel.put(name, value[0]);
                } else {
                    newObjectModel.put(name, value);
                }
            }
        }
        newObjectModel.put(Model.SITEMAP_URI, sitemapURI);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Environment environment = new LanewebEnvironment(newObjectModel, baos, false);
        ProcessingPipeline pipeline = null;
        try {
            pipeline = this.processor.buildPipeline(environment).processingPipeline;
            pipeline.prepareInternal(environment);
        } catch (Exception e) {
            throw new SourceException(e);
        }
        return new SitemapSource(uri, environment, pipeline, baos);
    }

    public void release(final Source source) {
    }

    protected abstract Map<String, Object> getModel();
}
