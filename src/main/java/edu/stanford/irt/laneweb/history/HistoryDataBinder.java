package edu.stanford.irt.laneweb.history;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.bookmarks.Bookmarks;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;


public class HistoryDataBinder implements DataBinder {
    
    private HistoryDAO historyDAO;

    @Override
    public void bind(Map<String, Object> model, HttpServletRequest request) {
        String emrid = (String) model.get(Model.EMRID);
        if (emrid != null) {
            Bookmarks history = null;
            HttpSession session = request.getSession();
            Bookmarks sessionHistory = (Bookmarks) session.getAttribute(Model.HISTORY);
            if (sessionHistory == null) {
                history = this.historyDAO.getHistory(emrid);
            } else {
                history = sessionHistory;
            }
            if (history == null) {
                history = new Bookmarks(emrid);
            }
            if (sessionHistory == null) {
                session.setAttribute(Model.HISTORY, history);
            }
            model.put(Model.HISTORY, history);
        }
    }
    
    public void setHistoryDAO(HistoryDAO historyDAO) {
        this.historyDAO = historyDAO;
    }
}
