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

import edu.stanford.irt.laneweb.model.DefaultObjectModelAware;
import edu.stanford.irt.laneweb.model.LanewebObjectModel;
import edu.stanford.irt.suggest.EresourceSuggestionManager;
import edu.stanford.irt.suggest.Suggestion;

public class ExtensionsSuggestReader extends DefaultObjectModelAware implements Reader {

    private OutputStream outputStream;

    private EresourceSuggestionManager eresourceSuggestionManager;

    private String query;

    public void generate() throws IOException {
        String query = this.query;
        SuggestionComparator comparator = new SuggestionComparator(query);
        TreeSet<String> suggestionSet = new TreeSet<String>(comparator);
        Collection<? extends Suggestion> suggestions = new ArrayList<Suggestion>();
        suggestions = this.eresourceSuggestionManager.getSuggestionsForTerm(query);
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
        String query = getString(LanewebObjectModel.QUERY);
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

    public boolean shouldSetContentLength() {
        return false;
    }
}
