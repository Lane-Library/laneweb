package edu.stanford.irt.laneweb.querymap;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Set;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.sitemap.SitemapModelComponent;
import org.apache.excalibur.source.Source;

import edu.stanford.irt.querymap.DescriptorWeightMap;
import edu.stanford.irt.querymap.QueryMap;
import edu.stanford.irt.querymap.QueryMapper;
import edu.stanford.irt.querymap.Resource;
import edu.stanford.irt.querymap.StreamResourceMapping;

public abstract class AbstractQueryMapComponent implements SitemapModelComponent {

    private static final String ABSTRACT_COUNT = "abstract-count";

    private static final String DESCRIPTOR_WEIGHTS = "descriptor-weights";

    private static final String QUERY = "query";

    private static final String RESOURCE_MAPS = "resource-maps";

    private ThreadLocal<Integer> abstractCount = new ThreadLocal<Integer>();

    private ThreadLocal<Map<String, Float>> descriptorWeights = new ThreadLocal<Map<String, Float>>();

    private ThreadLocal<String> query = new ThreadLocal<String>();

    private QueryMapper queryMapper;

    private ThreadLocal<Map<String, Set<Resource>>> resourceMaps = new ThreadLocal<Map<String, Set<Resource>>>();

    public void setQueryMapper(final QueryMapper queryMapper) {
        if (null == queryMapper) {
            throw new IllegalArgumentException("null queryMapper");
        }
        this.queryMapper = queryMapper;
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters params) {
        if (null == params) {
            throw new IllegalArgumentException("null params");
        }
        if (null == this.queryMapper) {
            throw new IllegalStateException("null queryMapper");
        }
        String query = params.getParameter(QUERY, null);
        String mapURL = params.getParameter(RESOURCE_MAPS, null);
        String weightURL = params.getParameter(DESCRIPTOR_WEIGHTS, null);
        int abstractCount = params.getParameterAsInteger(ABSTRACT_COUNT, 100);
        if ((null != mapURL) && (null != weightURL)) {
            try {
                Source mapSource = resolver.resolveURI(mapURL);
                this.resourceMaps.set(new StreamResourceMapping(mapSource.getInputStream()));
                Source weightSource = resolver.resolveURI(weightURL);
                this.descriptorWeights.set(new DescriptorWeightMap(weightSource.getInputStream()));
                this.abstractCount.set(new Integer(abstractCount));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.descriptorWeights.set(null);
            this.resourceMaps.set(null);
        }
        if (null == query) {
            throw new IllegalArgumentException("null query");
        }
        this.query.set(query);
    }

    protected QueryMap getQueryMap() {
        String query = this.query.get();
        if (null == query) {
            throw new IllegalStateException("null query");
        }
        if (null == this.resourceMaps.get()) {
            return this.queryMapper.getQueryMap(query);
        } else {
            return this.queryMapper.getQueryMap(query, this.resourceMaps.get(), this.descriptorWeights.get(), this.abstractCount.get().intValue());
        }
    }

    protected void reset() {
        this.query.set(null);
        this.descriptorWeights.set(null);
        this.resourceMaps.set(null);
    }
}
