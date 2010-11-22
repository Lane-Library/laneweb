package edu.stanford.irt.laneweb.suggest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import edu.stanford.irt.laneweb.Model;
import edu.stanford.irt.laneweb.cocoon.AbstractReader;
import edu.stanford.irt.laneweb.util.ModelUtil;
import edu.stanford.irt.suggest.Suggestion;
import edu.stanford.irt.suggest.SuggestionManager;

public class ExtensionsSuggestReader extends AbstractReader {

    private SuggestionManager eresourceSuggestionManager;

    private SuggestionManager meshSuggestionManager;

    private String query;

    public void generate() throws IOException {
        SuggestionComparator comparator = new SuggestionComparator(this.query);
        TreeSet<String> suggestionSet = new TreeSet<String>(comparator);
        Collection<Suggestion> suggestions = new ArrayList<Suggestion>();
        suggestions.addAll(this.eresourceSuggestionManager.getSuggestionsForTerm(this.query));
        suggestions.addAll(this.meshSuggestionManager.getSuggestionsForTerm(this.query));
        for (Suggestion suggestion : suggestions) {
            suggestionSet.add(suggestion.getSuggestionTitle());
        }
        Iterator<String> it = suggestionSet.iterator();
        this.outputStream.write(("[\"" + this.query + "\", [").getBytes());
        String maybeComma = "\"";
        while (it.hasNext()) {
            this.outputStream.write((maybeComma + it.next() + '"').getBytes());
            maybeComma = ",\"";
        }
        this.outputStream.write("]]".getBytes());
    }

    @Override
    public String getMimeType() {
        return "text/plain";
    }

    public void setEresourceSuggestionManager(final SuggestionManager eresourceSuggestionManager) {
        if (null == eresourceSuggestionManager) {
            throw new IllegalArgumentException("null eresourceSuggestionManager");
        }
        this.eresourceSuggestionManager = eresourceSuggestionManager;
    }

    public void setMeshSuggestionManager(final SuggestionManager meshSuggestionManager) {
        if (null == meshSuggestionManager) {
            throw new IllegalArgumentException("null meshSuggestionManager");
        }
        this.meshSuggestionManager = meshSuggestionManager;
    }

    @Override
    protected void initialize() {
        String query = ModelUtil.getString(this.model, Model.QUERY);
        if (null == query) {
            throw new IllegalArgumentException("null query");
        }
        // remove quotes and apostrophes
        if ((query.indexOf('\'') > -1) || (query.indexOf('"') > -1)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < query.length(); i++) {
                char c = query.charAt(i);
                if (('\'' != c) && ('"' != c)) {
                    sb.append(c);
                }
            }
            query = sb.toString();
        }
        this.query = query;
    }
}
