package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import edu.stanford.irt.laneweb.suggest.SuggestionService;

public class SuggestionControllerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private SuggestionController controller;

    private SuggestionService service;

    @Before
    public void setUp() {
        this.service = mock(SuggestionService.class);
        this.controller = new SuggestionController(this.service);
    }

    @Test
    public void testGetSuggestionList() {
        expect(this.service.getSuggestions(null, "term")).andReturn(Collections.singleton("suggestion"));
        replay(this.service);
        assertEquals("suggestion", this.controller.getSuggestionList(null, "term").stream().findFirst().orElse(null));
        verify(this.service);
    }

    @Test
    public void testGetSuggestions() {
        expect(this.service.getSuggestions(null, "term")).andReturn(Collections.singleton("suggestion"));
        replay(this.service);
        assertEquals("suggestion",
                this.controller.getSuggestions(null, "term").get("suggest").stream().findFirst().orElse(null));
        verify(this.service);
    }
}
