package edu.stanford.irt.laneweb.servlet.mvc.bookmarks;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ModelAttribute;

import edu.stanford.irt.laneweb.bookmarks.Bookmark;
import edu.stanford.irt.laneweb.bookmarks.BookmarkService;
import edu.stanford.irt.laneweb.servlet.binding.BookmarkDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;

public abstract class BookmarkController {

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
        if (!model.containsAttribute(edu.stanford.irt.laneweb.model.Model.BOOKMARKS)) {
            model.addAttribute(edu.stanford.irt.laneweb.model.Model.BOOKMARKS, null);
        }
    }

    protected void saveLinks(final String userid, final List<Bookmark> links) {
        this.bookmarkService.saveLinks(userid, links);
    }
}
