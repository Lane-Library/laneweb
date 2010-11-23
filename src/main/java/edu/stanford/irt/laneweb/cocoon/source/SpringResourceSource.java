package edu.stanford.irt.laneweb.cocoon.source;

import java.io.IOException;
import java.io.InputStream;

import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.FileTimeStampValidity;
import org.springframework.core.io.Resource;


public class SpringResourceSource implements Source {
    
    private Resource resource;
    
    public SpringResourceSource(Resource resource) {
        this.resource = resource;
    }

    
    public boolean exists() {
        return this.resource.exists();
    }

    
    public InputStream getInputStream() throws IOException, SourceNotFoundException {
        return this.resource.getInputStream();
    }

    
    public String getURI() {
        try {
            return this.resource.getURI().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    
    public String getScheme() {
        throw new UnsupportedOperationException();
    }

    
    public SourceValidity getValidity() {
        try {
             return new FileTimeStampValidity(this.resource.getFile());
        } catch (IOException e) {
            return null;
        }
    }

    
    public void refresh() {
    }

    
    public String getMimeType() {
        return null;
    }

    
    public long getContentLength() {
        try {
            return this.resource.contentLength();
        } catch (IOException e) {
            return -1;
        }
    }

    
    public long getLastModified() {
        try {
            return this.resource.lastModified();
        } catch (IOException e) {
            return -1;
        }
    }
}
