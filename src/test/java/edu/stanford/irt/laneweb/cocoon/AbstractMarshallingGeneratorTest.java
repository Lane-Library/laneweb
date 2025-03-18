package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.same;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import javax.xml.transform.sax.SAXResult;

import org.easymock.Capture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.MarshallingFailureException;
import org.springframework.oxm.XmlMappingException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;

public class AbstractMarshallingGeneratorTest {

    private static final class TestGenerator extends AbstractMarshallingGenerator {

        public TestGenerator(final Marshaller marshaller) {
            super(marshaller);
        }

        @Override
        protected void doGenerate(final XMLConsumer xmlConsumer) {
        }
    }

    private AbstractMarshallingGenerator generator;

    private Marshaller marshaller;

    private XMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() throws Exception {
        this.marshaller = mock(Marshaller.class);
        this.generator = new TestGenerator(this.marshaller);
        this.xmlConsumer = mock(XMLConsumer.class);
    }

    @Test
    public void testMarshall() throws XmlMappingException, IOException {
        Capture<SAXResult> capture = newCapture();
        Object obj = new Object();
        this.marshaller.marshal(same(obj), capture(capture));
        replay(this.marshaller, this.xmlConsumer);
        this.generator.marshal(obj, this.xmlConsumer);
        verify(this.marshaller, this.xmlConsumer);
        assertSame(this.xmlConsumer, capture.getValue().getHandler());
    }

    @Test
    public void testMarshallThrowIOException() throws XmlMappingException, IOException {
        Capture<SAXResult> capture = newCapture();
        Object obj = new Object();
        this.marshaller.marshal(same(obj), capture(capture));
        expectLastCall().andThrow(new IOException());
        replay(this.marshaller, this.xmlConsumer);
        assertThrows(LanewebException.class, () -> {
            this.generator.marshal(obj, this.xmlConsumer);
        });

    }

    @Test
    public void testMarshallThrowXmlMappingException() throws XmlMappingException, IOException {
        Capture<SAXResult> capture = newCapture();
        Object obj = new Object();
        this.marshaller.marshal(same(obj), capture(capture));
        expectLastCall().andThrow(new MarshallingFailureException("oopsie"));
        replay(this.marshaller, this.xmlConsumer);
        assertThrows(LanewebException.class, () -> {
            this.generator.marshal(obj, this.xmlConsumer);
        });
    }
}
