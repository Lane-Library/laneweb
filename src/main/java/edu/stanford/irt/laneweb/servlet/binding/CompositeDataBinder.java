package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class CompositeDataBinder implements DataBinder {

    private List<DataBinder> dataBinders;
    
    public CompositeDataBinder(final DataBinder... dataBinders) {
        this.dataBinders = Arrays.asList(dataBinders);
    }

    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        for (DataBinder binder : this.dataBinders) {
            binder.bind(model, request);
        }
    }
}
