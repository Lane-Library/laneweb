package edu.stanford.irt.laneweb.resource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.oxm.Marshaller;

import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.cocoon.AbstractMarshallingGenerator;

public class BannerResourceOutageGenerator extends AbstractMarshallingGenerator {

    BannerResourceOutageService service;
    SAXParser xmlSAXParser;

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
