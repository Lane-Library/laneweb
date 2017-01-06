package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.suggest.Suggestion;
import edu.stanford.irt.suggest.SuggestionManager;

public class StatusControllerTest {

    private StatusController controller;

    private HttpServletRequest request;

    private SitemapController requestHandler;

    private HttpServletResponse response;

    private SuggestionManager suggestionManager;

    @Before
    public void setUp() {
        this.suggestionManager = createMock(SuggestionManager.class);
        this.requestHandler = createMock(SitemapController.class);
        this.controller = new StatusController(this.requestHandler, this.suggestionManager, "version");
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
    }

    @Test
    public void testGetStatusERROR() throws IOException {
        expect(this.suggestionManager.getSuggestionsForTerm("cardio")).andThrow(new LanewebException("oops"));
        this.requestHandler.handleRequest(isA(HttpServletRequest.class), isA(HttpServletResponse.class));
        expectLastCall().andThrow(new IOException());
        replay(this.request, this.response, this.suggestionManager, this.requestHandler);
        String status = this.controller.getStatus(this.request, this.response);
        assertSame(16, status.indexOf("[ERROR] suggestions"));
        assertSame(0, status.indexOf("laneweb-version"));
        assertTrue(status.indexOf("[ERROR] index") > 0);
        verify(this.request, this.response, this.suggestionManager, this.requestHandler);
    }

    @Test
    public void testGetStatusOK() throws IOException {
        expect(this.suggestionManager.getSuggestionsForTerm("cardio")).andReturn(null);
        this.requestHandler.handleRequest(isA(HttpServletRequest.class), isA(HttpServletResponse.class));
        replay(this.request, this.response, this.suggestionManager, this.requestHandler);
        String status = this.controller.getStatus(this.request, this.response);
        assertSame(0, status.indexOf("laneweb-version"));
        assertSame(16, status.indexOf("[OK] suggestions"));
        assertTrue(status.indexOf("[OK] index") > 0);
        verify(this.request, this.response, this.suggestionManager, this.requestHandler);
    }

    @Test
    public void testGetStatusWarning() throws IOException {
        expect(this.suggestionManager.getSuggestionsForTerm("cardio")).andAnswer(new IAnswer<Collection<Suggestion>>() {

            @Override
            public Collection<Suggestion> answer() throws InterruptedException {
                Thread.sleep(300);
                return null;
            }
        });
        this.requestHandler.handleRequest(isA(HttpServletRequest.class), isA(HttpServletResponse.class));
        expectLastCall().andAnswer(new IAnswer<Void>() {

            @Override
            public Void answer() throws InterruptedException {
                Thread.sleep(300);
                return null;
            }
        });
        replay(this.request, this.response, this.suggestionManager, this.requestHandler);
        String status = this.controller.getStatus(this.request, this.response);
        assertSame(0, status.indexOf("laneweb-version"));
        assertSame(16, status.indexOf("[WARN] suggestions"));
        assertTrue(status.indexOf("[WARN] index") > 0);
        verify(this.request, this.response, this.suggestionManager, this.requestHandler);
    }
}
