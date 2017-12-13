package edu.stanford.irt.laneweb.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Qualifier;
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

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/grandrounds")
    @Scope("prototype")
    public Generator grandRoundsGenerator(final GrandRoundsService grandRoundsService) {
        return new GrandRoundsGenerator(grandRoundsService, presentationSAXStrategy());
    }

    @Bean
    public GrandRoundsService httpGrandRoundsService(
            @Qualifier("java.net.URI/catalog-service") final URI catalogServiceURI) {
        return new HTTPGrandRoundsService(catalogServiceURI);
    }

    @Bean
    public SAXStrategy<Presentation> presentationSAXStrategy() {
        return new PresentationSAXStrategy();
    }
}
