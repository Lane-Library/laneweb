package edu.stanford.irt.laneweb.search;

import java.io.IOException;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.core.xml.SAXParser;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.xml.EmbeddedXMLPipe;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.AggregatedValidity;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.pipeline.transform.AbstractTransformer;
import edu.stanford.irt.laneweb.LanewebException;

public class FilePathTransformer extends AbstractTransformer implements CacheableProcessingComponent {

    private static final String TYPE = "file-path";

    private XMLConsumer pipe;

    private SAXParser saxParser;

    private SourceResolver sourceResolver;

    private AggregatedValidity validity;

    public FilePathTransformer(final SourceResolver sourceResolver, final SAXParser saxParser) {
        this.sourceResolver = sourceResolver;
        this.saxParser = saxParser;
        this.validity = new AggregatedValidity();
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        if (!"file".equals(localName)) {
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
        if ("file".equals(localName)) {
            try {
                Source source = this.sourceResolver.resolveURI("file:" + atts.getValue("path"));
                this.validity.add(source.getValidity());
                InputSource inputSource = new InputSource(source.getInputStream());
                inputSource.setSystemId(source.getURI());
                this.saxParser.parse(inputSource, this.pipe, this.pipe);
            } catch (IOException e) {
                throw new LanewebException(e);
            }
        } else {
            super.startElement(uri, localName, qName, atts);
        }
    }
}
