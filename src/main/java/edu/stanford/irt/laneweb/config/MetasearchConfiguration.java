package edu.stanford.irt.laneweb.config;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.metasearch.AlternateContentResultSAXStrategy;
import edu.stanford.irt.laneweb.metasearch.ClinicalSearchResults;
import edu.stanford.irt.laneweb.metasearch.ClinicalSearchResultsFactory;
import edu.stanford.irt.laneweb.metasearch.ClinicalSearchResultsGenerator;
import edu.stanford.irt.laneweb.metasearch.ClinicalSearchResultsSAXStrategy;
import edu.stanford.irt.laneweb.metasearch.ContentResultConversionStrategy;
import edu.stanford.irt.laneweb.metasearch.ContentSearchGenerator;
import edu.stanford.irt.laneweb.metasearch.DescribeGenerator;
import edu.stanford.irt.laneweb.metasearch.EngineResultSAXStrategy;
import edu.stanford.irt.laneweb.metasearch.EngineSearchGenerator;
import edu.stanford.irt.laneweb.metasearch.FilePathTransformer;
import edu.stanford.irt.laneweb.metasearch.MetaSearchService;
import edu.stanford.irt.laneweb.metasearch.MetasearchResultSAXStrategy;
import edu.stanford.irt.laneweb.metasearch.PagingSearchResultList;
import edu.stanford.irt.laneweb.metasearch.PagingSearchResultListSAXStrategy;
import edu.stanford.irt.laneweb.metasearch.RESTMetaSearchService;
import edu.stanford.irt.laneweb.metasearch.ResourceResultSAXStrategy;
import edu.stanford.irt.laneweb.metasearch.ScoreStrategy;
import edu.stanford.irt.laneweb.metasearch.SearchDirectoryTransformer;
import edu.stanford.irt.laneweb.metasearch.SearchGenerator;
import edu.stanford.irt.laneweb.metasearch.SearchResultSAXStrategy;
import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.search.impl.Result;

@Configuration
public class MetasearchConfiguration {

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/clinical-all")
    @Scope("prototype")
    public Generator allClinicalSearchResultsGenerator(
            final MetaSearchService metaSearchService,
            final ClinicalSearchResultsFactory clinicalSearchResultsFactory) {
        List<String> engines = new ArrayList<>(20);
        engines.add("aafp_patients");
        engines.add("acpjc");
        engines.add("bmj_clinical_evidence");
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
        return new ClinicalSearchResultsGenerator(metaSearchService, clinicalSearchResultsSAXStrategy(), engines,
                clinicalSearchResultsFactory);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/chat-ehr")
    @Scope("prototype")
    public Generator chatEhrClinicalSearchResultsGenerator(final MetaSearchService metaSearchService,
            final ClinicalSearchResultsFactory clinicalSearchResultsFactory) {
        List<String> engines = new ArrayList<>(2);
        engines.add("pubmed");
        engines.add("uptodate");
        return new ClinicalSearchResultsGenerator(metaSearchService, clinicalSearchResultsSAXStrategy(), engines,
                clinicalSearchResultsFactory);
    }

    @Bean
    public ClinicalSearchResultsFactory clinicalSearchResultsFactory(
            final ContentResultConversionStrategy contentResultConversionStrategy) {
        return new ClinicalSearchResultsFactory(contentResultConversionStrategy);
    }

    @Bean
    public SAXStrategy<ClinicalSearchResults> clinicalSearchResultsSAXStrategy() {
        return new ClinicalSearchResultsSAXStrategy();
    }

    @Bean
    public ContentResultConversionStrategy contentResultConversionStrategy() {
        return new ContentResultConversionStrategy(new ScoreStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/search-content")
    @Scope("prototype")
    public Generator contentSearchGenerator(
            final MetaSearchService metaSearchService,
            final ContentResultConversionStrategy contentResultConversionStrategy) {
        return new ContentSearchGenerator(metaSearchService, pagingSearchResultListSAXStrategy(),
                contentResultConversionStrategy);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/search-describe")
    @Scope("prototype")
    public Generator describeGenerator(
            final MetaSearchService metaSearchService) {
        return new DescribeGenerator(metaSearchService, metasearchResultSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/search-engine")
    @Scope("prototype")
    public Generator engineSearchGenerator(
            final MetaSearchService metaSearchService) {
        return new EngineSearchGenerator(metaSearchService, metasearchResultSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/file-path")
    @Scope("prototype")
    public Transformer filePathTransformer(final SourceResolver sourceResolver,
            @Qualifier("edu.stanford.irt.cocoon.xml.SAXParser/html") final SAXParser saxParser) {
        return new FilePathTransformer(sourceResolver, saxParser);
    }

    @Bean
    public SAXStrategy<Result> metasearchResultSAXStrategy() {
        return new MetasearchResultSAXStrategy(
                new EngineResultSAXStrategy(new ResourceResultSAXStrategy(new AlternateContentResultSAXStrategy())));
    }

    @Bean
    public SAXStrategy<PagingSearchResultList> pagingSearchResultListSAXStrategy() {
        return new PagingSearchResultListSAXStrategy(new SearchResultSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/peds-all")
    @Scope("prototype")
    public Generator pedsClinicalSearchResultsGenerator(
            final MetaSearchService metaSearchService,
            final ClinicalSearchResultsFactory clinicalSearchResultsFactory) {
        List<String> engines = new ArrayList<>(19);
        engines.add("aafp_patients");
        engines.add("aappatient");
        engines.add("aapwebsite");
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
        return new ClinicalSearchResultsGenerator(metaSearchService, clinicalSearchResultsSAXStrategy(), engines,
                clinicalSearchResultsFactory);
    }

    @Bean
    public MetaSearchService restMetaSearchService(final RESTService restService,
            @Value("${edu.stanford.irt.laneweb.metasearch.scheme}") final String scheme,
            @Value("${edu.stanford.irt.laneweb.metasearch.host}") final String host,
            @Value("${edu.stanford.irt.laneweb.metasearch.port}") final int port,
            @Value("${edu.stanford.irt.laneweb.metasearch.path}") final String path) throws URISyntaxException {
        URI metaSearchURI = new URI(scheme, null, host, port, path, null, null);
        return new RESTMetaSearchService(metaSearchURI, restService);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/search-directory")
    @Scope("prototype")
    public Transformer searchDirectoryTransformer() {
        return new SearchDirectoryTransformer();
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/search")
    @Scope("prototype")
    public Generator searchGenerator(
            final MetaSearchService metaSearchService) {
        return new SearchGenerator(metaSearchService, metasearchResultSAXStrategy());
    }
}
