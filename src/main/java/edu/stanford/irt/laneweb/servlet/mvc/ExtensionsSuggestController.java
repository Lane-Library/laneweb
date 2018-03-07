package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.suggest.SuggestionService;

@Controller
public class ExtensionsSuggestController {

    private SuggestionService suggestionService;

    public ExtensionsSuggestController(final SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @RequestMapping(value = "/eresources/extensions-suggest")
    @ResponseBody
    public List<Object> getSuggestions(@RequestParam final String q) {
        List<Object> result = new ArrayList<>();
        result.add(q);
        result.add(this.suggestionService.getSuggestions(q, "er-mesh"));
        return result;
    }
}
