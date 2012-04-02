package edu.stanford.irt.laneweb.bookmarks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

public class BookmarkDataBinder implements DataBinder {

    private BookmarkDAO bookmarkDAO;

    private boolean enabled;

    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        if (this.enabled) {
            String sunetid = (String) model.get(Model.SUNETID);
            if (sunetid != null) {
                List<Bookmark> bookmarks = null;
                HttpSession session = request.getSession();
                @SuppressWarnings("unchecked")
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
            }
        }
    }

    public void setBookmarkDAO(final BookmarkDAO bookmarkDAO) {
        this.bookmarkDAO = bookmarkDAO;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
