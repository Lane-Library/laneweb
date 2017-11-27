package edu.stanford.irt.laneweb.suggest;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.strictMock;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.status.Status;
import edu.stanford.irt.status.StatusItem;
import edu.stanford.irt.suggest.SuggestionManager;

public class SuggestStatusProviderTest {

    private SuggestStatusProvider statusProvider;

    private SuggestionManager suggestionManager;

    @Before
    public void setUp() {
        this.suggestionManager = strictMock(SuggestionManager.class);
        this.statusProvider = new SuggestStatusProvider(this.suggestionManager, 250, "term");
    }

    @Test
    public void testGetStatuExceptions() {
        expect(this.suggestionManager.getSuggestionsForTerm("term")).andThrow(new RuntimeException("oopsie"));
        replay(this.suggestionManager);
        StatusItem item = this.statusProvider.getStatusItems().get(0);
        assertSame(Status.ERROR, item.getStatus());
        assertTrue(Pattern.compile("suggestion status failed in \\dms: java.lang.RuntimeException: oopsie")
                .matcher(item.getMessage()).matches());
        verify(this.suggestionManager);
    }

    @Test
    public void testGetStatus() {
        expect(this.suggestionManager.getSuggestionsForTerm("term")).andReturn(Collections.emptySet());
        replay(this.suggestionManager);
        StatusItem item = this.statusProvider.getStatusItems().get(0);
        assertSame(Status.OK, item.getStatus());
        assertTrue(Pattern.compile("suggestions took \\dms.").matcher(item.getMessage()).matches());
        verify(this.suggestionManager);
    }

    @Test
    public void testGetStatusWarn() {
        expect(this.suggestionManager.getSuggestionsForTerm("term")).andReturn(Collections.emptySet());
        replay(this.suggestionManager);
        SuggestStatusProvider provider = new SuggestStatusProvider(this.suggestionManager, -1, "term");
        StatusItem item = provider.getStatusItems().get(0);
        assertSame(Status.WARN, item.getStatus());
        assertTrue(Pattern.compile("suggestions took \\dms.").matcher(item.getMessage()).matches());
        verify(this.suggestionManager);
    }
}
