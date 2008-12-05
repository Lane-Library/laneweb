package edu.stanford.irt.laneweb.eresources;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.Eresource;

public class SearchEresourcesGenerator extends AbstractEresourcesGenerator {

    private static final String QUERY = "q";

    protected String query;

    @Override
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par)
            throws ProcessingException, SAXException, IOException {
        Request request = ObjectModelHelper.getRequest(objectModel);
        this.query = request.getParameter(QUERY);
        if (null != this.query) {
            if (this.query.length() == 0) {
                this.query = null;
            }
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
