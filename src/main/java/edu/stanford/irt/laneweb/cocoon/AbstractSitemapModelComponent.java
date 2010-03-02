package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.objectmodel.helper.ParametersMap;
import org.apache.cocoon.sitemap.SitemapModelComponent;
import org.apache.excalibur.source.Source;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelAware;


public abstract class AbstractSitemapModelComponent implements SitemapModelComponent, ModelAware {
    
    private static final String ALT_SOURCE = "alt-src";
    
    protected Model model;
    
    protected Map<String, String> parameterMap;
    
    protected Source source;

    public final void setModel(Model model) {
        this.model = model;
    }

    @SuppressWarnings("unchecked")
    public final void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) {
        this.parameterMap = new ParametersMap(par);
        if (null != src) {
            try {
                this.source = resolver.resolveURI(src);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
            if (!this.source.exists() && this.parameterMap.containsKey(ALT_SOURCE)) {
                try {
                    this.source = resolver.resolveURI(this.parameterMap.get(ALT_SOURCE));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        initialize();
    }
    
    protected void initialize() {
    }
}
