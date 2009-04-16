package edu.stanford.irt.laneweb.mesh;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.reading.Reader;

import edu.stanford.irt.lane.mesh.export.MeshDescriptor;
import edu.stanford.irt.lane.mesh.export.MeshDescriptorManager;

public class MeshSuggestReader implements Reader {

    private static final byte[] JSON_1 = "{\"mesh\":[".getBytes();

    private static final byte[] JSON_2 = "]}".getBytes();

    private static int JSON_RETURN_LIMIT = 20;

    private ThreadLocal<String> limit = new ThreadLocal<String>();

    private MeshDescriptorManager meshDescriptorManager;

    private ThreadLocal<OutputStream> outputStream = new ThreadLocal<OutputStream>();

    private ThreadLocal<String> query = new ThreadLocal<String>();

    public void generate() throws IOException {
        OutputStream out = this.outputStream.get();
        String q = this.query.get();
        String l = this.limit.get();
        MeshSuggestComparator comparator = new MeshSuggestComparator(q);
        TreeSet<String> meshSet = new TreeSet<String>(comparator);
        Collection<MeshDescriptor> descriptors;
        if ("d".equalsIgnoreCase(l) || "p".equalsIgnoreCase(l)) {
            descriptors = this.meshDescriptorManager.getDescriptorsForDiseaseTerm(q);
        } else if ("i".equalsIgnoreCase(l)) {
            descriptors = this.meshDescriptorManager.getDescriptorsForInterventionTerm(q);
        } else {
            descriptors = this.meshDescriptorManager.getDescriptorsForTerm(q);
        }
        for (MeshDescriptor descriptor : descriptors) {
            meshSet.add(descriptor.getDescriptorName());
        }
        Iterator<String> it = meshSet.iterator();
        int count = 0;
        try {
            out.write(JSON_1);
            String maybeComma = "\"";
            while (it.hasNext() && count < JSON_RETURN_LIMIT) {
                count++;
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

    public void setMeshDescriptorManager(final MeshDescriptorManager meshDescriptorManager) {
        if (null == meshDescriptorManager) {
            throw new IllegalArgumentException("null meshDescriptorManager");
        }
        this.meshDescriptorManager = meshDescriptorManager;
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

    public boolean shouldSetContentLength() {
        return false;
    }
}
