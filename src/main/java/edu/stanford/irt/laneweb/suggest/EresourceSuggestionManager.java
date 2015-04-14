package edu.stanford.irt.laneweb.suggest;

import java.util.Collection;
import java.util.LinkedList;

import edu.stanford.irt.laneweb.eresources.CollectionManager;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.suggest.Suggestion;
import edu.stanford.irt.suggest.SuggestionManager;

public class EresourceSuggestionManager implements SuggestionManager {

    private CollectionManager collectionManager;

    public EresourceSuggestionManager(final CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public Collection<Suggestion> getSuggestionsForTerm(final String term) {
        return getSuggestions(this.collectionManager.search(term));
    }

    @Override
    public Collection<Suggestion> getSuggestionsForTerm(final String type, final String term) {
        return getSuggestions(this.collectionManager.searchType(type, term));
    }

    private Collection<Suggestion> getSuggestions(final Collection<Eresource> eresources) {
        Collection<Suggestion> suggestions = new LinkedList<Suggestion>();
        for (Eresource eresource : eresources) {
            suggestions.add(new Suggestion(Integer.toString(eresource.getId()), eresource.getTitle()));
        }
        return suggestions;
    }
}
