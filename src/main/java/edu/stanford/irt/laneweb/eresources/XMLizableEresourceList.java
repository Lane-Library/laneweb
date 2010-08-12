package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.cocoon.xml.XMLUtils;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.Resource;

@SuppressWarnings("serial")
public class XMLizableEresourceList extends LinkedList<Eresource> implements XMLizable, Resource {

    public XMLizableEresourceList(final Collection<Eresource> eresources) {
        super(eresources);
    }

    public void toSAX(final ContentHandler handler) throws SAXException {
        if (null == handler) {
            throw new IllegalArgumentException("null handler");
        }
        handler.startDocument();
        handler.startPrefixMapping("", NAMESPACE);
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, SIZE, SIZE, "CDATA", Integer.toString(size()));
        XMLUtils.startElement(handler, NAMESPACE, RESOURCES, atts);
        for (Eresource eresource : this) {
            new EresourceResource(eresource).toSAX(handler);
        }
        XMLUtils.endElement(handler, NAMESPACE, RESOURCES);
        handler.endPrefixMapping("");
        handler.endDocument();
    }
}
