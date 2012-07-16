package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;

import org.apache.cocoon.core.xml.SAXParser;
import org.apache.xerces.parsers.AbstractSAXParser;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public class HTMLSAXParser extends AbstractSAXParser implements SAXParser {

    public HTMLSAXParser(final NekoHTMLConfiguration config) {
        super(config);
    }

    public void parse(final InputSource in, final ContentHandler consumer) throws SAXException, IOException {
        setContentHandler(consumer);
        parse(in);
    }

    public void parse(final InputSource in, final ContentHandler contentHandler, final LexicalHandler lexicalHandler)
            throws SAXException, IOException {
        setContentHandler(contentHandler);
        setLexicalHandler(lexicalHandler);
        parse(in);
    }
}
