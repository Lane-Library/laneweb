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

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;

import edu.stanford.irt.laneweb.bookmarks.BookmarkDAO;
import edu.stanford.irt.laneweb.servlet.binding.BookmarkDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.SunetIdAndTicketDataBinder;

public class BookmarkletControllerTest {

    private BookmarkDataBinder bookmarkBinder;

    private List<Object> bookmarks;

    private BookmarkletController controller;

    private BookmarkDAO dao;

    private Model model;

    private HttpServletRequest request;

    private SunetIdAndTicketDataBinder sunetidBinder;

    @Before
    public void setUp() {
        this.sunetidBinder = createMock(SunetIdAndTicketDataBinder.class);
        this.dao = createMock(BookmarkDAO.class);
        this.bookmarkBinder = createMock(BookmarkDataBinder.class);
        this.controller = new BookmarkletController(this.dao, this.bookmarkBinder, this.sunetidBinder);
        this.request = createMock(HttpServletRequest.class);
        this.model = createMock(Model.class);
        this.bookmarks = new ArrayList<Object>();
    }

    @Test
    public void testAddBookmark() throws UnsupportedEncodingException {
        this.dao.saveLinks("sunetid", this.bookmarks);
        replay(this.dao, this.sunetidBinder, this.bookmarkBinder);
        assertEquals("redirect:url", this.controller.addBookmark(null, this.bookmarks, "sunetid", "url", "label"));
        verify(this.dao, this.sunetidBinder, this.bookmarkBinder);
    }

    @Test
    public void testAddBookmarkNullSunetid() throws UnsupportedEncodingException {
        replay(this.dao, this.sunetidBinder, this.bookmarkBinder);
        assertEquals("redirect:/secure/bookmarklet?url=url&label=label",
                this.controller.addBookmark(null, this.bookmarks, null, "url", "label"));
        verify(this.dao, this.sunetidBinder, this.bookmarkBinder);
    }

    @Test
    public void testBind() {
        Map<String, Object> map = new HashMap<String, Object>();
        expect(this.model.asMap()).andReturn(map).times(2);
        expect(this.model.containsAttribute("bookmarks")).andReturn(true);
        expect(this.model.containsAttribute("sunetid")).andReturn(true);
        replay(this.request, this.model);
        this.controller.bind(this.request, this.model);
        verify(this.request, this.model);
    }

    @Test
    public void testBindNoSunetid() {
        Map<String, Object> map = new HashMap<String, Object>();
        expect(this.model.asMap()).andReturn(map).times(2);
        expect(this.model.containsAttribute("bookmarks")).andReturn(true);
        expect(this.model.containsAttribute("sunetid")).andReturn(false);
        expect(this.model.addAttribute("sunetid", null)).andReturn(this.model);
        expect(this.model.addAttribute("bookmarks", null)).andReturn(this.model);
        replay(this.request, this.model);
        this.controller.bind(this.request, this.model);
        verify(this.request, this.model);
    }
}
