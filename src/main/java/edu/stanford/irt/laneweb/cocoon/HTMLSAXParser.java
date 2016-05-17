package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;

import org.apache.xerces.parsers.AbstractSAXParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;

public class HTMLSAXParser extends AbstractSAXParser implements SAXParser {

    public HTMLSAXParser(final NekoHTMLConfiguration config) {
        super(config);
    }

    @Override
    public void parse(final Source source, final XMLConsumer xmlConsumer) {
        try {
            setContentHandler(xmlConsumer);
            // case 79097 processing instructions with joost transformations
            // nekohtml doesn't send endDTD which confuses joost. fix by not setting lexicalHandler
            InputSource inputSource = new InputSource();
            inputSource.setSystemId(source.getURI());
            inputSource.setByteStream(source.getInputStream());
            parse(inputSource);
        } catch (IOException | SAXException e) {
            throw new LanewebException(source.getURI(), e);
        }
    }
}
