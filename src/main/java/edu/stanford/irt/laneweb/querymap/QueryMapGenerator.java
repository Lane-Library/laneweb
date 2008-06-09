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
import org.apache.cocoon.xml.XMLUtils;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.querymap.Descriptor;
import edu.stanford.irt.querymap.QueryMap;
import edu.stanford.irt.querymap.ResourceMap;

public class QueryMapGenerator implements Generator, Serviceable, ThreadSafe {

    private static final String QUERY_MAP = "query-map";

    private static final String QUERY = "query";

    private static final String DESCRIPTOR = "descriptor";

    private static final String RESOURCE_MAP = "resource-map";

    private static final String RESOURCE = "resource";

    private static final String IDREF = "idref";

    private static final String NAMESPACE = "http://lane.stanford.edu/querymap/ns";

    protected QueryMapper queryMapper;

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
        setQueryMapper((QueryMapper) serviceManager
                .lookup(QueryMapper.class.getName()));
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
        this.query.set(query);;
    }

    public void generate() throws SAXException {
        String query = this.query.get();
        XMLConsumer consumer = this.consumer.get();
        QueryMap queryMap = this.queryMapper.getQueryMap(query);
        consumer.startDocument();
        XMLUtils.startElement(consumer, NAMESPACE, QUERY_MAP);
        XMLUtils.createElementNS(consumer, NAMESPACE, QUERY, queryMap
                .getQuery());
        Descriptor descriptor = queryMap.getDescriptor();
        if (null != descriptor) {
            XMLUtils.createElementNS(consumer, NAMESPACE, DESCRIPTOR,
                    descriptor.getDescriptorName());
            ResourceMap resourceMap = queryMap.getResourceMap();
            if (null != resourceMap) {
                XMLUtils.startElement(consumer, NAMESPACE, RESOURCE_MAP);
                XMLUtils.createElementNS(consumer, NAMESPACE, DESCRIPTOR,
                        resourceMap.getDescriptor().getDescriptorName());
                for (String idref : resourceMap.getResources()) {
                    AttributesImpl atts = new AttributesImpl();
                    atts.addAttribute("", IDREF, IDREF, "IDREF", idref);
                    XMLUtils.createElementNS(consumer, NAMESPACE,
                            RESOURCE, atts);
                }
                XMLUtils.endElement(consumer, NAMESPACE, RESOURCE_MAP);
            }
        }
        XMLUtils.endElement(consumer, NAMESPACE, QUERY_MAP);
        consumer.endDocument();
    }

    public void setConsumer(final XMLConsumer consumer) {
        if (null == consumer) {
            throw new IllegalArgumentException("null consumer");
        }
        this.consumer.set(consumer);
    }

}
