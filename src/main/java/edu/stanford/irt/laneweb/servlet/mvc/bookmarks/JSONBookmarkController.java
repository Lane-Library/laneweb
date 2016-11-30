package edu.stanford.irt.laneweb.servlet.mvc.bookmarks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import edu.stanford.irt.laneweb.bookmarks.Bookmark;
import edu.stanford.irt.laneweb.bookmarks.BookmarkDAO;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.BookmarkDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.RemoteProxyIPDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;

// TODO: revisit conscious decision not to synchronize list operations.
@Controller
@RequestMapping(value = "/bookmarks")
public class JSONBookmarkController extends BookmarkController {

    private RemoteProxyIPDataBinder proxyLinksDataBinder;

    @Autowired
    public JSONBookmarkController(final BookmarkDAO bookmarkDAO, final BookmarkDataBinder bookmarkDataBinder,
            final UserDataBinder userDataBinder, final RemoteProxyIPDataBinder proxyLinksDataBinder) {
        super(bookmarkDAO, bookmarkDataBinder, userDataBinder);
        this.proxyLinksDataBinder = proxyLinksDataBinder;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public void addBookmark(
            @ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks,
            @ModelAttribute(Model.USER_ID) final String userid,
            @RequestBody final Bookmark bookmark,
            final HttpSession session) {
        List<Bookmark> clone = new ArrayList<>(bookmarks);
        clone.add(0, bookmark);
        saveLinks(userid, clone, session);
        bookmarks.add(0, bookmark);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteBookmark(@ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks,
            @ModelAttribute(Model.USER_ID) final String userid,
            @RequestParam final String indexes,
            final HttpSession session) {
        // convert json array to an int[]
        String[] split = indexes.substring(1, indexes.length() - 1).split(",");
        int[] ints = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            ints[i] = Integer.parseInt(split[i]);
        }
        // sort the array to be sure in order
        Arrays.sort(ints);
        List<Bookmark> clone = new ArrayList<>(bookmarks);
        for (int j = ints.length - 1; j >= 0; --j) {
            clone.remove(ints[j]);
        }
        saveLinks(userid, clone, session);
        for (int j = ints.length - 1; j >= 0; --j) {
            bookmarks.remove(ints[j]);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public Bookmark getBookmark(@ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks,
            @ModelAttribute(Model.PROXY_LINKS) final Boolean proxyLinks,
            @RequestParam final int i,
            final HttpSession session) {
        // TODO: extend Bookmark or create a map to add the proxylink url
        return bookmarks.get(i);
    }

    @RequestMapping(value = "/move", method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public void moveBookmark(@ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks,
            @ModelAttribute(Model.USER_ID) final String userid,
            @RequestBody final Map<String, Integer> json,
            final HttpSession session) {
        int to = json.get("to").intValue();
        int from = json.get("from").intValue();
        List<Bookmark> clone = new ArrayList<>(bookmarks);
        clone.add(to, clone.remove(from));
        saveLinks(userid, clone, session);
        bookmarks.add(to, bookmarks.remove(from));
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public void saveBookmark(@ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks,
            @ModelAttribute(Model.USER_ID) final String userid,
            @RequestBody final Map<String, Object> json,
            final HttpSession session) {
        Bookmark bookmark = new Bookmark((String) json.get("label"), (String) json.get("url"));
        int position = ((Integer) json.get("position")).intValue();
        List<Bookmark> clone = new ArrayList<>(bookmarks);
        clone.set(position, bookmark);
        saveLinks(userid, clone, session);
        bookmarks.set(position, bookmark);
    }

    @Override
    protected void bind(final HttpServletRequest request, final org.springframework.ui.Model model) {
        super.bind(request, model);
        this.proxyLinksDataBinder.bind(model.asMap(), request);
    }
}
