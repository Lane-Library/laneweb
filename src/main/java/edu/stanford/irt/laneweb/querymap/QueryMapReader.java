package edu.stanford.irt.laneweb.querymap;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.cocoon.reading.Reader;
import org.xml.sax.SAXException;

//TODO: the label: property is not part of the QueryMap at the moment 
//so this is not useable.
public class QueryMapReader extends AbstractQueryMapComponent implements Reader {

    private ThreadLocal<OutputStream> outputStream = new ThreadLocal<OutputStream>();
    public void generate() throws SAXException, IOException {
        OutputStream outputStream = this.outputStream.get();
        if (null == outputStream) {
            super.reset();
            throw new IllegalStateException("null outputStream");
        }
        try {
            outputStream.write(new JSONableQueryMap(super.getQueryMap()).toString().getBytes());
        } finally {
            this.outputStream.set(null);
            super.reset();
        }
    }

    public long getLastModified() {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getMimeType() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setOutputStream(OutputStream outputStream) throws IOException {
        if (null == outputStream) {
            throw new IllegalArgumentException("null outputStream");
        }
        this.outputStream.set(outputStream);
    }

    public boolean shouldSetContentLength() {
        // TODO Auto-generated method stub
        return false;
    }

}
