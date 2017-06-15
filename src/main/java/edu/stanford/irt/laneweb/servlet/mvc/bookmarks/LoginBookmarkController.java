package edu.stanford.irt.laneweb.servlet.mvc.bookmarks;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.stanford.irt.laneweb.bookmarks.Bookmark;
import edu.stanford.irt.laneweb.bookmarks.BookmarkService;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.BookmarkDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;

@Controller
public class LoginBookmarkController extends BookmarkController {

    @Autowired
    public LoginBookmarkController(final BookmarkService bookmarkService, final BookmarkDataBinder bookmarkDataBinder,
            final UserDataBinder userDataBinder) {
        super(bookmarkService, bookmarkDataBinder, userDataBinder);
    }

    @RequestMapping(value = "/secure/addBookmark")
    public String addBookmark(final RedirectAttributes redirectAttrs,
            @ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks,
            @ModelAttribute(Model.USER_ID) final String userid,
            @RequestParam final String label,
            @RequestParam final String url,
            @RequestParam final String redirect,
            final HttpSession session) {
        Bookmark bookmark = new Bookmark(label, url);
        List<Bookmark> clone = new ArrayList<>(bookmarks);
        clone.add(0, bookmark);
        saveLinks(userid, clone, session);
        bookmarks.add(0, bookmark);
        return "redirect:" + redirect;
    }
}
