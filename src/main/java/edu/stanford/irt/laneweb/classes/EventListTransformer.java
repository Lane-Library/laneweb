package edu.stanford.irt.laneweb.classes;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.pipeline.transform.AbstractCacheableTransformer;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.cocoon.xml.EmbeddedXMLPipe;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.XMLConsumer;

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
    public void setXMLConsumer(final XMLConsumer xmlConsumer) {
        this.pipe = new EmbeddedXMLPipe(xmlConsumer);
        super.setXMLConsumer(xmlConsumer);
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
