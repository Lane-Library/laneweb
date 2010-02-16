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

import edu.stanford.irt.laneweb.model.DefaultObjectModelAware;
import edu.stanford.irt.laneweb.model.LanewebObjectModel;

public class TxtResourceReader extends DefaultObjectModelAware implements Reader, CacheableProcessingComponent {

    private String defaultPath;

    private String valueToSubstitute;

    protected OutputStream outputStream;

    protected String path;

    protected Source source;

    public void generate() throws IOException {
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
        } finally {
            if (bf != null) {
                bf.close();
            }
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
        //get the path from a sitemap parameter or the base-path from the model, or the default
        this.path = par.getParameter("path", getString(LanewebObjectModel.BASE_PATH, this.defaultPath));
        this.source = resolver.resolveURI(src);
    }

    public void setValueToSubstitute(final String valueToSubstitute) {
        this.valueToSubstitute = valueToSubstitute;
    }

    public boolean shouldSetContentLength() {
        return false;
    }
}
