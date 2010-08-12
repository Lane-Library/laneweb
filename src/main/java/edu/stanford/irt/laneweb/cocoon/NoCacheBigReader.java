package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.excalibur.source.SourceValidity;

/**
 * To prevent caching of large files this Reader returns a null key if the source size is greater than 1000000 bytes
 * (~1.2M).
 * 
 * @author ceyates
 */
public class NoCacheBigReader extends AbstractReader implements CacheableProcessingComponent {

    /**
     * Generates the requested resource.
     */
    public void generate() throws IOException {
        InputStream input = this.source.getInputStream();
        int i = 0;
        byte[] buffer = new byte[1024];
        while (true) {
            i = input.read(buffer);
            if (i == -1) {
                break;
            }
            this.outputStream.write(buffer, 0, i);
        }
    }

    public Serializable getKey() {
        if (this.source.getContentLength() > 1000000) {
            return null;
        }
        return this.source.getURI();
    }

    /**
     * @return the time the read source was last modified or 0 if it is not possible to detect
     */
    @Override
    public long getLastModified() {
        return this.source.getLastModified();
    }

    public SourceValidity getValidity() {
        return this.source.getValidity();
    }
}
