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
import edu.stanford.irt.laneweb.util.ServiceURIResolver;

@Configuration
public class GrandRoundsConfiguration {

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/grandrounds")
    @Scope("prototype")
    public Generator grandRoundsGenerator(
            @Qualifier("edu.stanford.irt.laneweb.catalog.grandrounds.GrandRoundsService/HTTP")
            final GrandRoundsService grandRoundsService) {
        return new GrandRoundsGenerator(grandRoundsService, presentationSAXStrategy());
    }

    @Bean("edu.stanford.irt.laneweb.catalog.grandrounds.GrandRoundsService/HTTP")
    public GrandRoundsService httpGrandRoundsService(
            @Qualifier("java.net.URI/catalog-service") final URI catalogServiceURI,
            final ServiceURIResolver uriResolver) {
        return new HTTPGrandRoundsService(catalogServiceURI, uriResolver);
    }

    @Bean
    public SAXStrategy<Presentation> presentationSAXStrategy() {
        return new PresentationSAXStrategy();
    }
}
