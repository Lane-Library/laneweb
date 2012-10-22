package edu.stanford.irt.laneweb.search;

import org.apache.cocoon.core.xml.SAXParser;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.xml.EmbeddedXMLPipe;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.AggregatedValidity;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.pipeline.transform.AbstractCacheableTransformer;

public class FilePathTransformer extends AbstractCacheableTransformer {

    private static final String TYPE = "file-path";

    private XMLConsumer pipe;

    private SAXParser saxParser;

    private SourceResolver sourceResolver;

    private AggregatedValidity validity;

    public FilePathTransformer(final SourceResolver sourceResolver, final SAXParser saxParser) {
        super(TYPE);
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

    @Override
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
            Source source = this.sourceResolver.resolveURI("file:" + atts.getValue("path"));
            this.validity.add(source.getValidity());
            this.saxParser.parse(source, this.pipe);
        } else {
            super.startElement(uri, localName, qName, atts);
        }
    }
}
