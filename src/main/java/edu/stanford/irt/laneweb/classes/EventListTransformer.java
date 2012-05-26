package edu.stanford.irt.laneweb.classes;

import java.io.IOException;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.core.xml.SAXParser;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.xml.EmbeddedXMLPipe;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.NOPValidity;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.pipeline.transform.AbstractTransformer;
import edu.stanford.irt.laneweb.LanewebException;

public class EventListTransformer extends AbstractTransformer implements CacheableProcessingComponent {

    private static final String TYPE = "eventlist";

    private XMLConsumer pipe;

    private SAXParser saxParser;

    private SourceResolver sourceResolver;

    private SourceValidity validity;

    public EventListTransformer(final SourceResolver sourceResolver, final SAXParser saxParser) {
        this.sourceResolver = sourceResolver;
        this.saxParser = saxParser;
        this.validity = NOPValidity.SHARED_INSTANCE;
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        if (!"event".equals(localName)) {
            super.endElement(uri, localName, qName);
        }
    }

    public String getKey() {
        return TYPE;
    }

    public String getType() {
        return TYPE;
    }

    public SourceValidity getValidity() {
        return this.validity;
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
            try {
                InputSource inputSource = new InputSource(this.sourceResolver.resolveURI(atts.getValue("href")).getInputStream());
                this.saxParser.parse(inputSource, this.pipe);
            } catch (IOException e) {
                throw new LanewebException(e);
            }
        } else {
            super.startElement(uri, localName, qName, atts);
        }
    }
}
