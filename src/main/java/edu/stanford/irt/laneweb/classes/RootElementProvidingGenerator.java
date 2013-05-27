package edu.stanford.irt.laneweb.classes;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.pipeline.generate.URLGenerator;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;

public class RootElementProvidingGenerator extends URLGenerator {

    private static final String NONCACHED_CLASSES = "noncached-classes";

    public RootElementProvidingGenerator(final String type, final SAXParser saxParser) {
        super(type, saxParser);
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        try {
            xmlConsumer.startElement("", NONCACHED_CLASSES, NONCACHED_CLASSES, new AttributesImpl());
            super.doGenerate(xmlConsumer);
            xmlConsumer.endElement("", NONCACHED_CLASSES, NONCACHED_CLASSES);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
