package edu.stanford.irt.laneweb.servlet.mvc.bookmarks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.bookmarks.BookmarkDAO;

@Controller
@RequestMapping(value = "/secure/metrics/bookmarks")
public class BookmarkMetricsController {

    @Autowired
    private BookmarkDAO bookmarkDAO;

    @RequestMapping(value = "/rowcount")
    @ResponseBody
    public int getRowCount() {
        return this.bookmarkDAO.getRowCount();
    }
}
