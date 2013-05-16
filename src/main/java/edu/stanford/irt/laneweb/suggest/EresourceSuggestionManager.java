package edu.stanford.irt.laneweb.suggest;

import java.util.Collection;
import java.util.LinkedList;

import edu.stanford.irt.laneweb.eresources.CollectionManager;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.suggest.Suggestion;
import edu.stanford.irt.suggest.SuggestionManager;

public class EresourceSuggestionManager implements SuggestionManager {

    private CollectionManager collectionManager;

    private Collection<Suggestion> getSuggestions(final Collection<Eresource> eresources) {
        Collection<Suggestion> suggestions = new LinkedList<Suggestion>();
        for (Eresource eresource : eresources) {
            suggestions.add(new Suggestion(Integer.toString(eresource.getId()), eresource.getTitle()));
        }
        return suggestions;
    }

    public Collection<Suggestion> getSuggestionsForTerm(final String term) {
        if (null == term) {
            throw new IllegalArgumentException("null term");
        }
        return getSuggestions(this.collectionManager.search(term));
    }

    public Collection<Suggestion> getSuggestionsForTerm(final String type, final String term) {
        if (null == term) {
            throw new IllegalArgumentException("null term");
        }
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        return getSuggestions(this.collectionManager.searchType(type, term));
    }

    public void setCollectionManager(final CollectionManager collectionManager) {
        if (null == collectionManager) {
            throw new IllegalArgumentException("null collectionManager");
        }
        this.collectionManager = collectionManager;
    }
}
