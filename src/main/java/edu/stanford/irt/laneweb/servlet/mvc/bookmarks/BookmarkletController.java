package edu.stanford.irt.laneweb.servlet.mvc.bookmarks;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;

@Controller
public class BookmarkletController extends BookmarkController {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    @Autowired
    public BookmarkletController(final BookmarkDAO bookmarkDAO, final BookmarkDataBinder bookmarkDataBinder,
            final UserDataBinder userDataBinder) {
        super(bookmarkDAO, bookmarkDataBinder, userDataBinder);
    }

    @RequestMapping(value = { "/bookmarklet", "/secure/bookmarklet" })
    public String addBookmark(final RedirectAttributes redirectAttrs,
            @ModelAttribute(Model.BOOKMARKS) final List<Object> bookmarks,
            @ModelAttribute(Model.USER_ID) final String userid,
            @RequestParam final String url,
            @RequestParam final String label) throws UnsupportedEncodingException {
        if (userid == null) {
            // not logged in, redirect through webauth:
            return "redirect:/secure/bookmarklet?url=" + URLEncoder.encode(url, UTF_8) + "&label="
                    + URLEncoder.encode(label, UTF_8);
        }
        Bookmark bookmark = new Bookmark(label, url);
        bookmarks.add(0, bookmark);
        saveLinks(userid, bookmarks);
        return "redirect:" + url;
    }

    @Override
    // provide null values for userid and bookmarks in model to prevent
    // BeanInstantiationException before addBookmark
    protected void bind(final HttpServletRequest request, final org.springframework.ui.Model model) {
        super.bind(request, model);
        if (!model.containsAttribute(Model.USER_ID)) {
            model.addAttribute(Model.USER_ID, null);
            model.addAttribute(Model.BOOKMARKS, null);
        }
    }
}
