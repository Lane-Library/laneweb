package edu.stanford.irt.laneweb.bookmarks;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class JSONBookmarkControllerTest {

    private Bookmark bookmark;

    private BookmarkDAO bookmarkDAO;

    private List<Bookmark> bookmarks;

    private JSONBookmarkController controller;

    private String sunetid;

    @Before
    public void setUp() throws Exception {
        this.controller = new JSONBookmarkController();
        this.sunetid = "ditenus";
        this.bookmarks = new ArrayList<Bookmark>();
        this.bookmark = new Bookmark("label", "url");
        this.bookmarkDAO = createMock(BookmarkDAO.class);
        this.controller.setBookmarkDAO(this.bookmarkDAO);
    }

    @Test
    public void testAddBookmark() {
        this.bookmarkDAO.saveLinks(this.sunetid, this.bookmarks);
        replay(this.bookmarkDAO);
        this.controller.addBookmark(this.bookmarks, this.sunetid, this.bookmark);
        verify(this.bookmarkDAO);
    }

    @Test
    public void testDeleteBookmark() {
        this.bookmarks.add(this.bookmark);
        this.bookmarks.add(this.bookmark);
        this.bookmarks.add(this.bookmark);
        this.bookmarks.add(this.bookmark);
        this.bookmarkDAO.saveLinks(this.sunetid, this.bookmarks);
        replay(this.bookmarkDAO);
        this.controller.deleteBookmark(this.bookmarks, this.sunetid, 3);
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
    public void testSaveBookmark() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("position", Integer.valueOf(0));
        json.put("label", "newlabel");
        json.put("url", "newurl");
        this.bookmarks.add(this.bookmark);
        this.bookmarkDAO.saveLinks(this.sunetid, this.bookmarks);
        replay(this.bookmarkDAO);
        this.controller.saveBookmark(this.bookmarks, this.sunetid, json);
        verify(this.bookmarkDAO);
    }
}
