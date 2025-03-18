package edu.stanford.irt.laneweb.suggest;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.strictMock;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.status.Status;
import edu.stanford.irt.status.StatusItem;

public class SuggestStatusProviderTest {

    private SuggestStatusProvider statusProvider;

    private SuggestionService suggestionService;

    @BeforeEach
    public void setUp() {
        this.suggestionService = strictMock(SuggestionService.class);
        this.statusProvider = new SuggestStatusProvider(this.suggestionService, 250, "term");
    }

    @Test
    public void testGetStatuExceptions() {
        expect(this.suggestionService.getSuggestions("term", null)).andThrow(new RuntimeException("oopsie"));
        replay(this.suggestionService);
        StatusItem item = this.statusProvider.getStatusItems().get(0);
        assertSame(Status.ERROR, item.getStatus());
        assertTrue(Pattern.compile("suggestion status failed in \\dms: java.lang.RuntimeException: oopsie")
                .matcher(item.getMessage()).matches());
        verify(this.suggestionService);
    }

    @Test
    public void testGetStatus() {
        expect(this.suggestionService.getSuggestions("term", null)).andReturn(Collections.emptySet());
        replay(this.suggestionService);
        StatusItem item = this.statusProvider.getStatusItems().get(0);
        assertSame(Status.OK, item.getStatus());
        assertTrue(Pattern.compile("suggestions took \\dms.").matcher(item.getMessage()).matches());
        verify(this.suggestionService);
    }

    @Test
    public void testGetStatusWarn() {
        expect(this.suggestionService.getSuggestions("term", null)).andReturn(Collections.emptySet());
        replay(this.suggestionService);
        SuggestStatusProvider provider = new SuggestStatusProvider(this.suggestionService, -1, "term");
        StatusItem item = provider.getStatusItems().get(0);
        assertSame(Status.WARN, item.getStatus());
        assertTrue(Pattern.compile("suggestions took \\dms.").matcher(item.getMessage()).matches());
        verify(this.suggestionService);
    }
}
