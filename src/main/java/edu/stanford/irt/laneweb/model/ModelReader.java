package edu.stanford.irt.laneweb.model;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map.Entry;

import edu.stanford.irt.laneweb.cocoon.AbstractSitemapModelComponent;
import edu.stanford.irt.laneweb.cocoon.LanewebReader;

//$Id$
public class ModelReader extends AbstractSitemapModelComponent implements LanewebReader {
    
    private OutputStream outputStream;

    public void generate() throws IOException {
        for (Entry<String, Object> entry : ((MapModel)this.model).entrySet()) {
            outputStream.write((entry.getKey() + ": " + entry.getValue().toString() + "\n").getBytes());
        }
    }

    public long getLastModified() {
        return 0;
    }

    public String getMimeType() {
        return "text/plain";
    }

    public void setOutputStream(OutputStream out) {
        this.outputStream = out;
    }

    public boolean shouldSetContentLength() {
        return false;
    }
}
