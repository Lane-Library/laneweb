/**
 * 
 */
package edu.stanford.irt.laneweb;

import java.io.Serializable;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Response;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.Transformer;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.NOPValidity;
import org.springframework.util.Assert;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * This transformer checks for elements within a body element and sets the
 * response code to 404 if there are none.
 * 
 * @author ceyates
 */
public class NoBodyContent404Transformer implements Transformer, CacheableProcessingComponent {

    private static final String KEY = "NBC";

    private XMLConsumer consumer;

    private Response response;

    private boolean parsingBody = false;

    private boolean hasBodyContent = false;

    private boolean done = false;

    public void startElement(final String uri, final String localName, final String name, final Attributes atts) throws SAXException {
        if (!this.parsingBody && !this.done) {
            if ("body".equals(localName)) {
                this.parsingBody = true;
            }
        } else {
            this.hasBodyContent = true;
            this.done = true;
        }
        this.consumer.startElement(uri, localName, name, atts);
    }

    public void endElement(final String uri, final String localName, final String name) throws SAXException {
        if (this.parsingBody) {
            if ("body".equals(localName)) {
                this.done = true;
                if (!this.hasBodyContent) {
                    this.response.setStatus(404);
                }
            }
        }
        this.consumer.endElement(uri, localName, name);
    }

    /*
     * (non-Javadoc)
     * @seeorg.apache.cocoon.xml.XMLProducer#setConsumer(org.apache.cocoon.xml.
     * XMLConsumer)
     */
    public void setConsumer(final XMLConsumer consumer) {
        Assert.notNull(consumer);
        this.consumer = consumer;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.apache.cocoon.sitemap.SitemapModelComponent#setup(org.apache.cocoon
     * .environment.SourceResolver, java.util.Map, java.lang.String,
     * org.apache.avalon.framework.parameters.Parameters)
     */
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par) {
        this.response = ObjectModelHelper.getResponse(objectModel);
        Assert.notNull(this.response);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.cocoon.caching.CacheableProcessingComponent#getKey()
     */
    public Serializable getKey() {
        return KEY;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.cocoon.caching.CacheableProcessingComponent#getValidity()
     */
    public SourceValidity getValidity() {
        return NOPValidity.SHARED_INSTANCE;
    }

    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        this.consumer.characters(ch, start, length);
    }

    public void comment(final char[] ch, final int start, final int length) throws SAXException {
        this.consumer.comment(ch, start, length);
    }

    public void endCDATA() throws SAXException {
        this.consumer.endCDATA();
    }

    public void endDocument() throws SAXException {
        this.consumer.endDocument();
    }

    public void endDTD() throws SAXException {
        this.consumer.endDTD();
    }

    public void endEntity(final String name) throws SAXException {
        this.consumer.endEntity(name);
    }

    public void endPrefixMapping(final String prefix) throws SAXException {
        this.consumer.endPrefixMapping(prefix);
    }

    public void ignorableWhitespace(final char[] ch, final int start, final int length) throws SAXException {
        this.consumer.ignorableWhitespace(ch, start, length);
    }

    public void processingInstruction(final String target, final String data) throws SAXException {
        this.consumer.processingInstruction(target, data);
    }

    public void setDocumentLocator(final Locator locator) {
        this.consumer.setDocumentLocator(locator);
    }

    public void skippedEntity(final String name) throws SAXException {
        this.consumer.skippedEntity(name);
    }

    public void startCDATA() throws SAXException {
        this.consumer.startCDATA();
    }

    public void startDocument() throws SAXException {
        this.consumer.startDocument();
    }

    public void startDTD(final String name, final String publicId, final String systemId) throws SAXException {
        this.consumer.startDTD(name, publicId, systemId);
    }

    public void startEntity(final String name) throws SAXException {
        this.consumer.startEntity(name);
    }

    public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
        this.consumer.startPrefixMapping(prefix, uri);
    }

}
