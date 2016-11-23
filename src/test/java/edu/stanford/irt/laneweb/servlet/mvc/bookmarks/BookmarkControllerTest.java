package edu.stanford.irt.laneweb.servlet.mvc.bookmarks;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;

import edu.stanford.irt.laneweb.bookmarks.BookmarkDAO;
import edu.stanford.irt.laneweb.servlet.binding.BookmarkDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;

public class BookmarkControllerTest {

    private static final class TestBookmarkController extends BookmarkController {

        public TestBookmarkController(final BookmarkDAO bookmarkDAO, final BookmarkDataBinder bookmarkDataBinder,
                final UserDataBinder userDataBinder) {
            super(bookmarkDAO, bookmarkDataBinder, userDataBinder);
        }
    }

    private BookmarkDAO bookmarkDAO;

    private BookmarkDataBinder bookmarkDataBinder;

    private BookmarkController controller;

    private Model model;

    private HttpServletRequest request;

    private HttpSession session;

    private UserDataBinder userDataBinder;

    @Before
    public void setUp() throws Exception {
        this.bookmarkDAO = createMock(BookmarkDAO.class);
        this.bookmarkDataBinder = createMock(BookmarkDataBinder.class);
        this.userDataBinder = createMock(UserDataBinder.class);
        this.controller = new TestBookmarkController(this.bookmarkDAO, this.bookmarkDataBinder, this.userDataBinder);
        this.request = createMock(HttpServletRequest.class);
        this.model = createMock(Model.class);
        this.session = createMock(HttpSession.class);
    }

    @Test
    public void testBindFalse() {
        Map<String, Object> map = new HashMap<>();
        expect(this.model.asMap()).andReturn(map).times(2);
        this.userDataBinder.bind(map, this.request);
        this.bookmarkDataBinder.bind(map, this.request);
        expect(this.model.containsAttribute("bookmarks")).andReturn(false);
        expect(this.model.addAttribute("bookmarks", null)).andReturn(this.model);
        replay(this.bookmarkDAO, this.bookmarkDataBinder, this.userDataBinder, this.request, this.model);
        this.controller.bind(this.request, this.model);
        verify(this.bookmarkDAO, this.bookmarkDataBinder, this.userDataBinder, this.request, this.model);
    }

    @Test
    public void testBindTrue() {
        Map<String, Object> map = new HashMap<>();
        expect(this.model.asMap()).andReturn(map).times(2);
        this.userDataBinder.bind(map, this.request);
        this.bookmarkDataBinder.bind(map, this.request);
        expect(this.model.containsAttribute("bookmarks")).andReturn(true);
        replay(this.bookmarkDAO, this.bookmarkDataBinder, this.userDataBinder, this.request, this.model);
        this.controller.bind(this.request, this.model);
        verify(this.bookmarkDAO, this.bookmarkDataBinder, this.userDataBinder, this.request, this.model);
    }

    @Test
    public void testSaveLinks() {
        this.bookmarkDAO.saveLinks("userid", Collections.emptyList());
        this.session.setAttribute("bookmarks", Collections.emptyList());
        replay(this.bookmarkDAO, this.bookmarkDataBinder, this.userDataBinder, this.request, this.model, this.session);
        this.controller.saveLinks("userid", Collections.emptyList(), this.session);
        verify(this.bookmarkDAO, this.bookmarkDataBinder, this.userDataBinder, this.request, this.model, this.session);
    }
}
