package edu.stanford.irt.laneweb.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.grandrounds.Presentation;
import edu.stanford.irt.laneweb.catalog.grandrounds.GrandRoundsGenerator;
import edu.stanford.irt.laneweb.catalog.grandrounds.GrandRoundsManager;
import edu.stanford.irt.laneweb.catalog.grandrounds.PresentationSAXStrategy;

@Configuration
public class GrandRoundsConfiguration {

    private DataSource dataSource;

    @Autowired
    public GrandRoundsConfiguration(@Qualifier("javax.sql.DataSource/grandrounds") final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/grandrounds")
    @Scope("prototype")
    public Generator grandRoundsGenerator() throws IOException {
        return new GrandRoundsGenerator(grandRoundsManager(), presentationSAXStrategy());
    }

    @Bean
    public GrandRoundsManager grandRoundsManager() throws IOException {
        return new GrandRoundsManager(this.dataSource,
                getClass().getResourceAsStream("/edu/stanford/irt/grandrounds/getGrandRounds.fnc"));
    }

    @Bean
    public SAXStrategy<Presentation> presentationSAXStrategy() {
        return new PresentationSAXStrategy();
    }
}
