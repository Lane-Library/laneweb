package edu.stanford.irt.laneweb.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.stanford.irt.cocoon.cache.validity.ExpiresValidity;
import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.eresources.EresourceBrowseService;
import edu.stanford.irt.laneweb.eresources.EresourceFacetService;
import edu.stanford.irt.laneweb.eresources.EresourceSAXStrategy;
import edu.stanford.irt.laneweb.eresources.EresourceSearchService;
import edu.stanford.irt.laneweb.eresources.EresourceStatusService;
import edu.stanford.irt.laneweb.eresources.browse.AtoZBrowseGenerator;
import edu.stanford.irt.laneweb.eresources.browse.AtoZBrowseSAXStrategy;
import edu.stanford.irt.laneweb.eresources.browse.BibIDToEresourceTransformer;
import edu.stanford.irt.laneweb.eresources.browse.BrowseAllEresourcesGenerator;
import edu.stanford.irt.laneweb.eresources.browse.BrowseEresourcesGenerator;
import edu.stanford.irt.laneweb.eresources.browse.BrowseLetter;
import edu.stanford.irt.laneweb.eresources.browse.EresourceListPagingDataSAXStrategy;
import edu.stanford.irt.laneweb.eresources.browse.LinkWithCoverEresourceSAXStrategy;
import edu.stanford.irt.laneweb.eresources.browse.PagingEresourceList;
import edu.stanford.irt.laneweb.eresources.browse.PagingEresourceListXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.eresources.model.Eresource;
import edu.stanford.irt.laneweb.eresources.model.solr.FacetFieldEntry;
import edu.stanford.irt.laneweb.eresources.model.solr.RestResult;
import edu.stanford.irt.laneweb.eresources.search.EresourcesCountGenerator;
import edu.stanford.irt.laneweb.eresources.search.FacetSAXStrategy;
import edu.stanford.irt.laneweb.eresources.search.FacetsGenerator;
import edu.stanford.irt.laneweb.eresources.search.SolrPagingEresourceSAXStrategy;
import edu.stanford.irt.laneweb.eresources.search.SolrSearchGenerator;
import edu.stanford.irt.laneweb.rest.RESTService;

@Configuration
public class EresourcesConfiguration {

  private static final int FACETS_TO_SHOW_SEARCH = 4;

    private Collection<String> facetFields;

    @Value("${edu.stanford.irt.laneweb.solr.laneSearch.collectionName}")
    private String laneSearchCollectionName;

    private Collection<String> publicationTypes;

  public EresourcesConfiguration() {
    this.facetFields = Arrays.asList("type", "publicationType", "recordType", "publicationTitle");
    this.facetFields = Collections.unmodifiableCollection(this.facetFields);
    this.publicationTypes = Arrays.asList("Review", "Clinical Trial", "Randomized Controlled Trial", "Systematic Review");
    this.publicationTypes = Collections.unmodifiableCollection(this.publicationTypes);
  }

  @Bean(name = "edu.stanford.irt.cocoon.xml.SAXStrategy/er-a2z-browse-xml")
  public SAXStrategy<List<BrowseLetter>> aToZBrowseSAXStrategy() {
    return new AtoZBrowseSAXStrategy();
  }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/er-a2z-browse-html")
    @Scope("prototype")
    public Generator eresourcesAtoZBrowseGenerator(final EresourceBrowseService restBrowseService) {
        return new AtoZBrowseGenerator("er-a2z-browse-html", restBrowseService, aToZBrowseSAXStrategy());
    }

  @Bean(name = "edu.stanford.irt.cocoon.xml.SAXStrategy/eresource-xml")
  public SAXStrategy<Eresource> eresourceSAXStrategy() {
    return new EresourceSAXStrategy();
  }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/er-browse-all-html")
    @Scope("prototype")
    public Generator eresourcesBrowseAllGenerator(final EresourceBrowseService restBrowseService) {
        return new BrowseAllEresourcesGenerator("er-browse-all-html", restBrowseService,
                pagingEresourceListHTMLSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/er-browse-html")
    @Scope("prototype")
    public Generator eresourcesBrowseGenerator(final EresourceBrowseService solrService) {
        return new BrowseEresourcesGenerator("er-browse-html", solrService, pagingEresourceListHTMLSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/eresources-count")
    @Scope("prototype")
    public Generator eresourcesCountGenerator( final EresourceSearchService searchService) {
        return new EresourcesCountGenerator(searchService);
    }

  @Bean(name = "edu.stanford.irt.cocoon.xml.SAXStrategy/facetSaxStrategy-xml")
  public SAXStrategy<Map<String, Collection<FacetFieldEntry>>> facetSAXStrategy() {
    return new FacetSAXStrategy(this.facetFields);
  }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/solr-facets")
    @Scope("prototype")
    public Generator facetsGenerator(final EresourceFacetService service) {
        FacetsGenerator generator = new FacetsGenerator(service, facetSAXStrategy(), FACETS_TO_SHOW_SEARCH,
                this.publicationTypes);
        generator.setFacet(this.facetFields);
        return generator;
    }

  
  @Bean
  public String laneSearchCollectionName() {
    return this.laneSearchCollectionName;
  }

  @Bean
  public SAXStrategy<Eresource> linkWithCoverSAXStrategy() {
    return new LinkWithCoverEresourceSAXStrategy();
  }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/link-with-cover")
    @Scope("prototype")
    public Transformer linkWithCoverTransformer(final EresourceSearchService restSearchService) {
        return new BibIDToEresourceTransformer(restSearchService, linkWithCoverSAXStrategy(), "link-with-cover",
                new ExpiresValidity(Duration.ofHours(1).toMillis()));
    }

  @Bean
  public SAXStrategy<PagingEresourceList> pagingEresourceListHTMLSAXStrategy() {
    return new PagingEresourceListXHTMLSAXStrategy(eresourceSAXStrategy(), new EresourceListPagingDataSAXStrategy());
  }

    @Bean
    public SAXStrategy<RestResult<Eresource>> solrPagingEresourceSAXStrategy() {
        return new SolrPagingEresourceSAXStrategy(eresourceSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/er-search")
    @Scope("prototype")
    public Generator solrSearchGenerator(final EresourceSearchService searchService) {
        return new SolrSearchGenerator(searchService, solrPagingEresourceSAXStrategy());
    }



    /**************************************************************/
    
    @Bean
    EresourceSearchService getRESTSearchService(
            @Qualifier("java.net.URI/eresource-service") final URI searchServiceURI,
            final RESTService restService) {
        return new EresourceSearchService(searchServiceURI, restService);
    }

    @Bean
    EresourceFacetService getRESTFacetService(
            @Qualifier("java.net.URI/eresource-service") final URI searchServiceURI,
            final RESTService restService) {
        return new EresourceFacetService(searchServiceURI, restService);
    }
    
    @Bean
    EresourceBrowseService getRESTBrowseService(
            @Qualifier("java.net.URI/eresource-service") final URI searchServiceURI,
            final RESTService restService) {
        return new EresourceBrowseService(searchServiceURI, restService);
    }
    
    @Bean
    EresourceStatusService getRESTStatusService(
            @Qualifier("java.net.URI/eresource-service") final URI searchServiceURI,
            final RESTService restService) {
        return new EresourceStatusService(searchServiceURI, restService);
    }
    
    @Bean("java.net.URI/eresource-service")
    URI searchServiceURI(
            @Value("${edu.stanford.irt.laneweb.eresource-search-service.scheme}") final String scheme,
            @Value("${edu.stanford.irt.laneweb.eresource-search-service.host}") final String host,
            @Value("${edu.stanford.irt.laneweb.eresource-search-service.port}") final int port,
            @Value("${edu.stanford.irt.laneweb.eresource-search-service.path}") final String path)
            throws URISyntaxException {
        return new URI(scheme, null, host, port, path, null, null);
    }
    
    
    
}
