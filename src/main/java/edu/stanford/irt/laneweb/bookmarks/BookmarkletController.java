package edu.stanford.irt.laneweb.bookmarks;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.stanford.irt.laneweb.model.Model;

@Controller
public class BookmarkletController extends BookmarkController {

    @RequestMapping(value = "/bookmarklet", method = RequestMethod.GET)
    public String addBookmark(
            final RedirectAttributes redirectAttrs,
            @ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks,
            @ModelAttribute(Model.SUNETID) final String sunetid,
            @RequestParam final String url,
            @RequestParam final String label) {
        Bookmark bookmark = new Bookmark(label, url);
        bookmarks.add(0, bookmark);
        saveLinks(sunetid, bookmarks);
        return "redirect:" + url;
    }
}
