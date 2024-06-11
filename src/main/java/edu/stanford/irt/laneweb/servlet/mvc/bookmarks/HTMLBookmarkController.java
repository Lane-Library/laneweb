package edu.stanford.irt.laneweb.servlet.mvc.bookmarks;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.stanford.irt.laneweb.bookmarks.Bookmark;
import edu.stanford.irt.laneweb.bookmarks.BookmarkService;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.BookmarkDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;

@Controller
@RequestMapping(value = "/bookmarks", consumes = "application/x-www-form-urlencoded")
public class HTMLBookmarkController extends BookmarkController {

    private String redirectURI = "redirect:/favorites.html";

    public HTMLBookmarkController(final BookmarkService bookmarkService, final BookmarkDataBinder bookmarkDataBinder,
            final UserDataBinder userDataBinder) {
        super(bookmarkService, bookmarkDataBinder, userDataBinder);
    }

    @PostMapping(params = "action=add")
    public String addBookmark(final RedirectAttributes redirectAttrs,
            @ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks,
            @ModelAttribute(Model.USER_ID) final String userid,
            final HttpSession session) {
        bookmarks.add(0, new Bookmark("", ""));
        saveLinks(userid, bookmarks, session);
        return editBookmark(redirectAttrs, 0);
    }

    @PostMapping(params = "action=cancel")
    public String cancelEdit(final RedirectAttributes redirectAttrs) {
        return this.redirectURI;
    }

    @PostMapping(params = "action=delete")
    public String deleteBookmark(final RedirectAttributes redirectAttrs,
            @ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks,
            @ModelAttribute(Model.USER_ID) final String userid,
            @RequestParam final int i,
            final HttpSession session) {
        bookmarks.remove(i);
        saveLinks(userid, bookmarks, session);
        return this.redirectURI;
    }

    @PostMapping(params = "action=edit")
    public String editBookmark(final RedirectAttributes redirectAttrs, @RequestParam final int i) {
        redirectAttrs.addAttribute("action", "edit");
        redirectAttrs.addAttribute("i", Integer.valueOf(i));
        return this.redirectURI;
    }

    @PostMapping(params = "action=save")
    public String saveBookmark(final RedirectAttributes redirectAttrs,
            @ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks,
            @ModelAttribute(Model.USER_ID) final String userid,
            @RequestParam final int i,
            @RequestParam final String label,
            @RequestParam final String url,
            final HttpSession session) {
        bookmarks.set(i, new Bookmark(label, url));
        saveLinks(userid, bookmarks, session);
        return this.redirectURI;
    }
}
