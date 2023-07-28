package edu.stanford.irt.laneweb.servlet.binding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.bookmarks.Bookmark;
import edu.stanford.irt.laneweb.bookmarks.BookmarkException;
import edu.stanford.irt.laneweb.bookmarks.BookmarkService;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class BookmarkDataBinder implements DataBinder {
    
    private static final Logger log = LoggerFactory.getLogger(BookmarkDataBinder.class);

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
                try {
                    bookmarks = this.bookmarkService.getLinks(userid);
                } catch (BookmarkException e) {
                    log.error(e.getMessage());
                    // if getting bookmarks from the service fails turn bookmarking off for this user
                    // and return without putting bookmarks in the model or session
                    model.put(Model.BOOKMARKING, "off");
                    return;
                }
                if (bookmarks == null) {
                    bookmarks = new ArrayList<>();
                }
                session.setAttribute(Model.BOOKMARKS, bookmarks);
            }
            model.put(Model.BOOKMARKS, bookmarks);
        }
    }
}
