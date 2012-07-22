package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Map;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.core.xml.SAXParser;
import org.apache.cocoon.xml.EmbeddedXMLPipe;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.NOPValidity;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.pipeline.transform.AbstractTransformer;
import edu.stanford.irt.laneweb.LanewebException;

public class TextNodeParsingTransformer extends AbstractTransformer implements CacheableProcessingComponent, ParametersAware {

    // the html parser creates screwy processing instructions from the classes
    // xml. TODO: this is specific to the classes yet this class is can be used
    // generally
    private static final class ProcessingInstructionSwallowingPipe extends EmbeddedXMLPipe {

        public ProcessingInstructionSwallowingPipe(final XMLConsumer consumer) {
            super(consumer);
        }

        @Override
        public void processingInstruction(final String target, final String data) throws SAXException {
        }
    }

    private StringBuilder content = new StringBuilder();

    private String elementName = "event_description";

    private boolean inElement = false;

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
        if (this.elementName.equals(qName)) {
            this.inElement = false;
            StringReader stringReader = new StringReader(this.content.toString());
            InputSource inputSource = new InputSource(stringReader);
            XMLConsumer pipe = new ProcessingInstructionSwallowingPipe(this.xmlConsumer);
            try {
                this.saxParser.parse(inputSource, pipe, pipe);
            } catch (IOException e) {
                throw new LanewebException(e);
            }
        }
        this.content = new StringBuilder();
        this.xmlConsumer.endElement(uri, localName, qName);
    }

    public Serializable getKey() {
        return "textNodeParsing";
    }

    public String getType() {
        return this.type;
    }

    public SourceValidity getValidity() {
        return NOPValidity.SHARED_INSTANCE;
    }

    @Override
    public void setConsumer(final XMLConsumer xmlConsumer) {
        this.xmlConsumer = xmlConsumer;
        super.setConsumer(xmlConsumer);
    }

    public void setParameters(final Map<String, String> parameters) {
        String name = parameters.get("elementName");
        if (name != null) {
            this.elementName = name;
        }
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes atts)
            throws SAXException {
        this.xmlConsumer.startElement(uri, localName, qName, atts);
        if (this.elementName.equals(qName)) {
            this.inElement = true;
        }
    }
}
