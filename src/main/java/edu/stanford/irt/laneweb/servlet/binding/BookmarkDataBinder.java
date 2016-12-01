package edu.stanford.irt.laneweb.servlet.binding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.bookmarks.Bookmark;
import edu.stanford.irt.laneweb.bookmarks.BookmarkDAO;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class BookmarkDataBinder implements DataBinder {

    private BookmarkDAO bookmarkDAO;

    @Override
    @SuppressWarnings("unchecked")
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        String userid = ModelUtil.getString(model, Model.USER_ID);
        if (userid != null) {
            List<Bookmark> bookmarks;
            HttpSession session = request.getSession();
            bookmarks = (List<Bookmark>) session.getAttribute(Model.BOOKMARKS);
            if (bookmarks == null) {
                bookmarks = this.bookmarkDAO.getLinks(userid);
                if (bookmarks == null) {
                    bookmarks = new ArrayList<>();
                }
                session.setAttribute(Model.BOOKMARKS, bookmarks);
            }
            model.put(Model.BOOKMARKS, bookmarks);
        }
    }

    public void setBookmarkDAO(final BookmarkDAO bookmarkDAO) {
        this.bookmarkDAO = bookmarkDAO;
    }
}
