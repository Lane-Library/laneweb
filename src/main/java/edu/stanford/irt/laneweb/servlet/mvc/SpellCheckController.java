package edu.stanford.irt.laneweb.servlet.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import edu.stanford.irt.laneweb.eresources.EresourceSearchService;
import edu.stanford.irt.spell.SpellCheckException;
import edu.stanford.irt.spell.SpellChecker;

@Controller
public class SpellCheckController {

    private static final Logger log = LoggerFactory.getLogger(SpellCheckController.class);

    private EresourceSearchService service;

    private SpellChecker spellChecker;

    public SpellCheckController(final SpellChecker spellChecker, final EresourceSearchService service) {
        this.service = service;
        this.spellChecker = spellChecker;
    }

    @GetMapping(value = "/apps/spellcheck/json")
    @ResponseBody
    public SpellingResult checkSpelling(@RequestParam final String q) {
        // only return a suggestion if search would return results for it
        SpellingResult suggestion = new SpellingResult(this.spellChecker.spellCheck(q).getSuggestion());
        String correction = suggestion.getSuggestion();
        if (null != correction && !correction.isBlank()) {
            int count = this.service.searchCount(correction).get("all").intValue();
            if (count > 0) {
                suggestion.suggestionResultCount = count;
                return suggestion;
            }
        }
        return SpellingResult.NULL_SPELLING_RESULT;
    }

    @ResponseStatus(value = HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(SpellCheckException.class)
    @ResponseBody
    public String spellCheckException(final SpellCheckException e) {
        log.error("spell check failed: {}", e.getMessage());
        return "spell check failed";
    }
}
