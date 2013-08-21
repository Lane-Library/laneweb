package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.springframework.core.io.Resource;

import edu.stanford.irt.cocoon.cache.Cacheable;
import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.cache.validity.FileTimeStampValidity;
import edu.stanford.irt.cocoon.cache.validity.NeverValid;
import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.source.SourceException;

public class SpringResourceSource implements Cacheable, Source {

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

    public Serializable getKey() {
        return getURI();
    }

    public String getURI() {
        try {
            return this.resource.getURI().toString();
        } catch (IOException e) {
            throw new SourceException(e);
        }
    }

    public Validity getValidity() {
        try {
            return new FileTimeStampValidity(this.resource.getFile());
        } catch (IOException e) {
            return NeverValid.SHARED_INSTANCE;
        }
    }

    @Override
    public String toString() {
        return this.resource.toString();
    }
}
