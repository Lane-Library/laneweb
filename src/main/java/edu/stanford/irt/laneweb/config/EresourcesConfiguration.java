package edu.stanford.irt.laneweb.config;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.convert.SolrConverter;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.data.solr.server.support.HttpSolrClientFactory;

import edu.stanford.irt.cocoon.cache.validity.ExpiresValidity;
import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.eresources.AdvancedQueryInspector;
import edu.stanford.irt.laneweb.eresources.DoiQueryInspector;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.EresourceSAXStrategy;
import edu.stanford.irt.laneweb.eresources.EscapingQueryInspector;
import edu.stanford.irt.laneweb.eresources.LcnQueryInspector;
import edu.stanford.irt.laneweb.eresources.NumberQueryInspector;
import edu.stanford.irt.laneweb.eresources.ORQueryInspector;
import edu.stanford.irt.laneweb.eresources.OrcidQueryInspector;
import edu.stanford.irt.laneweb.eresources.ParenthesesQueryInspector;
import edu.stanford.irt.laneweb.eresources.PmcQueryInspector;
import edu.stanford.irt.laneweb.eresources.PmidQueryInspector;
import edu.stanford.irt.laneweb.eresources.QueryInspector;
import edu.stanford.irt.laneweb.eresources.SolrQueryParser;
import edu.stanford.irt.laneweb.eresources.SolrRepository;
import edu.stanford.irt.laneweb.eresources.SolrService;
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
import edu.stanford.irt.laneweb.eresources.search.EresourcesCountGenerator;
import edu.stanford.irt.laneweb.eresources.search.FacetSAXStrategy;
import edu.stanford.irt.laneweb.eresources.search.FacetService;
import edu.stanford.irt.laneweb.eresources.search.FacetsGenerator;
import edu.stanford.irt.laneweb.eresources.search.SolrPagingEresourceSAXStrategy;
import edu.stanford.irt.laneweb.eresources.search.SolrSearchGenerator;
import edu.stanford.irt.laneweb.eresources.search.SolrSearchResult;

@Configuration
@EnableSolrRepositories(basePackages = {
        "edu.stanford.irt.laneweb.eresources" }, solrTemplateRef = "laneSearchSolrTemplate")
public class EresourcesConfiguration {

    private static final int FACETS_TO_SHOW_SEARCH = 4;

    private static final int SOLR_CONNECT_TIMEOUT = 5_000;

    private static final int SOLR_READ_TIMEOUT = 30_000;

    private Collection<String> facetFields;

    @Value("${edu.stanford.irt.laneweb.solr.laneSearch.collectionName}")
    private String laneSearchCollectionName;

    private Collection<String> publicationTypes;

    public EresourcesConfiguration() {
        this.facetFields = Arrays.asList("type", "publicationType", "recordType", "publicationTitle");
        this.facetFields = Collections.unmodifiableCollection(this.facetFields);
        this.publicationTypes = Arrays.asList("Review", "Clinical Trial", "Randomized Controlled Trial",
                "Systematic Review");
        this.publicationTypes = Collections.unmodifiableCollection(this.publicationTypes);
    }

    @Bean(name = "edu.stanford.irt.cocoon.xml.SAXStrategy/er-a2z-browse-xml")
    public SAXStrategy<List<BrowseLetter>> aToZBrowseSAXStrategy() {
        return new AtoZBrowseSAXStrategy();
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/er-a2z-browse-html")
    @Scope("prototype")
    public Generator eresourcesAtoZBrowseGenerator(final SolrService solrService) {
        return new AtoZBrowseGenerator("er-a2z-browse-html", solrService, aToZBrowseSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.xml.SAXStrategy/eresource-xml")
    public SAXStrategy<Eresource> eresourceSAXStrategy() {
        return new EresourceSAXStrategy();
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/er-browse-all-html")
    @Scope("prototype")
    public Generator eresourcesBrowseAllGenerator(final SolrService solrService) {
        return new BrowseAllEresourcesGenerator("er-browse-all-html", solrService,
                pagingEresourceListHTMLSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/er-browse-html")
    @Scope("prototype")
    public Generator eresourcesBrowseGenerator(final SolrService solrService) {
        return new BrowseEresourcesGenerator("er-browse-html", solrService, pagingEresourceListHTMLSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/eresources-count")
    @Scope("prototype")
    public Generator eresourcesCountGenerator(final SolrService solrService) {
        return new EresourcesCountGenerator(solrService);
    }

    @Bean(name = "edu.stanford.irt.cocoon.xml.SAXStrategy/facetSaxStrategy-xml")
    public SAXStrategy<Map<String, Collection<FacetFieldEntry>>> facetSAXStrategy() {
        return new FacetSAXStrategy(this.facetFields);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/solr-facets")
    @Scope("prototype")
    public Generator facetsGenerator(final FacetService service) {
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
    public Transformer linkWithCoverTransformer(final SolrService solrService) {
        return new BibIDToEresourceTransformer(solrService, linkWithCoverSAXStrategy(), "link-with-cover",
                new ExpiresValidity(Duration.ofHours(1).toMillis()));
    }

    @Bean
    public SAXStrategy<PagingEresourceList> pagingEresourceListHTMLSAXStrategy() {
        return new PagingEresourceListXHTMLSAXStrategy(eresourceSAXStrategy(),
                new EresourceListPagingDataSAXStrategy());
    }

    @Bean(name = "solrLaneSearchClient")
    public SolrClient solrClient(@Value("${edu.stanford.irt.laneweb.solr.laneSearch.url}") final String solrServerUrl) {
        return new HttpSolrClient.Builder(solrServerUrl).withConnectionTimeout(SOLR_CONNECT_TIMEOUT)
                .withSocketTimeout(SOLR_READ_TIMEOUT).build();
    }

    @Bean(name = "edu.stanford.irt.laneweb.solr.FacetService")
    public FacetService solrFacetService(final SolrRepository solrRepository,
            @Qualifier("laneSearchSolrTemplate") final SolrTemplate solrTemplate) {
        FacetService service = new FacetService(solrQueryParser(), solrTemplate, laneSearchCollectionName());
        service.setFacets(this.facetFields);
        return service;
    }

    @Bean
    public SAXStrategy<SolrSearchResult> solrPagingEresourceSAXStrategy() {
        return new SolrPagingEresourceSAXStrategy(eresourceSAXStrategy());
    }

    @Bean
    public SolrQueryParser solrQueryParser() {
        List<QueryInspector> queryInspectors = new ArrayList<>(10);
        queryInspectors.add(new AdvancedQueryInspector());
        queryInspectors.add(new DoiQueryInspector());
        queryInspectors.add(new PmidQueryInspector());
        queryInspectors.add(new PmcQueryInspector());
        queryInspectors.add(new LcnQueryInspector());
        queryInspectors.add(new OrcidQueryInspector());
        queryInspectors.add(new NumberQueryInspector());
        queryInspectors.add(new ORQueryInspector());
        queryInspectors.add(new EscapingQueryInspector());
        queryInspectors.add(new ParenthesesQueryInspector());
        return new SolrQueryParser(queryInspectors);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/er-search")
    @Scope("prototype")
    public Generator solrSearchGenerator(final SolrService solrService) {
        return new SolrSearchGenerator(solrService, solrPagingEresourceSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.laneweb.solr.SolrService")
    public SolrService solrService(final SolrRepository solrRepository,
            @Qualifier("laneSearchSolrTemplate") final SolrTemplate solrTemplate) {
        return new SolrService(solrQueryParser(), solrRepository, solrTemplate, laneSearchCollectionName());
    }

    @Bean(name = "laneSearchSolrTemplate")
    public SolrTemplate solrTemplate(@Qualifier("solrLaneSearchClient") final SolrClient solrClient,
            final SolrConverter solrConverter) {
        return new SolrTemplate(new HttpSolrClientFactory(solrClient), solrConverter);
    }
}
