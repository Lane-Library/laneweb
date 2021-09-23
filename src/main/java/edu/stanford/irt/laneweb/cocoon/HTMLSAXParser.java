package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;

import org.apache.xerces.parsers.AbstractSAXParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.xml.ContentHandlerWrapper;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;

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

    private static final Logger log = LoggerFactory.getLogger(HTMLSAXParser.class);

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
        } catch (SAXException e) {
            // LANEWEB-10810: bad markup in description element causing zero results
            // log errors to determine frequency (change to warn?)
            log.error("sax parse error: {}; continuing", e.getMessage());
        } catch (IOException e) {
            throw new LanewebException(source.getURI(), e);
        }
    }
}
