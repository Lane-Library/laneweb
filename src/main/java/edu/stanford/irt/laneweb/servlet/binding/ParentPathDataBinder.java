package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;

public class ParentPathDataBinder implements DataBinder {

    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        String basePath = (String) model.get(Model.BASE_PATH);
        String path = request.getRequestURI().substring(basePath.length());
        if (path.indexOf(".html") > -1 && path.indexOf('-') > -1) {
            int dashIndex = path.lastIndexOf('-');
            if (dashIndex > -1) {
                String parentPath = path.substring(0, dashIndex) + ".html";
                model.put(Model.PARENT_PATH, parentPath);
            }
        }
    }
}
