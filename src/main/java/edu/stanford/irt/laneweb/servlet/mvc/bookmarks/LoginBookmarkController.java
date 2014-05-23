package edu.stanford.irt.laneweb.servlet.mvc.bookmarks;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.stanford.irt.laneweb.bookmarks.Bookmark;
import edu.stanford.irt.laneweb.bookmarks.BookmarkDAO;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.BookmarkDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.SunetIdAndTicketDataBinder;

@Controller
public class LoginBookmarkController extends BookmarkController {

    @Autowired
    public LoginBookmarkController(BookmarkDAO bookmarkDAO, BookmarkDataBinder bookmarkDataBinder,
            SunetIdAndTicketDataBinder sunetidTicketDataBinder) {
        super(bookmarkDAO, bookmarkDataBinder, sunetidTicketDataBinder);
    }

    @RequestMapping(value = "/secure/addBookmark")
    public String addBookmark(
            final RedirectAttributes redirectAttrs,
            @ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks,
            @ModelAttribute(Model.SUNETID) final String sunetid,
            @RequestParam final String label,
            @RequestParam final String url,
            @RequestParam final String redirect) {
        Bookmark bookmark = new Bookmark(label, url);
        List<Bookmark> clone = new ArrayList<Bookmark>(bookmarks);
        clone.add(0, bookmark);
        saveLinks(sunetid, clone);
        bookmarks.add(0, bookmark);
        return "redirect:" + redirect;
    }
}
