package edu.stanford.irt.laneweb.suggest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import edu.stanford.irt.laneweb.cocoon.AbstractReader;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.suggest.EresourceSuggestionManager;
import edu.stanford.irt.suggest.HistorySuggestionManager;
import edu.stanford.irt.suggest.MeshSuggestionManager;
import edu.stanford.irt.suggest.Suggestion;

public class SuggestionReader extends AbstractReader {

    private static final byte[] JSON_1 = "{\"suggest\":[".getBytes();

    private static final byte[] JSON_2 = "]}".getBytes();

    private static final int JSON_RETURN_LIMIT = 10;

    private EresourceSuggestionManager eresourceSuggestionManager;

    private HistorySuggestionManager historySuggestionManager;

    private String limit;

    private MeshSuggestionManager meshSuggestionManager;

    private String query;

    public void generate() throws IOException {
        SuggestionComparator comparator = new SuggestionComparator(this.query);
        TreeSet<String> suggestionSet = new TreeSet<String>(comparator);
        Collection<? extends Suggestion> suggestions = new ArrayList<Suggestion>();
        if (this.limit.matches("(ej|book|database|software|cc|video|lanesite|bassett)")) {
            suggestions = this.eresourceSuggestionManager.getSuggestionsForTerm(this.limit, this.query);
        } else if (this.limit.matches("er-mesh")) {
            ArrayList<Suggestion> combo = new ArrayList<Suggestion>();
            combo.addAll(this.eresourceSuggestionManager.getSuggestionsForTerm(this.query));
            combo.addAll(this.meshSuggestionManager.getSuggestionsForTerm(this.query));
            suggestions = combo;
        } else if (this.limit.matches("ej-mesh")) {
            ArrayList<Suggestion> combo = new ArrayList<Suggestion>();
            combo.addAll(this.eresourceSuggestionManager.getSuggestionsForTerm("ej", this.query));
            combo.addAll(this.meshSuggestionManager.getSuggestionsForTerm(this.query));
            suggestions = combo;
        } else if ("mesh".equalsIgnoreCase(this.limit)) {
            suggestions = this.meshSuggestionManager.getSuggestionsForTerm(this.query);
        } else if (this.limit.matches("mesh-(d|i|di)")) {
            suggestions =
                    this.meshSuggestionManager.getSuggestionsForTerm(this.limit.replaceFirst("mesh-", ""), this.query);
        } else if ("history".equalsIgnoreCase(this.limit)) {
            suggestions = this.historySuggestionManager.getSuggestionsForTerm(this.query);
        } else {
            suggestions = this.eresourceSuggestionManager.getSuggestionsForTerm(this.query);
        }
        for (Suggestion suggestion : suggestions) {
            suggestionSet.add(suggestion.getSuggestionTitle());
        }
        Iterator<String> it = suggestionSet.iterator();
        int count = 0;
        this.outputStream.write(JSON_1);
        String maybeComma = "\"";
        while (it.hasNext() && count < JSON_RETURN_LIMIT) {
            count++;
            this.outputStream.write((maybeComma + escapeQuotes(it.next()) + '"').getBytes());
            maybeComma = ",\"";
        }
        this.outputStream.write(JSON_2);
    }

    @Override
    public String getMimeType() {
        return "text/plain";
    }

    public void setEresourceSuggestionManager(final EresourceSuggestionManager eresourceSuggestionManager) {
        if (null == eresourceSuggestionManager) {
            throw new IllegalArgumentException("null eresourceSuggestionManager");
        }
        this.eresourceSuggestionManager = eresourceSuggestionManager;
    }

    public void setHistorySuggestionManager(final HistorySuggestionManager historySuggestionManager) {
        if (null == historySuggestionManager) {
            throw new IllegalArgumentException("null historySuggestionManager");
        }
        this.historySuggestionManager = historySuggestionManager;
    }

    public void setMeshSuggestionManager(final MeshSuggestionManager meshSuggestionManager) {
        if (null == meshSuggestionManager) {
            throw new IllegalArgumentException("null meshSuggestionManager");
        }
        this.meshSuggestionManager = meshSuggestionManager;
    }

    private String escapeQuotes(final String string) {
        String result = string;
        if ((result.indexOf('\'') > -1) || (result.indexOf('"') > -1)) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < result.length(); i++) {
                char c = result.charAt(i);
                if ('"' == c) {
                    sb.append("\\");
                }
                sb.append(c);
            }
            result = sb.toString();
        }
        return result;
    }

    @Override
    protected void initialize() {
        this.limit = this.model.getString(Model.LIMIT, "");
        this.query = this.model.getString(Model.QUERY);
    }
}
