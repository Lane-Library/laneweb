package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.eresources.SolrService;
import edu.stanford.irt.spell.SpellCheckException;
import edu.stanford.irt.spell.SpellCheckResult;
import edu.stanford.irt.spell.SpellChecker;

public class SpellCheckControllerTest {

    private SpellCheckController controller;

    private SolrService solrService;

    private SpellChecker spellChecker;

    @Before
    public void setUp() {
        this.solrService = mock(SolrService.class);
        this.spellChecker = mock(SpellChecker.class);
        this.controller = new SpellCheckController(this.spellChecker, this.solrService);
    }

    @Test
    public void testCheckSpelling() {
        SpellCheckResult result = new SpellCheckResult("correct");
        expect(this.spellChecker.spellCheck("incorrect")).andReturn(result);
        Map<String, Long> map = new HashMap<>();
        map.put("all", 100L);
        expect(this.solrService.searchCount("correct")).andReturn(map);
        replay(this.spellChecker, this.solrService);
        SpellingResult spellResult = this.controller.checkSpelling("incorrect");
        assertEquals("correct", spellResult.getSuggestion());
        verify(this.spellChecker, this.solrService);
    }

    @Test
    public void testOtherException() {
        assertEquals("spell check failed",
                this.controller.spellCheckException(new SpellCheckException(new Exception("oopsie"))));
    }

    @Test
    public void testSocketTimeoutException() {
        assertEquals("spell check failed",
                this.controller.spellCheckException(new SpellCheckException(new SocketTimeoutException("oopsie"))));
    }

    @Test
    public void testSpellCheckException() {
        assertEquals("spell check failed", this.controller.spellCheckException(new SpellCheckException("oopsie")));
    }
}
