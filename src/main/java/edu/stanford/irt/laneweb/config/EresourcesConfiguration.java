package edu.stanford.irt.laneweb.config;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
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
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.oxm.Marshaller;

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
import edu.stanford.irt.laneweb.eresources.SolrFacetService;
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
import edu.stanford.irt.laneweb.eresources.search.FacetComparator;
import edu.stanford.irt.laneweb.eresources.search.SolrPagingEresourceSAXStrategy;
import edu.stanford.irt.laneweb.eresources.search.SolrSearchFacetsGenerator;
import edu.stanford.irt.laneweb.eresources.search.SolrSearchGenerator;
import edu.stanford.irt.laneweb.eresources.search.SolrSearchResult;
import edu.stanford.irt.laneweb.eresources.search.redesign.SearchFacetSaxStrategy;
import edu.stanford.irt.laneweb.eresources.search.redesign.SolrFacetsGenerator;

@Configuration
@EnableSolrRepositories(basePackages = {
        "edu.stanford.irt.laneweb.eresources" }, solrTemplateRef = "laneSearchSolrTemplate")
public class EresourcesConfiguration {

    private static final int FACETS_TO_SHOW_BROWSE = 20;

    private static final int FACETS_TO_SHOW_SEARCH = 4;

    private static final int SOLR_CONNECT_TIMEOUT = 5_000;

    private static final int SOLR_READ_TIMEOUT = 30_000;

    private FacetComparator facetComparator;
    
    private edu.stanford.irt.laneweb.eresources.search.redesign.FacetComparator redesignFacetComparator;
    
    

    private Collection<String> meshToIgnoreInSearch;

    private Collection<String> publicationTypes;

    public EresourcesConfiguration() {
        this.meshToIgnoreInSearch = new HashSet<>(25);
        this.meshToIgnoreInSearch.add("nomesh");
        this.meshToIgnoreInSearch.add("Adolescent");
        this.meshToIgnoreInSearch.add("Adult");
        this.meshToIgnoreInSearch.add("Aged");
        this.meshToIgnoreInSearch.add("Aged, 80 and over");
        this.meshToIgnoreInSearch.add("Animals");
        this.meshToIgnoreInSearch.add("Cats");
        this.meshToIgnoreInSearch.add("Cattle");
        this.meshToIgnoreInSearch.add("Chick Embryo");
        this.meshToIgnoreInSearch.add("Child");
        this.meshToIgnoreInSearch.add("Child, Preschool");
        this.meshToIgnoreInSearch.add("Cricetinae");
        this.meshToIgnoreInSearch.add("Dogs");
        this.meshToIgnoreInSearch.add("Female");
        this.meshToIgnoreInSearch.add("Guinea Pigs");
        this.meshToIgnoreInSearch.add("Humans");
        this.meshToIgnoreInSearch.add("Infant");
        this.meshToIgnoreInSearch.add("Infant, Newborn");
        this.meshToIgnoreInSearch.add("Male");
        this.meshToIgnoreInSearch.add("Mice");
        this.meshToIgnoreInSearch.add("Middle Aged");
        this.meshToIgnoreInSearch.add("Pregnancy");
        this.meshToIgnoreInSearch.add("Rabbits");
        this.meshToIgnoreInSearch.add("Rats");
        this.meshToIgnoreInSearch.add("Young Adult");
        this.meshToIgnoreInSearch = Collections.unmodifiableCollection(this.meshToIgnoreInSearch);
        this.publicationTypes = new HashSet<>(4);
        this.publicationTypes.add("Review");
        this.publicationTypes.add("Clinical Trial");
        this.publicationTypes.add("Randomized Controlled Trial");
        this.publicationTypes.add("Systematic Review");
        this.publicationTypes = Collections.unmodifiableCollection(this.publicationTypes);
        this.facetComparator = new FacetComparator(this.publicationTypes);
        this.redesignFacetComparator = new edu.stanford.irt.laneweb.eresources.search.redesign.FacetComparator(publicationTypes);
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
    public SolrClient solrClient(@Value("${edu.stanford.irt.laneweb.solr-url-laneSearch}") final String solrServerUrl) {
        return new HttpSolrClient.Builder(solrServerUrl).withConnectionTimeout(SOLR_CONNECT_TIMEOUT)
                .withSocketTimeout(SOLR_READ_TIMEOUT).build();
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

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/solr-search-facets")
    @Scope("prototype")
    public Generator solrSearchFacetsGenerator(final SolrService solrService, final Marshaller marshaller) {
        return new SolrSearchFacetsGenerator(solrService, marshaller, FACETS_TO_SHOW_BROWSE, FACETS_TO_SHOW_SEARCH,
                this.meshToIgnoreInSearch, this.publicationTypes, this.facetComparator);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/solr-facets")
    @Scope("prototype")
    public Generator solrFacetsGenerator(final SolrFacetService service, final Marshaller marshaller) {
        return new SolrFacetsGenerator(service , facetSolrSAXStrategy(), FACETS_TO_SHOW_SEARCH, this.publicationTypes);
    }

    @Bean(name = "edu.stanford.irt.cocoon.xml.SAXStrategy/searchFacetSaxStrategy-xml")
    public SAXStrategy<Map<String, Collection<FacetFieldEntry>>>  facetSolrSAXStrategy() {
        return new SearchFacetSaxStrategy();
    }
    
    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/er-search")
    @Scope("prototype")
    public Generator solrSearchGenerator(final SolrService solrService) {
        return new SolrSearchGenerator(solrService, solrPagingEresourceSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.laneweb.solr.SolrService")
    public SolrService solrService(final SolrRepository solrRepository,
            @Qualifier("laneSearchSolrTemplate") final SolrTemplate solrTemplate) {
        return new SolrService(solrQueryParser(), solrRepository, solrTemplate);
    }

    @Bean(name = "edu.stanford.irt.laneweb.solr.SolrFacetService")
    public SolrFacetService solrFacetService(final SolrRepository solrRepository,
            @Qualifier("laneSearchSolrTemplate") final SolrTemplate solrTemplate) {
        return new SolrFacetService(solrQueryParser(), solrRepository, solrTemplate);
    }

    
    
    @Bean(name = "laneSearchSolrTemplate")
    public SolrTemplate solrTemplate(@Qualifier("solrLaneSearchClient") final SolrClient solrClient) {
        return new SolrTemplate(solrClient);
    }
}
