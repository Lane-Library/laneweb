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

    private ThreadLocal<String> limit = new ThreadLocal<String>();

    private EresourceSuggestionManager eresourceSuggestionManager;

    private HistorySuggestionManager historySuggestionManager;
    
    private MeshSuggestionManager meshSuggestionManager;

    private ThreadLocal<OutputStream> outputStream = new ThreadLocal<OutputStream>();

    private ThreadLocal<String> query = new ThreadLocal<String>();

    public void generate() throws IOException {
        OutputStream out = this.outputStream.get();
        String q = this.query.get();
        String l = this.limit.get();
        SuggestionComparator comparator = new SuggestionComparator(q);
        TreeSet<String> suggestionSet = new TreeSet<String>(comparator);
        Collection<?> suggestions = new ArrayList<Suggestion>();
        if ("all".equalsIgnoreCase(l)) {
          suggestions = this.eresourceSuggestionManager.getAllButBassettSuggestionsForTerm(q);
        } else if ("ej".equalsIgnoreCase(l)) {
          suggestions = this.eresourceSuggestionManager.getJournalSuggestionsForTerm(q);
        } else if ("book".equalsIgnoreCase(l)) {
          suggestions = this.eresourceSuggestionManager.getBookSuggestionsForTerm(q);
        } else if ("database".equalsIgnoreCase(l)) {
          suggestions = this.eresourceSuggestionManager.getDatabaseSuggestionsForTerm(q);
        } else if ("software".equalsIgnoreCase(l)) {
          suggestions = this.eresourceSuggestionManager.getSoftwareSuggestionsForTerm(q);
        } else if ("cc".equalsIgnoreCase(l)) {
          suggestions = this.eresourceSuggestionManager.getCalcSuggestionsForTerm(q);
        } else if ("video".equalsIgnoreCase(l)) {
          suggestions = this.eresourceSuggestionManager.getVideoSuggestionsForTerm(q);
        } else if ("bassett".equalsIgnoreCase(l)) {
          suggestions = this.eresourceSuggestionManager.getBassettSuggestionsForTerm(q);
        } else if ("history".equalsIgnoreCase(l)) {
          suggestions = this.historySuggestionManager.getSuggestionsForTerm(q);
        } else if ("mesh".equalsIgnoreCase(l)) {
          suggestions = this.meshSuggestionManager.getSuggestionsForTerm(q);
        } else if ("mesh-d".equalsIgnoreCase(l)||"mesh-p".equalsIgnoreCase(l)) {
          suggestions = this.meshSuggestionManager.getDiseaseSuggestionsForTerm(q);
        } else if ("mesh-i".equalsIgnoreCase(l)) {
          suggestions = this.meshSuggestionManager.getInterventionSuggestionsForTerm(q);
        } else if ("mesh-di".equalsIgnoreCase(l)) {
          suggestions = this.meshSuggestionManager.getDiseaseOrInterventionSuggestionsForTerm(q);
        } else {
          suggestions = this.eresourceSuggestionManager.getSuggestionsForTerm(q);
        }
        
        for (Suggestion suggestion : (Collection<Suggestion>) suggestions) {
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
            this.outputStream.set(null);
            this.query.set(null);
            this.limit.set(null);
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
        this.outputStream.set(outputStream);
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver arg0, final Map arg1, final String arg2, final Parameters params) {
        this.limit.set(params.getParameter("limit", null));
        this.query.set(params.getParameter("query", null));
    }

    public boolean shouldSetContentLength() {
        return false;
    }
    
    private String escapeQuotes(final String string) {
      String s = string;
      if ((s.indexOf('\'') > -1) || (s.indexOf('"') > -1)) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
          char c = s.charAt(i);
          if ('"' == c) {
            sb.append("\\");
          }
          sb.append(c);
        }
        s = sb.toString();
      }
      return s;
    }
    
}
