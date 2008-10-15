package edu.stanford.irt.laneweb.querymap;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Set;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.SAXException;

import edu.stanford.irt.querymap.DescriptorWeightMap;
import edu.stanford.irt.querymap.StreamResourceMapping;

public class QueryMapGenerator implements Generator, Serviceable, ThreadSafe {

    private static final String QUERY = "query";

    private static final String RESOURCE_MAPS = "resource-maps";

    private static final String DESCRIPTOR_WEIGHTS = "descriptor-weights";

    private static final String ABSTRACT_COUNT = "abstract-count";

    private QueryMapper queryMapper;

    private ThreadLocal<String> query = new ThreadLocal<String>();;

    private ThreadLocal<XMLConsumer> consumer = new ThreadLocal<XMLConsumer>();

    private ThreadLocal<Map<String, Set<String>>> resourceMaps = new ThreadLocal<Map<String, Set<String>>>();

    private ThreadLocal<Map<String, Float>> descriptorWeights = new ThreadLocal<Map<String, Float>>();

    private ThreadLocal<Integer> abstractCount = new ThreadLocal<Integer>();

    public void setQueryMapper(final QueryMapper queryMapper) {
        if (null == queryMapper) {
            throw new IllegalArgumentException("null queryMapper");
        }
        this.queryMapper = queryMapper;
    }

    public void service(final ServiceManager serviceManager) throws ServiceException {
        if (null == serviceManager) {
            throw new IllegalArgumentException("null serviceManager");
        }
        setQueryMapper((QueryMapper) serviceManager.lookup(QueryMapper.class.getName()));
    }

    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters params) {
        if (null == params) {
            throw new IllegalArgumentException("null params");
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

    public void generate() throws SAXException {
        String query = this.query.get();
        if (null == query) {
            this.consumer.set(null);
            throw new IllegalStateException("null query");
        }
        XMLConsumer consumer = this.consumer.get();
        if (null == consumer) {
            this.query.set(null);
            throw new IllegalStateException("null consumer");
        }
        XMLizable queryMap = null;
        if (null == this.resourceMaps.get()) {
            queryMap = new XMLizableQueryMap(this.queryMapper.getQueryMap(query));
        } else {
            queryMap = new XMLizableQueryMap(this.queryMapper.getQueryMap(query, this.resourceMaps.get(), this.descriptorWeights.get(),
                    this.abstractCount.get().intValue()));
        }
        try {
            consumer.startDocument();
            queryMap.toSAX(consumer);
            consumer.endDocument();
        } finally {
            this.query.set(null);
            this.consumer.set(null);
            this.descriptorWeights.set(null);
            this.resourceMaps.set(null);
        }
    }

    public void setConsumer(final XMLConsumer consumer) {
        if (null == consumer) {
            throw new IllegalArgumentException("null consumer");
        }
        this.consumer.set(consumer);
    }

}
