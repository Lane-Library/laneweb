package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.suggest.Suggestion;
import edu.stanford.irt.suggest.SuggestionManager;

@Controller
public class ExtensionsSuggestController {

    private static final int RETURN_LIMIT = 10;

    private SuggestionManager suggestionManager;

    @Autowired
    public ExtensionsSuggestController(
            @Qualifier("edu.stanford.irt.suggest.SuggestionManager/extensions-suggest") final SuggestionManager suggestionManager) {
        this.suggestionManager = suggestionManager;
    }

    @RequestMapping(value = "/eresources/extensions-suggest")
    @ResponseBody
    public List<Object> getSuggestions(@RequestParam final String q) {
        String query = q;
        Collection<String> suggestions = new ArrayList<>();
        List<Object> objectList = new ArrayList<>();
        for (Suggestion suggestion : this.suggestionManager.getSuggestionsForTerm(query)) {
            suggestions.add(suggestion.getSuggestionTitle());
            if (suggestions.size() >= RETURN_LIMIT) {
                break;
            }
        }
        objectList.add(query);
        objectList.add(suggestions);
        return objectList;
    }
}
