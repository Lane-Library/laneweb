package edu.stanford.irt.laneweb.querymap;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import edu.stanford.irt.laneweb.cocoon.AbstractSitemapModelComponent;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.querymap.DescriptorWeightMap;
import edu.stanford.irt.querymap.QueryMap;
import edu.stanford.irt.querymap.QueryMapper;
import edu.stanford.irt.querymap.Resource;
import edu.stanford.irt.querymap.StreamResourceMapping;

public abstract class AbstractQueryMapComponent extends AbstractSitemapModelComponent {

    private static final String ABSTRACT_COUNT = "abstract-count";

    private static final String DESCRIPTOR_WEIGHTS = "descriptor-weights";

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

    protected void initialize() {
        if (null == this.queryMapper) {
            throw new IllegalStateException("null queryMapper");
        }
        String query = this.model.getString(Model.QUERY);
        if (null == query) {
            throw new IllegalArgumentException("null query");
        }
        this.query = query;
        String mapURL = this.parameterMap.get(RESOURCE_MAPS);
        String weightURL = this.parameterMap.get(DESCRIPTOR_WEIGHTS);
        String abstractCount = this.parameterMap.get(ABSTRACT_COUNT);
        if ((null != mapURL) && (null != weightURL)) {
            try {
                this.resourceMaps = new StreamResourceMapping(new URL(mapURL).openStream());
                this.descriptorWeights = new DescriptorWeightMap(new URL(weightURL).openStream());
                this.abstractCount = Integer.parseInt(abstractCount);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    protected QueryMap getQueryMap() {
        if (null == this.query) {
            throw new IllegalStateException("null query");
        }
        if (null == this.resourceMaps) {
            return this.queryMapper.getQueryMap(this.query);
        } else {
            return this.queryMapper.getQueryMap(this.query, this.resourceMaps, this.descriptorWeights,
                    this.abstractCount);
        }
    }
}
