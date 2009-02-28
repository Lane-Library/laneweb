package edu.stanford.irt.laneweb;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.reading.ResourceReader;

/**
 * To prevent caching of large files this Reader returns a null key if the
 * source size is greater than 1000000 bytes (~1.2M).
 * 
 * @author ceyates
 */
public class NoCacheBigReader extends ResourceReader {

    /**
     * Generates the requested resource.
     */
    @Override
    public void generate() throws IOException, ProcessingException {
        InputStream inputStream = this.inputSource.getInputStream();
        try {
            processStream(inputStream);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    @Override
    public Serializable getKey() {
        if (this.inputSource.getContentLength() > 1000000) {
            return null;
        }
        return super.getKey();
    }

    /**
     * @return the time the read source was last modified or 0 if it is not
     *         possible to detect
     */
    @Override
    public long getLastModified() {
        if (hasRanges()) {
            // This is a byte range request so we can't use the cache, return
            // null.
            return 0;
        }
        return this.inputSource.getLastModified();
    }
}
