package edu.stanford.irt.laneweb.bookmarks;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

public class BookmarksDataBinder implements DataBinder {

    @Autowired
    private BookmarksDAO bookmarksDAO;

    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        String emrid = (String) model.get(Model.EMRID);
        if (emrid != null) {
            Bookmarks bookmarks = null;
            HttpSession session = request.getSession();
            Bookmarks sessionBookmarks = (Bookmarks) session.getAttribute(Model.BOOKMARKS);
            if (sessionBookmarks == null) {
                bookmarks = this.bookmarksDAO.getBookmarks(emrid);
            } else {
                bookmarks = sessionBookmarks;
            }
            if (bookmarks != null) {
                if (sessionBookmarks == null) {
                    session.setAttribute(Model.BOOKMARKS, bookmarks);
                }
                model.put(Model.BOOKMARKS, bookmarks);
            }
            String position = request.getParameter("position");
            String action = request.getParameter("action");
            String url = request.getParameter("url");
            String label = request.getParameter("label");
            if (action != null) {
                model.put("bookmarks:action", action);
            }
            if (position != null) {
                model.put("bookmarks:position", position);
            }
            if (url != null) {
                model.put("bookmarks:url", url);
            }
            if (label != null) {
                model.put("bookmarks:label", label);
            }
        }
    }
}
