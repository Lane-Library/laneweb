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

    private static final int JSON_RETURN_LIMIT = 20;

    private String limit;

    private MeshDescriptorManager meshDescriptorManager;

    private OutputStream outputStream;

    private String query;

    public void generate() throws IOException {
        MeshSuggestComparator comparator = new MeshSuggestComparator(this.query);
        TreeSet<String> meshSet = new TreeSet<String>(comparator);
        Collection<MeshDescriptor> descriptors;
        if ("d".equalsIgnoreCase(this.limit) || "p".equalsIgnoreCase(this.limit)) {
            descriptors = this.meshDescriptorManager.getDescriptorsForDiseaseTerm(this.query);
        } else if ("i".equalsIgnoreCase(this.limit)) {
            descriptors = this.meshDescriptorManager.getDescriptorsForInterventionTerm(this.query);
        } else {
            descriptors = this.meshDescriptorManager.getDescriptorsForTerm(this.query);
        }
        for (MeshDescriptor descriptor : descriptors) {
            meshSet.add(descriptor.getDescriptorName());
        }
        Iterator<String> it = meshSet.iterator();
        int count = 0;
            this.outputStream.write(JSON_1);
            String maybeComma = "\"";
            while (it.hasNext() && count < JSON_RETURN_LIMIT) {
                count++;
                this.outputStream.write((maybeComma + it.next() + '"').getBytes());
                maybeComma = ",\"";
            }
            this.outputStream.write(JSON_2);
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
        this.outputStream = outputStream;
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver arg0, final Map arg1, final String arg2, final Parameters params) {
        this.limit = params.getParameter("limit", null);
        this.query = params.getParameter("query", null);
    }

    public boolean shouldSetContentLength() {
        return false;
    }
}
