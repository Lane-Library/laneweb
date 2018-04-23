package edu.stanford.irt.laneweb.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.xml.sax.XMLReader;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.laneweb.util.ServiceURIResolver;
import edu.stanford.irt.laneweb.voyager.HTTPLoginService;
import edu.stanford.irt.laneweb.voyager.LoginService;
import edu.stanford.irt.laneweb.voyager.RESTLoginService;
import edu.stanford.irt.laneweb.voyager.VoyagerLogin;
import edu.stanford.lane.catalog.impl.xml.DefaultMarcReader;

@Configuration
public class VoyagerConfiguration {

    @Bean("edu.stanford.irt.laneweb.voyager.LoginService/HTTP")
    public LoginService loginService(final ObjectMapper objectMapper,
            @Qualifier("java.net.URI/catalog-service") final URI catalogServiceURI,
            final ServiceURIResolver uriResolver) {
        return new HTTPLoginService(objectMapper, catalogServiceURI, uriResolver);
    }

    @Bean(name = "org.xml.sax.XMLReader/marc")
    @Scope("prototype")
    public XMLReader marcXMLReader() {
        return new DefaultMarcReader();
    }

    @Bean("edu.stanford.irt.laneweb.voyager.LoginService/HTTP")
    public LoginService loginService(@Qualifier("java.net.URI/catalog-service") final URI catalogServiceURI,
            final RESTService restService) {
        return new RESTLoginService(catalogServiceURI, restService);
    }

    @Bean
    public VoyagerLogin voyagerLogin(
            @Qualifier("edu.stanford.irt.laneweb.voyager.LoginService/REST") final LoginService loginService) {
        return new VoyagerLogin(loginService);
    }
}
