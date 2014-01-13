package edu.stanford.irt.laneweb.servlet.mvc.bookmarks;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.stanford.irt.laneweb.bookmarks.Bookmark;
import edu.stanford.irt.laneweb.model.Model;

@Controller
@RequestMapping(value = "/bookmarks", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
public class HTMLBookmarkController extends BookmarkController {

    private String redirectURI = "redirect:/favorites.html";

    @RequestMapping(params = "action=add")
    public String addBookmark(
            final RedirectAttributes redirectAttrs,
            @ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks,
            @ModelAttribute(Model.SUNETID) final String sunetid) {
        bookmarks.add(0, new Bookmark("", ""));
        saveLinks(sunetid, bookmarks);
        return editBookmark(redirectAttrs, 0);
    }

    @RequestMapping(params = "action=cancel")
    public String cancelEdit(final RedirectAttributes redirectAttrs) {
        return this.redirectURI;
    }

    @RequestMapping(params = "action=delete")
    public String deleteBookmark(
            final RedirectAttributes redirectAttrs,
            @ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks,
            @ModelAttribute(Model.SUNETID) final String sunetid,
            @RequestParam final int i) {
        bookmarks.remove(i);
        saveLinks(sunetid, bookmarks);
        return this.redirectURI;
    }

    @RequestMapping(params = "action=edit")
    public String editBookmark(final RedirectAttributes redirectAttrs, @RequestParam final int i) {
        redirectAttrs.addAttribute("action", "edit");
        redirectAttrs.addAttribute("i", Integer.valueOf(i));
        return this.redirectURI;
    }

    @RequestMapping(params = "action=save")
    public String saveBookmark(
            final RedirectAttributes redirectAttrs,
            @ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks,
            @ModelAttribute(Model.SUNETID) final String sunetid,
            @RequestParam final int i,
            @RequestParam final String label,
            @RequestParam final String url) {
        bookmarks.set(i, new Bookmark(label, url));
        saveLinks(sunetid, bookmarks);
        return this.redirectURI;
    }
}
