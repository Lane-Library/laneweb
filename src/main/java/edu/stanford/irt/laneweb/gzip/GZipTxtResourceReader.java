package edu.stanford.irt.laneweb.gzip;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.processing.ProcessInfoProvider;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.TxtResourceReader;

public class GZipTxtResourceReader extends TxtResourceReader {

    private ProcessInfoProvider pip;

    @Override
    public void generate() throws IOException {
        super.generate();
        ((GZIPOutputStream) this.outputStream).finish();
    }

    @Override
    public Serializable getKey() {
        return super.getKey() + ";gzip";
    }

    @Override
    public void setOutputStream(final OutputStream out) {
        try {
            this.outputStream = new GZIPOutputStream(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setProcessInfoProvider(final ProcessInfoProvider pip) {
        this.pip = pip;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par) throws ProcessingException, SAXException,
            IOException {
        super.setup(resolver, objectModel, src, par);
        this.pip.getResponse().setHeader("Content-Encoding", "gzip");
        this.pip.getResponse().setHeader("Vary", "Accept-Encoding");
    }

    @Override
    public boolean shouldSetContentLength() {
        return true;
    }
}
