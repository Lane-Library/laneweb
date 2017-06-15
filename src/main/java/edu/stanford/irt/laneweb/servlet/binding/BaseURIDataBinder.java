package edu.stanford.irt.laneweb.servlet.binding;

import java.net.URI;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.LanewebException;

public class BaseURIDataBinder implements DataBinder {

    private String key;

    private URI uri;

    public BaseURIDataBinder(final String key, final URI uri) {
        this.key = key;
        if (uri == null) {
            throw new LanewebException("null uri");
        }
        this.uri = uri;
    }

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        model.put(this.key, this.uri);
    }
}
