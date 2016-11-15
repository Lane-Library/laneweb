package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.suggest.SuggestionComparator;
import edu.stanford.irt.suggest.Suggestion;
import edu.stanford.irt.suggest.SuggestionManager;

@Controller
public class SuggestionController {

    private static final Map<String, List<String>> EMPTY_SUGGESTIONS = Collections.singletonMap("suggest",
            Collections.emptyList());

    private static final Pattern ER_PATTERN = Pattern.compile("^(Book|Journal|Bassett)$");

    private static final Logger LOG = LoggerFactory.getLogger(SuggestionController.class);

    private static final int MAX_QUERY_LENGTH = 100;

    private static final String MESH_PREFIX = "mesh-";

    private static final int MESH_PREFIX_LENGTH = MESH_PREFIX.length();

    private static final int MIN_QUERY_LENGTH = 3;

    private static final Collection<Suggestion> NO_SUGGESTIONS = Collections.emptyList();

    private static final int RETURN_LIMIT = 10;

    private SuggestionManager eresourceSuggestionManager;

    private SuggestionManager meshSuggestionManager;

    @Autowired
    public SuggestionController(
            @Qualifier("edu.stanford.irt.suggest.SuggestionManager/eresource") final SuggestionManager eresourceSuggestionManager,
            @Qualifier("edu.stanford.irt.suggest.SuggestionManager/mesh") final SuggestionManager meshSuggestionManager) {
        this.eresourceSuggestionManager = eresourceSuggestionManager;
        this.meshSuggestionManager = meshSuggestionManager;
    }

    @RequestMapping(value = "/apps/suggest/getSuggestionList")
    @ResponseBody
    public List<String> getSuggestionList(@RequestParam final String q,
            @RequestParam(required = false) final String l) {
        String query = q.trim();
        Set<Suggestion> suggestions = new TreeSet<>(new SuggestionComparator(query));
        suggestions.addAll(internalGetSuggestions(query, l));
        List<String> strings = new ArrayList<>();
        for (Suggestion suggestion : suggestions) {
            strings.add(suggestion.getSuggestionTitle());
            if (strings.size() >= RETURN_LIMIT) {
                break;
            }
        }
        return strings;
    }

    @RequestMapping(value = "/apps/suggest/json")
    @ResponseBody
    public Map<String, List<String>> getSuggestions(@RequestParam final String q,
            @RequestParam(required = false) final String l) {
        return Collections.singletonMap("suggest", getSuggestionList(q, l));
    }

    /**
     * Handle situations where the SuggestionManager chokes on a String like so: java.lang.IllegalArgumentException:
     * cleaned query is less than 3, query:,ed, cleaned:ed
     *
     * @param ex
     *            the IllegalArgumentException
     * @return an empty map;
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public Map<String, List<String>> handleIllegalArgumentException(final IllegalArgumentException ex) {
        if (LOG.isWarnEnabled()) {
            LOG.warn(ex.getMessage(), ex);
        }
        return EMPTY_SUGGESTIONS;
    }

    private Collection<Suggestion> internalGetSuggestions(final String query, final String limit) {
        Collection<Suggestion> suggestions = null;
        int length = query.length();
        if (length >= MIN_QUERY_LENGTH && length <= MAX_QUERY_LENGTH) {
            // queries > 100 characters are likely to produce zero results
            // queries < 3 characters, will throw IllegalArgumentException
            if (limit == null) {
                suggestions = this.eresourceSuggestionManager.getSuggestionsForTerm(query);
            } else if ("er-mesh".equals(limit)) {
                suggestions = this.eresourceSuggestionManager.getSuggestionsForTerm(query);
                suggestions.addAll(this.meshSuggestionManager.getSuggestionsForTerm(query));
            } else if (limit.indexOf(MESH_PREFIX) == 0) {
                suggestions = this.meshSuggestionManager.getSuggestionsForTerm(limit.substring(MESH_PREFIX_LENGTH),
                        query);
            } else if (ER_PATTERN.matcher(limit).matches()) {
                suggestions = this.eresourceSuggestionManager.getSuggestionsForTerm(limit, query);
            } else if ("mesh".equals(limit)) {
                suggestions = this.meshSuggestionManager.getSuggestionsForTerm(query);
            } else if ("ej-mesh".equals(limit)) {
                suggestions = this.eresourceSuggestionManager.getSuggestionsForTerm("ej", query);
                suggestions.addAll(this.meshSuggestionManager.getSuggestionsForTerm(query));
            }
        }
        return suggestions == null ? NO_SUGGESTIONS : suggestions;
    }
}
