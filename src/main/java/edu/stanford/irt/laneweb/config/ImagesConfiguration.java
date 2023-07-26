package edu.stanford.irt.laneweb.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;

import edu.stanford.irt.bassett.configuration.BassettConfiguration;
import edu.stanford.irt.bassett.model.BassettImage;
import edu.stanford.irt.bassett.service.BassettImageService;
import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;

import edu.stanford.irt.laneweb.images.BassettAccordionGenerator;
import edu.stanford.irt.laneweb.images.BassettCountSAXStrategy;
import edu.stanford.irt.laneweb.images.BassettImageGenerator;
import edu.stanford.irt.laneweb.images.BassettImageListSAXStrategy;

@Configuration
@Import({ BassettConfiguration.class })
public class ImagesConfiguration {
   
    @Autowired
    BassettImageService  service;
        
    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/bassett-accordion")
    @Scope("prototype")
    Generator bassettAccordionGenerator() {
        return new BassettAccordionGenerator(this.service, countSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/bassett")
    @Scope("prototype")
    Generator bassettGenerator() {
        return new BassettImageGenerator(this.service, pageSAXStrategy());
    }

    @Bean
    SAXStrategy<Map<String, Map<String, Integer>>> countSAXStrategy() {
        return new BassettCountSAXStrategy();
    }

    @Bean
    SAXStrategy<Page<BassettImage>> pageSAXStrategy() {
        return new BassettImageListSAXStrategy();
    }

}
