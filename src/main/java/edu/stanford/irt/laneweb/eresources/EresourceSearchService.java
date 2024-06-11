package edu.stanford.irt.laneweb.eresources;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.stanford.irt.laneweb.eresources.model.Eresource;
import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.laneweb.rest.TypeReference;

public class EresourceSearchService extends AbstractRestService {

    private static final Logger log = LoggerFactory.getLogger(EresourceSearchService.class);

    private RESTService restService;

    public EresourceSearchService(final URI searchServiceURI, final RESTService restService) {
        super(searchServiceURI);
        this.restService = restService;
    }

    public Eresource getByBibID(final String bibID) {
        String path = "search/bib/" + this.urlEncode(bibID);
        log.info("Bib Id resource service {}", path);
        return this.restService.getObject(this.getURI(path), Eresource.class);
    }

    public Map<String, Integer> searchCount(final String searchTerm) {
        URI path = this.getURI("search/count/" + this.urlEncode(searchTerm));
        log.info("count  Eresource service {}", path);
        return this.restService.getObject(path, new TypeReference<Map<String, Integer>>() {
        });
    }

    public Page<Eresource> searchType(final String type, final String searchTerm, final Pageable pageRequest) {
        String path = "search/type/".concat(this.urlEncode(searchTerm));
        List<NameValuePair> parameters = Collections.singletonList(new BasicNameValuePair("type", type));
        URI uri = this.getURIWithParameters(path, pageRequest, parameters);
        log.info("Search by Type eresource service {}", uri);
        return this.restService.getObject(uri, PAGE_ERESOURCES_TYPE);
    }

    public Page<Eresource> searchWithFilters(final String searchTerm, final String facets, final Pageable pageRequest) {
        String path = "search/facets/".concat(this.urlEncode(searchTerm));
        List<NameValuePair> parameters = Collections.singletonList(new BasicNameValuePair("facets", facets));
        URI uri = this.getURIWithParameters(path, pageRequest, parameters);
        log.info("Search eresource service {}", uri);
        return this.restService.getObject(uri, PAGE_ERESOURCES_TYPE);
    }

    public Map<String, String> suggestFindAll(final String searchTerm) {
        String path = "search/suggest/" + this.urlEncode(searchTerm);
        log.info("Suggest Eresource service {}", path);
        return this.restService.getObject(this.getURI(path), SUGGESTION_ERESOURCES_TYPE);
    }

    public Map<String, String> suggestFindByType(final String searchTerm, final String type) {
        String path = "search/suggest/".concat(type).concat("/").concat(this.urlEncode(searchTerm));
        log.info("Suggest by Type eresource service {}", path);
        return this.restService.getObject(this.getURI(path), SUGGESTION_ERESOURCES_TYPE);
    }
}
