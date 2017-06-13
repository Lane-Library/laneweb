package edu.stanford.irt.laneweb.config;

import static edu.stanford.irt.laneweb.util.IOUtils.getResourceAsString;

import java.net.URI;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.grandrounds.Presentation;
import edu.stanford.irt.laneweb.catalog.grandrounds.GrandRoundsGenerator;
import edu.stanford.irt.laneweb.catalog.grandrounds.GrandRoundsService;
import edu.stanford.irt.laneweb.catalog.grandrounds.HTTPGrandRoundsService;
import edu.stanford.irt.laneweb.catalog.grandrounds.JDBCGrandRoundsService;
import edu.stanford.irt.laneweb.catalog.grandrounds.PresentationSAXStrategy;

@Configuration
public class GrandRoundsConfiguration {

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/grandrounds")
    @Scope("prototype")
    public Generator grandRoundsGenerator(final GrandRoundsService grandRoundsService) {
        return new GrandRoundsGenerator(grandRoundsService, presentationSAXStrategy());
    }

    @Bean
    @Profile("gce")
    public GrandRoundsService httpGrandRoundsService(final URI catalogServiceURI) {
        return new HTTPGrandRoundsService(catalogServiceURI);
    }

    @Bean
    @Profile("!gce")
    public GrandRoundsService jdbcGrandRoundsService(
            @Qualifier("javax.sql.DataSource/catalog") final DataSource dataSource) {
        return new JDBCGrandRoundsService(dataSource, getResourceAsString(Presentation.class, "getGrandRounds.fnc"));
    }

    @Bean
    public SAXStrategy<Presentation> presentationSAXStrategy() {
        return new PresentationSAXStrategy();
    }
}
