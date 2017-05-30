package edu.stanford.irt.laneweb.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.xml.sax.XMLReader;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.voyager.HTTPLoginService;
import edu.stanford.irt.laneweb.voyager.VoyagerLogin;
import edu.stanford.lane.catalog.impl.xml.DefaultMarcReader;

@Configuration
public class VoyagerConfiguration {

    private URI catalogServiceURI;

    private ObjectMapper objectMapper;

    @Autowired
    public VoyagerConfiguration(final ObjectMapper objectMapper, final URI catalogServiceURI) {
        this.objectMapper = objectMapper;
        this.catalogServiceURI = catalogServiceURI;
    }

    @Bean(name = "org.xml.sax.XMLReader/marc")
    @Scope("prototype")
    public XMLReader marcXMLReader() {
        return new DefaultMarcReader();
    }

    @Bean
    public VoyagerLogin voyagerLogin() {
        return new VoyagerLogin(new HTTPLoginService(this.objectMapper, this.catalogServiceURI));
    }
}
