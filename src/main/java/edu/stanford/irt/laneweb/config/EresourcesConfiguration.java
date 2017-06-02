package edu.stanford.irt.laneweb.config;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.solr.core.SolrTemplate;
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
import edu.stanford.irt.laneweb.eresources.NumberQueryInspector;
import edu.stanford.irt.laneweb.eresources.ORQueryInspector;
import edu.stanford.irt.laneweb.eresources.PmidQueryInspector;
import edu.stanford.irt.laneweb.eresources.QueryInspector;
import edu.stanford.irt.laneweb.eresources.SolrQueryParser;
import edu.stanford.irt.laneweb.eresources.SolrRepository;
import edu.stanford.irt.laneweb.eresources.SolrService;
import edu.stanford.irt.laneweb.eresources.browse.BibIDToEresourceTransformer;
import edu.stanford.irt.laneweb.eresources.browse.BrowseAllEresourcesGenerator;
import edu.stanford.irt.laneweb.eresources.browse.BrowseEresourcesGenerator;
import edu.stanford.irt.laneweb.eresources.browse.CoreEresourcesGenerator;
import edu.stanford.irt.laneweb.eresources.browse.EresourceListPagingDataSAXStrategy;
import edu.stanford.irt.laneweb.eresources.browse.LinkWithCoverEresourceSAXStrategy;
import edu.stanford.irt.laneweb.eresources.browse.MeSHEresourcesGenerator;
import edu.stanford.irt.laneweb.eresources.browse.PagingEresourceList;
import edu.stanford.irt.laneweb.eresources.browse.PagingEresourceListXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.eresources.search.EresourcesCountGenerator;
import edu.stanford.irt.laneweb.eresources.search.FacetComparator;
import edu.stanford.irt.laneweb.eresources.search.SolrPagingEresourceSAXStrategy;
import edu.stanford.irt.laneweb.eresources.search.SolrSearchFacetsGenerator;
import edu.stanford.irt.laneweb.eresources.search.SolrSearchGenerator;
import edu.stanford.irt.laneweb.eresources.search.SolrSearchResult;

@Configuration
@EnableSolrRepositories(basePackages = {
        "edu.stanford.irt.laneweb.eresources" }, multicoreSupport = true, solrClientRef = "laneSearchSolrServer")
public class EresourcesConfiguration {

    private static final int FACETS_TO_SHOW_BROWSE = 20;

    private static final int FACETS_TO_SHOW_SEARCH = 4;

    private FacetComparator facetComparator;

    private Marshaller marshaller;

    private Collection<String> meshToIgnoreInSearch;

    private Collection<String> publicationTypes;

    @Autowired
    private SolrRepository solrRepository;

    private String solrServerUrl;

    @Autowired
    public EresourcesConfiguration(@Value("${edu.stanford.irt.laneweb.solr-url-laneSearch}") final String solrServerUrl,
            final Marshaller marshaller) {
        this.solrServerUrl = solrServerUrl;
        this.marshaller = marshaller;
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
        this.publicationTypes.add("Clinical Trial");
        this.publicationTypes.add("Randomized Controlled Trial");
        this.publicationTypes.add("Review");
        this.publicationTypes.add("Systematic Review");
        this.publicationTypes = Collections.unmodifiableCollection(this.publicationTypes);
        this.facetComparator = new FacetComparator(this.publicationTypes);
    }

    @Bean(name = "edu.stanford.irt.cocoon.xml.SAXStrategy/eresource-xml")
    public SAXStrategy<Eresource> eresourceSAXStrategy() {
        return new EresourceSAXStrategy();
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/er-browse-all-html")
    @Scope("prototype")
    public Generator eresourcesBrowseAllGenerator() {
        return new BrowseAllEresourcesGenerator("er-browse-all-html", solrService(),
                pagingEresourceListHTMLSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/er-browse-html")
    @Scope("prototype")
    public Generator eresourcesBrowseGenerator() {
        return new BrowseEresourcesGenerator("er-browse-html", solrService(), pagingEresourceListHTMLSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/er-core-html")
    @Scope("prototype")
    public Generator eresourcesCoreGenerator() {
        return new CoreEresourcesGenerator("er-core-html", solrService(), pagingEresourceListHTMLSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/eresources-count")
    @Scope("prototype")
    public Generator eresourcesCountGenerator() {
        return new EresourcesCountGenerator(solrService());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/er-mesh-html")
    @Scope("prototype")
    public Generator eresourcesMeshGenerator() {
        return new MeSHEresourcesGenerator("er-mesh-html", solrService(), pagingEresourceListHTMLSAXStrategy());
    }

    @Bean
    public SAXStrategy<Eresource> linkWithCoverSAXStrategy() {
        return new LinkWithCoverEresourceSAXStrategy();
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/link-with-cover")
    @Scope("prototype")
    public Transformer linkWithCoverTransformer() {
        return new BibIDToEresourceTransformer(solrService(), linkWithCoverSAXStrategy(), "link-with-cover",
                new ExpiresValidity(Duration.ofHours(1).toMillis()));
    }

    @Bean
    public SAXStrategy<PagingEresourceList> pagingEresourceListHTMLSAXStrategy() {
        return new PagingEresourceListXHTMLSAXStrategy(eresourceSAXStrategy(),
                new EresourceListPagingDataSAXStrategy());
    }

    @Bean(name = "laneSearchSolrServer")
    public SolrClient solrClient() {
        return new HttpSolrClient(this.solrServerUrl);
    }

    @Bean
    public SAXStrategy<SolrSearchResult> solrPagingEresourceSAXStrategy() {
        return new SolrPagingEresourceSAXStrategy(eresourceSAXStrategy());
    }

    @Bean
    public SolrQueryParser solrQueryParser() {
        List<QueryInspector> queryInspectors = new ArrayList<>(6);
        queryInspectors.add(new AdvancedQueryInspector());
        queryInspectors.add(new DoiQueryInspector());
        queryInspectors.add(new PmidQueryInspector());
        queryInspectors.add(new NumberQueryInspector());
        queryInspectors.add(new ORQueryInspector());
        queryInspectors.add(new EscapingQueryInspector());
        return new SolrQueryParser(queryInspectors);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/solr-search-facets")
    @Scope("prototype")
    public Generator solrSearchFacetsGenerator() {
        return new SolrSearchFacetsGenerator(solrService(), this.marshaller, FACETS_TO_SHOW_BROWSE,
                FACETS_TO_SHOW_SEARCH, this.meshToIgnoreInSearch, this.publicationTypes, this.facetComparator);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/er-search")
    @Scope("prototype")
    public Generator solrSearchGenerator() {
        return new SolrSearchGenerator(solrService(), solrPagingEresourceSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.laneweb.solr.SolrService")
    public SolrService solrService() {
        return new SolrService(solrQueryParser(), this.solrRepository, solrTemplate());
    }

    @Bean(name = "laneSearchSolrTemplate")
    public SolrTemplate solrTemplate() {
        return new SolrTemplate(solrClient());
    }
}
