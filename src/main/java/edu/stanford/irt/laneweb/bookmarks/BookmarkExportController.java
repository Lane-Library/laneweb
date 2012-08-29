package edu.stanford.irt.laneweb.bookmarks;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.model.Model;

@Controller
@RequestMapping(value = "/bookmarks/export")
public class BookmarkExportController extends BookmarkController {

    private static final String HTML_HEADER = "<!DOCTYPE NETSCAPE-Bookmark-file-1>\n"
            + "<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">\n"
            + "<TITLE>Bookmarks</TITLE>\n"
            + "<H1>Lane Bookmarks</H1>\n"
            + "<DL><p>\n"
            + "<DT><H3>Lane Bookmarks</H3>\n"
            + "<DL><p>\n";

    private static final String HTML_ITEM = "<DT><A HREF=\"";

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
        StringBuilder sb = new StringBuilder(HTML_HEADER);
        for (Bookmark bookmark : bookmarks) {
            sb.append(HTML_ITEM);
            maybePrependLane(sb, bookmark.getUrl());
            sb.append("\">").append(bookmark.getLabel()).append("</A>\n");
        }
        sb.append("</DL><p>\n</DT><p>\n</DL><p>\n");
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
        } else {
            sb.append(url);
        }
    }
}
