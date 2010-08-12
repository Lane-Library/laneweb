package edu.stanford.irt.laneweb.cocoon;

import java.io.OutputStream;

import org.apache.cocoon.reading.Reader;

public abstract class AbstractReader extends AbstractSitemapModelComponent implements Reader {

    protected OutputStream outputStream;

    public long getLastModified() {
        return 0;
    }

    public String getMimeType() {
        return null;
    }

    public final void setOutputStream(final OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public boolean shouldSetContentLength() {
        return false;
    }
}
