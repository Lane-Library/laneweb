package edu.stanford.irt.cocoon.sitemap.select;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.selection.Selector;

/**
 * A Selector that reports true if a source exists. This selector is thread safe
 * so can exist as a singleton.
 */
public class ResourceExistsSelector implements Selector {

    private SourceResolver sourceResolver;

    /**
     * Create a new ResourceExistsSelector.
     * 
     * @param sourceResolver
     *            the SourceResolver used to resolve the resource
     */
    public ResourceExistsSelector(final SourceResolver sourceResolver) {
        this.sourceResolver = sourceResolver;
    }

    /**
     * Test if the resource represented by expression exists.
     * 
     * @param expression
     *            the resource to test
     * @param model
     *            the model
     * @param parameters
     *            the parameters
     */
    @SuppressWarnings("rawtypes")
    @Override
    public boolean select(final String expression, final Map model, final Parameters parameters) {
        try {
            return this.sourceResolver.resolveURI(expression).exists();
        } catch (IOException e) {
            return false;
        }
    }
}
