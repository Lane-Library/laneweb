package edu.stanford.irt.laneweb.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.oxm.Marshaller;

import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.laneweb.resource.BannerResourceOutageService;
import edu.stanford.irt.laneweb.resource.BannerResourceOutageGenerator;
import edu.stanford.irt.laneweb.rest.RESTService;

@Configuration
public class BannerResourceOutageConfiguration {

    @Bean
    public BannerResourceOutageService resourceOutageService(
            @Qualifier("java.net.URI/resource=outages-service") final URI libanswerOutageServiceURI,
            final RESTService restService) {
        return new BannerResourceOutageService(libanswerOutageServiceURI, restService);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/banner-resource-outages")
    @Scope("prototype")
    public Generator ResourceOutageGenerator(
            final Marshaller marshaller,
            @Qualifier("edu.stanford.irt.cocoon.xml.SAXParser/xml") final SAXParser xmlSAXParser,
            BannerResourceOutageService resourceOutageService) {
        return new BannerResourceOutageGenerator(marshaller, xmlSAXParser, resourceOutageService);
    }

    @Bean("java.net.URI/resource=outages-service")
    public URI libanswerOutageServiceURI(
            @Value("${edu.stanford.irt.laneweb.libanswer-outages.scheme}") final String scheme,
            @Value("${edu.stanford.irt.laneweb.libanswer-outages.host}") final String host,
            @Value("${edu.stanford.irt.laneweb.libanswer-outages.port}") final int port,
            @Value("${edu.stanford.irt.laneweb.libanswer-outages.path}") final String path)
            throws URISyntaxException {
        return new URI(scheme, null, host, port, path, null, null);
    }
}
