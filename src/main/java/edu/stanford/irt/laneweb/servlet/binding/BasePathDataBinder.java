package edu.stanford.irt.laneweb.servlet.binding;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;


public class BasePathDataBinder implements DataBinder {

    private static final String[][] BASE_MAPPINGS = new String[][] {
            { "/alainb", "file:/afs/ir.stanford.edu/users/a/l/alainb/laneweb" },
            { "/ceyates", "file:/afs/ir.stanford.edu/users/c/e/ceyates/laneweb" },
            { "/olgary", "file:/afs/ir.stanford.edu/users/o/l/olgary/laneweb" },
            { "/ryanmax", "file:/afs/ir.stanford.edu/users/r/y/ryanmax/laneweb" },
            { "/ajchrist", "file:/afs/ir.stanford.edu/users/a/j/ajchrist/laneweb" },
            { "/rzwies", "file:/afs/ir.stanford.edu/users/r/z/rzwies/laneweb" } };

    protected Map<String, String> baseMappings;
    
    public BasePathDataBinder() {
        this.baseMappings = new HashMap<String, String>(BASE_MAPPINGS.length);
        for (String[] element : BASE_MAPPINGS) {
            this.baseMappings.put(element[0], element[1]);
        }
    }

    public void bind(Map<String, Object> model, HttpServletRequest request) {
        StringBuilder builder = new StringBuilder(request.getContextPath());
        String uri = request.getRequestURI().substring(request.getContextPath().length());
        if (uri.indexOf("/stage") == 0) {
            builder.append("/stage");
        } else {
            for (String key : this.baseMappings.keySet()) {
                if (uri.indexOf(key) == 0) {
                    builder.append(key);
                    break;
                }
            }
        }
        model.put(Model.BASE_PATH, builder.toString());
    }
}
