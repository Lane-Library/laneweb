package edu.stanford.irt.laneweb.cocoon;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.cache.validity.AlwaysValid;
import edu.stanford.irt.cocoon.pipeline.CacheablePipelineComponent;
import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.xml.AbstractXMLPipe;
import edu.stanford.irt.cocoon.xml.EmbeddedXMLPipe;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;

public class TextNodeParsingTransformer extends AbstractXMLPipe
        implements Transformer, CacheablePipelineComponent, ParametersAware {

    // the html parser creates screwy processing instructions from the classes
    // xml.
    private static final class ProcessingInstructionSwallowingPipe extends EmbeddedXMLPipe {

        public ProcessingInstructionSwallowingPipe(final XMLConsumer consumer) {
            super(consumer);
        }

        @Override
        public void processingInstruction(final String target, final String data) {
            // do nothing
        }
    }

    private StringBuilder content = new StringBuilder();

    private Collection<String> elementNames;

    private boolean inElement;

    private SAXParser saxParser;

    private String type;

    private XMLConsumer xmlConsumer;

    public TextNodeParsingTransformer(final String type, final SAXParser saxParser) {
        this.type = type;
        this.saxParser = saxParser;
    }

    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        if (this.inElement) {
            this.content.append(ch, start, length);
        } else {
            this.xmlConsumer.characters(ch, start, length);
        }
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        if (this.elementNames.contains(qName)) {
            this.inElement = false;
            final ByteArrayInputStream inputStream = new ByteArrayInputStream(
                    this.content.toString().getBytes(StandardCharsets.UTF_8));
            Source source = new Source() {

                @Override
                public boolean exists() {
                    return true;
                }

                @Override
                public InputStream getInputStream() {
                    return inputStream;
                }

                @Override
                public String getURI() {
                    return null;
                }
            };
            XMLConsumer pipe = new ProcessingInstructionSwallowingPipe(this.xmlConsumer);
            this.saxParser.parse(source, pipe);
        }
        this.content = new StringBuilder();
        this.xmlConsumer.endElement(uri, localName, qName);
    }

    @Override
    public Serializable getKey() {
        return this.type;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public Validity getValidity() {
        return AlwaysValid.SHARED_INSTANCE;
    }

    @Override
    public void setParameters(final Map<String, String> parameters) {
        if (!parameters.containsKey("elementNames")) {
            throw new LanewebException("elementNames parameter is required");
        }
        this.elementNames = Arrays.asList(parameters.get("elementNames").split(","));
    }

    @Override
    public void setXMLConsumer(final XMLConsumer xmlConsumer) {
        this.xmlConsumer = xmlConsumer;
        super.setXMLConsumer(xmlConsumer);
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes atts)
            throws SAXException {
        this.xmlConsumer.startElement(uri, localName, qName, atts);
        if (this.elementNames.contains(qName)) {
            this.inElement = true;
        }
    }
}
