package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.suggest.SuggestionService;

@Controller
public class SuggestionController {

    private SuggestionService suggestionService;

    @Autowired
    public SuggestionController(final SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @RequestMapping(value = "/apps/suggest/getSuggestionList")
    @ResponseBody
    public Collection<String> getSuggestionList(@RequestParam final String q,
            @RequestParam(required = false) final String l) {
        return this.suggestionService.getSuggestions(q, l);
    }

    @RequestMapping(value = "/apps/suggest/json")
    @ResponseBody
    public Map<String, Collection<String>> getSuggestions(@RequestParam final String q,
            @RequestParam(required = false) final String l) {
        return Collections.singletonMap("suggest", getSuggestionList(q, l));
    }
}
