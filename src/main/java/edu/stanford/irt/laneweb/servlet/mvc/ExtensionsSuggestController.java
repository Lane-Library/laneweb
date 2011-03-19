package edu.stanford.irt.laneweb.servlet.mvc;

import edu.stanford.irt.suggest.Suggestion;
import edu.stanford.irt.suggest.SuggestionManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ExtensionsSuggestController {

    private static final int RETURN_LIMIT = 10;

    @Resource(name = "edu.stanford.irt.suggest.SuggestionManager/extensions-suggest")
    private SuggestionManager suggestionManager;

    @RequestMapping(value = "/eresources/extensions-suggest")
    @ResponseBody
    public List<Object> getSuggestions(@RequestParam final String q) {
        String query = q;
        Collection<String> suggestions = new LinkedList<String>();
        List<Object> objectList = new ArrayList<Object>();
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
