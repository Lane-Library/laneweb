package edu.stanford.irt.laneweb.suggest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.stanford.irt.laneweb.eresources.EresourceSearchService;
import edu.stanford.irt.suggest.Suggestion;
import edu.stanford.irt.suggest.SuggestionManager;

public class SolrSuggestionManager implements SuggestionManager {

    private EresourceSearchService searchService;

    public SolrSuggestionManager(final EresourceSearchService searchService) {
        this.searchService = searchService;
    }

    @Override
    public Collection<Suggestion> getSuggestionsForTerm(final String term) {
        if (null == term) {
            throw new IllegalArgumentException("null term");
        }
        return suggestionsFromEresources(this.searchService.suggestFindAll(term));
    }

    @Override
    public Collection<Suggestion> getSuggestionsForTerm(final String type, final String term) {
        if (null == term) {
            throw new IllegalArgumentException("null term");
        }
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        return suggestionsFromEresources(this.searchService.suggestFindByType(term, type));
    }

    private List<Suggestion> suggestionsFromEresources(final Map<String, String> eresourceTitles) {
        List<Suggestion> suggestions = new ArrayList<>();
        Set<Entry<String, String>> pairs = eresourceTitles.entrySet() ;
        for (Entry<String,String> suggestion : pairs) {
            String title = suggestion.getValue();
            if (title.endsWith(".") || title.endsWith("/")) {
                title = title.substring(0, title.length() - 1);
            }
            suggestions.add(new Suggestion(suggestion.getKey(), title.trim()));
        }
        return suggestions;
    }
}
