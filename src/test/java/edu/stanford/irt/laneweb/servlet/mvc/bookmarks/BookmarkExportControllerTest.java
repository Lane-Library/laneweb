package edu.stanford.irt.laneweb.servlet.mvc.bookmarks;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.bookmarks.Bookmark;

public class BookmarkExportControllerTest {

    private Bookmark bookmark;

    private List<Bookmark> bookmarks;

    private BookmarkExportController controller;

    @Before
    public void setUp() throws Exception {
        this.controller = new BookmarkExportController(null, null, null);
        this.bookmark = mock(Bookmark.class);
        this.bookmarks = Arrays.asList(new Bookmark[] { this.bookmark, this.bookmark });
    }

    @Test
    public void testGetBookmarksHTML() {
        expect(this.bookmark.getUrl()).andReturn("url");
        expect(this.bookmark.getLabel()).andReturn("label");
        expect(this.bookmark.getUrl()).andReturn("/url");
        expect(this.bookmark.getLabel()).andReturn("label");
        replay(this.bookmark);
        String desired = "<!DOCTYPE NETSCAPE-Bookmark-file-1>\n<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">\n<TITLE>Bookmarks</TITLE>\n<H1>Lane Bookmarks</H1>\n<DL><p>\n    <DT><H3 ADD_DATE=\"0\">Lane Bookmarks</H3>\n    <DL><p>\n        <DT><A HREF=\"url\" ADD_DATE=\"0\">label</A>\n        <DT><A HREF=\"http://lane.stanford.edu/url\" ADD_DATE=\"0\">label</A>\n    </DL><p>\n</DL><p>\n";
        assertEquals(desired, this.controller.getBookmarksHTML(this.bookmarks));
        verify(this.bookmark);
    }

    @Test
    public void testGetBookmarksRIS() {
        expect(this.bookmark.getUrl()).andReturn("url");
        expect(this.bookmark.getLabel()).andReturn("label");
        expect(this.bookmark.getUrl()).andReturn("/url");
        expect(this.bookmark.getLabel()).andReturn("label");
        replay(this.bookmark);
        String desired = "TY  - ELEC\r\nTI  - label\r\nUR  - url\r\nER  -\r\nTY  - ELEC\r\nTI  - label\r\nUR  - http://lane.stanford.edu/url\r\nER  -\r\n";
        assertEquals(desired, this.controller.getBookmarksRIS(this.bookmarks));
        verify(this.bookmark);
    }

    @Test
    public void testGetBookmarksTSV() {
        expect(this.bookmark.getUrl()).andReturn("url");
        expect(this.bookmark.getLabel()).andReturn("label");
        expect(this.bookmark.getUrl()).andReturn("/url");
        expect(this.bookmark.getLabel()).andReturn("label");
        replay(this.bookmark);
        String desired = "label\turl\nlabel\turl\nlabel\thttp://lane.stanford.edu/url\n";
        assertEquals(desired, this.controller.getBookmarksTSV(this.bookmarks));
        verify(this.bookmark);
    }
}
