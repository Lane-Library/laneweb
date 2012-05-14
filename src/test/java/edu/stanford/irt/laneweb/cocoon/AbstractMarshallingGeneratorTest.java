package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.fail;

import java.io.IOException;

import javax.xml.transform.sax.SAXResult;

import org.apache.cocoon.xml.XMLConsumer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.XmlMappingException;

import edu.stanford.irt.laneweb.LanewebException;

public class AbstractMarshallingGeneratorTest {

    private static final class TestGenerator extends AbstractMarshallingGenerator {

        @Override
        protected void doGenerate(final XMLConsumer xmlConsumer) {
        }
    }

    private AbstractMarshallingGenerator generator;

    private Marshaller marshaller;

    @Before
    public void setUp() throws Exception {
        this.generator = new TestGenerator();
        this.marshaller = createMock(Marshaller.class);
    }

    @Test
    public void testMarshall() throws XmlMappingException, IOException {
        Object obj = new Object();
        this.marshaller.marshal(eq(obj), isA(SAXResult.class));
        replay(this.marshaller);
        this.generator.setMarshaller(this.marshaller);
        this.generator.marshall(obj, null);
        verify(this.marshaller);
    }

    @Test
    public void testMarshallNull() {
        try {
            this.generator.marshall(null, null);
            fail();
        } catch (LanewebException e) {
        }
    }

    @Test
    public void testSetMarshaller() {
        replay(this.marshaller);
        this.generator.setMarshaller(this.marshaller);
        verify(this.marshaller);
    }

    @Test
    public void testSetMarshallerNull() {
        try {
            this.generator.setMarshaller(null);
            fail();
        } catch (LanewebException e) {
        }
    }
}
