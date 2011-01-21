package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;
import java.io.InputStream;

import org.apache.excalibur.xmlizer.XMLizer;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class LanewebXMLizer implements XMLizer {

    public void toSAX(final InputStream stream, final String mimeType, final String systemID, final ContentHandler handler)
            throws SAXException, IOException {
        InputSource inputSource = new InputSource(stream);
        inputSource.setSystemId(systemID);
        XMLReader reader = XMLReaderFactory.createXMLReader();
        reader.setContentHandler(handler);
        reader.parse(inputSource);
    }
}
