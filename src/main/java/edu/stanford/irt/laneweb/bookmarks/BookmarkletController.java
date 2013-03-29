package edu.stanford.irt.laneweb.bookmarks;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.stanford.irt.laneweb.model.Model;

@Controller
public class BookmarkletController extends BookmarkController {

    @RequestMapping(value = { "/bookmarklet", "/secure/bookmarklet" })
    public String addBookmark(final RedirectAttributes redirectAttrs,
            @ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks,
            @ModelAttribute(Model.SUNETID) final String sunetid,
            @RequestParam final String url,
            @RequestParam final String label) throws UnsupportedEncodingException {
        if (sunetid == null) {
            // not logged in, redirect through webauth:
            return "redirect:/secure/bookmarklet?url=" + URLEncoder.encode(url, "UTF-8") + "&label="
                    + URLEncoder.encode(label, "UTF-8");
        }
        Bookmark bookmark = new Bookmark(label, url);
        bookmarks.add(0, bookmark);
        saveLinks(sunetid, bookmarks);
        return "redirect:" + url;
    }

    @Override
    // provide null values for sunetid and bookmarks in model to prevent
    // BeanInstantiationException before addBookmark
    protected void bind(final HttpServletRequest request, final org.springframework.ui.Model model) {
        super.bind(request, model);
        if (!model.containsAttribute(Model.SUNETID)) {
            model.addAttribute(Model.SUNETID, null);
            model.addAttribute(Model.BOOKMARKS, null);
        }
    }
}
