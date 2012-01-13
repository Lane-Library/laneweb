package edu.stanford.irt.laneweb.bookmarks;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

public class BookmarkControllerTest {

    private Bookmark bookmark;

    private List<Bookmark> bookmarks;

    private BookmarkController controller;

    private BookmarkDAO<Bookmark> dao;

    private RequestDispatcher dispatcher;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private HttpSession session;

    private String sunetid;

    @Before
    public void setUp() throws Exception {
        this.controller = new BookmarkController();
        this.bookmark = new Bookmark("label", "url");
        this.bookmarks = new ArrayList<Bookmark>();
        this.sunetid = "ditenus";
        this.dao = createMock(BookmarkDAO.class);
        this.session = createMock(HttpSession.class);
        this.response = createMock(HttpServletResponse.class);
        this.request = createMock(HttpServletRequest.class);
        this.dispatcher = createMock(RequestDispatcher.class);
        this.controller.setBookmarkDAO(this.dao);
    }

    @Test
    public void testGetBookmarksInSession() {
        expect(this.session.getAttribute(Model.BOOKMARKS)).andReturn(this.bookmarks);
        replay(this.session, this.dao);
        assertEquals(this.bookmarks, this.controller.getBookmarks(this.session, this.sunetid));
        verify(this.session, this.dao);
    }

    @Test
    public void testGetBookmarksNotInDAO() {
        expect(this.session.getAttribute(Model.BOOKMARKS)).andReturn(null);
        expect(this.dao.getLinks(this.sunetid)).andReturn(null);
        this.session.setAttribute(eq(Model.BOOKMARKS), isA(List.class));
        replay(this.session, this.dao);
        assertEquals(0, this.controller.getBookmarks(this.session, this.sunetid).size());
        verify(this.session, this.dao);
    }

    @Test
    public void testGetBookmarksNotInSession() {
        expect(this.session.getAttribute(Model.BOOKMARKS)).andReturn(null);
        expect(this.dao.getLinks(this.sunetid)).andReturn(this.bookmarks);
        this.session.setAttribute(Model.BOOKMARKS, this.bookmarks);
        replay(this.session, this.dao);
        assertEquals(this.bookmarks, this.controller.getBookmarks(this.session, this.sunetid));
        verify(this.session, this.dao);
    }

    @Test
    public void testHandleAddBookmark() throws ServletException, IOException {
        this.dao.saveLinks(this.sunetid, this.bookmarks);
        expect(this.request.getRequestDispatcher("/samples/favorites.html?action=edit&i=0")).andReturn(this.dispatcher);
        this.dispatcher.forward(this.request, this.response);
        replay(this.request, this.response, this.dispatcher, this.dao);
        this.controller.handleAddBookmark(this.request, this.response, this.bookmarks, this.sunetid);
        verify(this.request, this.response, this.dispatcher, this.dao);
    }

    @Test
    public void testHandleDeleteBookmarks() throws ServletException, IOException {
        this.bookmarks.add(this.bookmark);
        this.bookmarks.add(this.bookmark);
        this.dao.saveLinks(this.sunetid, this.bookmarks);
        expect(this.request.getRequestDispatcher("/samples/favorites.html")).andReturn(this.dispatcher);
        this.dispatcher.forward(this.request, this.response);
        replay(this.request, this.response, this.dispatcher, this.dao);
        this.controller.handleDeleteBookmarks(this.request, this.response, this.bookmarks, this.sunetid, new int[] { 0 });
        assertEquals(1, this.bookmarks.size());
        verify(this.request, this.response, this.dispatcher, this.dao);
    }

    @Test
    public void testHandleEditBookmark() throws ServletException, IOException {
        expect(this.request.getRequestDispatcher("/samples/favorites.html")).andReturn(this.dispatcher);
        this.dispatcher.forward(this.request, this.response);
        replay(this.request, this.response, this.dispatcher, this.dao);
        this.controller.handleEditBookmark(this.request, this.response);
        verify(this.request, this.response, this.dispatcher, this.dao);
    }

    @Test
    public void testHandleSaveBookmark() throws ServletException, IOException {
        this.bookmarks.add(this.bookmark);
        this.dao.saveLinks(this.sunetid, this.bookmarks);
        expect(this.request.getRequestDispatcher("/samples/favorites.html")).andReturn(this.dispatcher);
        this.dispatcher.forward(this.request, this.response);
        replay(this.request, this.response, this.dispatcher, this.dao);
        this.controller.handleSaveBookmark(this.request, this.response, this.bookmarks, this.sunetid, 0, "newlabel", "newurl");
        assertEquals("newlabel", this.bookmarks.get(0).getLabel());
        verify(this.request, this.response, this.dispatcher, this.dao);
    }
}
