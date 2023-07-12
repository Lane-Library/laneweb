package edu.stanford.irt.laneweb.eresources;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.eresources.model.Eresource;
import edu.stanford.irt.laneweb.eresources.model.solr.FacetFieldEntry;
import edu.stanford.irt.laneweb.eresources.model.solr.RestPage;
import edu.stanford.irt.laneweb.rest.TypeReference;

public abstract class AbstractRestService {

    private URI searchServiceURI;

    protected static final TypeReference<List<Eresource>> LIST_ERESOURCES_TYPE = new TypeReference<List<Eresource>>() {
    };
    
    protected static final TypeReference<Map<String,List<FacetFieldEntry>>> FACET_PAGE_ERESOURCES_TYPE = new TypeReference<Map<String,List<FacetFieldEntry>>>() {
    };
 
    protected static final TypeReference<RestPage<Eresource>> PAGE_ERESOURCES_TYPE = new TypeReference<RestPage<Eresource>>() {
    };
    
    protected static final TypeReference<LinkedHashMap<String, String>> SUGGESTION_ERESOURCES_TYPE = new TypeReference<LinkedHashMap<String, String>>() {
    };
    
    
    AbstractRestService(final URI uri) {
        this.searchServiceURI = uri;
    }


    protected URI getURIWithParameters(String path, Pageable pageable, List<NameValuePair> parameters) {
        try {
            path = this.searchServiceURI.getPath().concat(path);
            URIBuilder builder = new URIBuilder(this.searchServiceURI).setPath(path);
            if(pageable != null) {
                addPagingToURI(builder, pageable);
            }
            if (parameters != null && !parameters.isEmpty()) {
                builder.addParameters(parameters);
            }
            return builder.build();
        } catch (URISyntaxException e) {
            throw new LanewebException(e);
        }
    }
    
    private void addPagingToURI( URIBuilder builder,  Pageable pageable ) {
        if (pageable.getPageSize() != 0) {
            builder.addParameter("size", String.valueOf(pageable.getPageSize()));
        }
        if (pageable.getPageNumber() != 0) {
            builder.addParameter("page", String.valueOf(pageable.getPageNumber()));
        }
        Sort sort = pageable.getSort();
        if (!sort.isEmpty()) {
            Iterator<Order> orders = sort.iterator();
            while (orders.hasNext()) {
                Order order = orders.next();
                builder.addParameter("sort", order.getProperty() + "," + order.getDirection().name());
            }
        }
    }

    protected URI getURI(String path) {
        try {
            path = this.searchServiceURI.getPath().concat(path);
            return new URIBuilder(this.searchServiceURI).setPath(path).build();
        } catch (URISyntaxException e) {
            throw new LanewebException(e);
        }
    }

    protected String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.displayName());
        } catch (UnsupportedEncodingException e) {
            throw new LanewebException(e);
        }
    }
}
