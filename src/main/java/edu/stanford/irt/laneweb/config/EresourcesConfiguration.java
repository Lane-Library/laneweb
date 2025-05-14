package edu.stanford.irt.laneweb.config;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import edu.stanford.irt.cocoon.cache.validity.ExpiresValidity;
import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.eresources.EresourceFacetService;
import edu.stanford.irt.laneweb.eresources.EresourceSAXStrategy;
import edu.stanford.irt.laneweb.eresources.EresourceSearchService;
import edu.stanford.irt.laneweb.eresources.EresourceStatusService;
import edu.stanford.irt.laneweb.eresources.browse.BibIDToEresourceTransformer;
import edu.stanford.irt.laneweb.eresources.browse.LinkWithoutCoverEresourceSAXStrategy;
import edu.stanford.irt.laneweb.eresources.model.Eresource;
import edu.stanford.irt.laneweb.eresources.model.solr.FacetFieldEntry;
import edu.stanford.irt.laneweb.eresources.model.solr.RestResult;
import edu.stanford.irt.laneweb.eresources.search.EresourcesCountGenerator;
import edu.stanford.irt.laneweb.eresources.search.FacetSAXStrategy;
import edu.stanford.irt.laneweb.eresources.search.SolrPagingEresourceSAXStrategy;
import edu.stanford.irt.laneweb.eresources.search.SolrSearchGenerator;
import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.laneweb.eresources.search.FacetsGenerator;

@Configuration
public class EresourcesConfiguration {

    JsonObject facetConfig;

    public EresourcesConfiguration() {

        try (Reader reader = new InputStreamReader(getClass().getResourceAsStream("/config/facetConfiguration.json"),
                "UTF-8")) {
            this.facetConfig = new Gson().fromJson(reader, JsonObject.class);
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to load facetConfiguration.json", e);
        }
    }

    @Bean(name = "edu.stanford.irt.cocoon.xml.SAXStrategy/eresource-xml")
    public SAXStrategy<Eresource> eresourceSAXStrategy() {
        return new EresourceSAXStrategy();
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/eresources-count") @Scope("prototype")
    public Generator eresourcesCountGenerator(final EresourceSearchService searchService) {
        return new EresourcesCountGenerator(searchService);
    }

    @Bean(name = "edu.stanford.irt.cocoon.xml.SAXStrategy/facetSaxStrategy-xml")
    public SAXStrategy<Map<String, Collection<FacetFieldEntry>>> facetSAXStrategy() {
        Collection<String> facetFields = new java.util.ArrayList<>();
        facetConfig.get("facets").getAsJsonArray().forEach(element -> facetFields.add(element.getAsString()));
        return new FacetSAXStrategy(facetFields);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/solr-facets") @Scope("prototype")
    public Generator facetsGenerator(final EresourceFacetService service) {
        return new FacetsGenerator(service, facetSAXStrategy(), facetConfig);

    }

    @Bean
    public SAXStrategy<Eresource> linkWithoutCoverSAXStrategy() {
        return new LinkWithoutCoverEresourceSAXStrategy();
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/link-without-cover") @Scope("prototype")
    public Transformer linkWithoutCoverTransformer(final EresourceSearchService restSearchService) {
        return new BibIDToEresourceTransformer(restSearchService, linkWithoutCoverSAXStrategy(), "link-without-cover",
                new ExpiresValidity(Duration.ofHours(1).toMillis()));
    }

    @Bean
    public SAXStrategy<RestResult<Eresource>> solrPagingEresourceSAXStrategy() {
        return new SolrPagingEresourceSAXStrategy(eresourceSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/er-search") @Scope("prototype")
    public Generator solrSearchGenerator(final EresourceSearchService searchService) {
        return new SolrSearchGenerator(searchService, solrPagingEresourceSAXStrategy());
    }

    /**************************************************************/
    @Bean
    EresourceSearchService getRESTSearchService(@Qualifier("java.net.URI/eresource-service") final URI searchServiceURI,
            final RESTService restService) {
        return new EresourceSearchService(searchServiceURI, restService);
    }

    @Bean
    EresourceFacetService getRESTFacetService(@Qualifier("java.net.URI/eresource-service") final URI searchServiceURI,
            final RESTService restService) {
        return new EresourceFacetService(searchServiceURI, restService);
    }

    @Bean
    EresourceStatusService getRESTStatusService(@Qualifier("java.net.URI/eresource-service") final URI searchServiceURI,
            final RESTService restService) {
        return new EresourceStatusService(searchServiceURI, restService);
    }

    @Bean("java.net.URI/eresource-service")
    URI searchServiceURI(@Value("${edu.stanford.irt.laneweb.eresource-search-service.scheme}") final String scheme,
            @Value("${edu.stanford.irt.laneweb.eresource-search-service.host}") final String host,
            @Value("${edu.stanford.irt.laneweb.eresource-search-service.port}") final int port,
            @Value("${edu.stanford.irt.laneweb.eresource-search-service.path}") final String path)
            throws URISyntaxException {
        return new URI(scheme, null, host, port, path, null, null);
    }
}
