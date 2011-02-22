package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.suggest.SuggestionComparator;
import edu.stanford.irt.suggest.Suggestion;
import edu.stanford.irt.suggest.SuggestionManager;

@Controller
public class SuggestionController {

    private static final String CLOSE_CALLBACK = ");";

    private static final Pattern ER_PATTERN = Pattern.compile("(?:ej|book|database|software|cc|video|lanesite|bassett)");

    private static final String JSON_1 = "{\"suggest\":[";

    private static final String JSON_2 = "]}";

    private static final int JSON_RETURN_LIMIT = 10;

    private static final String OPEN_CALLBACK = "(";

    @Resource(name = "edu.stanford.irt.suggest.SuggestionManager/eresource")
    private SuggestionManager eresourceSuggestionManager;

    @Resource(name = "edu.stanford.irt.suggest.SuggestionManager/history")
    private SuggestionManager historySuggestionManager;

    @Resource(name = "edu.stanford.irt.suggest.SuggestionManager/mesh")
    private SuggestionManager meshSuggestionManager;
    
    @RequestMapping(value = "/apps/suggest/json")
    @ResponseBody
    public String getSuggestions(@RequestParam String q, @RequestParam(required = false) String l, @RequestParam(required = false) String callback) {
        String query = escapeQuotes(q);
        Collection<Suggestion> suggestions = internalGetSuggestions(query, l);
        List<Suggestion> result = new LinkedList<Suggestion>();
        for (Suggestion suggestion: suggestions) {
            result.add(suggestion);
            if (result.size() >= JSON_RETURN_LIMIT) {
                break;
            }
        }
        StringBuilder sb = new StringBuilder();
        if (callback != null) {
            sb.append(callback).append(OPEN_CALLBACK);
        }
        sb.append(JSON_1);
        String maybeComma = "\"";
        for (Suggestion suggestion : result) {
            sb.append(maybeComma).append(escapeQuotes(suggestion.getSuggestionTitle())).append('"');
            maybeComma = ",\"";
        }
        sb.append(JSON_2);
        if (callback != null) {
            sb.append(CLOSE_CALLBACK);
        }
        return sb.toString();
    }
    
    private Collection<Suggestion> internalGetSuggestions(String query, String limit) {
        if (ER_PATTERN.matcher(limit).matches()) {
            return this.eresourceSuggestionManager.getSuggestionsForTerm(limit, query);
        } else if ("er-mesh".equals(limit)) {
            SuggestionComparator comparator = new SuggestionComparator(query);
            TreeSet<Suggestion> suggestions = new TreeSet<Suggestion>(comparator);
            suggestions.addAll(this.eresourceSuggestionManager.getSuggestionsForTerm(query));
            suggestions.addAll(this.meshSuggestionManager.getSuggestionsForTerm(query));
            return suggestions;
        } else if ("ej-mesh".equals(limit)) {
            SuggestionComparator comparator = new SuggestionComparator(query);
            TreeSet<Suggestion> suggestions = new TreeSet<Suggestion>(comparator);
            suggestions.addAll(this.eresourceSuggestionManager.getSuggestionsForTerm("ej", query));
            suggestions.addAll(this.meshSuggestionManager.getSuggestionsForTerm(query));
            return suggestions;
        } else if ("mesh".equals(limit)) {
            return this.meshSuggestionManager.getSuggestionsForTerm(query);
        } else if (limit.indexOf("mesh-") == 0) {
            return this.meshSuggestionManager.getSuggestionsForTerm(limit.substring(5), query);
        } else if ("history".equals(limit)) {
            return this.historySuggestionManager.getSuggestionsForTerm(query);
        } else {
            return this.eresourceSuggestionManager.getSuggestionsForTerm(query);
        }
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
}
