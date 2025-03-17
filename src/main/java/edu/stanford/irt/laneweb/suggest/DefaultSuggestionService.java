package edu.stanford.irt.laneweb.suggest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import edu.stanford.irt.suggest.MeshSuggestionManager;
import edu.stanford.irt.suggest.Suggestion;

public class DefaultSuggestionService implements SuggestionService {

    private static final Pattern ER_PATTERN = Pattern.compile("^(Book|Journal|Database|Exam Prep)$");

    private static final int MAX_QUERY_LENGTH = 100;

    private static final int MIN_QUERY_LENGTH = 3;

    private static final Collection<Suggestion> NO_SUGGESTIONS = Collections.emptyList();

    private static final int RETURN_LIMIT = 10;

    private SolrSuggestionManager eresourceSuggestionManager;

    private MeshSuggestionManager meshSuggestionManager;

    public DefaultSuggestionService(final SolrSuggestionManager eresourceSuggestionManager,
            final MeshSuggestionManager meshSuggestionManager) {
        this.eresourceSuggestionManager = eresourceSuggestionManager;
        this.meshSuggestionManager = meshSuggestionManager;
    }

    @Override
    public Collection<String> getSuggestions(final String term, final String type) {
        String query = term.trim();
        Set<Suggestion> suggestions = new TreeSet<>(new SuggestionComparator(query));
        suggestions.addAll(internalGetSuggestions(query, type));
        List<String> strings = new ArrayList<>();
        for (Suggestion suggestion : suggestions) {
            strings.add(suggestion.getSuggestionTitle());
            if (strings.size() >= RETURN_LIMIT) {
                break;
            }
        }
        return strings;
    }

    private Collection<Suggestion> internalGetSuggestions(final String query, final String limit) {
        Collection<Suggestion> suggestions = null;
        int length = query.length();
        if (length >= MIN_QUERY_LENGTH && length <= MAX_QUERY_LENGTH) {
            // queries > 100 characters are likely to produce zero results
            // queries < 3 characters, will throw IllegalArgumentException
            if (limit == null) {
                suggestions = this.eresourceSuggestionManager.getSuggestionsForTerm(query);
            } else if ("er-mesh".equals(limit)) {
                suggestions = this.eresourceSuggestionManager.getSuggestionsForTerm(query);
                suggestions.addAll(this.meshSuggestionManager.getSuggestionsForTerm(query));
            } else if (ER_PATTERN.matcher(limit).matches()) {
                suggestions = this.eresourceSuggestionManager.getSuggestionsForTerm(limit, query);
            } else if ("mesh".equals(limit)) {
                suggestions = this.meshSuggestionManager.getSuggestionsForTerm(query);
            }
        }
        return suggestions == null ? NO_SUGGESTIONS : suggestions;
    }
}
