package edu.stanford.irt.laneweb.servlet.mvc.bookmarks;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;

import edu.stanford.irt.laneweb.bookmarks.Bookmark;
import edu.stanford.irt.laneweb.bookmarks.BookmarkService;
import edu.stanford.irt.laneweb.servlet.binding.BookmarkDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;

public class BookmarkletControllerTest {

    private BookmarkDataBinder bookmarkBinder;

    private List<Bookmark> bookmarks;

    private BookmarkletController controller;

    private Model model;

    private HttpServletRequest request;

    private BookmarkService service;

    private HttpSession session;

    private UserDataBinder userBinder;

    @Before
    public void setUp() {
        this.userBinder = mock(UserDataBinder.class);
        this.service = mock(BookmarkService.class);
        this.bookmarkBinder = mock(BookmarkDataBinder.class);
        this.controller = new BookmarkletController(this.service, this.bookmarkBinder, this.userBinder);
        this.request = mock(HttpServletRequest.class);
        this.model = mock(Model.class);
        this.session = mock(HttpSession.class);
        this.bookmarks = new ArrayList<>();
    }

    @Test
    public void testAddBookmark() throws UnsupportedEncodingException {
        this.service.saveLinks(edu.stanford.irt.laneweb.model.Model.USER_ID, this.bookmarks);
        this.session.setAttribute("bookmarks", this.bookmarks);
        replay(this.service, this.userBinder, this.bookmarkBinder, this.session);
        assertEquals("redirect:url", this.controller.addBookmark(null, this.bookmarks,
                edu.stanford.irt.laneweb.model.Model.USER_ID, "url", "label", this.session));
        verify(this.service, this.userBinder, this.bookmarkBinder, this.session);
    }

    @Test
    public void testAddBookmarkNullUserId() throws UnsupportedEncodingException {
        replay(this.service, this.userBinder, this.bookmarkBinder, this.session);
        assertEquals("redirect:/secure/bookmarklet?url=url&label=label",
                this.controller.addBookmark(null, this.bookmarks, null, "url", "label", this.session));
        verify(this.service, this.userBinder, this.bookmarkBinder, this.session);
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
