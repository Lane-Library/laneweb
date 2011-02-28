package edu.stanford.irt.laneweb.servlet.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.spell.SpellCheckResult;
import edu.stanford.irt.spell.SpellChecker;

@Controller
public class SpellCheckController {
    
    @Autowired
    private SpellChecker spellChecker;
    
    @RequestMapping(value = "**/apps/spellcheck/json")
    @ResponseBody
    public SpellCheckResult checkSpelling(@RequestParam String q) {
        return this.spellChecker.spellCheck(q);
    }
}
