package edu.stanford.irt.laneweb.querymap;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.cocoon.reading.Reader;
import org.xml.sax.SAXException;

public class QueryMapReader extends AbstractQueryMapComponent implements Reader {

    private static final String MIME_TYPE = "text/plain";

    private OutputStream outputStream;

    public void generate() throws SAXException, IOException {
        if (null == this.outputStream) {
            throw new IllegalStateException("null outputStream");
        }
        this.outputStream.write(new JSONableQueryMap(super.getQueryMap()).toString().getBytes());
    }

    public long getLastModified() {
        return 0;
    }

    public String getMimeType() {
        return MIME_TYPE;
    }

    public void setOutputStream(final OutputStream outputStream) throws IOException {
        if (null == outputStream) {
            throw new IllegalArgumentException("null outputStream");
        }
        this.outputStream = outputStream;
    }

    public boolean shouldSetContentLength() {
        return false;
    }
}
