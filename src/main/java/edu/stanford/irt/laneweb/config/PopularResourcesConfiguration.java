package edu.stanford.irt.laneweb.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.popular.BigqueryService;
import edu.stanford.irt.laneweb.popular.PopularListGenerator;
import edu.stanford.irt.laneweb.popular.PopularResourcesSAXStrategy;
import edu.stanford.irt.laneweb.popular.RESTBigqueryService;
import edu.stanford.irt.laneweb.rest.RESTService;

@Configuration
public class PopularResourcesConfiguration {

    @Bean("java.net.URI/bigquery-service")
    public URI bigqueryServiceURI(@Value("${edu.stanford.irt.laneweb.bigquery-service.scheme}") final String scheme,
            @Value("${edu.stanford.irt.laneweb.bigquery-service.host}") final String host,
            @Value("${edu.stanford.irt.laneweb.bigquery-service.port}") final int port,
            @Value("${edu.stanford.irt.laneweb.bigquery-service.path}") final String path) throws URISyntaxException {
        return new URI(scheme, null, host, port, path, null, null);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/popular-resources-list")
    @Scope("prototype")
    public Generator popularResourcesListGenerator(final BigqueryService bigqueryService) {
        return new PopularListGenerator(bigqueryService, popularResourcesSAXStrategy());
    }

    @Bean
    public SAXStrategy<List<Map<String, String>>> popularResourcesSAXStrategy() {
        return new PopularResourcesSAXStrategy();
    }

    @Bean
    public BigqueryService restBigqueryService(@Qualifier("java.net.URI/bigquery-service") final URI bigqueryURI,
            final RESTService restService) {
        return new RESTBigqueryService(bigqueryURI, restService);
    }
}
