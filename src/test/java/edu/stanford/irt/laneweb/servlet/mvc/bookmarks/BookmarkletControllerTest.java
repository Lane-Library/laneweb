package edu.stanford.irt.laneweb.servlet.mvc.bookmarks;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;

import edu.stanford.irt.laneweb.bookmarks.Bookmark;
import edu.stanford.irt.laneweb.bookmarks.BookmarkDAO;
import edu.stanford.irt.laneweb.servlet.binding.BookmarkDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;

public class BookmarkletControllerTest {

    private BookmarkDataBinder bookmarkBinder;

    private List<Bookmark> bookmarks;

    private BookmarkletController controller;

    private BookmarkDAO dao;

    private Model model;

    private HttpServletRequest request;

    private HttpSession session;

    private UserDataBinder userBinder;

    @Before
    public void setUp() {
        this.userBinder = createMock(UserDataBinder.class);
        this.dao = createMock(BookmarkDAO.class);
        this.bookmarkBinder = createMock(BookmarkDataBinder.class);
        this.controller = new BookmarkletController(this.dao, this.bookmarkBinder, this.userBinder);
        this.request = createMock(HttpServletRequest.class);
        this.model = createMock(Model.class);
        this.session = createMock(HttpSession.class);
        this.bookmarks = new ArrayList<>();
    }

    @Test
    public void testAddBookmark() throws UnsupportedEncodingException {
        this.dao.saveLinks(edu.stanford.irt.laneweb.model.Model.USER_ID, this.bookmarks);
        this.session.setAttribute("bookmarks", this.bookmarks);
        replay(this.dao, this.userBinder, this.bookmarkBinder, this.session);
        assertEquals("redirect:url", this.controller.addBookmark(null, this.bookmarks,
                edu.stanford.irt.laneweb.model.Model.USER_ID, "url", "label", this.session));
        verify(this.dao, this.userBinder, this.bookmarkBinder, this.session);
    }

    @Test
    public void testAddBookmarkNullUserId() throws UnsupportedEncodingException {
        replay(this.dao, this.userBinder, this.bookmarkBinder, this.session);
        assertEquals("redirect:/secure/bookmarklet?url=url&label=label",
                this.controller.addBookmark(null, this.bookmarks, null, "url", "label", this.session));
        verify(this.dao, this.userBinder, this.bookmarkBinder, this.session);
    }

    @Test
    public void testBind() {
        Map<String, Object> map = new HashMap<>();
        expect(this.model.asMap()).andReturn(map).times(2);
        expect(this.model.containsAttribute(edu.stanford.irt.laneweb.model.Model.BOOKMARKS)).andReturn(true);
        expect(this.model.containsAttribute(edu.stanford.irt.laneweb.model.Model.USER_ID)).andReturn(true);
        replay(this.request, this.model);
        this.controller.bind(this.request, this.model);
        verify(this.request, this.model);
    }

    @Test
    public void testBindNoUserId() {
        Map<String, Object> map = new HashMap<>();
        expect(this.model.asMap()).andReturn(map).times(2);
        expect(this.model.containsAttribute(edu.stanford.irt.laneweb.model.Model.BOOKMARKS)).andReturn(true);
        expect(this.model.containsAttribute(edu.stanford.irt.laneweb.model.Model.USER_ID)).andReturn(false);
        expect(this.model.addAttribute(edu.stanford.irt.laneweb.model.Model.USER_ID, null)).andReturn(this.model);
        expect(this.model.addAttribute(edu.stanford.irt.laneweb.model.Model.BOOKMARKS, null)).andReturn(this.model);
        replay(this.request, this.model);
        this.controller.bind(this.request, this.model);
        verify(this.request, this.model);
    }
}
