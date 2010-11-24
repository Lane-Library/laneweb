package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.objectmodel.helper.ParametersMap;
import org.apache.cocoon.sitemap.SitemapModelComponent;
import org.apache.excalibur.source.Source;

public abstract class AbstractSitemapModelComponent implements SitemapModelComponent {

    private static final String ALT_SOURCE = "alt-src";

    protected Map<String, String> parameterMap;

    protected Map<String, Object> model;

    protected Source source;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par) {
        this.model = objectModel;
        this.parameterMap = new ParametersMap(par);
        if (null != src) {
            try {
                this.source = resolver.resolveURI(src);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
            if (this.parameterMap.containsKey(ALT_SOURCE) && !this.source.exists()) {
                try {
                    this.source = resolver.resolveURI(this.parameterMap.get(ALT_SOURCE));
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
        initialize();
    }

    protected void initialize() {
    }

}
