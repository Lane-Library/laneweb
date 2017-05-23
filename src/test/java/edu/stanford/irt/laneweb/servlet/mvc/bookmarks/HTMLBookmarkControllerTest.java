package edu.stanford.irt.laneweb.servlet.mvc.bookmarks;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.stanford.irt.laneweb.bookmarks.Bookmark;
import edu.stanford.irt.laneweb.bookmarks.BookmarkService;
import edu.stanford.irt.laneweb.servlet.binding.BookmarkDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;

public class HTMLBookmarkControllerTest {

    private BookmarkDataBinder bookmarkDataBinder;

    private List<Bookmark> bookmarks;

    private BookmarkService bookmarkService;

    private HTMLBookmarkController controller;

    private RedirectAttributes redirectAttributes;

    private UserDataBinder userDataBinder;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        this.bookmarkService = createMock(BookmarkService.class);
        this.bookmarkDataBinder = createMock(BookmarkDataBinder.class);
        this.userDataBinder = createMock(UserDataBinder.class);
        this.controller = new HTMLBookmarkController(this.bookmarkService, this.bookmarkDataBinder,
                this.userDataBinder);
        this.redirectAttributes = createMock(RedirectAttributes.class);
        this.bookmarks = createMock(List.class);
    }

    @Test
    public void testAddBookmark() {
        this.bookmarks.add(eq(0), isA(Bookmark.class));
        expect(this.redirectAttributes.addAttribute("action", "edit")).andReturn(this.redirectAttributes);
        expect(this.redirectAttributes.addAttribute("i", 0)).andReturn(this.redirectAttributes);
        replay(this.redirectAttributes, this.bookmarks);
        this.controller.addBookmark(this.redirectAttributes, this.bookmarks, "userid");
        verify(this.redirectAttributes, this.bookmarks);
    }

    @Test
    public void testCancelEdit() {
        replay(this.redirectAttributes);
        this.controller.cancelEdit(this.redirectAttributes);
        verify(this.redirectAttributes);
    }

    @Test
    public void testDeleteBookmark() {
        expect(this.bookmarks.remove(0)).andReturn(null);
        replay(this.redirectAttributes, this.bookmarks);
        this.controller.deleteBookmark(this.redirectAttributes, this.bookmarks, "userid", 0);
        verify(this.redirectAttributes, this.bookmarks);
    }

    @Test
    public void testEditBookmark() {
        expect(this.redirectAttributes.addAttribute("action", "edit")).andReturn(this.redirectAttributes);
        expect(this.redirectAttributes.addAttribute("i", 0)).andReturn(this.redirectAttributes);
        replay(this.redirectAttributes);
        this.controller.editBookmark(this.redirectAttributes, 0);
        verify(this.redirectAttributes);
    }

    @Test
    public void testSaveBookmark() {
        expect(this.bookmarks.set(eq(0), isA(Bookmark.class))).andReturn(null);
        replay(this.redirectAttributes, this.bookmarks);
        this.controller.saveBookmark(this.redirectAttributes, this.bookmarks, "userid", 0, "label", "url");
        verify(this.redirectAttributes, this.bookmarks);
    }
}
