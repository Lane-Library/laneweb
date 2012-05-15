package edu.stanford.irt.laneweb.servlet.mvc;

import edu.stanford.irt.laneweb.suggest.SuggestionComparator;
import edu.stanford.irt.suggest.Suggestion;
import edu.stanford.irt.suggest.SuggestionManager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SuggestionController {

    private static final Pattern ER_PATTERN = Pattern
            .compile("(?:ej|book|database|software|cc|video|lanesite|bassett)");

    private static final Collection<Suggestion> NO_SUGGESTIONS = Collections.emptyList();

    private static final int RETURN_LIMIT = 10;

    @Resource(name = "edu.stanford.irt.suggest.SuggestionManager/eresource")
    private SuggestionManager eresourceSuggestionManager;

    @Resource(name = "edu.stanford.irt.suggest.SuggestionManager/history")
    private SuggestionManager historySuggestionManager;

    @Resource(name = "edu.stanford.irt.suggest.SuggestionManager/mesh")
    private SuggestionManager meshSuggestionManager;

    @RequestMapping(value = "/**/apps/suggest/json")
    @ResponseBody
    public Map<String, List<String>> getSuggestions(@RequestParam final String q,
            @RequestParam(required = false) final String l) {
        String query = q.trim();
        TreeSet<Suggestion> suggestions = new TreeSet<Suggestion>(new SuggestionComparator(query));
        suggestions.addAll(internalGetSuggestions(query, l));
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        List<String> strings = new LinkedList<String>();
        for (Suggestion suggestion : suggestions) {
            strings.add(suggestion.getSuggestionTitle());
            if (strings.size() >= RETURN_LIMIT) {
                break;
            }
        }
        map.put("suggest", strings);
        return map;
    }

    private Collection<Suggestion> internalGetSuggestions(final String query, final String limit) {
        if (query.length() > 32) {
            // return an empty list for queries > 32 characters, liable to cause
            // SQLExceptions
            return NO_SUGGESTIONS;
        }
        if (limit != null && ER_PATTERN.matcher(limit).matches()) {
            return this.eresourceSuggestionManager.getSuggestionsForTerm(limit, query);
        } else if ("er-mesh".equals(limit)) {
            List<Suggestion> suggestions = new LinkedList<Suggestion>();
            suggestions.addAll(this.eresourceSuggestionManager.getSuggestionsForTerm(query));
            suggestions.addAll(this.meshSuggestionManager.getSuggestionsForTerm(query));
            return suggestions;
        } else if ("ej-mesh".equals(limit)) {
            List<Suggestion> suggestions = new LinkedList<Suggestion>();
            suggestions.addAll(this.eresourceSuggestionManager.getSuggestionsForTerm("ej", query));
            suggestions.addAll(this.meshSuggestionManager.getSuggestionsForTerm(query));
            return suggestions;
        } else if ("mesh".equals(limit)) {
            return this.meshSuggestionManager.getSuggestionsForTerm(query);
        } else if (limit != null && limit.indexOf("mesh-") == 0) {
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
}
