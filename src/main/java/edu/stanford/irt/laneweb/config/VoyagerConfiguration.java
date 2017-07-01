package edu.stanford.irt.laneweb.config;

import java.net.URI;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.xml.sax.XMLReader;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.voyager.HTTPLoginService;
import edu.stanford.irt.laneweb.voyager.JDBCLoginService;
import edu.stanford.irt.laneweb.voyager.LoginService;
import edu.stanford.irt.laneweb.voyager.VoyagerLogin;
import edu.stanford.lane.catalog.impl.xml.DefaultMarcReader;

@Configuration
public class VoyagerConfiguration {

    @Bean
    @Profile("gce")
    public LoginService httpLoginService(final ObjectMapper objectMapper,
            @Qualifier("java.net.URI/catalog-service") final URI catalogServiceURI) {
        return new HTTPLoginService(objectMapper, catalogServiceURI);
    }

    @Bean
    @Profile("!gce")
    public LoginService jdbcVoyagerLoginService(
            @Qualifier("javax.sql.DataSource/voyager-login") final DataSource dataSource) {
        return new JDBCLoginService(dataSource);
    }

    @Bean(name = "org.xml.sax.XMLReader/marc")
    @Scope("prototype")
    public XMLReader marcXMLReader() {
        return new DefaultMarcReader();
    }

    @Bean
    public VoyagerLogin voyagerLogin(final LoginService loginService) {
        return new VoyagerLogin(loginService);
    }
}
