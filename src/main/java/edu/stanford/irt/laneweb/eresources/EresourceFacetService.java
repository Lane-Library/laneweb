package edu.stanford.irt.laneweb.eresources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import edu.stanford.irt.laneweb.eresources.model.solr.FacetFieldEntry;
import edu.stanford.irt.laneweb.eresources.model.solr.FacetSort;
import edu.stanford.irt.laneweb.rest.RESTService;

public class EresourceFacetService extends AbstractRestService {

    private static final Logger log = LoggerFactory.getLogger(EresourceFacetService.class);

    RESTService restService;

    public EresourceFacetService(final URI uri, final RESTService restService) {
        super(uri);
        this.restService = restService;
    }

    public Map<String, List<FacetFieldEntry>> facetByField(final String searchTerm, final String filters,
            final String field, final int facetLimit, final int facetMinCount, final FacetSort facetSort) {
        String path = "facet/field/sort/".concat(this.urlEncode(searchTerm));
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("filters", filters));
        parameters.add(new BasicNameValuePair("field", field));
        parameters.add(new BasicNameValuePair("facetLimit", String.valueOf(facetLimit)));
        parameters.add(new BasicNameValuePair("facetMinCount", String.valueOf(facetMinCount)));
        parameters.add(new BasicNameValuePair("facetSort", facetSort.name()));
        URI uri = this.getURIWithParameters(path, null, parameters);
        log.info("EresourceFacetService facetByField {}", uri);
        return this.restService.getObject(uri, FACET_PAGE_ERESOURCES_TYPE);
    }

    public Map<String, List<FacetFieldEntry>> facetByFieldContains(final String query, final String searchTerm,
            final String field, final String filters, final int facetMinCount) {
        String path = "facet/many/field/contains/".concat(this.urlEncode(query));
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("searchTerm", searchTerm));
        parameters.add(new BasicNameValuePair("field", field));
        parameters.add(new BasicNameValuePair("filters", filters));
        parameters.add(new BasicNameValuePair("facetMinCount", String.valueOf(facetMinCount)));
        URI uri = this.getURIWithParameters(path, null, parameters);
        log.info("EresourceFacetService facetByFieldContains {}", uri);
        return this.restService.getObject(uri, FACET_PAGE_ERESOURCES_TYPE);
    }

    public Map<String, List<FacetFieldEntry>> facetByManyFields(final String searchTerm, final String filters,
            final JsonObject facetConfig) {
        String path = "facet/many/field/".concat(this.urlEncode(searchTerm));
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("filters", filters));
        parameters.add(new BasicNameValuePair("limit", facetConfig.get("limit").getAsString()));
        parameters.add(new BasicNameValuePair("facets", facetConfig.get("facets").toString()));
        parameters.add(new BasicNameValuePair("facetMatch", facetConfig.get("facet-result-match").toString()));
        parameters.add(new BasicNameValuePair("limitByFacet", facetConfig.get("limit-by-facet").toString()));
        URI uri = this.getURIWithParameters(path, null, parameters);
        log.info("EresourceFacetService facetByManyFields {}", uri);
        return this.restService.getObject(uri, FACET_PAGE_ERESOURCES_TYPE);
    }
}
