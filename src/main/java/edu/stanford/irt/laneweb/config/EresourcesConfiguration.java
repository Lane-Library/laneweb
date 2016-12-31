package edu.stanford.irt.laneweb.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.oxm.Marshaller;

import edu.stanford.irt.laneweb.eresources.AdvancedQueryInspector;
import edu.stanford.irt.laneweb.eresources.DoiQueryInspector;
import edu.stanford.irt.laneweb.eresources.EresourceSAXStrategy;
import edu.stanford.irt.laneweb.eresources.EscapingQueryInspector;
import edu.stanford.irt.laneweb.eresources.NumberQueryInspector;
import edu.stanford.irt.laneweb.eresources.ORQueryInspector;
import edu.stanford.irt.laneweb.eresources.PmidQueryInspector;
import edu.stanford.irt.laneweb.eresources.QueryInspector;
import edu.stanford.irt.laneweb.eresources.SolrQueryParser;
import edu.stanford.irt.laneweb.eresources.SolrRepository;
import edu.stanford.irt.laneweb.eresources.SolrService;
import edu.stanford.irt.laneweb.eresources.browse.BrowseAllEresourcesGenerator;
import edu.stanford.irt.laneweb.eresources.browse.BrowseEresourcesGenerator;
import edu.stanford.irt.laneweb.eresources.browse.CoreEresourcesGenerator;
import edu.stanford.irt.laneweb.eresources.browse.EresourceListPagingDataSAXStrategy;
import edu.stanford.irt.laneweb.eresources.browse.MeSHEresourcesGenerator;
import edu.stanford.irt.laneweb.eresources.browse.PagingEresourceListXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.eresources.search.EresourcesCountGenerator;
import edu.stanford.irt.laneweb.eresources.search.SolrPagingEresourceSAXStrategy;
import edu.stanford.irt.laneweb.eresources.search.SolrSearchFacetsGenerator;
import edu.stanford.irt.laneweb.eresources.search.SolrSearchGenerator;

@Configuration
@EnableSolrRepositories(basePackages = {
        "edu.stanford.irt.laneweb.eresources" }, multicoreSupport = true, solrClientRef = "laneSearchSolrServer")
public class EresourcesConfiguration {

    private Marshaller marshaller;

    @Autowired
    private SolrRepository solrRepository;

    private String solrServerUrl;

    @Autowired
    public EresourcesConfiguration(@Value("%{edu.stanford.irt.laneweb.solr-url-laneSearch}") final String solrServerUrl,
            final Marshaller marshaller) {
        this.solrServerUrl = solrServerUrl;
        this.marshaller = marshaller;
    }

    @Bean(name = "edu.stanford.irt.cocoon.xml.SAXStrategy/eresource-xml")
    public EresourceSAXStrategy eresourceSAXStrategy() {
        return new EresourceSAXStrategy();
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/er-browse-all-html")
    @Scope("prototype")
    public BrowseAllEresourcesGenerator eresourcesBrowseAllGenerator() {
        return new BrowseAllEresourcesGenerator("er-browse-all-html", this.solrService(),
                pagingEresourceListHTMLSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/er-browse-html")
    @Scope("prototype")
    public BrowseEresourcesGenerator eresourcesBrowseGenerator() {
        return new BrowseEresourcesGenerator("er-browse-html", this.solrService(),
                pagingEresourceListHTMLSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/er-core-html")
    @Scope("prototype")
    public CoreEresourcesGenerator eresourcesCoreGenerator() {
        return new CoreEresourcesGenerator("er-core-html", this.solrService(), pagingEresourceListHTMLSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/eresources-count")
    @Scope("prototype")
    public EresourcesCountGenerator eresourcesCountGenerator() {
        return new EresourcesCountGenerator(this.solrService());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/er-mesh-html")
    @Scope("prototype")
    public MeSHEresourcesGenerator eresourcesMeshGenerator() {
        return new MeSHEresourcesGenerator("er-mesh-html", this.solrService(), pagingEresourceListHTMLSAXStrategy());
    }

    @Bean
    public PagingEresourceListXHTMLSAXStrategy pagingEresourceListHTMLSAXStrategy() {
        return new PagingEresourceListXHTMLSAXStrategy(eresourceSAXStrategy(),
                new EresourceListPagingDataSAXStrategy());
    }

    @Bean(name = "laneSearchSolrServer")
    public HttpSolrClient solrClient() {
        return new HttpSolrClient(this.solrServerUrl);
    }

    @Bean
    public SolrPagingEresourceSAXStrategy solrPagingEresourceSAXStrategy() {
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
    public SolrSearchFacetsGenerator solrSearchFacetsGenerator() {
        SolrSearchFacetsGenerator generator = new SolrSearchFacetsGenerator(this.solrService(), this.marshaller);
        generator.setFacetsToShowBrowse(20);
        generator.setFacetsToShowSearch(4);
        List<String> meshToIgnoreInSearch = new ArrayList<>(25);
        meshToIgnoreInSearch.add("nomesh");
        meshToIgnoreInSearch.add("Adolescent");
        meshToIgnoreInSearch.add("Adult");
        meshToIgnoreInSearch.add("Aged");
        meshToIgnoreInSearch.add("Aged, 80 and over");
        meshToIgnoreInSearch.add("Animals");
        meshToIgnoreInSearch.add("Cats");
        meshToIgnoreInSearch.add("Cattle");
        meshToIgnoreInSearch.add("Chick Embryo");
        meshToIgnoreInSearch.add("Child");
        meshToIgnoreInSearch.add("Child, Preschool");
        meshToIgnoreInSearch.add("Cricetinae");
        meshToIgnoreInSearch.add("Dogs");
        meshToIgnoreInSearch.add("Female");
        meshToIgnoreInSearch.add("Guinea Pigs");
        meshToIgnoreInSearch.add("Humans");
        meshToIgnoreInSearch.add("Infant");
        meshToIgnoreInSearch.add("Infant, Newborn");
        meshToIgnoreInSearch.add("Male");
        meshToIgnoreInSearch.add("Mice");
        meshToIgnoreInSearch.add("Middle Aged");
        meshToIgnoreInSearch.add("Pregnancy");
        meshToIgnoreInSearch.add("Rabbits");
        meshToIgnoreInSearch.add("Rats");
        meshToIgnoreInSearch.add("Young Adult");
        generator.setMeshToIgnoreInSearch(meshToIgnoreInSearch);
        List<String> publicationTypes = new ArrayList<>(4);
        publicationTypes.add("Clinical Trial");
        publicationTypes.add("Randomized Controlled Trial");
        publicationTypes.add("Review");
        publicationTypes.add("Systematic Review");
        generator.setPrioritizedPublicationTypes(publicationTypes);
        return generator;
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/er-search")
    @Scope("prototype")
    public SolrSearchGenerator solrSearchGenerator() {
        return new SolrSearchGenerator(this.solrService(), solrPagingEresourceSAXStrategy());
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
