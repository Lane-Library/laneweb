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
    
    @Override
    public void generate() throws IOException {
        super.generate();
        ((GZIPOutputStream)this.outputStream).finish();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws ProcessingException, SAXException, IOException {
        super.setup(resolver, objectModel, src, par);
        this.pip.getResponse().setHeader("Content-Encoding", "gzip");
    }

    private ProcessInfoProvider pip;
    
    public void setProcessInfoProvider(ProcessInfoProvider pip) {
        this.pip = pip;
    }

    public Serializable getKey() {
        return super.getKey() + ";gzip";
    }

    public void setOutputStream(final OutputStream out) {
        try {
            this.outputStream = new GZIPOutputStream(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean shouldSetContentLength() {
        return true;
    }
}
