package edu.stanford.irt.laneweb.suggest;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.suggest.Suggestion;
import edu.stanford.irt.suggest.SuggestionManager;

public class SolrSuggestionManager implements SuggestionManager {

    private SolrServer solrServer;

    public SolrSuggestionManager(final SolrServer solrServer) {
        this.solrServer = solrServer;
    }

    private List<Suggestion> doGet(final SolrParams params) {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setRequestHandler("/lane-suggest");
        solrQuery.add(params);
        QueryResponse rsp;
        try {
            rsp = this.solrServer.query(solrQuery);
        } catch (SolrServerException e) {
            throw new LanewebException(e);
        }
        SolrDocumentList rdocs = rsp.getResults();
        LinkedList<Suggestion> suggestions = new LinkedList<Suggestion>();
        for (SolrDocument doc : rdocs) {
            suggestions.add(new Suggestion((String) doc.getFieldValue("id"), (String) doc.getFieldValue("title")));
        }
        return suggestions;
    }

    public Collection<Suggestion> getSuggestionsForTerm(final String term) {
        if (null == term) {
            throw new IllegalArgumentException("null term");
        }
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set("q", term);
        return doGet(params);
    }

    public Collection<Suggestion> getSuggestionsForTerm(final String type, final String term) {
        if (null == term) {
            throw new IllegalArgumentException("null term");
        }
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set("fq", "type:\"" + type + "\"");
        params.set("q", term);
        return doGet(params);
    }
}