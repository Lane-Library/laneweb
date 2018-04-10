package edu.stanford.irt.laneweb.servlet.binding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.bookmarks.Bookmark;
import edu.stanford.irt.laneweb.bookmarks.BookmarkService;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class BookmarkDataBinder implements DataBinder {

    private BookmarkService bookmarkService;

    public BookmarkDataBinder(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        String userid = ModelUtil.getString(model, Model.USER_ID);
        if (userid != null) {
            List<Bookmark> bookmarks;
            HttpSession session = request.getSession();
            bookmarks = (List<Bookmark>) session.getAttribute(Model.BOOKMARKS);
            if (bookmarks == null) {
                bookmarks = this.bookmarkService.getLinks(userid);
                if (bookmarks == null) {
                    bookmarks = new ArrayList<>();
                }
                session.setAttribute(Model.BOOKMARKS, bookmarks);
            }
            model.put(Model.BOOKMARKS, bookmarks);
        }
    }
}
