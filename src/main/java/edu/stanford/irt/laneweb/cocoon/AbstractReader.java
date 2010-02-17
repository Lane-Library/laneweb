package edu.stanford.irt.laneweb.cocoon;

import java.io.OutputStream;


public abstract class AbstractReader extends AbstractSitemapModelComponent implements LanewebReader {
    
    protected OutputStream outputStream;

    public String getMimeType() {
        return null;
    }

    public final void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public boolean shouldSetContentLength() {
        return false;
    }

    public long getLastModified() {
        return 0;
    }
    
    
}
