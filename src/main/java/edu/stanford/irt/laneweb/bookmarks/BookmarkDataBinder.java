package edu.stanford.irt.laneweb.bookmarks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

public class BookmarkDataBinder implements DataBinder {

    private BookmarkDAO bookmarkDAO;

    @SuppressWarnings("unchecked")
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        String sunetid = ModelUtil.getString(model, Model.SUNETID);
        if (sunetid != null) {
            List<Bookmark> bookmarks = null;
            HttpSession session = request.getSession();
            synchronized (session) {
                bookmarks = (List<Bookmark>) session.getAttribute(Model.BOOKMARKS);
                if (bookmarks == null) {
                    bookmarks = this.bookmarkDAO.getLinks(sunetid);
                    if (bookmarks == null) {
                        bookmarks = new ArrayList<Bookmark>();
                    }
                    session.setAttribute(Model.BOOKMARKS, bookmarks);
                }
            }
            model.put(Model.BOOKMARKS, bookmarks);
        }
    }

    public void setBookmarkDAO(final BookmarkDAO bookmarkDAO) {
        this.bookmarkDAO = bookmarkDAO;
    }
}
