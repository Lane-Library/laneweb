package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;
import java.io.InputStream;

import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceValidity;
import org.springframework.core.io.Resource;

import edu.stanford.irt.cocoon.source.FileTimeStampValidity;
import edu.stanford.irt.cocoon.source.SourceException;

public class SpringResourceSource implements Source {

    private Resource resource;

    public SpringResourceSource(final Resource resource) {
        this.resource = resource;
    }

    /**
     * Whether or not this Source exists, delegates to the Spring Resource.
     */
    public boolean exists() {
        return this.resource.exists();
    }

    public InputStream getInputStream() throws IOException {
        return this.resource.getInputStream();
    }

    public String getURI() {
        try {
            return this.resource.getURI().toString();
        } catch (IOException e) {
            throw new SourceException(e);
        }
    }

    public SourceValidity getValidity() {
        try {
            return new FileTimeStampValidity(this.resource.getFile());
        } catch (IOException e) {
            return null;
        }
    }
}
