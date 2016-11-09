package edu.stanford.irt.laneweb.eresources;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.resource.PagingData;
import edu.stanford.irt.laneweb.solr.Eresource;
import edu.stanford.irt.laneweb.solr.SolrService;

public class BrowseAllEresourcesGenerator extends BrowseEresourcesGenerator {

    private String type;

    public BrowseAllEresourcesGenerator(final String type, final SolrService solrService,
            final SAXStrategy<PagingEresourceList> saxStrategy) {
        super(type, solrService, saxStrategy);
    }

    @Override
    public void setParameters(final Map<String, String> parameters) {
        super.setParameters(parameters);
        if (parameters.containsKey(Model.TYPE)) {
            this.type = decode(parameters.get(Model.TYPE));
        }
    }

    @Override
    protected List<Eresource> getEresourceList(final SolrService solrService) {
        List<Eresource> list = null;
        if (this.type == null) {
            list = Collections.emptyList();
        } else {
            list = solrService.getType(this.type);
        }
        return list;
    }

    @Override
    protected String getHeading() {
        return null;
    }

    @Override
    protected PagingData getPagingData(final List<Eresource> eresources, final int page, final String baseQuery) {
        return new EresourceListPagingData(eresources, page, baseQuery, null);
    }
}
