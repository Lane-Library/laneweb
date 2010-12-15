package edu.stanford.irt.laneweb.suggest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.regex.Pattern;

import edu.stanford.irt.laneweb.cocoon.AbstractReader;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.suggest.Suggestion;
import edu.stanford.irt.suggest.SuggestionManager;

public class SuggestionReader extends AbstractReader {
    
    private static final Pattern ER_PATTERN = Pattern.compile("(?:ej|book|database|software|cc|video|lanesite|bassett)");

    private static final byte[] JSON_1 = "{\"suggest\":[".getBytes();

    private static final byte[] JSON_2 = "]}".getBytes();

    private static final byte[] OPEN_CALLBACK = "(".getBytes();

    private static final byte[] CLOSE_CALLBACK = ");".getBytes();

    private static final int JSON_RETURN_LIMIT = 10;

    private SuggestionManager eresourceSuggestionManager;

    private SuggestionManager historySuggestionManager;

    private String limit;

    private String callback;

    private SuggestionManager meshSuggestionManager;

    private String query;

    public void generate() throws IOException {
        SuggestionComparator comparator = new SuggestionComparator(this.query);
        TreeSet<String> suggestionSet = new TreeSet<String>(comparator);
        Collection<? extends Suggestion> suggestions;
        if (ER_PATTERN.matcher(this.limit).matches()) {
            suggestions = this.eresourceSuggestionManager.getSuggestionsForTerm(this.limit, this.query);
        } else if ("er-mesh".equals(this.limit)) {
            ArrayList<Suggestion> combo = new ArrayList<Suggestion>();
            combo.addAll(this.eresourceSuggestionManager.getSuggestionsForTerm(this.query));
            combo.addAll(this.meshSuggestionManager.getSuggestionsForTerm(this.query));
            suggestions = combo;
        } else if ("ej-mesh".equals(this.limit)) {
            ArrayList<Suggestion> combo = new ArrayList<Suggestion>();
            combo.addAll(this.eresourceSuggestionManager.getSuggestionsForTerm("ej", this.query));
            combo.addAll(this.meshSuggestionManager.getSuggestionsForTerm(this.query));
            suggestions = combo;
        } else if ("mesh".equals(this.limit)) {
            suggestions = this.meshSuggestionManager.getSuggestionsForTerm(this.query);
        } else if (this.limit.indexOf("mesh-") == 0) {
            suggestions = this.meshSuggestionManager.getSuggestionsForTerm(this.limit.substring(5), this.query);
        } else if ("history".equals(this.limit)) {
            suggestions = this.historySuggestionManager.getSuggestionsForTerm(this.query);
        } else {
            suggestions = this.eresourceSuggestionManager.getSuggestionsForTerm(this.query);
        }
        for (Suggestion suggestion : suggestions) {
            suggestionSet.add(suggestion.getSuggestionTitle());
        }
        Iterator<String> it = suggestionSet.iterator();
        int count = 0;
        if (!"".equals(this.callback)) {
            this.outputStream.write(this.callback.getBytes());
            this.outputStream.write(OPEN_CALLBACK);
        }
        this.outputStream.write(JSON_1);
        String maybeComma = "\"";
        while (it.hasNext() && count < JSON_RETURN_LIMIT) {
            count++;
            this.outputStream.write((maybeComma + escapeQuotes(it.next()) + '"').getBytes());
            maybeComma = ",\"";
        }
        this.outputStream.write(JSON_2);
        if (!"".equals(this.callback)) {
            this.outputStream.write(CLOSE_CALLBACK);
        }
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

    public void setHistorySuggestionManager(final SuggestionManager historySuggestionManager) {
        if (null == historySuggestionManager) {
            throw new IllegalArgumentException("null historySuggestionManager");
        }
        this.historySuggestionManager = historySuggestionManager;
    }

    public void setMeshSuggestionManager(final SuggestionManager meshSuggestionManager) {
        if (null == meshSuggestionManager) {
            throw new IllegalArgumentException("null meshSuggestionManager");
        }
        this.meshSuggestionManager = meshSuggestionManager;
    }

    private String escapeQuotes(final String string) {
        String result = string;
        if (result.indexOf('"') > -1) {
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
        this.callback = ModelUtil.getString(this.model, Model.CALLBACK, "");
        this.limit = ModelUtil.getString(this.model, Model.LIMIT, "");
        this.query = ModelUtil.getString(this.model, Model.QUERY);
    }
}
