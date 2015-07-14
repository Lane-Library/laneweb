package edu.stanford.irt.laneweb.suggest;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.solr.SolrRepository;
import edu.stanford.irt.laneweb.solr.SolrTypeManager;
import edu.stanford.irt.suggest.Suggestion;
import edu.stanford.irt.suggest.SuggestionManager;

public class SolrSuggestionManager implements SuggestionManager {

    @Autowired
    private SolrRepository repository;

    @Override
    public Collection<Suggestion> getSuggestionsForTerm(final String term) {
        if (null == term) {
            throw new IllegalArgumentException("null term");
        }
        return suggestionsFromEresources(this.repository.suggestFindAll(term.toLowerCase(), term.replaceAll(" ", " +"),
                new PageRequest(0, 10)));
    }

    @Override
    public Collection<Suggestion> getSuggestionsForTerm(final String type, final String term) {
        if (null == term) {
            throw new IllegalArgumentException("null term");
        }
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        return suggestionsFromEresources(this.repository.suggestFindByType(term,
                SolrTypeManager.convertToNewType(type), new PageRequest(0, 10)));
    }

    private List<Suggestion> suggestionsFromEresources(final List<Eresource> eresources) {
        List<Suggestion> suggestions = new LinkedList<Suggestion>();
        for (Eresource eresource : eresources) {
            String title = eresource.getTitle();
            if (title.endsWith(".")) {
                title = title.substring(0, title.length() - 1);
            }
            suggestions.add(new Suggestion(eresource.getId(), title));
        }
        return suggestions;
    }
}