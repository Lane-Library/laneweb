package edu.stanford.irt.laneweb.suggest;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.suggest.SuggestionManager;

public class CompositeSuggestionManagerTest {

    private SuggestionManager childManager;

    private CompositeSuggestionManager manager;

    @Before
    public void setUp() throws Exception {
        this.childManager = mock(SuggestionManager.class);
        this.manager = new CompositeSuggestionManager(Collections.singletonList(this.childManager));
    }

    @Test
    public void testGetSuggestionsForTermShort() {
        replay(this.childManager);
        assertNotNull(this.manager.getSuggestionsForTerm("te"));
        verify(this.childManager);
    }

    @Test
    public void testGetSuggestionsForTermString() {
        expect(this.childManager.getSuggestionsForTerm("term")).andReturn(Collections.emptySet());
        replay(this.childManager);
        assertNotNull(this.manager.getSuggestionsForTerm("term"));
        verify(this.childManager);
    }

    @Test
    public void testGetSuggestionsForTermStringString() {
        expect(this.childManager.getSuggestionsForTerm("type", "term")).andReturn(Collections.emptySet());
        replay(this.childManager);
        assertNotNull(this.manager.getSuggestionsForTerm("type", "term"));
        verify(this.childManager);
    }
}
