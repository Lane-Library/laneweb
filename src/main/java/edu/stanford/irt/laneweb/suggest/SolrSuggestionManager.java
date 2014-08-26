package edu.stanford.irt.laneweb.suggest;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.support.SolrRepositoryFactory;

import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.solr.SolrRepository;
import edu.stanford.irt.suggest.Suggestion;
import edu.stanford.irt.suggest.SuggestionManager;

public class SolrSuggestionManager implements SuggestionManager {

    private SolrRepository repository;

    @Autowired
    public SolrSuggestionManager(final SolrTemplate laneSearchSolrTemplate) {
        this.repository = new SolrRepositoryFactory(laneSearchSolrTemplate).getRepository(SolrRepository.class);
    }

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
        return suggestionsFromEresources(this.repository.suggestFindByType(term, type, new PageRequest(0, 10)));
    }

    private List<Suggestion> suggestionsFromEresources(final List<Eresource> eresources) {
        List<Suggestion> suggestions = new LinkedList<Suggestion>();
        for (Eresource eresource : eresources) {
            suggestions.add(new Suggestion(eresource.getId(), eresource.getTitle()));
        }
        return suggestions;
    }
}