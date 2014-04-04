package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Properties;

import javax.xml.transform.sax.SAXTransformerFactory;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.pipeline.serialize.TransformerSerializer;
import edu.stanford.irt.laneweb.LanewebException;

public class HTML5Serializer extends TransformerSerializer {

    private static final byte[] HTML5_DOCTYPE_DECLARATION = "<!DOCTYPE html>\n".getBytes(Charset.forName("UTF-8"));

    private OutputStream outputStream;

    public HTML5Serializer(final String type, final SAXTransformerFactory factory, final Properties properties) {
        super(type, factory, properties);
    }

    @Override
    public void setOutputStream(final OutputStream out) {
        super.setOutputStream(out);
        this.outputStream = out;
    }

    @Override
    public void startDocument() throws SAXException {
        try {
            this.outputStream.write(HTML5_DOCTYPE_DECLARATION);
        } catch (IOException e) {
            throw new LanewebException(e);
        }
        super.startDocument();
    }
}
