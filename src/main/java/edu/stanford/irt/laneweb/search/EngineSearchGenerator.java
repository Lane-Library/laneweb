package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.SourceResolver;

import edu.stanford.irt.search.Result;

public class EngineSearchGenerator extends SearchGenerator {

    private String[] engines;

    @Override
    public Result doSearch() {
        Collection<String> engines = new HashSet<String>();
        if ((this.engines != null) && (this.engines.length > 0)) {
            for (String element : this.engines) {
                engines.add(element);
            }
        }
        return super.doSearch(engines);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par) {
        super.setup(resolver, objectModel, src, par);
        HttpServletRequest request = ObjectModelHelper.getRequest(objectModel);
        this.engines = request.getParameterValues("e");
        if (null == this.engines) {
            throw new IllegalArgumentException("null engines");
        }
    }
}
