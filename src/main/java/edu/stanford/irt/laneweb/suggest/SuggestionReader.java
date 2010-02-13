package edu.stanford.irt.laneweb.suggest;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.reading.Reader;

import edu.stanford.irt.suggest.EresourceSuggestionManager;
import edu.stanford.irt.suggest.HistorySuggestionManager;
import edu.stanford.irt.suggest.MeshSuggestionManager;
import edu.stanford.irt.suggest.Suggestion;

public class SuggestionReader implements Reader {

    private static final byte[] JSON_1 = "{\"suggest\":[".getBytes();

    private static final byte[] JSON_2 = "]}".getBytes();

    private static int JSON_RETURN_LIMIT = 20;

    private EresourceSuggestionManager eresourceSuggestionManager;

    private HistorySuggestionManager historySuggestionManager;

    private String limit;

    private MeshSuggestionManager meshSuggestionManager;

    private OutputStream outputStream;

    private String query;

    public void generate() throws IOException {
        OutputStream out = this.outputStream;
        String query = this.query;
        String limit = this.limit;
        SuggestionComparator comparator = new SuggestionComparator(query);
        TreeSet<String> suggestionSet = new TreeSet<String>(comparator);
        Collection<? extends Suggestion> suggestions = new ArrayList<Suggestion>();
        if (limit.matches("(ej|book|database|software|cc|video|lanesite|bassett)")) {
            suggestions = this.eresourceSuggestionManager.getSuggestionsForTerm(limit, query);
        } else if (limit.matches("ej-mesh")) {
            ArrayList<Suggestion> combo = new ArrayList<Suggestion>();
            combo.addAll(this.eresourceSuggestionManager.getSuggestionsForTerm("ej", query));
            combo.addAll(this.meshSuggestionManager.getSuggestionsForTerm(query));
            suggestions = combo;
        } else if ("mesh".equalsIgnoreCase(limit)) {
            suggestions = this.meshSuggestionManager.getSuggestionsForTerm(query);
        } else if (limit.matches("mesh-(d|i|di)")) {
            suggestions = this.meshSuggestionManager.getSuggestionsForTerm(limit.replaceFirst("mesh-", ""), query);
        } else if ("history".equalsIgnoreCase(limit)) {
            suggestions = this.historySuggestionManager.getSuggestionsForTerm(query);
        } else {
            suggestions = this.eresourceSuggestionManager.getSuggestionsForTerm(query);
        }
        for (Suggestion suggestion : suggestions) {
            suggestionSet.add(suggestion.getSuggestionTitle());
        }
        Iterator<String> it = suggestionSet.iterator();
        int count = 0;
        try {
            out.write(JSON_1);
            String maybeComma = "\"";
            while (it.hasNext() && count < JSON_RETURN_LIMIT) {
                count++;
                out.write((maybeComma + escapeQuotes(it.next()) + '"').getBytes());
                maybeComma = ",\"";
            }
            out.write(JSON_2);
        } finally {
            this.outputStream = null;
            this.query = null;
            this.limit = null;
        }
    }

    public long getLastModified() {
        return 0;
    }

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

    public void setOutputStream(final OutputStream outputStream) {
        if (null == outputStream) {
            throw new IllegalArgumentException("null outputStream");
        }
        this.outputStream = outputStream;
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver arg0, final Map arg1, final String arg2, final Parameters params) {
        this.limit = params.getParameter("limit", null);
        this.query = params.getParameter("query", null);
    }

    public boolean shouldSetContentLength() {
        return false;
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
}
