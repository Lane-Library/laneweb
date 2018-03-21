package edu.stanford.irt.laneweb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;

import edu.stanford.irt.cocoon.pipeline.serialize.TransformerSerializer;

public class TestXMLConsumer extends TransformerSerializer {

    private static Map<String, String> PROPS;

    private static final String UTF_8 = StandardCharsets.UTF_8.name();
    static {
        PROPS = new HashMap<>();
        PROPS.put("method", "xml");
        PROPS.put("encoding", UTF_8);
        PROPS.put("indent", "yes");
    }

    private ByteArrayOutputStream baos = new ByteArrayOutputStream();

    public TestXMLConsumer() {
        super("test", getTransformerHandler(
                (SAXTransformerFactory) TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl", null)),
                PROPS);
        setParameters(Collections.emptyMap());
        setOutputStream(this.baos);
    }

    private static final TransformerHandler getTransformerHandler(final SAXTransformerFactory factory) {
        try {
            return factory.newTransformerHandler();
        } catch (TransformerConfigurationException e) {
            throw new LanewebException(e);
        }
    }

    public String getExpectedResult(final Object test, final String fileName) throws IOException {
        StringWriter sw = new StringWriter();
        InputStreamReader br = new InputStreamReader(test.getClass().getResourceAsStream(fileName),
                StandardCharsets.UTF_8);
        char[] cbuf = new char[1024];
        while (true) {
            int i = br.read(cbuf);
            if (i == -1) {
                break;
            }
            sw.write(cbuf, 0, i);
        }
        return sw.toString();
    }

    public String getStringValue() {
        return new String(this.baos.toByteArray(), StandardCharsets.UTF_8);
    }
}
