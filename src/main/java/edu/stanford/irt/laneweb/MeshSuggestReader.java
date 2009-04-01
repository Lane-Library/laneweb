package edu.stanford.irt.laneweb;

import edu.stanford.irt.laneweb.voyager.VoyagerMeshSuggest;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.reading.Reader;
import org.xml.sax.SAXException;

public class MeshSuggestReader implements Reader {

    private VoyagerMeshSuggest voyagerMeshSuggest = null;
    
    private ThreadLocal<OutputStream> outputStream = new ThreadLocal<OutputStream>();

    private ThreadLocal<String> query = new ThreadLocal<String>();

    private ThreadLocal<String> limit = new ThreadLocal<String>();
    
    public void generate() throws IOException, SAXException, ProcessingException {
        OutputStream out = this.outputStream.get();
        String q = this.query.get();
        String l = this.limit.get().toLowerCase();
        ArrayList<String> meshList = new ArrayList<String>();
        meshList = this.voyagerMeshSuggest.getMesh(q, l);
        Iterator<String> it = meshList.iterator();
        try {
            out.write(("{\"mesh\": [").getBytes());
            String maybeComma = "\"";
            while (it.hasNext()) {
                out.write((maybeComma + it.next() + '"').getBytes());
                maybeComma = ",\"";
            }
            out.write("]}".getBytes());
        } finally {
            this.outputStream.set(null);
            this.query.set(null);
            this.limit.set(null);
        }
    }

    public long getLastModified() {
        return 0;
    }

    public String getMimeType() {
        return "text/plain";
    }

    public void setOutputStream(final OutputStream outputStream) throws IOException {
        if (null == outputStream) {
            throw new IllegalArgumentException("null outputStream");
        }
        this.outputStream.set(outputStream);
    }

    public void setVoyagerMeshSuggest(final VoyagerMeshSuggest voyagerMeshSuggest ) {
        if (null == voyagerMeshSuggest) {
            throw new IllegalArgumentException("null voyagerMeshSuggest");
        }
        this.voyagerMeshSuggest = voyagerMeshSuggest;
    }
    
    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver arg0, final Map arg1, final String arg2, final Parameters params) throws ProcessingException, SAXException,
            IOException {
        String l = params.getParameter("limit", null);
        this.limit.set(l);
        String q = params.getParameter("query", null);
        this.query.set(q);
    }

    public boolean shouldSetContentLength() {
        return false;
    }
}
