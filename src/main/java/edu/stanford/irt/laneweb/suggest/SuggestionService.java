package edu.stanford.irt.laneweb.suggest;

import java.util.Collection;

public interface SuggestionService {

    Collection<String> getSuggestions(String term, String type);
}
