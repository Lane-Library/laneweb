package edu.stanford.irt.laneweb.servlet.mvc.bookmarks;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
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

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.bookmarks.Bookmark;
import edu.stanford.irt.laneweb.bookmarks.BookmarkDAO;

// TODO: make assertions about this.bookmark after done, also throw exceptions
// when saving.
public class JSONBookmarkControllerTest {

    private Bookmark bookmark;

    private BookmarkDAO bookmarkDAO;

    private List<Bookmark> bookmarks;

    private JSONBookmarkController controller;

    private String userid;

    @Before
    public void setUp() throws Exception {
        this.bookmarkDAO = createMock(BookmarkDAO.class);
        this.controller = new JSONBookmarkController(this.bookmarkDAO, null, null, null);
        this.userid = "ditenus";
        this.bookmarks = new ArrayList<Bookmark>();
        this.bookmark = new Bookmark("label", "url");
    }

    @Test
    public void testAddBookmark() {
        this.bookmarkDAO.saveLinks(eq(this.userid), eq(Collections.singletonList(this.bookmark)));
        replay(this.bookmarkDAO);
        this.controller.addBookmark(this.bookmarks, this.userid, this.bookmark);
        verify(this.bookmarkDAO);
    }

    @Test
    public void testDeleteBookmark() {
        this.bookmarks.add(this.bookmark);
        this.bookmarks.add(this.bookmark);
        this.bookmarks.add(this.bookmark);
        this.bookmarks.add(this.bookmark);
        this.bookmarkDAO.saveLinks(eq(this.userid), eq(Arrays.asList(new Bookmark[] { this.bookmark, this.bookmark })));
        replay(this.bookmarkDAO);
        this.controller.deleteBookmark(this.bookmarks, this.userid, "[0,3]");
        verify(this.bookmarkDAO);
    }

    @Test
    public void testGetBookmark() {
        this.bookmarks.add(this.bookmark);
        replay(this.bookmarkDAO);
        assertEquals(this.bookmark, this.controller.getBookmark(this.bookmarks, false, 0));
        verify(this.bookmarkDAO);
    }

    @Test
    public void testMoveBookmarkBadFrom() {
        this.bookmarks.add(new Bookmark("newlabel", "newurl"));
        this.bookmarks.add(this.bookmark);
        Map<String, Integer> json = new HashMap<String, Integer>();
        json.put("to", 0);
        json.put("from", 5);
        replay(this.bookmarkDAO);
        try {
            this.controller.moveBookmark(this.bookmarks, this.userid, json);
        } catch (IndexOutOfBoundsException e) {
        }
        assertEquals(this.bookmark, this.bookmarks.get(1));
        verify(this.bookmarkDAO);
    }

    @Test
    public void testMoveBookmarkBadTo() {
        this.bookmarks.add(new Bookmark("newlabel", "newurl"));
        this.bookmarks.add(this.bookmark);
        Map<String, Integer> json = new HashMap<String, Integer>();
        json.put("to", 5);
        json.put("from", 0);
        replay(this.bookmarkDAO);
        try {
            this.controller.moveBookmark(this.bookmarks, this.userid, json);
        } catch (IndexOutOfBoundsException e) {
        }
        assertEquals(this.bookmark, this.bookmarks.get(1));
        verify(this.bookmarkDAO);
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
        Map<String, Integer> json = new HashMap<String, Integer>();
        json.put("to", 1);
        json.put("from", 8);
        Capture<List<Bookmark>> capture = newCapture();
        this.bookmarkDAO.saveLinks(eq(this.userid), capture(capture));
        replay(this.bookmarkDAO);
        this.controller.moveBookmark(this.bookmarks, this.userid, json);
        assertEquals("bookmark8", ((Bookmark) this.bookmarks.get(1)).getLabel());
        assertEquals("bookmark1", ((Bookmark) this.bookmarks.get(2)).getLabel());
        assertEquals("bookmark7", ((Bookmark) this.bookmarks.get(8)).getLabel());
        assertEquals(this.bookmarks, capture.getValue());
        verify(this.bookmarkDAO);
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
        Map<String, Integer> json = new HashMap<String, Integer>();
        json.put("to", 8);
        json.put("from", 1);
        Capture<List<Bookmark>> capture = newCapture();
        this.bookmarkDAO.saveLinks(eq(this.userid), capture(capture));
        replay(this.bookmarkDAO);
        this.controller.moveBookmark(this.bookmarks, this.userid, json);
        assertEquals("bookmark1", ((Bookmark) this.bookmarks.get(8)).getLabel());
        assertEquals("bookmark2", ((Bookmark) this.bookmarks.get(1)).getLabel());
        assertEquals("bookmark8", ((Bookmark) this.bookmarks.get(7)).getLabel());
        assertEquals(this.bookmarks, capture.getValue());
        verify(this.bookmarkDAO);
    }

    @Test
    public void testSaveBookmark() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("position", Integer.valueOf(0));
        json.put("label", "newlabel");
        json.put("url", "newurl");
        this.bookmarks.add(this.bookmark);
        this.bookmarkDAO.saveLinks(eq(this.userid),
                eq(Collections.singletonList(new Bookmark("newlabel", "newurl"))));
        replay(this.bookmarkDAO);
        this.controller.saveBookmark(this.bookmarks, this.userid, json);
        verify(this.bookmarkDAO);
    }
}
