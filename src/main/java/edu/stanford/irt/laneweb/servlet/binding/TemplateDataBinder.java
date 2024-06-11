package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class TemplateDataBinder implements DataBinder {

    private TemplateChooser templateChooser;

    public TemplateDataBinder(final TemplateChooser templateChooser) {
        this.templateChooser = templateChooser;
    }

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        if (ModelUtil.getObject(model, Model.RESPONSIVE, Boolean.class, Boolean.FALSE)) {
            // use the responsive template if responsive toggle is on.
            model.put(Model.TEMPLATE, Model.RESPONSIVE);
        } else if (ModelUtil.getObject(model, Model.DEBUG, Boolean.class, Boolean.FALSE)) {
            // use the debug template if debug mode is on.
            model.put(Model.TEMPLATE, Model.DEBUG);
        } else {
            model.put(Model.TEMPLATE, this.templateChooser.getTemplate(request));
        }
    }
}
