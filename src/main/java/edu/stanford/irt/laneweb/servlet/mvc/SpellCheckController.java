package edu.stanford.irt.laneweb.servlet.mvc;

import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @Autowired
    public SpellCheckController(final SpellChecker spellChecker) {
        this.spellChecker = spellChecker;
    }

    @RequestMapping(value = "/apps/spellcheck/json")
    @ResponseBody
    public SpellCheckResult checkSpelling(@RequestParam final String q) {
        return this.spellChecker.spellCheck(q);
    }

    @ResponseStatus(value = HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(SpellCheckException.class)
    @ResponseBody
    public String spellCheckException(final SpellCheckException e) {
        Throwable t = e.getCause();
        if (t instanceof SocketTimeoutException) {
            log.warn(e.toString(), e);
        } else {
            log.error(e.toString(), e);
        }
        return "spell check failed";
    }
}
