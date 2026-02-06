package edu.stanford.irt.laneweb.resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;

import org.springframework.oxm.Marshaller;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.spring.SpringResourceSource;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.XMLByteStreamInterpreter;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.cocoon.xml.XMLizable;
import edu.stanford.irt.cocoon.xml.XPointerProcessor;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.cocoon.AbstractMarshallingGenerator;
import edu.stanford.irt.laneweb.cocoon.CachedXMLSourceResolver;

public class BannerResourceOutageGenerator extends AbstractMarshallingGenerator {

    BannerResourceOutageService service;
    SAXParser xmlSAXParser;
    CachedXMLSourceResolver cachedXMLSourceResolver;

    public BannerResourceOutageGenerator(final Marshaller marshaller,
            final SAXParser xmlSAXParser,
            BannerResourceOutageService resourceOutageService) {
        super(marshaller);

        this.service = resourceOutageService;
        this.xmlSAXParser = xmlSAXParser;
    }

    @Override
    protected void doGenerate(XMLConsumer xmlConsumer) {

        final ByteArrayInputStream inputStream = this.service.getHtmlResourceOutages();
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
        this.xmlSAXParser.parse(source, xmlConsumer);
    }

}
