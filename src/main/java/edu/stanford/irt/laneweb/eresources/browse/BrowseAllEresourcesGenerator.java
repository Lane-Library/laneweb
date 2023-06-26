package edu.stanford.irt.laneweb.eresources.browse;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.eresources.EresourceBrowseService;
import edu.stanford.irt.laneweb.eresources.model.Eresource;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.resource.PagingData;

public class BrowseAllEresourcesGenerator extends BrowseEresourcesGenerator {

    private String query;

    public BrowseAllEresourcesGenerator(final String type, final EresourceBrowseService restBrowseService,
            final SAXStrategy<PagingEresourceList> saxStrategy) {
        super(type, restBrowseService, saxStrategy);
    }

    @Override
    public void setParameters(final Map<String, String> parameters) {
        super.setParameters(parameters);
        if (parameters.containsKey(Model.QUERY)) {
            this.query = decode(parameters.get(Model.QUERY));
        }
    }

    @Override
    protected StringBuilder createKey() {
        return new StringBuilder("p=;a=;q=").append(this.query);
    }

    @Override
    protected List<Eresource> getEresourceList(final EresourceBrowseService restBrowseService) {
        List<Eresource> list;
        if (this.query == null) {
            list = Collections.emptyList();
        } else {
            list = restBrowseService.browseByQuery(this.query);
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
