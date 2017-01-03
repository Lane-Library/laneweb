package edu.stanford.irt.laneweb.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.stanford.irt.laneweb.eresources.SolrService;
import edu.stanford.irt.laneweb.suggest.CompositeSuggestionManager;
import edu.stanford.irt.laneweb.suggest.SolrSuggestionManager;
import edu.stanford.irt.suggest.MeshSuggestionManager;
import edu.stanford.irt.suggest.SuggestionManager;

@Configuration
public class SuggestConfiguration {

    private SolrService solrService;

    @Autowired
    public SuggestConfiguration(final SolrService solrService) {
        this.solrService = solrService;
    }

    @Bean(name = "edu.stanford.irt.suggest.SuggestionManager/eresource")
    public SuggestionManager eresourceSuggestionManager() {
        return new SolrSuggestionManager(this.solrService);
    }

    @Bean(name = "edu.stanford.irt.suggest.SuggestionManager/extensions-suggest")
    public SuggestionManager extensionsSuggestionManager() {
        return new CompositeSuggestionManager(
                Arrays.asList(new SuggestionManager[] { eresourceSuggestionManager(), meshSuggestionManager() }));
    }

    @Bean(name = "edu.stanford.irt.suggest.SuggestionManager/mesh")
    public SuggestionManager meshSuggestionManager() {
        return new MeshSuggestionManager();
    }
}
