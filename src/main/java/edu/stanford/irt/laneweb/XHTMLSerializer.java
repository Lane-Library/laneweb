package edu.stanford.irt.laneweb;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.cocoon.serialization.AbstractTextSerializer;

public class XHTMLSerializer extends AbstractTextSerializer {

    @Override
    public void init() throws Exception {
        super.init();
        this.format.put(OutputKeys.METHOD, "xhtml");
    }

    /**
     * Set the {@link OutputStream} where the requested resource should be serialized.
     */
    @Override
    public void setOutputStream(final OutputStream out) throws IOException {
        super.setOutputStream(out);
        try {
            TransformerHandler handler = this.getTransformerHandler();
            handler.getTransformer().setOutputProperties(this.format);
            handler.setResult(new StreamResult(this.output));
            this.setContentHandler(handler);
            this.setLexicalHandler(handler);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }
}
