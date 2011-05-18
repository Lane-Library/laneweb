package edu.stanford.irt.laneweb.servlet.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import edu.stanford.irt.laneweb.bookmarks.Bookmark;
import edu.stanford.irt.laneweb.bookmarks.Bookmarks;
import edu.stanford.irt.laneweb.history.HistoryDAO;
import edu.stanford.irt.laneweb.model.Model;

@Controller
@SessionAttributes({ Model.HISTORY, Model.EMRID })
@RequestMapping(value = "/history")
public class HistoryTrackerController {
    
    private static final int MAX_SIZE = 10;

    @Autowired
    private HistoryDAO historyDAO;

    @ModelAttribute(Model.HISTORY)
    public Bookmarks getHistory(@ModelAttribute(Model.EMRID) final String emrid) {
        Bookmarks history = this.historyDAO.getHistory(emrid);
        if (history == null) {
            history = new Bookmarks(emrid);
        }
        return history;
    }

    @RequestMapping(value = "/track")
    @ResponseBody
    public void trackHistory(@RequestBody final Bookmark trackingData, @ModelAttribute(Model.EMRID) final String emrid,
            @ModelAttribute(Model.HISTORY) final Bookmarks history) {
        history.addFirst(trackingData);
        if (history.size() > MAX_SIZE) {
            history.removeLast();
        }
        this.historyDAO.saveHistory(history);
    }

    @RequestMapping(value = "/get")
    @ResponseBody
    public Bookmarks getHistory(@ModelAttribute(Model.HISTORY) final Bookmarks history) {
        return history;
    }
}
