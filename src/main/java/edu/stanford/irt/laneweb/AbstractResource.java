package edu.stanford.irt.laneweb;

import org.apache.cocoon.xml.XMLUtils;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;


public abstract class AbstractResource implements Resource {

    /**
     * handles elements without child nodes
     * 
     * @param handler
     *            the ContentHandler
     * @param name
     *            the element tagname
     * @param content
     *            the String content
     * @throws SAXException
     */
    public void handleElement(final ContentHandler handler, final String name, final String content)
            throws SAXException {
        if ((content == null) || (content.length() == 0)) {
            return;
        }
        XMLUtils.startElement(handler, NAMESPACE, name);
        XMLUtils.data(handler, content);
        XMLUtils.endElement(handler, name);
    }
}
