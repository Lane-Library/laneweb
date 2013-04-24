package edu.stanford.irt.laneweb.cocoon;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.pipeline.CacheablePipelineComponent;
import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.pipeline.transform.AbstractTransformer;
import edu.stanford.irt.cocoon.cache.validity.NOPValidity;
import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.xml.EmbeddedXMLPipe;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class TextNodeParsingTransformer extends AbstractTransformer implements CacheablePipelineComponent, ParametersAware {

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
            final ByteArrayInputStream inputStream = new ByteArrayInputStream(this.content.toString().getBytes());
            Source source = new Source() {

                public boolean exists() {
                    return true;
                }
                public InputStream getInputStream() throws IOException {
                    return inputStream;
                }
                public String getURI() {
                    return null;
                }

                public Validity getValidity() {
                    throw new UnsupportedOperationException();
                }
                
                public Serializable getKey() {
                    return null;
                }
            };
            XMLConsumer pipe = new ProcessingInstructionSwallowingPipe(this.xmlConsumer);
            this.saxParser.parse(source, pipe);
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

    public Validity getValidity() {
        return NOPValidity.SHARED_INSTANCE;
    }

    @Override
    public void setXMLConsumer(final XMLConsumer xmlConsumer) {
        this.xmlConsumer = xmlConsumer;
        super.setXMLConsumer(xmlConsumer);
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
