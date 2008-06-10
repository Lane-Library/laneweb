package edu.stanford.irt.laneweb.querymap;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.SAXException;

public class QueryMapGenerator implements Generator, Serviceable, ThreadSafe {

    private static final String QUERY = "query";

    private QueryMapper queryMapper;

    private ThreadLocal<String> query = new ThreadLocal<String>();;

    private ThreadLocal<XMLConsumer> consumer = new ThreadLocal<XMLConsumer>();

    public void setQueryMapper(final QueryMapper queryMapper) {
        if (null == queryMapper) {
            throw new IllegalArgumentException("null queryMapper");
        }
        this.queryMapper = queryMapper;
    }

    public void service(final ServiceManager serviceManager)
            throws ServiceException {
        if (null == serviceManager) {
            throw new IllegalArgumentException("null serviceManager");
        }
        setQueryMapper((QueryMapper) serviceManager.lookup(QueryMapper.class
                .getName()));
    }

    public void setup(final SourceResolver resolver, final Map objectModel,
            final String src, final Parameters params) {
        if (null == params) {
            throw new IllegalArgumentException("null params");
        }
        String query = params.getParameter(QUERY, null);
        if (null == query) {
            throw new IllegalArgumentException("null query");
        }
        this.query.set(query);
        ;
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
        try {
            XMLizable queryMap = new XMLizableQueryMap(this.queryMapper
                    .getQueryMap(query));
            consumer.startDocument();
            queryMap.toSAX(consumer);
            consumer.endDocument();
        } finally {
            this.query.set(null);
            this.consumer.set(null);
        }
    }

    public void setConsumer(final XMLConsumer consumer) {
        if (null == consumer) {
            throw new IllegalArgumentException("null consumer");
        }
        this.consumer.set(consumer);
    }

}
