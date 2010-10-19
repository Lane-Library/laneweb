package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;


public class TemplateDataBinder implements DataBinder {
    
    private TemplateChooser templateChooser;

    public void bind(Map<String, Object> model, HttpServletRequest request) {
        model.put(Model.TEMPLATE, this.templateChooser.getTemplate(request));
    }
    
    public void setTemplateChooser(TemplateChooser templateChooser) {
        this.templateChooser = templateChooser;
    }
}
