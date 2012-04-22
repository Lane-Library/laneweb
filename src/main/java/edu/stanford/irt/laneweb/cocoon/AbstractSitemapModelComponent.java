package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.objectmodel.helper.ParametersMap;
import org.apache.cocoon.sitemap.SitemapModelComponent;

import edu.stanford.irt.laneweb.LanewebException;

public abstract class AbstractSitemapModelComponent implements SitemapModelComponent {
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final void setup(final SourceResolver resolver, final Map model, final String src, final Parameters par) {
        if (this instanceof ModelAware) {
            ((ModelAware)this).setModel(model);
        }
        if (this instanceof ParametersAware) {
            ((ParametersAware)this).setParameters(new ParametersMap(par));
        }
        if (this instanceof SourceAware) {
            try {
                ((SourceAware)this).setSource(resolver.resolveURI(src));
            } catch (IOException e) {
                throw new LanewebException(e);
            }
        }
        initialize();
    }

    protected void initialize() {
    }
    // TODO: implement these more useful protected methods instead of ModelUtil
    // protected String getValue(String name) {
    // return getValue(name, null);
    // }
    //
    // protected String getValue(String name, String defaultValue) {
    // String value = this.parameterMap.get(name);
    // if (value == null) {
    // value = ModelUtil.getString(this.model, name, defaultValue);
    // }
    // return value;
    // }
}
