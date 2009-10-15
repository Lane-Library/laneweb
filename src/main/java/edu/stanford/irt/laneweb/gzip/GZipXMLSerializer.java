package edu.stanford.irt.laneweb.gzip;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.zip.GZIPOutputStream;

import org.apache.cocoon.processing.ProcessInfoProvider;
import org.apache.cocoon.serialization.XMLSerializer;
import org.xml.sax.SAXException;

public class GZipXMLSerializer extends XMLSerializer {

    private ProcessInfoProvider pip;

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        this.pip.getResponse().setHeader("Content-Encoding", "gzip");
        this.pip.getResponse().setHeader("Vary", "Accept-Encoding");
        try {
            ((GZIPOutputStream) this.output).finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Serializable getKey() {
        return super.getKey() + ";gzip";
    }

    @Override
    public void setOutputStream(final OutputStream arg0) throws IOException {
        super.setOutputStream(new GZIPOutputStream(arg0));
    }

    public void setProcessInfoProvider(final ProcessInfoProvider pip) {
        this.pip = pip;
    }

    @Override
    public boolean shouldSetContentLength() {
        return true;
    }
}
