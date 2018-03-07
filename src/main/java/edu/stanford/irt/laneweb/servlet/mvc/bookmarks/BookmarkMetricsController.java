package edu.stanford.irt.laneweb.servlet.mvc.bookmarks;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.bookmarks.BookmarkService;

@Controller
@RequestMapping(value = "/secure/metrics/bookmarks")
public class BookmarkMetricsController {

    private BookmarkService bookmarkService;

    public BookmarkMetricsController(final BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @RequestMapping(value = "/rowcount")
    @ResponseBody
    public int getRowCount() {
        return this.bookmarkService.getRowCount();
    }
}
