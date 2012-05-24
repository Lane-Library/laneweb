package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;

import javax.xml.transform.sax.SAXResult;

import org.apache.cocoon.xml.XMLConsumer;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.XmlMappingException;

import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.laneweb.LanewebException;

public abstract class AbstractMarshallingGenerator extends AbstractGenerator {

    private Marshaller marshaller;

    public void setMarshaller(final Marshaller marshaller) {
        if (marshaller == null) {
            throw new LanewebException("null marshaller");
        }
        this.marshaller = marshaller;
    }

    protected void marshall(final Object object, final XMLConsumer xmlConsumer) {
        if (this.marshaller == null) {
            throw new LanewebException("null marshaller");
        }
        try {
            this.marshaller.marshal(object, new SAXResult(xmlConsumer));
        } catch (XmlMappingException e) {
            throw new LanewebException(e);
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }
}
