package edu.stanford.irt.laneweb.eresources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import edu.stanford.irt.laneweb.eresources.model.Eresource;
import edu.stanford.irt.laneweb.eresources.model.solr.FacetFieldEntry;
import edu.stanford.irt.laneweb.eresources.model.solr.FacetSort;
import edu.stanford.irt.laneweb.rest.RESTService;

public class EresourceBrowseService extends AbstractRestService {

    RESTService restService;

    public EresourceBrowseService(final URI uri, final RESTService restService) {
        super(uri);
        this.restService = restService;
    }

    public List<Eresource> browseByQuery(String searchTerm, char charAt) {
        URI path = this.getURI("/browse/".concat(searchTerm).concat("/").concat(String.valueOf(charAt)));
        return this.restService.getObject(path, LIST_ERESOURCES_TYPE);
    }

    public List<Eresource> browseByQuery(String searchTerm) {
        URI path = this.getURI("/browse/" + searchTerm);
        return this.restService.getObject(path, LIST_ERESOURCES_TYPE);
    }

    public Map<String, List<FacetFieldEntry>> facetByField(final String query, final String filters, final String field,
            final int pageNumber, final int facetLimit, final int facetMinCount,
            final FacetSort facetSort) {
        String path = "/facet/field/".concat(this.urlEncode(query));
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("filters", filters));
        parameters.add(new BasicNameValuePair("field", field));
        parameters.add(new BasicNameValuePair("pageNumber", String.valueOf(pageNumber)));
        parameters.add(new BasicNameValuePair("facetLimit", String.valueOf(facetLimit)));
        parameters.add(new BasicNameValuePair("facetMinCount", String.valueOf(facetMinCount)));
        parameters.add(new BasicNameValuePair("facetSort", facetSort.toString()));
        URI uri = super.getURIWithParameters(path, null, parameters);
        return this.restService.getObject(uri, FACET_PAGE_ERESOURCES_TYPE);
    }
}
