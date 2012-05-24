package edu.stanford.irt.laneweb.search;

import java.io.IOException;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.xml.EmbeddedXMLPipe;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.AggregatedValidity;
import org.apache.xerces.parsers.AbstractSAXParser;
import org.cyberneko.html.HTMLConfiguration;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.pipeline.transform.AbstractTransformer;

public class FilePathTransformer extends AbstractTransformer implements CacheableProcessingComponent {

    private static class HtmlSAXParser extends AbstractSAXParser {

        protected HtmlSAXParser(final HTMLConfiguration conf) {
            super(conf);
        }
    }

    private static final String TYPE = "file-path";

    private HtmlSAXParser htmlSaxParser;

    private XMLConsumer pipe;

    private SourceResolver sourceResolver;

    private AggregatedValidity validity;

    public FilePathTransformer(final SourceResolver sourceResolver) {
        this.sourceResolver = sourceResolver;
        HTMLConfiguration conf = new HTMLConfiguration();
        // TODO: review properties
        conf.setProperty("http://cyberneko.org/html/properties/default-encoding", "UTF-8");
        conf.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
        conf.setFeature("http://cyberneko.org/html/features/insert-namespaces", true);
        conf.setProperty("http://cyberneko.org/html/properties/namespaces-uri", "http://www.w3.org/1999/xhtml");
        this.htmlSaxParser = new HtmlSAXParser(conf);
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
        this.htmlSaxParser.setContentHandler(this.pipe);
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
                this.htmlSaxParser.parse(inputSource);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            super.startElement(uri, localName, qName, atts);
        }
    }
}
