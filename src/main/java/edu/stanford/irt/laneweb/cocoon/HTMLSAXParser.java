package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;

import org.apache.xerces.parsers.AbstractSAXParser;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.xml.ContentHandlerWrapper;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.ResourceNotFoundException;

public class HTMLSAXParser extends AbstractSAXParser implements SAXParser {

    // nekohtml doesn't provide namespaces for the elements it creates, breaking saxon
    private static class MissingNamespaceProvider extends ContentHandlerWrapper {

        public MissingNamespaceProvider(final XMLConsumer xmlConsumer) {
            super(xmlConsumer, null);
        }

        @Override
        public void endElement(final String uri, final String localName, final String qName) throws SAXException {
            String ns = uri;
            if ("".equals(uri)) {
                ns = "http://www.w3.org/1999/xhtml";
            }
            super.endElement(ns, localName, qName);
        }

        @Override
        public void startElement(final String uri, final String localName, final String qName, final Attributes atts)
                throws SAXException {
            String ns = uri;
            if ("".equals(uri)) {
                ns = "http://www.w3.org/1999/xhtml";
            }
            super.startElement(ns, localName, qName, atts);
        }
    }

    public HTMLSAXParser(final NekoHTMLConfiguration config) {
        super(config);
    }

    @Override
    public void parse(final Source source, final XMLConsumer xmlConsumer) {
        try {
            setContentHandler(new MissingNamespaceProvider(xmlConsumer));
            // case 79097 processing instructions with joost transformations
            // nekohtml doesn't send endDTD which confuses joost. fix by not setting lexicalHandler
            InputSource inputSource = new InputSource();
            inputSource.setSystemId(source.getURI());
            inputSource.setByteStream(source.getInputStream());
            parse(inputSource);
        } catch (IOException ioe) {
            throw new ResourceNotFoundException(ioe.getMessage());          
        }catch(SAXException e) {
            throw new LanewebException(source.getURI(), e);
        }
    }
}
