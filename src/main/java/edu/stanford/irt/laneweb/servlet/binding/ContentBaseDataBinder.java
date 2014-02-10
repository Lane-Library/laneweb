package edu.stanford.irt.laneweb.servlet.binding;

import java.net.URL;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;

public class ContentBaseDataBinder implements DataBinder {

    private URL contentBase;

    public ContentBaseDataBinder(final URL contentBase) {
        this.contentBase = contentBase;
    }

    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        model.put(Model.CONTENT_BASE, this.contentBase);
    }
}
