package edu.stanford.irt.laneweb.suggest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.stanford.irt.laneweb.solr.Eresource;
import edu.stanford.irt.laneweb.solr.SolrSearchService;
import edu.stanford.irt.suggest.Suggestion;
import edu.stanford.irt.suggest.SuggestionManager;

public class SolrSuggestionManager implements SuggestionManager {

    private SolrSearchService searchService;

    public SolrSuggestionManager(final SolrSearchService searchService) {
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

    private List<Suggestion> suggestionsFromEresources(final List<Eresource> eresources) {
        List<Suggestion> suggestions = new ArrayList<>();
        for (Eresource eresource : eresources) {
            String title = eresource.getTitle();
            if (title.endsWith(".") || title.endsWith("/")) {
                title = title.substring(0, title.length() - 1);
            }
            suggestions.add(new Suggestion(eresource.getId(), title.trim()));
        }
        return suggestions;
    }
}