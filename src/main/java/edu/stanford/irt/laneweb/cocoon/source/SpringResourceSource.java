package edu.stanford.irt.laneweb.cocoon.source;

import java.io.IOException;
import java.io.InputStream;

import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.FileTimeStampValidity;
import org.springframework.core.io.Resource;

public class SpringResourceSource implements Source {

    /**
     * A private always invalid source validity to avoid returning a null validity.
     */
    private static SourceValidity INVALID = new SourceValidity() {

        private static final long serialVersionUID = 1L;

        public int isValid() {
            return SourceValidity.INVALID;
        }

        public int isValid(SourceValidity newValidity) {
            return SourceValidity.INVALID;
        }
        
    };

    private Resource resource;

    public SpringResourceSource(final Resource resource) {
        this.resource = resource;
    }

    public boolean exists() {
        return this.resource.exists();
    }

    public long getContentLength() {
        try {
            return this.resource.contentLength();
        } catch (IOException e) {
            return -1;
        }
    }

    public InputStream getInputStream() throws IOException {
        return this.resource.getInputStream();
    }

    public long getLastModified() {
        try {
            return this.resource.lastModified();
        } catch (IOException e) {
            return 0;
        }
    }

    public String getMimeType() {
        return null;
    }

    public String getScheme() {
        throw new UnsupportedOperationException();
    }

    public String getURI() {
        try {
            return this.resource.getURI().toString();
        } catch (IOException e) {
            throw new LanewebSourceException(e);
        }
    }

    public SourceValidity getValidity() {
        try {
            return new FileTimeStampValidity(this.resource.getFile());
        } catch (IOException e) {
            return INVALID;
        }
    }
    
    public void refresh() {
    }
}
