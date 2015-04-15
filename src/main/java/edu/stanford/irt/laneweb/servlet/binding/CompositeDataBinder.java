package edu.stanford.irt.laneweb.servlet.binding;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class CompositeDataBinder implements DataBinder {

    private List<DataBinder> dataBinders;

    public CompositeDataBinder(final List<DataBinder> dataBinders) {
        this.dataBinders = dataBinders;
    }

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        for (DataBinder binder : this.dataBinders) {
            binder.bind(model, request);
        }
    }
}
