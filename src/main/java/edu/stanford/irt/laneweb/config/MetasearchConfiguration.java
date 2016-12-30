package edu.stanford.irt.laneweb.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.laneweb.metasearch.AlternateContentResultSAXStrategy;
import edu.stanford.irt.laneweb.metasearch.ClinicalSearchResultsFactory;
import edu.stanford.irt.laneweb.metasearch.ClinicalSearchResultsGenerator;
import edu.stanford.irt.laneweb.metasearch.ClinicalSearchResultsSAXStrategy;
import edu.stanford.irt.laneweb.metasearch.ContentResultConversionStrategy;
import edu.stanford.irt.laneweb.metasearch.ContentSearchGenerator;
import edu.stanford.irt.laneweb.metasearch.DescribeGenerator;
import edu.stanford.irt.laneweb.metasearch.EngineResultSAXStrategy;
import edu.stanford.irt.laneweb.metasearch.EngineSearchGenerator;
import edu.stanford.irt.laneweb.metasearch.FilePathTransformer;
import edu.stanford.irt.laneweb.metasearch.MetaSearchManagerFactoryBean;
import edu.stanford.irt.laneweb.metasearch.MetaSearchManagerSource;
import edu.stanford.irt.laneweb.metasearch.MetasearchResultSAXStrategy;
import edu.stanford.irt.laneweb.metasearch.PagingSearchResultListSAXStrategy;
import edu.stanford.irt.laneweb.metasearch.ResourceResultSAXStrategy;
import edu.stanford.irt.laneweb.metasearch.ResourceSearchGenerator;
import edu.stanford.irt.laneweb.metasearch.ScoreStrategy;
import edu.stanford.irt.laneweb.metasearch.SearchDirectoryTransformer;
import edu.stanford.irt.laneweb.metasearch.SearchGenerator;
import edu.stanford.irt.laneweb.metasearch.SearchResultSAXStrategy;

@Configuration
@EnableSolrRepositories(basePackages = {
        "edu.stanford.irt.solr.repository.search" }, multicoreSupport = true, solrClientRef = "solrSearcherServer")
public class MetasearchConfiguration {

    private String imageSearchURL;

    @Autowired
    @Qualifier("edu.stanford.irt.cocoon.xml.SAXParser/html")
    private SAXParser saxParser;

    @Autowired
    private SourceResolver sourceResolver;

    public MetasearchConfiguration(@Value("%{laneweb.solr-url-imageSearch}") final String imageSearchURL) {
        this.imageSearchURL = imageSearchURL;
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/clinical-all")
    @Scope("prototype")
    public ClinicalSearchResultsGenerator allClinicalSearchResultsGenerator() {
        List<String> engines = new ArrayList<>(21);
        engines.add("aafp_patients");
        engines.add("acpjc");
        engines.add("bmj_clinical_evidence");
        engines.add("dynamed");
        engines.add("guideline_clearinghouse");
        engines.add("medlineplus");
        engines.add("pubmed");
        engines.add("pubmed_clinicaltrial");
        engines.add("pubmed_cochrane_reviews");
        engines.add("pubmed_cost");
        engines.add("pubmed_diagnosis_focused");
        engines.add("pubmed_epidemiology_focused");
        engines.add("pubmed_etiology_focused");
        engines.add("pubmed_harm_focused");
        engines.add("pubmed_prevention");
        engines.add("pubmed_prognosis_focused");
        engines.add("pubmed_qualityimprovement");
        engines.add("pubmed_rct");
        engines.add("pubmed_systematicreviews");
        engines.add("pubmed_treatment_focused");
        engines.add("uptodate");
        return new ClinicalSearchResultsGenerator(metaSearchManager().getObject(), clinicalSearchResultsSAXStrategy(),
                engines, clinicalSearchResultsFactory());
    }

    @Bean
    public ClinicalSearchResultsFactory clinicalSearchResultsFactory() {
        return new ClinicalSearchResultsFactory(contentResultConversionStrategy());
    }

    @Bean
    public ClinicalSearchResultsSAXStrategy clinicalSearchResultsSAXStrategy() {
        return new ClinicalSearchResultsSAXStrategy();
    }

    @Bean
    public ContentResultConversionStrategy contentResultConversionStrategy() {
        return new ContentResultConversionStrategy(new ScoreStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/search-content")
    @Scope("prototype")
    public ContentSearchGenerator contentSearchGenerator() {
        return new ContentSearchGenerator(metaSearchManager().getObject(), pagingSearchResultListSAXStrategy(),
                contentResultConversionStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/search-describe")
    @Scope("prototype")
    public DescribeGenerator describeGenerator() {
        return new DescribeGenerator(metaSearchManager().getObject(), metasearchResultSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/search-engine")
    @Scope("prototype")
    public EngineSearchGenerator engineSearchGenerator() {
        return new EngineSearchGenerator(metaSearchManager().getObject(), metasearchResultSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/file-path")
    @Scope("prototype")
    public FilePathTransformer filePathTransformer() {
        return new FilePathTransformer(this.sourceResolver, this.saxParser);
    }

    @Bean
    @Scope("prototype")
    public MetaSearchManagerFactoryBean metaSearchManager() {
        return new MetaSearchManagerFactoryBean(metaSearchManagerSource());
    }

    @Bean(destroyMethod = "dispose")
    public MetaSearchManagerSource metaSearchManagerSource() {
        return new MetaSearchManagerSource("search-lane.xml");
    }

    @Bean
    public MetasearchResultSAXStrategy metasearchResultSAXStrategy() {
        return new MetasearchResultSAXStrategy(
                new EngineResultSAXStrategy(new ResourceResultSAXStrategy(new AlternateContentResultSAXStrategy())));
    }

    @Bean
    public PagingSearchResultListSAXStrategy pagingSearchResultListSAXStrategy() {
        return new PagingSearchResultListSAXStrategy(new SearchResultSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/peds-all")
    @Scope("prototype")
    public ClinicalSearchResultsGenerator pedsClinicalSearchResultsGenerator() {
        List<String> engines = new ArrayList<>(20);
        engines.add("aafp_patients");
        engines.add("aappatient");
        engines.add("aapwebsite");
        engines.add("dynamed_allchild");
        engines.add("guideline_clearinghouse_allchild");
        engines.add("pubmed_allchild");
        engines.add("pubmed_clinicaltrial_allchild");
        engines.add("pubmed_cost_allchild");
        engines.add("pubmed_diagnosis_focused_allchild");
        engines.add("pubmed_epidemiology_focused_allchild");
        engines.add("pubmed_etiology_focused_allchild");
        engines.add("pubmed_harm_focused_allchild");
        engines.add("pubmed_peds_journals");
        engines.add("pubmed_prevention_allchild");
        engines.add("pubmed_prognosis_focused_allchild");
        engines.add("pubmed_qualityimprovement_allchild");
        engines.add("pubmed_rct_allchild");
        engines.add("pubmed_systematicreviews_allchild");
        engines.add("pubmed_treatment_focused_allchild");
        engines.add("uptodate_allchild");
        return new ClinicalSearchResultsGenerator(metaSearchManager().getObject(), clinicalSearchResultsSAXStrategy(),
                engines, clinicalSearchResultsFactory());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/search-resource")
    @Scope("prototype")
    public ResourceSearchGenerator resourceSearchGenerator() {
        return new ResourceSearchGenerator(metaSearchManager().getObject(), metasearchResultSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/search-directory")
    @Scope("prototype")
    public SearchDirectoryTransformer searchDirectoryTransformer() {
        return new SearchDirectoryTransformer();
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/search")
    @Scope("prototype")
    public SearchGenerator searchGenerator() {
        return new SearchGenerator(metaSearchManager().getObject(), metasearchResultSAXStrategy());
    }

    @Bean(name = "solrSearcherServer")
    public HttpSolrClient solrClient() {
        return new HttpSolrClient(this.imageSearchURL);
    }
}
