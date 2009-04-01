package edu.stanford.irt.laneweb;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.reading.Reader;

import edu.stanford.irt.laneweb.voyager.VoyagerMeshSuggest;

public class MeshSuggestReader implements Reader {

    private static final byte[] JSON_1 = "{\"mesh\": [".getBytes();

    private static final byte[] JSON_2 = "]}".getBytes();

    private ThreadLocal<String> limit = new ThreadLocal<String>();

    private ThreadLocal<OutputStream> outputStream = new ThreadLocal<OutputStream>();

    private ThreadLocal<String> query = new ThreadLocal<String>();

    private VoyagerMeshSuggest voyagerMeshSuggest = null;

    public void generate() throws IOException {
        OutputStream out = this.outputStream.get();
        String q = this.query.get();
        String l = this.limit.get();
        List<String> meshList = this.voyagerMeshSuggest.getMesh(q, l);
        Iterator<String> it = meshList.iterator();
        try {
            out.write(JSON_1);
            String maybeComma = "\"";
            while (it.hasNext()) {
                out.write((maybeComma + it.next() + '"').getBytes());
                maybeComma = ",\"";
            }
            out.write(JSON_2);
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

    public void setOutputStream(final OutputStream outputStream) {
        if (null == outputStream) {
            throw new IllegalArgumentException("null outputStream");
        }
        this.outputStream.set(outputStream);
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver arg0, final Map arg1, final String arg2, final Parameters params) {
        this.limit.set(params.getParameter("limit", null));
        this.query.set(params.getParameter("query", null));
    }

    public void setVoyagerMeshSuggest(final VoyagerMeshSuggest voyagerMeshSuggest) {
        if (null == voyagerMeshSuggest) {
            throw new IllegalArgumentException("null voyagerMeshSuggest");
        }
        this.voyagerMeshSuggest = voyagerMeshSuggest;
    }

    public boolean shouldSetContentLength() {
        return false;
    }
}
