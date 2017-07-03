package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.pipeline.serialize.TransformerSerializer;

public class HTML5Serializer extends TransformerSerializer {

    private static final byte[] HTML5_DOCTYPE_DECLARATION = "<!DOCTYPE html>\n".getBytes(StandardCharsets.UTF_8);

    private OutputStream outputStream;

    public HTML5Serializer(final String type, final TransformerHandler transformerHandler, final Properties properties) {
        super(type, transformerHandler, properties);
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
            throw new SAXException(e);
        }
        super.startDocument();
    }
}
