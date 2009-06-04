package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;

import edu.stanford.irt.eresources.Eresource;

public class SearchEresourcesGenerator extends AbstractEresourcesGenerator {

    private static final String QUERY = "query";

    protected String query;

    @SuppressWarnings("unchecked")
    @Override
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par) {
        this.query = par.getParameter(QUERY, null);
        if (null != this.query && this.query.length() == 0) {
            this.query = null;
        }
        super.setup(resolver, objectModel, src, par);
    }

    @Override
    protected Collection<Eresource> getEresourceList() {
        if (null == this.query) {
            throw new IllegalStateException("null query");
        }
        if (null != this.type) {
            return this.collectionManager.searchType(this.type, this.query);
        } else if (null != this.subset) {
            return this.collectionManager.searchSubset(this.subset, this.query);
        }
        return this.collectionManager.search(this.query);
    }
}
