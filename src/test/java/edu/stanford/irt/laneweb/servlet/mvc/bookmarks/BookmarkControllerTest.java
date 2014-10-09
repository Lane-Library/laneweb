package edu.stanford.irt.laneweb.servlet.mvc.bookmarks;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;

import edu.stanford.irt.laneweb.bookmarks.BookmarkDAO;
import edu.stanford.irt.laneweb.servlet.binding.BookmarkDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.UserIdAndTicketDataBinder;

public class BookmarkControllerTest {

    private static final class TestBookmarkController extends BookmarkController {

        public TestBookmarkController(final BookmarkDAO bookmarkDAO, final BookmarkDataBinder bookmarkDataBinder,
                final UserIdAndTicketDataBinder useridTicketDataBinder) {
            super(bookmarkDAO, bookmarkDataBinder, useridTicketDataBinder);
        }
    }

    private BookmarkDAO bookmarkDAO;

    private BookmarkDataBinder bookmarkDataBinder;

    private BookmarkController controller;

    private Model model;

    private HttpServletRequest request;

    private UserIdAndTicketDataBinder useridTicketDataBinder;

    @Before
    public void setUp() throws Exception {
        this.bookmarkDAO = createMock(BookmarkDAO.class);
        this.bookmarkDataBinder = createMock(BookmarkDataBinder.class);
        this.useridTicketDataBinder = createMock(UserIdAndTicketDataBinder.class);
        this.controller = new TestBookmarkController(this.bookmarkDAO, this.bookmarkDataBinder,
                this.useridTicketDataBinder);
        this.request = createMock(HttpServletRequest.class);
        this.model = createMock(Model.class);
    }

    @Test
    public void testBindFalse() {
        Map<String, Object> map = new HashMap<String, Object>();
        expect(this.model.asMap()).andReturn(map).times(2);
        this.useridTicketDataBinder.bind(map, this.request);
        this.bookmarkDataBinder.bind(map, this.request);
        expect(this.model.containsAttribute("bookmarks")).andReturn(false);
        expect(this.model.addAttribute("bookmarks", null)).andReturn(this.model);
        replay(this.bookmarkDAO, this.bookmarkDataBinder, this.useridTicketDataBinder, this.request, this.model);
        this.controller.bind(this.request, this.model);
        verify(this.bookmarkDAO, this.bookmarkDataBinder, this.useridTicketDataBinder, this.request, this.model);
    }

    @Test
    public void testBindTrue() {
        Map<String, Object> map = new HashMap<String, Object>();
        expect(this.model.asMap()).andReturn(map).times(2);
        this.useridTicketDataBinder.bind(map, this.request);
        this.bookmarkDataBinder.bind(map, this.request);
        expect(this.model.containsAttribute("bookmarks")).andReturn(true);
        replay(this.bookmarkDAO, this.bookmarkDataBinder, this.useridTicketDataBinder, this.request, this.model);
        this.controller.bind(this.request, this.model);
        verify(this.bookmarkDAO, this.bookmarkDataBinder, this.useridTicketDataBinder, this.request, this.model);
    }

    @Test
    public void testSaveLinks() {
        this.bookmarkDAO.saveLinks("userid", Collections.<Object> emptyList());
        replay(this.bookmarkDAO, this.bookmarkDataBinder, this.useridTicketDataBinder, this.request, this.model);
        this.controller.saveLinks("userid", Collections.<Object> emptyList());
        verify(this.bookmarkDAO, this.bookmarkDataBinder, this.useridTicketDataBinder, this.request, this.model);
    }
}
