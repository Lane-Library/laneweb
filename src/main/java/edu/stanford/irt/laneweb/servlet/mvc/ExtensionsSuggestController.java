package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.stanford.irt.suggest.Suggestion;
import edu.stanford.irt.suggest.SuggestionManager;

@Controller
public class ExtensionsSuggestController {

    @Resource(name = "edu.stanford.irt.suggest.SuggestionManager/extensions-suggest")
    private SuggestionManager suggestionManager;

    @RequestMapping(value = "/eresources/extensions-suggest")
    public Collection<Suggestion> getSuggestions(@RequestParam String q) {
        String query = q;
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
        return this.suggestionManager.getSuggestionsForTerm(query);
    }
}
