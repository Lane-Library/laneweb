package edu.stanford.irt.laneweb.suggest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import edu.stanford.irt.suggest.Suggestion;
import edu.stanford.irt.suggest.SuggestionManager;

public class CompositeSuggestionManager implements SuggestionManager {

    private static final int MINIMUM_TERM_LENGTH = 3;

    private static final Collection<Suggestion> NO_SUGGESTIONS = Collections.emptySet();

    private List<SuggestionManager> suggestionManagers;

    public CompositeSuggestionManager(final List<SuggestionManager> suggestionManagers) {
        this.suggestionManagers = new ArrayList<>(suggestionManagers);
    }

    @Override
    public Collection<Suggestion> getSuggestionsForTerm(final String term) {
        return doGetSuggestions(null, term);
    }

    @Override
    public Collection<Suggestion> getSuggestionsForTerm(final String type, final String term) {
        return doGetSuggestions(type, term);
    }

    private Collection<Suggestion> doGetSuggestions(final String type, final String term) {
        if (term.length() < MINIMUM_TERM_LENGTH) {
            return NO_SUGGESTIONS;
        }
        Collection<Suggestion> suggestions = new TreeSet<>(new SuggestionComparator(term));
        for (SuggestionManager suggestionManager : this.suggestionManagers) {
            if (type == null) {
                suggestions.addAll(suggestionManager.getSuggestionsForTerm(term));
            } else {
                suggestions.addAll(suggestionManager.getSuggestionsForTerm(type, term));
            }
        }
        return suggestions;
    }
}
