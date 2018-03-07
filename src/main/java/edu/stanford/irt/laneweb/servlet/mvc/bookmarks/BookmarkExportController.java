package edu.stanford.irt.laneweb.servlet.mvc.bookmarks;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.bookmarks.Bookmark;
import edu.stanford.irt.laneweb.bookmarks.BookmarkService;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.BookmarkDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;

@Controller
@RequestMapping(value = "/bookmarks/export")
public class BookmarkExportController extends BookmarkController {

    private static final String DOCUMENT_FOOTER = "    </DL><p>\n</DL><p>\n";

    private static final String DOCUMENT_HEADER =
            "<!DOCTYPE NETSCAPE-Bookmark-file-1>\n"
            + "<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">\n"
            + "<TITLE>Bookmarks</TITLE>\n"
            + "<H1>Lane Bookmarks</H1>\n"
            + "<DL><p>\n"
            + "    <DT><H3 ADD_DATE=\"0\">Lane Bookmarks</H3>\n"
            + "    <DL><p>\n";

    private static final String ITEM_MIDDLEFIX = "\" ADD_DATE=\"0\">";

    private static final String ITEM_PREFIX = "        <DT><A HREF=\"";

    private static final String ITEM_SUFFIX = "</A>\n";

    public BookmarkExportController(final BookmarkService bookmarkService, final BookmarkDataBinder bookmarkDataBinder,
            final UserDataBinder userDataBinder) {
        super(bookmarkService, bookmarkDataBinder, userDataBinder);
    }

    /**
     * Produces importable html bookmarks.
     *
     * @param bookmarks
     *            the Bookmarks
     * @return the bookmarks in netscape bookmark format
     */
    @RequestMapping(value = "/bookmarks.html", method = RequestMethod.GET, produces = "application/x-netscape-bookmarks")
    @ResponseBody
    public String getBookmarksHTML(@ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks) {
        StringBuilder sb = new StringBuilder(DOCUMENT_HEADER);
        for (Bookmark bookmark : bookmarks) {
            sb.append(ITEM_PREFIX);
            maybePrependLane(sb, bookmark.getUrl());
            sb.append(ITEM_MIDDLEFIX).append(bookmark.getLabel()).append(ITEM_SUFFIX);
        }
        sb.append(DOCUMENT_FOOTER);
        return sb.toString();
    }

    /**
     * Converts bookmarks to RIS (Research Information Systems) format
     *
     * @param bookmarks
     *            the bookmarks
     * @return the bookmarks in RIS format
     */
    @RequestMapping(value = "/bookmarks.ris", method = RequestMethod.GET, produces = "application/x-research-info-systems")
    @ResponseBody
    public String getBookmarksRIS(@ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks) {
        StringBuilder sb = new StringBuilder();
        for (Bookmark bookmark : bookmarks) {
            sb.append("TY  - ELEC\r\n").append("TI  - ").append(bookmark.getLabel()).append("\r\n").append("UR  - ");
            maybePrependLane(sb, bookmark.getUrl());
            sb.append("\r\nER  -\r\n");
        }
        return sb.toString();
    }

    /**
     * Converts bookmarks to a list of tab separated values.
     *
     * @param bookmarks
     *            the Bookmarks
     * @return the bookmarks as a list of tab separated values
     */
    @RequestMapping(value = "/bookmarks.tsv", method = RequestMethod.GET, produces = "text/tab-separated-values")
    @ResponseBody
    public String getBookmarksTSV(@ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks) {
        StringBuilder sb = new StringBuilder("label\turl\n");
        for (Bookmark bookmark : bookmarks) {
            sb.append(bookmark.getLabel()).append('\t');
            maybePrependLane(sb, bookmark.getUrl());
            sb.append('\n');
        }
        return sb.toString();
    }

    private void maybePrependLane(final StringBuilder sb, final String url) {
        if (url.charAt(0) == '/') {
            sb.append("http://lane.stanford.edu");
        }
        sb.append(url);
    }
}
