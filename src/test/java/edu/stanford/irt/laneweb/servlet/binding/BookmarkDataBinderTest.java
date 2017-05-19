package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.bookmarks.Bookmark;
import edu.stanford.irt.laneweb.bookmarks.BookmarkService;
import edu.stanford.irt.laneweb.model.Model;

public class BookmarkDataBinderTest {

    private BookmarkDataBinder binder;

    private List<Bookmark> bookmarks;

    private BookmarkService dao;

    private Map<String, Object> model;

    private HttpServletRequest request;

    private HttpSession session;

    @Before
    public void setUp() throws Exception {
        this.binder = new BookmarkDataBinder();
        this.model = new HashMap<>();
        this.model.put(Model.USER_ID, "ditenus");
        this.bookmarks = Collections.emptyList();
        this.dao = createMock(BookmarkService.class);
        this.binder.setBookmarkDAO(this.dao);
        this.request = createMock(HttpServletRequest.class);
        this.session = createMock(HttpSession.class);
    }

    @Test
    public void testInSession() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.BOOKMARKS)).andReturn(this.bookmarks);
        replay(this.dao, this.request, this.session);
        this.binder.bind(this.model, this.request);
        assertEquals(this.bookmarks, this.model.get(Model.BOOKMARKS));
        verify(this.dao, this.request, this.session);
    }

    @Test
    public void testNotInDAO() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.BOOKMARKS)).andReturn(null);
        expect(this.dao.getLinks("ditenus")).andReturn(null);
        this.session.setAttribute(Model.BOOKMARKS, this.bookmarks);
        replay(this.dao, this.request, this.session);
        this.binder.bind(this.model, this.request);
        assertEquals(this.bookmarks, this.model.get(Model.BOOKMARKS));
        assertNotSame(this.bookmarks, this.model.get(Model.BOOKMARKS));
        verify(this.dao, this.request, this.session);
    }

    @Test
    public void testNotInSession() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.BOOKMARKS)).andReturn(null);
        expect(this.dao.getLinks("ditenus")).andReturn(this.bookmarks);
        this.session.setAttribute(Model.BOOKMARKS, this.bookmarks);
        replay(this.dao, this.request, this.session);
        this.binder.bind(this.model, this.request);
        assertEquals(this.bookmarks, this.model.get(Model.BOOKMARKS));
        verify(this.dao, this.request, this.session);
    }

    @Test
    public void testNoUserid() {
        this.model = new HashMap<>();
        this.binder.bind(this.model, this.request);
        assertFalse(this.model.containsKey(Model.BOOKMARKS));
    }
}
