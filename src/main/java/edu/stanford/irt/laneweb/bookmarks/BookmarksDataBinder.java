package edu.stanford.irt.laneweb.bookmarks;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

public class BookmarksDataBinder implements DataBinder {

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
        }
    }

    public void setBookmarksDAO(final BookmarksDAO bookmarksDAO) {
        this.bookmarksDAO = bookmarksDAO;
    }
}
