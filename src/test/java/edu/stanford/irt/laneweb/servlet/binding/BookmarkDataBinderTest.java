package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.bookmarks.Bookmark;
import edu.stanford.irt.laneweb.bookmarks.BookmarkException;
import edu.stanford.irt.laneweb.bookmarks.BookmarkService;
import edu.stanford.irt.laneweb.model.Model;

public class BookmarkDataBinderTest {

    private BookmarkDataBinder binder;

    private List<Bookmark> bookmarks;

    private BookmarkService bookmarkService;

    private Map<String, Object> model;

    private HttpServletRequest request;

    private HttpSession session;

    @Before
    public void setUp() throws Exception {
        this.bookmarkService = mock(BookmarkService.class);
        this.binder = new BookmarkDataBinder(this.bookmarkService);
        this.model = new HashMap<>();
        this.model.put(Model.USER_ID, "ditenus");
        this.bookmarks = Collections.emptyList();
        this.request = mock(HttpServletRequest.class);
        this.session = mock(HttpSession.class);
    }

    @Test
    public void testBookmarkException() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.BOOKMARKS)).andReturn(null);
        expect(this.bookmarkService.getLinks("ditenus")).andThrow(new BookmarkException(null));
        replay(this.bookmarkService, this.request, this.session);
        this.binder.bind(this.model, this.request);
        assertNull(this.model.get(Model.BOOKMARKS));
        assertEquals("off", this.model.get(Model.BOOKMARKING));
        verify(this.bookmarkService, this.request, this.session);
    }

    @Test
    public void testInSession() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.BOOKMARKS)).andReturn(this.bookmarks);
        replay(this.bookmarkService, this.request, this.session);
        this.binder.bind(this.model, this.request);
        assertEquals(this.bookmarks, this.model.get(Model.BOOKMARKS));
        verify(this.bookmarkService, this.request, this.session);
    }

    @Test
    public void testNotFromService() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.BOOKMARKS)).andReturn(null);
        expect(this.bookmarkService.getLinks("ditenus")).andReturn(null);
        this.session.setAttribute(Model.BOOKMARKS, this.bookmarks);
        replay(this.bookmarkService, this.request, this.session);
        this.binder.bind(this.model, this.request);
        assertEquals(this.bookmarks, this.model.get(Model.BOOKMARKS));
        assertNotSame(this.bookmarks, this.model.get(Model.BOOKMARKS));
        verify(this.bookmarkService, this.request, this.session);
    }

    @Test
    public void testNotInSession() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.BOOKMARKS)).andReturn(null);
        expect(this.bookmarkService.getLinks("ditenus")).andReturn(this.bookmarks);
        this.session.setAttribute(Model.BOOKMARKS, this.bookmarks);
        replay(this.bookmarkService, this.request, this.session);
        this.binder.bind(this.model, this.request);
        assertEquals(this.bookmarks, this.model.get(Model.BOOKMARKS));
        verify(this.bookmarkService, this.request, this.session);
    }

    @Test
    public void testNoUserid() {
        this.model = new HashMap<>();
        this.binder.bind(this.model, this.request);
        assertFalse(this.model.containsKey(Model.BOOKMARKS));
    }
}
