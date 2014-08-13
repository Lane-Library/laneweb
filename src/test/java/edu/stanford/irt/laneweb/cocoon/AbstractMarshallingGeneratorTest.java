package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;

import javax.xml.transform.sax.SAXResult;

import org.junit.Before;
import org.junit.Test;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.XmlMappingException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class AbstractMarshallingGeneratorTest {

    private static final class TestGenerator extends AbstractMarshallingGenerator {

        public TestGenerator(Marshaller marshaller) {
            super(marshaller);
        }

        @Override
        protected void doGenerate(final XMLConsumer xmlConsumer) {
        }
    }

    private AbstractMarshallingGenerator generator;

    private Marshaller marshaller;

    @Before
    public void setUp() throws Exception {
        this.marshaller = createMock(Marshaller.class);
        this.generator = new TestGenerator(this.marshaller);
    }

    @Test
    public void testMarshall() throws XmlMappingException, IOException {
        Object obj = new Object();
        this.marshaller.marshal(eq(obj), isA(SAXResult.class));
        replay(this.marshaller);
        this.generator.marshall(obj, null);
        verify(this.marshaller);
    }
}
