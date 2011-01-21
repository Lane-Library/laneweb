package edu.stanford.irt.laneweb.querymap;

import java.util.Iterator;
import java.util.Set;

import edu.stanford.irt.querymap.Descriptor;
import edu.stanford.irt.querymap.QueryMap;
import edu.stanford.irt.querymap.Resource;
import edu.stanford.irt.querymap.ResourceMap;

public class JSONableQueryMap {

    private static final String COLON_QUOTE = "\":\"";

    private static final String COMMA_QUOTE = "\",\"";

    private QueryMap queryMap;

    /**
     * Converts a querymap into a JSON String via the toString() method.
     * 
     * @param queryMap
     *            the QueryMap
     * @throws IllegalArgumentException
     *             if null
     */
    public JSONableQueryMap(final QueryMap queryMap) {
        if (null == queryMap) {
            throw new IllegalArgumentException("null queryMap");
        }
        this.queryMap = queryMap;
    }

    /**
     * @return the JSON representation of the querymap
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("{\"query").append(COLON_QUOTE);
        escapeForJS(sb, this.queryMap.getQuery());
        Descriptor desc = this.queryMap.getDescriptor();
        if (null != desc) {
            sb.append(COMMA_QUOTE).append("descriptor").append(COLON_QUOTE);
            escapeForJS(sb, desc.getDescriptorName());
            ResourceMap map = this.queryMap.getResourceMap();
            if (null != map) {
                sb.append(COMMA_QUOTE).append("resourceMap\":{\"descriptor").append(COLON_QUOTE);
                escapeForJS(sb, map.getDescriptor().getDescriptorName());
                Set<Resource> resources = map.getResources();
                if (null != resources) {
                    sb.append(COMMA_QUOTE).append("resources\":[");
                    for (Iterator<Resource> it = resources.iterator(); it.hasNext();) {
                        Resource resource = it.next();
                        sb.append("{\"id").append(COLON_QUOTE).append(resource.getId()).append(COMMA_QUOTE).append("label")
                                .append(COLON_QUOTE);
                        escapeForJS(sb, resource.getLabel());
                        sb.append("\"}");
                        if (it.hasNext()) {
                            sb.append(',');
                        }
                    }
                    sb.append("]");
                }
                sb.append("}");
            } else {
                sb.append("\"");
            }
        } else {
            sb.append("\"");
        }
        return sb.append("}").toString();
    }

    /**
     * prepend quotes and apostrophes with a backslash
     * 
     * @param sb
     *            the StringBuffer accumulating the text
     * @param text
     *            the text to escape
     */
    private void escapeForJS(final StringBuffer sb, final String text) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if ((c == '"')) {
                sb.append('\\');
            }
            sb.append(c);
        }
    }
}
