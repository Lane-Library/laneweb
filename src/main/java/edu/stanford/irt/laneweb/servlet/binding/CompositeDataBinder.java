package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class CompositeDataBinder implements DataBinder {

    private List<DataBinder> dataBinders = Collections.emptyList();
    
    //TODO: remove this constructor after changing bean definition to constructor injection.
    public CompositeDataBinder() {}
    
    public CompositeDataBinder(final List<DataBinder> dataBinders) {
        this.dataBinders = dataBinders;
    }
    
    public CompositeDataBinder(final DataBinder... dataBinders) {
        this.dataBinders = Arrays.asList(dataBinders);
    }

    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        for (DataBinder binder : this.dataBinders) {
            binder.bind(model, request);
        }
    }

    public void setDataBinders(final List<DataBinder> dataBinders) {
        this.dataBinders = dataBinders;
    }
}
