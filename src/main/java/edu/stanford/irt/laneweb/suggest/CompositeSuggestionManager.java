package edu.stanford.irt.laneweb.suggest;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import edu.stanford.irt.suggest.Suggestion;
import edu.stanford.irt.suggest.SuggestionManager;


public class CompositeSuggestionManager implements SuggestionManager {
    
    private List<SuggestionManager> suggestionManagers;

    public Collection<Suggestion> getSuggestionsForTerm(String term) {
        Collection<Suggestion> suggestions = new TreeSet<Suggestion>(new SuggestionComparator(term));
        for (SuggestionManager suggestionManager : this.suggestionManagers) {
            suggestions.addAll(suggestionManager.getSuggestionsForTerm(term));
        }
        return suggestions;
    }

    public Collection<Suggestion> getSuggestionsForTerm(String type, String term) {
        Collection<Suggestion> suggestions = new TreeSet<Suggestion>(new SuggestionComparator(term));
        for (SuggestionManager suggestionManager : this.suggestionManagers) {
            suggestions.addAll(suggestionManager.getSuggestionsForTerm(type, term));
        }
        return suggestions;
    }
    
    public void setSuggestionManagers(List<SuggestionManager> suggestionMangers) {
        this.suggestionManagers = suggestionMangers;
    }
}
