package edu.stanford.irt.laneweb.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.grandrounds.Presentation;
import edu.stanford.irt.laneweb.catalog.grandrounds.GrandRoundsGenerator;
import edu.stanford.irt.laneweb.catalog.grandrounds.GrandRoundsService;
import edu.stanford.irt.laneweb.catalog.grandrounds.HTTPGrandRoundsService;
import edu.stanford.irt.laneweb.catalog.grandrounds.PresentationSAXStrategy;

@Configuration
public class GrandRoundsConfiguration {

    private URI catalogServiceURI;

    @Autowired
    public GrandRoundsConfiguration(final URI catalogServiceURI) {
        this.catalogServiceURI = catalogServiceURI;
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/grandrounds")
    @Scope("prototype")
    public Generator grandRoundsGenerator() {
        return new GrandRoundsGenerator(grandRoundsService(), presentationSAXStrategy());
    }

    @Bean
    public GrandRoundsService grandRoundsService() {
        return new HTTPGrandRoundsService(this.catalogServiceURI);
    }

    @Bean
    public SAXStrategy<Presentation> presentationSAXStrategy() {
        return new PresentationSAXStrategy();
    }
}
