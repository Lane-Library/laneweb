package edu.stanford.irt.laneweb.servlet.mvc.bookmarks;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ModelAttribute;

import edu.stanford.irt.laneweb.bookmarks.Bookmark;
import edu.stanford.irt.laneweb.bookmarks.BookmarkService;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.BookmarkDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;

public class BookmarkController {

    private BookmarkDataBinder bookmarkDataBinder;

    private BookmarkService bookmarkService;

    private UserDataBinder userDataBinder;

    public BookmarkController(final BookmarkService bookmarkService, final BookmarkDataBinder bookmarkDataBinder,
            final UserDataBinder userDataBinder) {
        this.bookmarkService = bookmarkService;
        this.bookmarkDataBinder = bookmarkDataBinder;
        this.userDataBinder = userDataBinder;
    }

    @ModelAttribute
    protected void bind(final HttpServletRequest request, final org.springframework.ui.Model model) {
        this.userDataBinder.bind(model.asMap(), request);
        this.bookmarkDataBinder.bind(model.asMap(), request);
        // case 73359 need to put null value into model if not present.
        if (!model.containsAttribute(Model.BOOKMARKS)) {
            model.addAttribute(Model.BOOKMARKS, null);
        }
    }

    protected void saveLinks(final String userid, final List<Bookmark> links, final HttpSession session) {
        this.bookmarkService.saveLinks(userid, links);
        session.setAttribute(Model.BOOKMARKS, links);
    }
}
