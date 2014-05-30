package edu.stanford.irt.laneweb.servlet.mvc.bookmarks;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ModelAttribute;

import edu.stanford.irt.laneweb.bookmarks.Bookmark;
import edu.stanford.irt.laneweb.bookmarks.BookmarkDAO;
import edu.stanford.irt.laneweb.servlet.binding.BookmarkDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.SunetIdAndTicketDataBinder;

public abstract class BookmarkController {

    private BookmarkDAO bookmarkDAO;

    private BookmarkDataBinder bookmarkDataBinder;

    private SunetIdAndTicketDataBinder sunetidTicketDataBinder;

    public BookmarkController(final BookmarkDAO bookmarkDAO, final BookmarkDataBinder bookmarkDataBinder,
            final SunetIdAndTicketDataBinder sunetidTicketDataBinder) {
        this.bookmarkDAO = bookmarkDAO;
        this.bookmarkDataBinder = bookmarkDataBinder;
        this.sunetidTicketDataBinder = sunetidTicketDataBinder;
    }

    @ModelAttribute
    protected void bind(final HttpServletRequest request, final org.springframework.ui.Model model) {
        this.sunetidTicketDataBinder.bind(model.asMap(), request);
        this.bookmarkDataBinder.bind(model.asMap(), request);
        // case 73359 need to put null value into model if not present.
        if (!model.containsAttribute(edu.stanford.irt.laneweb.model.Model.BOOKMARKS)) {
            model.addAttribute(edu.stanford.irt.laneweb.model.Model.BOOKMARKS, null);
        }
    }

    protected void saveLinks(final String sunetid, final List<Bookmark> links) {
        this.bookmarkDAO.saveLinks(sunetid, links);
    }
}
