package edu.stanford.irt.laneweb.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.xml.sax.XMLReader;

import edu.stanford.irt.laneweb.rest.BasicAuthRESTService;
import edu.stanford.irt.laneweb.voyager.LoginService;
import edu.stanford.irt.laneweb.voyager.RESTLoginService;
import edu.stanford.irt.laneweb.voyager.VoyagerLogin;
import edu.stanford.lane.catalog.impl.xml.DefaultMarcReader;

@Configuration
public class VoyagerConfiguration {

    @Bean(name = "org.xml.sax.XMLReader/marc")
    @Scope("prototype")
    public XMLReader marcXMLReader() {
        return new DefaultMarcReader();
    }

    @Bean
    public LoginService loginService(@Qualifier("java.net.URI/catalog-service") final URI catalogServiceURI,
            final BasicAuthRESTService restService) {
        return new RESTLoginService(catalogServiceURI, restService);
    }

    @Bean
    public VoyagerLogin voyagerLogin(final LoginService loginService) {
        return new VoyagerLogin(loginService);
    }
}
