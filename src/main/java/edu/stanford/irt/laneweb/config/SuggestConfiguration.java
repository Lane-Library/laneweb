package edu.stanford.irt.laneweb.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.stanford.irt.laneweb.eresources.EresourceSearchService;
import edu.stanford.irt.laneweb.suggest.CompositeSuggestionManager;
import edu.stanford.irt.laneweb.suggest.DefaultSuggestionService;
import edu.stanford.irt.laneweb.suggest.SolrSuggestionManager;
import edu.stanford.irt.laneweb.suggest.SuggestionService;
import edu.stanford.irt.suggest.MeshSuggestionManager;

@Configuration
public class SuggestConfiguration {

    @Bean
    public SolrSuggestionManager eresourceSuggestionManager(final EresourceSearchService searchService) {
        return new SolrSuggestionManager(searchService);
    }

    @Bean
    public CompositeSuggestionManager extensionsSuggestionManager(
            final SolrSuggestionManager eresourceSuggestionManager, final MeshSuggestionManager meshSuggestionManager) {
        return new CompositeSuggestionManager(
                Arrays.asList(eresourceSuggestionManager, meshSuggestionManager));
    }

    @Bean
    public MeshSuggestionManager meshSuggestionManager() {
        return new MeshSuggestionManager();
    }

    @Bean
    public SuggestionService suggestionService(final SolrSuggestionManager eresourceSuggestionManager,
            final MeshSuggestionManager meshSuggestionManager) {
        return new DefaultSuggestionService(eresourceSuggestionManager, meshSuggestionManager);
    }
}
