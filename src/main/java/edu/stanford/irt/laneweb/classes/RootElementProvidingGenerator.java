package edu.stanford.irt.laneweb.classes;

import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.pipeline.generate.URLGenerator;
import edu.stanford.irt.laneweb.LanewebException;

public class RootElementProvidingGenerator extends URLGenerator {

    public RootElementProvidingGenerator(final String type) {
        super(type);
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        try {
            xmlConsumer.startElement("", "noncached-classes", "noncached-classes", new AttributesImpl());
            super.doGenerate(xmlConsumer);
            xmlConsumer.endElement("", "noncached-classes", "noncached-classes");
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
