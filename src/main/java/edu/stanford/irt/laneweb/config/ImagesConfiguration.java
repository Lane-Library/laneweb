package edu.stanford.irt.laneweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetPage;

import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.images.BassettAccordionGenerator;
import edu.stanford.irt.laneweb.images.BassettCountSAXStrategy;
import edu.stanford.irt.laneweb.images.BassettImageGenerator;
import edu.stanford.irt.laneweb.images.BassettImageListSAXStrategy;
import edu.stanford.irt.solr.BassettImage;
import edu.stanford.irt.solr.configuration.SolrLaneImageConfiguration;
import edu.stanford.irt.solr.service.SolrImageService;

@Configuration
@Import({ SolrLaneImageConfiguration.class })
public class ImagesConfiguration {

    private SolrImageService solrImageService;

    public ImagesConfiguration(final SolrImageService solrImageService) {
        this.solrImageService = solrImageService;
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/bassett-accordion")
    @Scope("prototype")
    Generator bassettAccordionGenerator() {
        return new BassettAccordionGenerator(this.solrImageService, countSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/bassett")
    @Scope("prototype")
    Generator bassettGenerator() {
        return new BassettImageGenerator(this.solrImageService, pageSAXStrategy());
    }

    @Bean
    SAXStrategy<FacetPage<BassettImage>> countSAXStrategy() {
        return new BassettCountSAXStrategy();
    }

    @Bean
    SAXStrategy<Page<BassettImage>> pageSAXStrategy() {
        return new BassettImageListSAXStrategy();
    }

}
