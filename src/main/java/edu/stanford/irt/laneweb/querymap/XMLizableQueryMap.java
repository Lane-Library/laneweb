package edu.stanford.irt.laneweb.querymap;

import org.apache.cocoon.xml.XMLUtils;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.querymap.Descriptor;
import edu.stanford.irt.querymap.QueryMap;
import edu.stanford.irt.querymap.ResourceMap;
import edu.stanford.irt.querymap.WeightedDescriptor;

public class XMLizableQueryMap implements XMLizable {

    private static final String QUERY_MAP = "query-map";

    private static final String QUERY = "query";

    private static final String DESCRIPTOR = "descriptor";

    private static final String RESOURCE_MAP = "resource-map";

    private static final String RESOURCE = "resource";

    private static final String IDREF = "idref";

    private static final String NAMESPACE = "http://lane.stanford.edu/querymap/ns";

    private QueryMap queryMap;

    public XMLizableQueryMap(final QueryMap queryMap) {
        if (null == queryMap) {
            throw new IllegalArgumentException("null queryMap");
        }
        this.queryMap = queryMap;
    }

    public void toSAX(final ContentHandler consumer) throws SAXException {
        consumer.startPrefixMapping("", NAMESPACE);
        XMLUtils.startElement(consumer, NAMESPACE, QUERY_MAP);
        XMLUtils.createElementNS(consumer, NAMESPACE, QUERY, this.queryMap
                .getQuery());
        Descriptor descriptor = this.queryMap.getDescriptor();
        if (null != descriptor) {
            XMLUtils.createElementNS(consumer, NAMESPACE, DESCRIPTOR,
                    descriptor.getDescriptorName());
            ResourceMap resourceMap = this.queryMap.getResourceMap();
            if (null != resourceMap) {
                XMLUtils.startElement(consumer, NAMESPACE, RESOURCE_MAP);
                XMLUtils.createElementNS(consumer, NAMESPACE, DESCRIPTOR,
                        resourceMap.getDescriptor().getDescriptorName());
                for (String idref : resourceMap.getResources()) {
                    AttributesImpl atts = new AttributesImpl();
                    atts.addAttribute("", IDREF, IDREF, "IDREF", idref);
                    XMLUtils.createElementNS(consumer, NAMESPACE, RESOURCE,
                            atts);
                }
                XMLUtils.endElement(consumer, NAMESPACE, RESOURCE_MAP);
            }
        }
        if (null != this.queryMap.getTreePath()) {
            XMLUtils.startElement(consumer, NAMESPACE, "tree-paths");
            for (Descriptor path : this.queryMap.getTreePath()) {
                XMLUtils.startElement(consumer, NAMESPACE,  "tree-path");
                XMLUtils.createElementNS(consumer, NAMESPACE, DESCRIPTOR,
                        path.getDescriptorName());
                for (String tree : path.getTreeNumbers()) {
                    XMLUtils.createElementNS(consumer, NAMESPACE, "tree", tree);
                }
                XMLUtils.endElement(consumer, NAMESPACE, "tree-path");
            }
            XMLUtils.endElement(consumer, NAMESPACE, "tree-paths");
        }
        if (null != this.queryMap.getFrequencies()) {
            XMLUtils.startElement(consumer, NAMESPACE, "frequencies");
            int maxDescriptors = 10;
            for (Descriptor frequency : this.queryMap.getFrequencies()) {
                if (maxDescriptors-- < 0) {
                    break;
                }
                XMLUtils.createElementNS(consumer, NAMESPACE, DESCRIPTOR,
                        frequency.toString());
            }
            XMLUtils.endElement(consumer, NAMESPACE, "frequencies");
        }
        XMLUtils.endElement(consumer, NAMESPACE, QUERY_MAP);
        consumer.endPrefixMapping("");
    }

}
