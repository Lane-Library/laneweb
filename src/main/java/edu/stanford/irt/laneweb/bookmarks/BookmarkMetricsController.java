package edu.stanford.irt.laneweb.bookmarks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/secure/metrics/bookmarks")
public class BookmarkMetricsController {

    @Autowired
    private BookmarkDAO bookmarkDAO;

    @RequestMapping(value = "/rowcount")
    public int getRowCount() {
        return this.bookmarkDAO.getRowCount();
    }
}
