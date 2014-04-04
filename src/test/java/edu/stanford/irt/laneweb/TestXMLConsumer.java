package edu.stanford.irt.laneweb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Properties;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;

import edu.stanford.irt.cocoon.pipeline.serialize.TransformerSerializer;

public class TestXMLConsumer extends TransformerSerializer {
    
    private static Properties PROPS;
    
    static {
        PROPS = new Properties();
        PROPS.setProperty("method", "xml");
        PROPS.setProperty("encoding", "UTF-8");
        PROPS.setProperty("indent", "yes");
    }
    
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    
    public TestXMLConsumer() {
        super("test", (SAXTransformerFactory) TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl",
                null), PROPS);
        setOutputStream(this.baos);
    }
    
    public String getStringValue() {
        return new String(this.baos.toByteArray(), Charset.forName("UTF-8"));
    }

    public String getExpectedResult(Object test, final String fileName) throws IOException {
        StringWriter sw = new StringWriter();
        InputStreamReader br = new InputStreamReader(test.getClass().getResourceAsStream(fileName), Charset.forName("UTF-8"));
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
}
