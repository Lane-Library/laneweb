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
import edu.stanford.irt.suggest.Suggestion;

public class ExtensionsSuggestReader implements Reader {

    private OutputStream outputStream;

    private EresourceSuggestionManager eresourceSuggestionManager;

    private String query;

    public void generate() throws IOException {
        String q = this.query;
        SuggestionComparator comparator = new SuggestionComparator(q);
        TreeSet<String> suggestionSet = new TreeSet<String>(comparator);
        Collection<? extends Suggestion> suggestions = new ArrayList<Suggestion>();
        suggestions = this.eresourceSuggestionManager.getSuggestionsForTerm(q);
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

    public void setOutputStream(final OutputStream outputStream) throws IOException {
        if (null == outputStream) {
            throw new IllegalArgumentException("null outputStream");
        }
        this.outputStream = outputStream;
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver arg0, final Map arg1, final String arg2, final Parameters params) {
        String q = params.getParameter("query", null);
        // remove quotes and apostrophes
        if ((q.indexOf('\'') > -1) || (q.indexOf('"') > -1)) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < q.length(); i++) {
                char c = q.charAt(i);
                if (('\'' != c) && ('"' != c)) {
                    sb.append(c);
                }
            }
            q = sb.toString();
        }
        this.query = q;
    }

    public boolean shouldSetContentLength() {
        return false;
    }
}
