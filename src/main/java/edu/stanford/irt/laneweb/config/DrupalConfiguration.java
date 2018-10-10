package edu.stanford.irt.laneweb.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.laneweb.drupal.DrupalAPIService;
import edu.stanford.irt.laneweb.drupal.DrupalNodeTransformer;
import edu.stanford.irt.laneweb.rest.RESTService;

@Configuration
public class DrupalConfiguration {

    @Bean
    public DrupalAPIService drupalAPIService(
            @Value("${edu.stanford.irt.laneweb.drupal-service.uri}") final URI drupalBaseURI,
            final RESTService restService) {
        return new DrupalAPIService(drupalBaseURI, restService);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/drupal")
    @Scope("prototype")
    public Transformer drupalTransformer(final DrupalAPIService drupalAPIService) {
        return new DrupalNodeTransformer(drupalAPIService);
    }
}
