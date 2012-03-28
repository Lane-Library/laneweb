package edu.stanford.irt.laneweb.bookmarks;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.RemoteProxyIPDataBinder;

@Controller
@RequestMapping(value = "/bookmarks", consumes = "application/json")
public class JSONBookmarkController extends BookmarkController {
    
    @Autowired
    private RemoteProxyIPDataBinder proxyLinksDataBinder;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void addBookmark(
            @ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks,
            @ModelAttribute(Model.SUNETID) final String sunetid,
            @RequestBody final Bookmark bookmark) {
        bookmarks.add(0, bookmark);
        saveLinks(sunetid, bookmarks);
    }
    
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteBookmark(
            @ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks,
            @ModelAttribute(Model.SUNETID) final String sunetid,
            @RequestBody final int i) {
        bookmarks.remove(i);
        saveLinks(sunetid, bookmarks);
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public Bookmark getBookmark(
            @ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks,
            @ModelAttribute(Model.PROXY_LINKS) final Boolean proxyLinks,
            @RequestParam final int i) {
        //TODO: extend Bookmark or create a map to add the proxylink url
        return bookmarks.get(i);
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void saveBookmark(
            @ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks,
            @ModelAttribute(Model.SUNETID) final String sunetid,
            @RequestBody final Map<String, Object> json) {
        bookmarks.set((Integer)json.get("position"), new Bookmark((String)json.get("label"), (String)json.get("url")));
        saveLinks(sunetid, bookmarks);
    }

    @Override
    protected void bind(HttpServletRequest request, org.springframework.ui.Model model) {
        super.bind(request, model);
        this.proxyLinksDataBinder.bind(model.asMap(), request);
    }
}
