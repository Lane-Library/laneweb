package edu.stanford.irt.laneweb.servlet.mvc.bookmarks;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import edu.stanford.irt.laneweb.bookmarks.BookmarkService;
import edu.stanford.irt.laneweb.servlet.binding.BookmarkDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;

public class BookmarkControllerTest {

    private static final class TestBookmarkController extends BookmarkController {

        public TestBookmarkController(final BookmarkService bookmarkService,
                final BookmarkDataBinder bookmarkDataBinder, final UserDataBinder userDataBinder) {
            super(bookmarkService, bookmarkDataBinder, userDataBinder);
        }
    }

    private BookmarkDataBinder bookmarkDataBinder;

    private BookmarkService bookmarkService;

    private BookmarkController controller;

    private Model model;

    private HttpServletRequest request;

    private HttpSession session;

    private UserDataBinder userDataBinder;

    @BeforeEach
    public void setUp() throws Exception {
        this.bookmarkService = mock(BookmarkService.class);
        this.bookmarkDataBinder = mock(BookmarkDataBinder.class);
        this.userDataBinder = mock(UserDataBinder.class);
        this.controller = new TestBookmarkController(this.bookmarkService, this.bookmarkDataBinder,
                this.userDataBinder);
        this.request = mock(HttpServletRequest.class);
        this.model = mock(Model.class);
        this.session = mock(HttpSession.class);
    }

    @Test
    public void testBindFalse() {
        Map<String, Object> map = new HashMap<>();
        expect(this.model.asMap()).andReturn(map).times(2);
        this.userDataBinder.bind(map, this.request);
        this.bookmarkDataBinder.bind(map, this.request);
        expect(this.model.containsAttribute("bookmarks")).andReturn(false);
        expect(this.model.addAttribute("bookmarks", null)).andReturn(this.model);
        replay(this.bookmarkService, this.bookmarkDataBinder, this.userDataBinder, this.request, this.model);
        this.controller.bind(this.request, this.model);
        verify(this.bookmarkService, this.bookmarkDataBinder, this.userDataBinder, this.request, this.model);
    }

    @Test
    public void testBindTrue() {
        Map<String, Object> map = new HashMap<>();
        expect(this.model.asMap()).andReturn(map).times(2);
        this.userDataBinder.bind(map, this.request);
        this.bookmarkDataBinder.bind(map, this.request);
        expect(this.model.containsAttribute("bookmarks")).andReturn(true);
        replay(this.bookmarkService, this.bookmarkDataBinder, this.userDataBinder, this.request, this.model);
        this.controller.bind(this.request, this.model);
        verify(this.bookmarkService, this.bookmarkDataBinder, this.userDataBinder, this.request, this.model);
    }

    @Test
    public void testSaveLinks() {
        this.bookmarkService.saveLinks("userid", Collections.emptyList());
        this.session.setAttribute("bookmarks", Collections.emptyList());
        replay(this.bookmarkService, this.bookmarkDataBinder, this.userDataBinder, this.request, this.model,
                this.session);
        this.controller.saveLinks("userid", Collections.emptyList(), this.session);
        verify(this.bookmarkService, this.bookmarkDataBinder, this.userDataBinder, this.request, this.model,
                this.session);
    }
}
