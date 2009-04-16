package edu.stanford.irt.laneweb;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.reading.Reader;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceValidity;
import org.xml.sax.SAXException;

public class TxtResourceReader implements Reader, CacheableProcessingComponent {

    private String defaultPath;

    private OutputStream outputStream;

    private String path;

    private Source source;

    private String valueToSubstitute;

    public void generate() throws IOException, SAXException, ProcessingException {
        BufferedReader bf = null;
        try {
            bf = new BufferedReader(new InputStreamReader(this.source.getInputStream()));
            String line = null;
            while ((line = bf.readLine()) != null) {
                line = line.replaceAll(this.valueToSubstitute, this.path);
                this.outputStream.write(line.getBytes());
                this.outputStream.write('\n');
            }
            this.outputStream.flush();
        } catch (IOException e) {
            throw e;
        } finally {
            if (bf != null)
                bf.close();
        }
    }

    public Serializable getKey() {
        return this.source.getURI() + ";path=" + this.path;
    }

    public long getLastModified() {
        return this.source.getLastModified();
    }

    public String getMimeType() {
        return null;
    }

    public SourceValidity getValidity() {
        return this.source.getValidity();
    }

    public void setDefaultPath(final String path) {
        this.defaultPath = path;
    }

    public void setOutputStream(final OutputStream out) {
        if ((out instanceof BufferedOutputStream) || (out instanceof org.apache.cocoon.util.BufferedOutputStream)) {
            this.outputStream = out;
        } else {
            this.outputStream = new BufferedOutputStream(out, 1536);
        }
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par)
            throws ProcessingException, SAXException, IOException {
        this.path = par.getParameter("path", this.defaultPath);
        this.source = resolver.resolveURI(src);
    }

    public void setValueToSubstitute(final String valueToSubstitute) {
        this.valueToSubstitute = valueToSubstitute;
    }

    public boolean shouldSetContentLength() {
        return false;
    }
}
