package edu.stanford.irt.laneweb.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.stanford.irt.laneweb.voyager.VoyagerLogin;
import edu.stanford.lane.catalog.impl.xml.DefaultMarcReader;

@Configuration
public class VoyagerConfiguration {

    private DataSource dataSource;

    @Autowired
    public VoyagerConfiguration(@Qualifier("javax.sql.DataSource/voyager") final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean(name = "org.xml.sax.XMLReader/marc")
    @Scope("prototype")
    public DefaultMarcReader marcXMLReader() {
        return new DefaultMarcReader();
    }

    @Bean
    public VoyagerLogin voyagerLogin() {
        return new VoyagerLogin(this.dataSource);
    }
}
