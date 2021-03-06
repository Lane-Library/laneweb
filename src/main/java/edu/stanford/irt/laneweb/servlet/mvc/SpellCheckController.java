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

import edu.stanford.irt.spell.SpellCheckException;
import edu.stanford.irt.spell.SpellCheckResult;
import edu.stanford.irt.spell.SpellChecker;

@Controller
public class SpellCheckController {

    private static final Logger log = LoggerFactory.getLogger(SpellCheckController.class);

    private SpellChecker spellChecker;

    public SpellCheckController(final SpellChecker spellChecker) {
        this.spellChecker = spellChecker;
    }

    @GetMapping(value = "/apps/spellcheck/json")
    @ResponseBody
    public SpellCheckResult checkSpelling(@RequestParam final String q) {
        return this.spellChecker.spellCheck(q);
    }

    @ResponseStatus(value = HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(SpellCheckException.class)
    @ResponseBody
    public String spellCheckException(final SpellCheckException e) {
        log.error("spell check failed: {}", e.getMessage());
        return "spell check failed";
    }
}
