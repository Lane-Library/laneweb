package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.net.SocketTimeoutException;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.spell.SpellCheckException;
import edu.stanford.irt.spell.SpellCheckResult;
import edu.stanford.irt.spell.SpellChecker;

public class SpellCheckControllerTest {

    private SpellCheckController controller;

    private SpellChecker spellChecker;

    @Before
    public void setUp() {
        this.spellChecker = createMock(SpellChecker.class);
        this.controller = new SpellCheckController(this.spellChecker);
    }

    @Test
    public void testCheckSpelling() {
        SpellCheckResult result = new SpellCheckResult("correct");
        expect(this.spellChecker.spellCheck("incorrect")).andReturn(result);
        replay(this.spellChecker);
        assertSame(result, this.controller.checkSpelling("incorrect"));
        verify(this.spellChecker);
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
