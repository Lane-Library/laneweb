package edu.stanford.irt.laneweb.personalize;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.model.Model;

// @Controller
// @RequestMapping(value = "/history")
public class HistoryTrackerController {

    private static final int MAX_SIZE = 10;

    @Autowired
    private BookmarkDAO<History> historyDAO;

    @RequestMapping(value = "/get")
    @ResponseBody
    public List<History> getHistory(@ModelAttribute(Model.HISTORY) final List<History> history) {
        return history;
    }

    @ModelAttribute(Model.HISTORY)
    public List<History> getHistory(@ModelAttribute(Model.SUNETID) final String sunetid) {
        List<History> history = this.historyDAO.getLinks(sunetid);
        if (history == null) {
            history = new ArrayList<History>();
        }
        return history;
    }

    @RequestMapping(value = "/track")
    @ResponseBody
    public void trackHistory(@RequestBody final History trackingData, @ModelAttribute(Model.SUNETID) final String sunetid,
            @ModelAttribute(Model.HISTORY) final List<History> history) {
        history.add(0, trackingData);
        if (history.size() > MAX_SIZE) {
            history.remove(history.size() - 1);
        }
        this.historyDAO.saveLinks(sunetid, history);
    }

    public void setBookmarkDAO(BookmarkDAO<History> historyDAO) {
        this.historyDAO = historyDAO;
    }
}
