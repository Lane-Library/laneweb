package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class CompositeDataBinder implements DataBinder {
    
    private List<DataBinder> dataBinders = Collections.emptyList();
    
    public void setDataBinders(List<DataBinder> dataBinders) {
        this.dataBinders = dataBinders;
    }

    public void bind(Map<String, Object> model, HttpServletRequest request) {
        for (DataBinder binder: this.dataBinders) {
            binder.bind(model, request);
        }
    }
}
