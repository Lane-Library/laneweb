package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import edu.stanford.irt.laneweb.history.HistoryDAO;
import edu.stanford.irt.laneweb.history.TrackingData;
import edu.stanford.irt.laneweb.model.Model;

@Controller
@SessionAttributes({ Model.HISTORY, Model.EMRID })
@RequestMapping(value = "/history")
public class HistoryTrackerController {

    @Autowired
    private HistoryDAO historyDAO;

    @ModelAttribute(Model.HISTORY)
    public List<TrackingData> getHistory(@ModelAttribute(Model.EMRID) final String emrid) {
        List<TrackingData> history = this.historyDAO.getHistory(emrid);
        if (history == null) {
            history = new LinkedList<TrackingData>();
        }
        return history;
    }

    @RequestMapping(value = "/track")
    @ResponseBody
    public void trackHistory(@RequestBody final TrackingData trackingData, @ModelAttribute(Model.EMRID) final String emrid,
            @ModelAttribute(Model.HISTORY) final List<TrackingData> history) {
        history.add(0, trackingData);
        this.historyDAO.saveHistory(history, emrid);
    }

    @RequestMapping(value = "/get")
    @ResponseBody
    public List<TrackingData> getHistory(@ModelAttribute(Model.HISTORY) final List<TrackingData> history) {
        return history;
    }
}
