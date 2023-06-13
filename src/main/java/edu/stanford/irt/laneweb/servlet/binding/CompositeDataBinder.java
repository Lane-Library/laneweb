package edu.stanford.irt.laneweb.servlet.binding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

public class CompositeDataBinder implements DataBinder {

    private List<DataBinder> dataBinders;

    public CompositeDataBinder(final List<DataBinder> dataBinders) {
        this.dataBinders = new ArrayList<>(dataBinders);
    }

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        for (DataBinder binder : this.dataBinders) {
            binder.bind(model, request);
        }
    }
}
