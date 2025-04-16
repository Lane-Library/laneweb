package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.sax.SAXResult;

import org.easymock.Capture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.oxm.Marshaller;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;

public class ParameterMapGeneratorTest {

    private ParameterMapGenerator generator;

    private Marshaller marshaller;

    private XMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() {
        this.marshaller = mock(Marshaller.class);
        this.generator = new ParameterMapGenerator(this.marshaller);
        this.xmlConsumer = mock(XMLConsumer.class);
    }

    @Test
    public void testDoGenerateXMLConsumer() throws IOException {
        Capture<Map<String, String[]>> mapCapture = newCapture();
        Capture<SAXResult> saxResultCapture = newCapture();
        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("foo", new String[] { "bar", "baz" });
        this.generator.setModel(Collections.singletonMap(Model.PARAMETER_MAP, parameterMap));
        this.marshaller.marshal(capture(mapCapture), capture(saxResultCapture));
        replay(this.marshaller);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.marshaller);
        assertEquals(parameterMap, mapCapture.getValue());
        assertSame(this.xmlConsumer, saxResultCapture.getValue().getHandler());
    }
}
