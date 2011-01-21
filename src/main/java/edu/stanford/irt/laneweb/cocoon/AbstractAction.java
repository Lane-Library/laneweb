package edu.stanford.irt.laneweb.cocoon;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.objectmodel.helper.ParametersMap;

public abstract class AbstractAction implements Action {

    protected Map<String, Object> model;

    protected Map<String, String> parametersMap;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Map act(final Redirector redirector, final SourceResolver resolver, final Map objectModel, final String source,
            final Parameters parameters) throws Exception {
        this.parametersMap = new ParametersMap(parameters);
        this.model = objectModel;
        return doAct();
    }

    protected abstract Map<String, String> doAct();
}
