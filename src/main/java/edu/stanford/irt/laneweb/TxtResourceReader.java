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

public class TxtResourceReader extends GzipOutputComponent implements Reader, CacheableProcessingComponent {

    private String defaultPath;

    private String path;

    private String valueToSubstitute;

    private Source source;

    public void setDefaultPath(final String path) {
        this.defaultPath = path;
    }

    public void setValueToSubstitute(final String valueToSubstitute) {
        this.valueToSubstitute = valueToSubstitute;
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par)
            throws ProcessingException, SAXException, IOException {
    	super.setup(resolver, objectModel, src, par);
        this.path = par.getParameter("path", this.defaultPath);
        this.source = resolver.resolveURI(src);
    }

    public Serializable getKey() {
        return this.source.getURI() + ";path=" + this.path;
    }

    public void generate() throws IOException, SAXException, ProcessingException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(this.source.getInputStream()));
        String line = null;
        while ((line = bf.readLine()) != null) {
            line = line.replaceAll(this.valueToSubstitute, this.path);
            this.outputStream.write(line.getBytes());
            this.outputStream.write('\n');
        }
        this.outputStream.flush();
    }

    public long getLastModified() {
        return this.source.getLastModified();
    }

    public SourceValidity getValidity() {
        return this.source.getValidity();
    }
}
