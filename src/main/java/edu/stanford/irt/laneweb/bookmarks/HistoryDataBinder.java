package edu.stanford.irt.laneweb.bookmarks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

//TODO: BookmarkDataBinder and HistoryDataBinder very similar, extract superclass?
public class HistoryDataBinder implements DataBinder {

    private BookmarkDAO<History> historyDAO;

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        String sunetid = (String) model.get(Model.SUNETID);
        if (sunetid != null) {
            List<History> history = null;
            HttpSession session = request.getSession();
            List<History> sessionHistory = (List<History>) session.getAttribute(Model.HISTORY);
            if (sessionHistory == null) {
                history = this.historyDAO.getLinks(sunetid);
            } else {
                history = sessionHistory;
            }
            if (history == null) {
                history = new ArrayList<History>();
            }
            if (sessionHistory == null) {
                session.setAttribute(Model.HISTORY, history);
            }
            model.put(Model.HISTORY, history);
        }
    }

    public void setHistoryDAO(final BookmarkDAO<History> historyDAO) {
        this.historyDAO = historyDAO;
    }
}
