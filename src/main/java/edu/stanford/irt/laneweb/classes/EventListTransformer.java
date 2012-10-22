package edu.stanford.irt.laneweb.classes;

import org.apache.cocoon.core.xml.SAXParser;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.xml.EmbeddedXMLPipe;
import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.pipeline.transform.AbstractCacheableTransformer;

public class EventListTransformer extends AbstractCacheableTransformer {

    private static final String TYPE = "eventlist";

    private XMLConsumer pipe;

    private SAXParser saxParser;

    private SourceResolver sourceResolver;

    public EventListTransformer(final SourceResolver sourceResolver, final SAXParser saxParser) {
        super(TYPE);
        this.sourceResolver = sourceResolver;
        this.saxParser = saxParser;
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        if (!"event".equals(localName)) {
            super.endElement(uri, localName, qName);
        }
    }

    @Override
    public void setConsumer(final XMLConsumer xmlConsumer) {
        this.pipe = new EmbeddedXMLPipe(xmlConsumer);
        super.setConsumer(xmlConsumer);
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes atts)
            throws SAXException {
        if ("event".equals(localName)) {
            this.saxParser.parse(this.sourceResolver.resolveURI(atts.getValue("href")), this.pipe);
        } else {
            super.startElement(uri, localName, qName, atts);
        }
    }
}
