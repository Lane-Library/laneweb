package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.eresources.EresourceSearchService;
import edu.stanford.irt.spell.SpellCheckException;
import edu.stanford.irt.spell.SpellCheckResult;
import edu.stanford.irt.spell.SpellChecker;

public class SpellCheckControllerTest {

    private SpellCheckController controller;

    private EresourceSearchService searchService;

    private SpellChecker spellChecker;

    @Before
    public void setUp() {
        this.searchService = mock(EresourceSearchService.class);
        this.spellChecker = mock(SpellChecker.class);
        this.controller = new SpellCheckController(this.spellChecker, this.searchService);
    }

    @Test
    public void testCheckSpelling() {
        SpellCheckResult result = new SpellCheckResult("correct");
        expect(this.spellChecker.spellCheck("incorrect")).andReturn(result);
        Map<String, Integer> map = new HashMap<>();
        map.put("all", 100);
        expect(this.searchService.searchCount("correct")).andReturn(map);
        replay(this.spellChecker, this.searchService);
        SpellingResult spellResult = this.controller.checkSpelling("incorrect");
        assertEquals("correct", spellResult.getSuggestion());
        assertEquals(100, spellResult.getSuggestionResultCount());
        verify(this.spellChecker, this.searchService);
    }

    @Test
    public void testCheckSpellingNoresults() {
        SpellCheckResult result = new SpellCheckResult("corrected");
        expect(this.spellChecker.spellCheck("original")).andReturn(result);
        Map<String, Integer> map = new HashMap<>();
        map.put("all", 0);
        expect(this.searchService.searchCount("corrected")).andReturn(map);
        replay(this.spellChecker, this.searchService);
        SpellingResult spellResult = this.controller.checkSpelling("original");
        assertNull(spellResult.getSuggestion());
        verify(this.spellChecker, this.searchService);
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
