package edu.stanford.irt.laneweb.servlet.mvc.bookmarks;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.bookmarks.Bookmark;
import edu.stanford.irt.laneweb.bookmarks.BookmarkService;
import edu.stanford.irt.laneweb.model.Model;

// TODO: make assertions about this.bookmark after done, also throw exceptions
// when saving.
public class JSONBookmarkControllerTest {

    private Bookmark bookmark;

    private List<Bookmark> bookmarks;

    private BookmarkService bookmarkService;

    private JSONBookmarkController controller;

    private HttpSession session;

    private String userid;

    @Before
    public void setUp() throws Exception {
        this.bookmarkService = mock(BookmarkService.class);
        this.controller = new JSONBookmarkController(this.bookmarkService, null, null, null);
        this.userid = "ditenus";
        this.bookmarks = new ArrayList<>();
        this.bookmark = new Bookmark("label", "url");
        this.session = mock(HttpSession.class);
    }

    @Test
    public void testAddBookmark() {
        this.bookmarkService.saveLinks(eq(this.userid), eq(Collections.singletonList(this.bookmark)));
        this.session.setAttribute(eq(Model.BOOKMARKS), eq(Collections.singletonList(this.bookmark)));
        replay(this.bookmarkService, this.session);
        this.controller.addBookmark(this.bookmarks, this.userid, this.bookmark, this.session);
        verify(this.bookmarkService, this.session);
    }

    @Test
    public void testDeleteBookmark() {
        this.bookmarks.add(this.bookmark);
        this.bookmarks.add(this.bookmark);
        this.bookmarks.add(this.bookmark);
        this.bookmarks.add(this.bookmark);
        this.bookmarkService.saveLinks(eq(this.userid),
                eq(Arrays.asList(new Bookmark[] { this.bookmark, this.bookmark })));
        this.session.setAttribute(eq(Model.BOOKMARKS),
                eq(Arrays.asList(new Object[] { this.bookmark, this.bookmark })));
        replay(this.bookmarkService, this.session);
        this.controller.deleteBookmark(this.bookmarks, this.userid, "[0,3]", this.session);
        verify(this.bookmarkService, this.session);
    }

    @Test
    public void testGetBookmark() {
        this.bookmarks.add(this.bookmark);
        replay(this.bookmarkService, this.session);
        assertEquals(this.bookmark, this.controller.getBookmark(this.bookmarks, false, 0, this.session));
        verify(this.bookmarkService, this.session);
    }

    @Test
    public void testMoveBookmarkBadFrom() {
        this.bookmarks.add(new Bookmark("newlabel", "newurl"));
        this.bookmarks.add(this.bookmark);
        Map<String, Integer> json = new HashMap<>();
        json.put("to", 0);
        json.put("from", 5);
        replay(this.bookmarkService, this.session);
        try {
            this.controller.moveBookmark(this.bookmarks, this.userid, json, this.session);
        } catch (IndexOutOfBoundsException e) {
        }
        assertEquals(this.bookmark, this.bookmarks.get(1));
        verify(this.bookmarkService, this.session);
    }

    @Test
    public void testMoveBookmarkBadTo() {
        this.bookmarks.add(new Bookmark("newlabel", "newurl"));
        this.bookmarks.add(this.bookmark);
        Map<String, Integer> json = new HashMap<>();
        json.put("to", 5);
        json.put("from", 0);
        replay(this.bookmarkService, this.session);
        try {
            this.controller.moveBookmark(this.bookmarks, this.userid, json, this.session);
        } catch (IndexOutOfBoundsException e) {
        }
        assertEquals(this.bookmark, this.bookmarks.get(1));
        verify(this.bookmarkService, this.session);
    }

    @Test
    public void testMoveBookmarkDown() {
        this.bookmarks.add(new Bookmark("bookmark0", "bookmark0"));
        this.bookmarks.add(new Bookmark("bookmark1", "bookmark1"));
        this.bookmarks.add(new Bookmark("bookmark2", "bookmark2"));
        this.bookmarks.add(new Bookmark("bookmark3", "bookmark3"));
        this.bookmarks.add(new Bookmark("bookmark4", "bookmark4"));
        this.bookmarks.add(new Bookmark("bookmark5", "bookmark5"));
        this.bookmarks.add(new Bookmark("bookmark6", "bookmark6"));
        this.bookmarks.add(new Bookmark("bookmark7", "bookmark7"));
        this.bookmarks.add(new Bookmark("bookmark8", "bookmark8"));
        this.bookmarks.add(new Bookmark("bookmark9", "bookmark9"));
        Map<String, Integer> json = new HashMap<>();
        json.put("to", 1);
        json.put("from", 8);
        Capture<List<Bookmark>> capture = newCapture();
        this.bookmarkService.saveLinks(eq(this.userid), capture(capture));
        this.session.setAttribute(eq(Model.BOOKMARKS), isA(List.class));
        replay(this.bookmarkService, this.session);
        this.controller.moveBookmark(this.bookmarks, this.userid, json, this.session);
        assertEquals("bookmark8", this.bookmarks.get(1).getLabel());
        assertEquals("bookmark1", this.bookmarks.get(2).getLabel());
        assertEquals("bookmark7", this.bookmarks.get(8).getLabel());
        assertEquals(this.bookmarks, capture.getValue());
        verify(this.bookmarkService, this.session);
    }

    @Test
    public void testMoveBookmarkUp() {
        this.bookmarks.add(new Bookmark("bookmark0", "bookmark0"));
        this.bookmarks.add(new Bookmark("bookmark1", "bookmark1"));
        this.bookmarks.add(new Bookmark("bookmark2", "bookmark2"));
        this.bookmarks.add(new Bookmark("bookmark3", "bookmark3"));
        this.bookmarks.add(new Bookmark("bookmark4", "bookmark4"));
        this.bookmarks.add(new Bookmark("bookmark5", "bookmark5"));
        this.bookmarks.add(new Bookmark("bookmark6", "bookmark6"));
        this.bookmarks.add(new Bookmark("bookmark7", "bookmark7"));
        this.bookmarks.add(new Bookmark("bookmark8", "bookmark8"));
        this.bookmarks.add(new Bookmark("bookmark9", "bookmark9"));
        this.bookmarks.add(new Bookmark("newlabel", "newurl"));
        this.bookmarks.add(new Bookmark("anotherlabel", "anotherurl"));
        Map<String, Integer> json = new HashMap<>();
        json.put("to", 8);
        json.put("from", 1);
        Capture<List<Bookmark>> capture = newCapture();
        this.bookmarkService.saveLinks(eq(this.userid), capture(capture));
        this.session.setAttribute(eq(Model.BOOKMARKS), isA(List.class));
        replay(this.bookmarkService, this.session);
        this.controller.moveBookmark(this.bookmarks, this.userid, json, this.session);
        assertEquals("bookmark1", this.bookmarks.get(8).getLabel());
        assertEquals("bookmark2", this.bookmarks.get(1).getLabel());
        assertEquals("bookmark8", this.bookmarks.get(7).getLabel());
        assertEquals(this.bookmarks, capture.getValue());
        verify(this.bookmarkService, this.session);
    }

    @Test
    public void testSaveBookmark() {
        Map<String, Object> json = new HashMap<>();
        json.put("position", Integer.valueOf(0));
        json.put("label", "newlabel");
        json.put("url", "newurl");
        this.bookmarks.add(this.bookmark);
        this.bookmarkService.saveLinks(eq(this.userid),
                eq(Collections.singletonList(new Bookmark("newlabel", "newurl"))));
        this.session.setAttribute(eq(Model.BOOKMARKS),
                eq(Collections.singletonList(new Bookmark("newlabel", "newurl"))));
        replay(this.bookmarkService, this.session);
        this.controller.saveBookmark(this.bookmarks, this.userid, json, this.session);
        verify(this.bookmarkService, this.session);
    }
}
