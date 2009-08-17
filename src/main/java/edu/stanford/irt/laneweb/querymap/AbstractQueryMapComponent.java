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

    private int abstractCount;

    private Map<String, Float> descriptorWeights;

    private String query;

    private QueryMapper queryMapper;

    private Map<String, Set<Resource>> resourceMaps;

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
                this.resourceMaps = new StreamResourceMapping(mapSource.getInputStream());
                Source weightSource = resolver.resolveURI(weightURL);
                this.descriptorWeights = new DescriptorWeightMap(weightSource.getInputStream());
                this.abstractCount = abstractCount;
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (null == query) {
            throw new IllegalArgumentException("null query");
        }
        this.query = query;
    }

    protected QueryMap getQueryMap() {
        if (null == this.query) {
            throw new IllegalStateException("null query");
        }
        if (null == this.resourceMaps) {
            return this.queryMapper.getQueryMap(this.query);
        } else {
            return this.queryMapper.getQueryMap(this.query, this.resourceMaps, this.descriptorWeights, this.abstractCount);
        }
    }
}
