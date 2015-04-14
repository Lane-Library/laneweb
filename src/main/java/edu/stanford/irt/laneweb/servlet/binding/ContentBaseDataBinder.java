package edu.stanford.irt.laneweb.servlet.binding;

import java.net.URL;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;

public class ContentBaseDataBinder implements DataBinder {

    private URL contentBase;

    public ContentBaseDataBinder(final URL contentBase) {
        if (contentBase == null) {
            throw new LanewebException("null contentBase");
        }
        this.contentBase = contentBase;
    }

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        model.put(Model.CONTENT_BASE, this.contentBase);
    }
}
