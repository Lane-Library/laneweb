package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;

import javax.xml.transform.sax.SAXResult;

import org.springframework.oxm.Marshaller;
import org.springframework.oxm.XmlMappingException;

import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;

public abstract class AbstractMarshallingGenerator extends AbstractGenerator {

    private Marshaller marshaller;

    protected AbstractMarshallingGenerator(final Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    protected void marshal(final Object object, final XMLConsumer xmlConsumer) {
        try {
            this.marshaller.marshal(object, new SAXResult(xmlConsumer));
        } catch (IOException | XmlMappingException e) {
            throw new LanewebException(e);
        }
    }
}
