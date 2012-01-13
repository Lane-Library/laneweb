package edu.stanford.irt.laneweb.bookmarks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

//TODO: BookmarkDataBinder and HistoryDataBinder very similar, extract superclass?
public class BookmarkDataBinder implements DataBinder {

    private BookmarkDAO<Bookmark> bookmarkDAO;

    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        String sunetid = (String) model.get(Model.SUNETID);
        if (sunetid != null) {
            List<Bookmark> bookmarks = null;
            HttpSession session = request.getSession();
            List<Bookmark> sessionBookmarks = (List<Bookmark>) session.getAttribute(Model.BOOKMARKS);
            if (sessionBookmarks == null) {
                bookmarks = this.bookmarkDAO.getLinks(sunetid);
            } else {
                bookmarks = sessionBookmarks;
            }
            if (bookmarks == null) {
                bookmarks = new ArrayList<Bookmark>();
            }
            if (sessionBookmarks == null) {
                session.setAttribute(Model.BOOKMARKS, bookmarks);
            }
            model.put(Model.BOOKMARKS, bookmarks);
//            String position = request.getParameter("position");
//            String action = request.getParameter("action");
//            String url = request.getParameter("url");
//            String label = request.getParameter("label");
//            if (action != null) {
//                model.put("bookmarks:action", action);
//            }
//            if (position != null) {
//                model.put("bookmarks:position", position);
//            }
//            if (url != null) {
//                model.put("bookmarks:url", url);
//            }
//            if (label != null) {
//                model.put("bookmarks:label", label);
//            }
        }
    }

    public void setBookmarkDAO(final BookmarkDAO<Bookmark> bookmarkDAO) {
        this.bookmarkDAO = bookmarkDAO;
    }
}
