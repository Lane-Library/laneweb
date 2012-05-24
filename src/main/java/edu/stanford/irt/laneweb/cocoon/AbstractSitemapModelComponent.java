package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.objectmodel.helper.ParametersMap;
import org.apache.cocoon.sitemap.SitemapModelComponent;

import edu.stanford.irt.cocoon.pipeline.Initializable;
import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.pipeline.SourceAware;
import edu.stanford.irt.laneweb.LanewebException;

public abstract class AbstractSitemapModelComponent implements SitemapModelComponent {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final void setup(final SourceResolver resolver, final Map model, final String src, final Parameters par) {
        if (this instanceof ModelAware) {
            ((ModelAware) this).setModel(model);
        }
        if (this instanceof ParametersAware) {
            ((ParametersAware) this).setParameters(new ParametersMap(par));
        }
        if (this instanceof SourceAware) {
            try {
                ((SourceAware) this).setSource(resolver.resolveURI(src));
            } catch (IOException e) {
                throw new LanewebException(e);
            }
        }
        if (this instanceof Initializable) {
            ((Initializable) this).initialize();
        }
    }
}
