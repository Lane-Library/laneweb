package edu.stanford.irt.laneweb.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.stanford.irt.laneweb.eresources.SolrService;
import edu.stanford.irt.laneweb.suggest.CompositeSuggestionManager;
import edu.stanford.irt.laneweb.suggest.SolrSuggestionManager;
import edu.stanford.irt.suggest.MeshSuggestionManager;
import edu.stanford.irt.suggest.SuggestionManager;

@Configuration
public class SuggestConfiguration {

    @Bean(name = "edu.stanford.irt.suggest.SuggestionManager/eresource")
    public SolrSuggestionManager eresourceSuggestionManager(final SolrService solrService) {
        return new SolrSuggestionManager(solrService);
    }

    @Bean(name = "edu.stanford.irt.suggest.SuggestionManager/extensions-suggest")
    public SuggestionManager extensionsSuggestionManager(final SolrSuggestionManager eresourceSuggestionManager,
            final MeshSuggestionManager meshSuggestionManager) {
        return new CompositeSuggestionManager(
                Arrays.asList(new SuggestionManager[] { eresourceSuggestionManager, meshSuggestionManager }));
    }

    @Bean(name = "edu.stanford.irt.suggest.SuggestionManager/mesh")
    public MeshSuggestionManager meshSuggestionManager() {
        return new MeshSuggestionManager();
    }
}
